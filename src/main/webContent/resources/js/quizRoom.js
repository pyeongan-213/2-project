// WebSocket 연결 및 퀴즈 방 관리 JS
let stompClient;  // WebSocket 클라이언트 객체
const roomId = getRoomIdFromPath();  // URL 경로에서 roomId 추출
let isPlaying = false;  // 음악 재생 상태
let currentVideo = null;  // 현재 재생 중인 비디오 코드
let currentAnswers = [];  // 현재 퀴즈 정답 배열
let currentSongName = '';  // 현재 노래 제목
let countdownInterval = null;  // 카운트다운 인터벌
let hintIndex = 0;  // 힌트 인덱스
let currentAnswer = '';  // 현재 퀴즈 정답
let isStoppedByUser = false;  // 사용자가 음악을 중지했는지 여부
const roomName = '방 이름';
let currentUserNickname = window.currentUserNickname || "유저";  // JSP에서 전달된 닉네임을 사용, 없으면 기본값
let currentUserId = window.currentUserId || 1;  // JSP에서 전달된 ID를 사용, 없으면 기본값
let isSubscribedToQuiz = false;  // 퀴즈 구독 상태 추적 변수
let isSubscribedToChat = false;  // 채팅 구독 상태 추적 변수
let isSubscribedToPlayers = false;  // 플레이어 목록 구독 상태 추적 변수

// 효과음 객체 생성
const correctAnswerSound = new Audio(`${root}/audio/correct_answer.mp3`);  

// **경로에서 roomId 추출 함수**
function getRoomIdFromPath() {
    const path = window.location.pathname;
    const pathSegments = path.split('/');
    const roomIndex = pathSegments.indexOf('rooms');
    return roomIndex !== -1 && pathSegments.length > roomIndex + 1 
        ? pathSegments[roomIndex + 1] 
        : 1;  // 기본값 1 반환
}

// **정답 효과음 재생 함수**
function playCorrectAnswerSound() {
    correctAnswerSound.play().catch(error => {
        console.error('오디오 재생 오류:', error);
    });
}

// **툴팁 표시 함수 (전역으로 정의)**
function showTooltip() {
    const tooltip = document.getElementById('command-tooltip');  
    const maximizeButton = document.getElementById('tooltip-maximize-btn');  

    tooltip.classList.remove('hidden');
    tooltip.classList.add('visible');
    maximizeButton.classList.add('hidden');  
}

document.addEventListener('DOMContentLoaded', () => {
    const tooltip = document.getElementById('command-tooltip');
    const minimizeButton = document.getElementById('tooltip-minimize-btn');
    const maximizeButton = document.getElementById('tooltip-maximize-btn');

    function toggleTooltip() {
    const tooltip = document.getElementById('command-tooltip');
    const maximizeButton = document.getElementById('tooltip-maximize-btn');

    if (tooltip.classList.contains('minimized')) {
        console.log('최대화 버튼 클릭됨');
        tooltip.classList.remove('minimized');
        tooltip.classList.add('visible');
        tooltip.style.display = 'block';
        tooltip.style.visibility = 'visible';
        
        // 툴팁 위치를 고정값으로 리셋
        tooltip.style.top = '62px';  // 초기 top 위치로 리셋
        tooltip.style.left = '62.5%';  // 초기 left 위치로 리셋

        // 최대화 버튼 숨기기
        maximizeButton.style.display = 'none';
    } else {
        console.log('최소화 버튼 클릭됨');
        tooltip.classList.add('minimized');
        tooltip.classList.remove('visible');
        tooltip.style.display = 'none';
        tooltip.style.visibility = 'hidden';
        
        // 최대화 버튼의 위치도 리셋 (최소화된 위치 고정)
        maximizeButton.style.top = '70px';  
        maximizeButton.style.left = '76%';  
        maximizeButton.style.display = 'block';
    }
}

    let isDragging = false;
    let offsetX, offsetY;

    tooltip.addEventListener('mousedown', (e) => {
        isDragging = true;
        dragStart = true;
        offsetX = e.clientX - tooltip.getBoundingClientRect().left;
        offsetY = e.clientY - tooltip.getBoundingClientRect().top;
        tooltip.classList.add('grabbing');
    });

    document.addEventListener('mousemove', (e) => {
        if (isDragging) {
            tooltip.style.left = `${e.clientX - offsetX}px`;
            tooltip.style.top = `${e.clientY - offsetY}px`;
        }
    });

    document.addEventListener('mouseup', () => {
        if (isDragging) {
            isDragging = false;
            tooltip.classList.remove('grabbing');
        }
        dragStart = false;
    });

    maximizeButton.addEventListener('mousedown', (e) => {
        isDragging = true;
        dragStart = true;
        offsetX = e.clientX - maximizeButton.getBoundingClientRect().left;
        offsetY = e.clientY - maximizeButton.getBoundingClientRect().top;
        maximizeButton.classList.add('grabbing');
    });

    document.addEventListener('mousemove', (e) => {
        if (isDragging) {
            maximizeButton.style.left = `${e.clientX - offsetX}px`;
            maximizeButton.style.top = `${e.clientY - offsetY}px`;
        }
    });

    document.addEventListener('mouseup', () => {
        if (isDragging) {
            isDragging = false;
            maximizeButton.classList.remove('grabbing');
        }
        dragStart = false;
    });

    minimizeButton.addEventListener('click', toggleTooltip);
    maximizeButton.addEventListener('click', toggleTooltip);
});

// **채팅 메시지 화면에 표시**
function displayChatMessage(sender, message) {
    const chatMessages = document.getElementById('chat-messages');
    const messageElement = document.createElement('p');

    // 메시지가 유효할 경우 출력
    if (!message) {
        console.error("채팅 메시지 내용이 없습니다:", message);
        return;
    }

    messageElement.innerHTML = `<strong>${sender}:</strong> ${message}`;
    chatMessages.appendChild(messageElement);

    // 너무 많은 메시지가 쌓일 경우 제거
    const MAX_CHAT_MESSAGES = 14;
    while (chatMessages.children.length > MAX_CHAT_MESSAGES) {
        chatMessages.removeChild(chatMessages.firstChild);
    }
}

// **채팅 구독 및 처리**
function connectWebSocket() {
    if (stompClient && stompClient.connected) {
        console.log("WebSocket 이미 연결됨");
        return;  // 이미 연결된 경우 추가 작업을 하지 않음
    }

    socket = new SockJS(`${root}/ws-stomp`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
        console.log('WebSocket 연결 성공:', frame);

        // **채팅 구독**
        if (!isSubscribedToChat) {
            chatSubscription = stompClient.subscribe(`/sub/chat/${roomId}`, (message) => {
                const chat = JSON.parse(message.body);
                console.log("채팅 수신 - 메시지 구조 확인:", chat);

                if (chat.sender === currentUserId) {
                    console.log("본인이 보낸 메시지이므로 무시합니다.");
                    return;  // 본인이 보낸 메시지라면 아무 처리도 하지 않음
                }

                // 다른 사용자가 보낸 메시지 처리
                displayChatMessage(chat.sender, chat.message);
            });
            isSubscribedToChat = true;  // 구독 상태 업데이트
        }

    }, (error) => {
        console.error('WebSocket 연결 실패. 재연결 시도 중...', error);
        setTimeout(connectWebSocket, 5000);  // 재연결 시도
    });
}

// WebSocket 연결 시도
connectWebSocket();

// **게임 시작 함수**
async function startQuiz() {
    console.log('게임 시작 버튼이 클릭되었습니다.');
    try {
        const response = await fetch(`${root}/quiz/rooms/${roomId}/random`);
        if (response.ok) {
            const data = await response.json();
            const quiz = data.quiz;
            currentVideo = quiz.code;
            currentAnswers = quiz.answer.map(answer => answer.trim().toLowerCase());
            currentSongName = quiz.name;
            currentAnswer = quiz.answer[0];
            hintIndex = 0;
            playMusic(quiz.code, quiz.start);
        } else {
            console.error('랜덤 퀴즈 가져오기 실패:', response.statusText);
        }
    } catch (error) {
        console.error('퀴즈 가져오는 중 오류:', error);
    }
}

// **음악 재생 함수**
function playMusic(videoCode, startTime) {
    const player = document.getElementById('youtube-player');
    const embedUrl = `https://www.youtube.com/embed/${videoCode}?autoplay=1&start=${startTime}&mute=0`;
    player.src = embedUrl;
    isPlaying = true;
    updatePlayIcon();
}

function togglePlayPause() {
    const player = document.getElementById('youtube-player');
    if (isPlaying) {
        console.log('음악 중지');
        player.src = '';
        isPlaying = false;
        isStoppedByUser = true; // 수동 중지 시 상태 저장
    } else {
        console.log('음악 재개');
        const embedUrl = `https://www.youtube.com/embed/${currentVideo}?autoplay=1`;
        player.src = embedUrl;
        isPlaying = true;
        isStoppedByUser = false; // 재생 시 상태 초기화
    }
    updatePlayIcon();
}

document.querySelector('.quiz-room-music-icon').addEventListener('click', togglePlayPause);


// **재생 아이콘 업데이트**
function updatePlayIcon() {
    const musicIcon = document.querySelector('.quiz-room-music-icon i');
    if (isPlaying) {
        musicIcon.classList.replace('bi-play-circle-fill', 'bi-pause-circle-fill');
    } else {
        musicIcon.classList.replace('bi-pause-circle-fill', 'bi-play-circle-fill');
    }
}

function processChatMessage(sender, content) {


    // 서버에서 받은 메시지 구조를 확인한 후 적절히 수정
    const message = typeof content === 'string' ? content.trim().toLowerCase() : '';

    // 명령어 처리
    if (['!스킵', '!skip'].includes(message)) {
        console.log("스킵 명령어 감지됨");  // 스킵 감지 로그
        skipQuiz(sender);  // 스킵 처리 함수 호출
        return;  // 스킵 메시지는 DB에 저장하지 않음
    } else if (['!힌트', '!hint'].includes(message)) {
        console.log("힌트 명령어 감지됨");  // 힌트 감지 로그
        displayHint();  // 힌트 표시 함수 호출
        return;  // 힌트 메시지도 DB에 저장하지 않음
    }

    // 정답 처리
    if (currentAnswers.some(answer => isExactMatch(message, answer))) {
        checkAnswer(sender, message);  // 정답 체크 함수 호출
        return;  // 정답은 DB에 저장하지 않음
    }


   // 일반 채팅 메시지만 저장
    saveChatMessage(roomId, currentUserId, message);
}
function skipQuiz(sender) {
    console.log(`${sender}님이 문제를 스킵했습니다.`);

    // 타이머가 작동 중이라면 종료합니다.
    if (countdownInterval) {
        clearInterval(countdownInterval);
        countdownInterval = null; // 타이머 초기화
    }

    // 스킵 알림 메시지 전송
    stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
        sender: '시스템',
        content: `${sender}님이 문제를 스킵했습니다. 다음 문제로 이동합니다!`
    }));

    hideAnswerInfo();
    hideHint();
    startQuiz(); // 즉시 다음 퀴즈 시작
}

// **힌트 표시 함수**
function displayHint() {
    const hintDisplay = document.getElementById('hint-info');
    if (hintIndex >= currentAnswer.length) return;

    let hint = '';
    for (let i = 0; i < currentAnswer.length; i++) {
        hint += i <= hintIndex ? currentAnswer[i] : 'O';
    }
    hintIndex++;
    hintDisplay.textContent = `힌트: ${hint}`;
    hintDisplay.classList.remove('hidden');
}

function skipQuiz(sender) {
    console.log(`${sender}님이 문제를 스킵했습니다.`);

    // 타이머가 작동 중이라면 종료합니다.
    if (countdownInterval) {
        clearInterval(countdownInterval);
        countdownInterval = null; // 타이머 초기화
    }

    // 스킵 알림 메시지 전송
    stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
        sender: '시스템',
        content: `${sender}님이 문제를 스킵했습니다. 다음 문제로 이동합니다!`
    }));

    // 타이머 표시를 숨김
    hideAnswerInfo(); 
    hideHint();

    startQuiz(); // 즉시 다음 퀴즈 시작
}

// 괄호 안의 문자열과 불필요한 문자를 제거하는 정규 표현식을 사용하여 정답을 처리
function normalizeAnswer(answer) {
    return answer.replace(/\(.*?\)/g, '') // 괄호 안의 내용을 제거
                 .replace(/[^\w\s가-힣]|_/g, '') // 특수문자 제거, 한글 범위 추가
                 .replace(/\s+/g, '') // 모든 공백 제거
                 .trim();  // 양쪽 공백을 제거
}

// 정답과 사용자 입력이 정확히 일치하는지 확인하는 함수
function isExactMatch(userAnswer, correctAnswer) {
    const normalizedUserAnswer = normalizeAnswer(userAnswer);
    const normalizedCorrectAnswer = normalizeAnswer(correctAnswer);
    // 입력한 답과 정답이 정확히 일치하는지 확인
    return normalizedUserAnswer === normalizedCorrectAnswer;
}


function checkAnswer(sender, userAnswer) {
    const normalizedUserAnswer = normalizeAnswer(userAnswer);

    // 정답 리스트를 순회하며 정확한 일치를 확인
    const isCorrect = currentAnswers.some(correctAnswer => {
        const normalizedCorrectAnswer = normalizeAnswer(correctAnswer);
        return normalizedUserAnswer === normalizedCorrectAnswer;  // 전체 일치 확인
    });

    if (isCorrect) {
        //console.log('정답입니다!');
        playCorrectAnswerSound(); // 정답 맞췄을 때 효과음 재생
        displayAnswerInfo(sender); // 정답 정보 표시

        stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
            sender: '시스템',
            content: `${sender}님이 정답을 맞췄습니다!`
        }));

        hideHint(); // 힌트 숨기기
        startCountdown(); // 카운트다운 시작
    } else {
        //console.log('오답입니다.');
    }
}

// **정답 정보 표시 함수**
function displayAnswerInfo(sender) {
    document.getElementById('correct-player').textContent = `정답자: ${sender}`;
    document.getElementById('song-info').textContent = currentSongName;
    document.getElementById('answer-info').classList.remove('hidden');
    startCountdown();
}


function startCountdown() {
    // 기존 타이머가 있다면 초기화
    if (countdownInterval) {
        clearInterval(countdownInterval);
        countdownInterval = null; // 타이머 초기화
    }

    let timeLeft = 10;
    const countdownDisplay = document.getElementById('next-quiz-timer');
    countdownDisplay.textContent = `다음 퀴즈가 시작됩니다 (${timeLeft})`;
    countdownDisplay.classList.remove('hidden');

    countdownInterval = setInterval(() => {
        timeLeft--;
        countdownDisplay.textContent = `다음 퀴즈가 시작됩니다 (${timeLeft})`;

        // 남은 시간이 0이 되었을 때
        if (timeLeft < 0) {
            clearInterval(countdownInterval); // 타이머 종료
            countdownInterval = null; // 초기화
            hideAnswerInfo();

            // 사용자가 수동으로 음악을 중지했는지 확인
            if (!isStoppedByUser) {
                startQuiz(); // 음악 자동 재생 없이 다음 퀴즈 시작
            } else {
                console.log('사용자가 음악을 중지했습니다.');
                isStoppedByUser = false; // 상태 초기화
            }
        }
    }, 1000);
}

function hideAnswerInfo() {
    const answerInfo = document.getElementById('answer-info');
    const countdownDisplay = document.getElementById('next-quiz-timer');

    document.getElementById('correct-player').textContent = '';
    document.getElementById('song-info').textContent = '';

    // 타이머 표시 숨김
    answerInfo.classList.add('hidden');
    countdownDisplay.classList.add('hidden');
    countdownDisplay.textContent = '';
}


// **힌트 숨기기 함수**
function hideHint() {
    const hintDisplay = document.getElementById('hint-info');
    hintDisplay.classList.add('hidden');
    hintDisplay.textContent = '';
}

// **채팅 메시지 화면에 표시**
function displayChatMessage(sender, message) {
    const chatMessages = document.getElementById('chat-messages');
    const messageElement = document.createElement('p');

    // 메시지가 유효할 경우 출력
    if (!message) {
        console.error("채팅 메시지 내용이 없습니다:", message);
        return;
    }

    messageElement.innerHTML = `<strong>${sender}:</strong> ${message}`;
    chatMessages.appendChild(messageElement);

    // 너무 많은 메시지가 쌓일 경우 제거
    const MAX_CHAT_MESSAGES = 14;
    while (chatMessages.children.length > MAX_CHAT_MESSAGES) {
        chatMessages.removeChild(chatMessages.firstChild);
    }
}



document.getElementById('send-chat-btn').addEventListener('click', sendMessage);
document.getElementById('chat-input').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') sendMessage();
});

// **채팅 저장 함수**
async function saveChatMessage(roomId, memberId, message) {
    try {
        const response = await fetch(`${root}/saveChat`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                roomId: parseInt(roomId),  // roomId가 숫자로 전달되도록 수정
                memberId: parseInt(memberId),  // memberId도 숫자로 전달
                content: message  // message를 content로 전달
            }),
        });

        if (!response.ok) {
            const errorMsg = await response.text();
            console.error('채팅 저장 실패:', errorMsg);
            return false;
        } else {
            console.log('채팅 저장 성공');
            return true;
        }
    } catch (error) {
        console.error('채팅 저장 중 오류:', error.message);
        return false;
    }
}

// 메시지 전송 함수에서 연결 여부 확인
async function sendMessage() {
    let isSendingMessage = false;  // 함수 내에서 지역 변수로 선언
    const chatInput = document.getElementById('chat-input');
    const message = chatInput.value.trim();  // 메시지를 입력받아 공백 제거 후 저장

    if (!message || isSendingMessage) {
        console.log("메시지 전송 중복 방지 또는 메시지 없음");
        return;  // 메시지가 없거나 이미 전송 중이면 중복 전송 방지
    }

    isSendingMessage = true;  // 전송 중 상태로 설정

  		try {
        //WebSocket으로 메시지 전송
        stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
            sender: currentUserNickname,  // JSP에서 전달된 닉네임 사용
            message: message,  // 실제로 전송할 메시지 내용
           
        }));
         console.log("로그!");

        // 메시지 전송 후 DB에 저장
        await saveChatMessage(roomId, currentUserId, message);

        // 채팅 메시지를 화면에 표시
        displayChatMessage(currentUserNickname, message);
    } catch (error) {
        console.error("메시지 전송 중 오류:", error);
    } finally {
        chatInput.value = '';  // 입력창 초기화
        isSendingMessage = false;  // 전송 완료 후 상태 해제
    }
}	

document.addEventListener('DOMContentLoaded', () => {
    const startQuizButton = document.getElementById('start-quiz-btn');
    const sendChatBtn = document.getElementById('send-chat-btn');
    const chatInput = document.getElementById('chat-input');

    // 게임 시작 버튼에 대한 이벤트 리스너가 중복되지 않도록 먼저 제거 후 추가
    if (startQuizButton) {
        startQuizButton.removeEventListener('click', startQuiz);  // 기존 리스너 제거
        startQuizButton.addEventListener('click', startQuiz);  // 새로운 리스너 추가
    }

    // 채팅 전송 버튼에 대한 이벤트 리스너가 중복되지 않도록 먼저 제거 후 추가
    if (sendChatBtn) {
        sendChatBtn.removeEventListener('click', sendMessage);  // 기존 리스너 제거
        sendChatBtn.addEventListener('click', sendMessage);  // 새로운 리스너 추가
    }

    // 채팅 입력창에서 'Enter' 키를 눌렀을 때의 이벤트 리스너가 중복되지 않도록 처리
    if (chatInput) {
        chatInput.removeEventListener('keydown', handleChatInput);  // 기존 리스너 제거
        chatInput.addEventListener('keydown', handleChatInput);  // 새로운 리스너 추가
    }
});

// 채팅 입력에서 'Enter' 키를 눌렀을 때 실행될 핸들러 함수 분리
function handleChatInput(event) {
    if (event.key === 'Enter') {
        sendMessage();  // 엔터키 입력시 메시지 전송
    }
}


// 페이지를 떠나기 전에 방 나가기 처리
window.addEventListener('beforeunload', function (event) {
    leaveRoom();  // 사용자가 페이지를 떠날 때 leaveRoom 함수 호출
    event.returnValue = ''; // 메시지를 반환하면 브라우저가 경고 메시지를 띄울 수 있음
});

// 방 나가기 API 호출
async function leaveRoom() {
    try {
        const response = await fetch(`${root}/quiz/rooms/leave`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ 
                roomId: roomId,  // 현재 방 ID
                memberId: currentUserId  // 현재 사용자 ID
            })
        });

        if (response.ok) {
            console.log('방에서 성공적으로 나갔습니다.');
        } else {
            console.error('방 나가기 실패:', response.statusText);
        }
    } catch (error) {
        console.error('방 나가기 요청 중 오류 발생:', error);
    }
}
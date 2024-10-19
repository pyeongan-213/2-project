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
let chatSubscription;  // 구독을 추적할 변수
let playerSubscription = null;  // 전역 변수로 선언하여 구독 상태 관리


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

// 방에 참가하는 함수
function joinRoom(roomId) {
    fetch(`${root}/quiz/rooms/join`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ roomId: roomId })  // 서버로 roomId를 전달
    })
    .then(response => response.json())
    .then(data => {
        console.log("서버 응답 확인:", data);

        if (data.success) {
            // 방에 입장 시 서버에서 받은 플레이어 목록을 수동으로 업데이트
            if (data.players && data.players.length > 0) {
                console.log("플레이어 목록 받음:", data.players);
                updatePlayerList(data.players);  // 첫 번째로 서버에서 받은 플레이어 목록 업데이트
            } else {
                console.log("플레이어 목록이 비어 있습니다.");
            }
            
            // WebSocket 연결은 여기서 구독함
            connectWebSocket();  // 방에 참가한 후 WebSocket 연결
        } else {
            console.error("방 참여 실패: ", data.message);
        }
    })
    .catch(error => console.error("방 참여 중 오류 발생: ", error));  // 에러 처리
}

// WebSocket 연결 및 구독 처리
function connectWebSocket() {
    if (stompClient && stompClient.connected) {
        console.log("WebSocket 이미 연결됨");
        return;
    }

    socket = new SockJS(`${root}/ws-stomp`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
        console.log('WebSocket 연결 성공:', frame);

        // 퀴즈 정보 구독
        stompClient.subscribe(`/sub/quiz/${roomId}`, (message) => {
            const msg = JSON.parse(message.body);
            currentVideo = msg.code;
            currentAnswers = msg.answer.map(answer => answer.trim().toLowerCase());
            currentSongName = msg.name;
            currentAnswer = msg.answer[0];
            hintIndex = 0;
            playMusic(msg.code, msg.start);
        });
        
// WebSocket 메시지 처리
stompClient.subscribe(`/sub/chat/${roomId}`, (message) => {
    const chatMessage = JSON.parse(message.body);
    
    // 메시지가 내가 보낸 메시지라면 중복 처리하지 않음
    if (chatMessage.sender === currentUserNickname) {
        return;
    }
    
    // 수신한 메시지의 내용을 정규화하여 저장
    const normalizedMessage = chatMessage.message ? chatMessage.message.trim().toLowerCase() : '';  // 메시지가 undefined일 경우 빈 문자열로 처리
   
    if (normalizedMessage === '') {
        //console.warn('메시지가 비어있습니다. 처리하지 않습니다.');
        return;
    }
    const isCorrectAnswer = currentAnswers.some(answer => isExactMatch(normalizedMessage, answer));

    // 수신된 메시지 화면에 표시
    displayChatMessage(chatMessage.sender, chatMessage.message, isCorrectAnswer);
});
        // 플레이어 목록 구독 설정
        if (!playerSubscription) {
            playerSubscription = stompClient.subscribe(`/sub/quizRoom/${roomId}/players`, (message) => {
                const chatMessage = JSON.parse(message.body);
                if (chatMessage.type === 'PLAYER_LIST') {
                    updatePlayerList(chatMessage.message);  // 전체 플레이어 목록 갱신
                } else if (chatMessage.type === 'PLAYER_JOIN') {
                    addPlayerToList(chatMessage.sender);  // 새로 들어온 플레이어 추가
                } else if (chatMessage.type === 'PLAYER_LEAVE') {
                    removePlayerFromList(chatMessage.sender);  // 나간 플레이어 목록에서 제거
                }
            });
            console.log("플레이어 목록 구독 설정됨");

            // 새로운 참가자가 구독된 후 서버에 플레이어 목록을 요청하여 다시 한번 업데이트
            fetch(`${root}/quiz/rooms/${roomId}/players`)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    updatePlayerList(data.players);  // WebSocket 구독 후에도 수동으로 플레이어 목록 업데이트
                }
            })
            .catch(error => console.error("플레이어 목록 업데이트 오류:", error));
        }

    }, (error) => {
        console.error('WebSocket 연결 실패. 재연결 시도 중...', error);
        setTimeout(connectWebSocket, 5000);  // 재연결 시도
    });
}

// 페이지 로드 후 WebSocket 연결 시도
document.addEventListener('DOMContentLoaded', () => {
    connectWebSocket();
});

// 플레이어 목록 갱신 함수
function updatePlayerList(players) {
    const playerListContainer = document.getElementById('players');  // 'players' id를 가진 ul 요소 선택
    playerListContainer.innerHTML = '';  // 기존 목록 초기화
    
    // 플레이어 목록을 순회하며 li 요소로 추가
    players.forEach(player => {
        const playerItem = document.createElement('li');  // 각 플레이어를 li 요소로 생성
        playerItem.className = 'player-item';  // li 요소에 클래스 설정
        playerItem.textContent = player;  // 플레이어 닉네임 설정
        playerListContainer.appendChild(playerItem);  // ul 요소에 li 요소 추가
    });
}

// 새로운 플레이어를 목록에 추가하는 함수 (중복 추가 방지)
function addPlayerToList(playerName) {
    const playerListContainer = document.getElementById('players');  // 플레이어 목록 container
    const playerItems = playerListContainer.getElementsByClassName('player-item');  // 모든 플레이어 항목 선택
    
    // 플레이어가 이미 목록에 있는지 확인
    for (let i = 0; i < playerItems.length; i++) {
        if (playerItems[i].textContent === playerName) {
            return;  // 이미 존재하는 경우 추가하지 않음
        }
    }

    // 플레이어가 목록에 없으면 새로운 li 요소 추가
    const playerItem = document.createElement('li');
    playerItem.className = 'player-item';
    playerItem.textContent = playerName;
    playerListContainer.appendChild(playerItem);
}

// 플레이어가 퇴장했을 때 목록에서 제거하는 함수
function removePlayerFromList(playerName) {
    const playerListContainer = document.getElementById('players');  // 플레이어 목록 container
    const playerItems = playerListContainer.getElementsByClassName('player-item');  // 클래스가 'player-item'인 모든 요소 선택

    for (let i = 0; i < playerItems.length; i++) {
        if (playerItems[i].textContent === playerName) {
            playerListContainer.removeChild(playerItems[i]);  // 이름이 일치하는 플레이어를 목록에서 제거
            break;
        }
    }
}

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
       		// 정답 맞춘 후 상태를 초기화
            hideAnswerInfo();
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
        player.src = '';  // 음악을 중지하기 위해 플레이어의 src를 비웁니다.
        isPlaying = false;
        isStoppedByUser = true;  // 사용자가 음악을 수동으로 중지했음을 기록
    } else {
        console.log('음악 재개');
        
        // 사용자가 수동으로 음악을 중지하고, 타이머가 끝난 후 다시 재생할 경우 새로운 퀴즈 시작
        if (isStoppedByUser && isQuizCompleted) {
            console.log('새로운 퀴즈로 이동합니다.');
            startQuiz();  // 새로운 퀴즈 시작
        } else {
            const embedUrl = `https://www.youtube.com/embed/${currentVideo}?autoplay=1`;
            player.src = embedUrl;  // 기존 음악 재개
        }
        
        isPlaying = true;
        isStoppedByUser = false;  // 재생 시 상태 초기화
    }

    updatePlayIcon();  // 재생 아이콘 업데이트
}

// 음악 아이콘에 대한 클릭 이벤트 리스너 추가
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

function processChatMessage(sender, message, messageId) {
    // message가 문자열인지 확인
    if (typeof message !== 'string') {
        try {
            message = JSON.stringify(message); // 객체를 문자열로 변환
        } catch (error) {
            console.error("메시지를 문자열로 변환할 수 없습니다:", error);
            return;
        }
    }

    const normalizedMessage = message.trim().toLowerCase();  // 메시지를 소문자로 변환하고 공백 제거

    // 명령어 처리
    if (['!스킵', '!skip'].includes(normalizedMessage)) {
        console.log("스킵 명령어 감지됨");
        skipQuiz(sender);  // 스킵 처리 함수 호출
        return;  // 스킵 메시지는 DB에 저장하지 않음
    } else if (['!힌트', '!hint'].includes(normalizedMessage)) {
        console.log("힌트 명령어 감지됨");
        displayHint();  // 힌트 표시 함수 호출
        return;  // 힌트 메시지도 DB에 저장하지 않음
    }

    // 정답 여부 체크
    const isCorrectAnswer = currentAnswers.some(answer => isExactMatch(normalizedMessage, answer));

    // 메시지 화면에 표시
    if (isCorrectAnswer) {
        displayChatMessage(sender, message, true);  // 정답인 경우 파란색 처리
        checkAnswer(sender, message);
    } else {
        displayChatMessage(sender, message, false);  // 일반 메시지 처리
    }
}



// **힌트 표시 함수**
function displayHint() {
    const hintDisplay = document.getElementById('hint-info');
    
    // 힌트가 이미 최대 인덱스에 도달한 경우 더 이상 힌트를 표시하지 않음
    if (hintIndex >= currentAnswer.length) {
        console.log('더 이상 힌트를 제공할 수 없습니다.');
        return;
    }

    let hint = '';
    for (let i = 0; i < currentAnswer.length; i++) {
        // 현재 힌트 인덱스까지 글자를 표시하고 나머지는 'O'로 처리
        hint += i <= hintIndex ? currentAnswer[i] : 'O';
    }
    
    hintIndex++;  // 다음 힌트를 위해 인덱스를 증가시킴
    hintDisplay.textContent = `힌트: ${hint}`;  // 힌트 내용을 표시
    hintDisplay.classList.remove('hidden');  // 힌트 영역을 표시
}

let isSkipping = false; // 중복 스킵 방지를 위한 플래그

function skipQuiz(sender) {
    // 스킵이 이미 진행 중이면 함수 종료
    if (isSkipping) {
        return;
    }

    console.log(`${sender}님이 문제를 스킵했습니다.`);
    isSkipping = true; // 스킵 진행 중으로 설정

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

    // 스킵이 완료되었음을 알리기 위해 플래그 초기화 (다음 퀴즈가 시작된 후 적절한 위치에서 해제)
    setTimeout(() => {
        isSkipping = false; // 스킵 완료 후 플래그 해제
    }, 2000); // 2초 후에 해제 (필요 시 시간 조정 가능)
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
        currentAnswers = [];  // 정답 리스트 초기화
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
function displayChatMessage(sender, message, isCorrectAnswer) {
    const chatMessages = document.getElementById('chat-messages');
    const messageElement = document.createElement('p');

    // 메시지가 유효할 경우 출력
    if (!message) {
        console.log("정답 또는 명령어 감지", message);
        return;
    }

    // 보낸 사람이 본인인지 확인
    if (sender === "시스템") {
        messageElement.classList.add('system-chat-messages');
        messageElement.innerHTML = `<strong>${message}</strong>`;
    } else if (sender === currentUserNickname) {
        messageElement.classList.add('chat-message-right'); // 본인의 메시지일 경우 오른쪽 정렬
        messageElement.innerHTML = `<strong>${message}</strong>`;  // 본인은 메시지만 표시
    } else {
        messageElement.classList.add('chat-message-left'); // 다른 사용자의 메시지일 경우 왼쪽 정렬
        messageElement.innerHTML = `<strong>${sender}:</strong> ${message}`;  // 상대방은 이름과 메시지 표시
    }

    
    // 정답인 경우 파란색 배경 추가
    if (isCorrectAnswer) {
        messageElement.style.backgroundColor = '#0d6efd'; // 파란색 배경 설정
        messageElement.style.color = 'white'; // 글씨 색상은 흰색으로 변경
        messageElement.style.borderRadius = '10px'; // 둥근 모서리 추가
        messageElement.style.padding = '8px 12px'; // 패딩 추가
        messageElement.style.fontWeight = 'bold'; // 폰트 굵게  
    }

    chatMessages.appendChild(messageElement);

    // 너무 많은 메시지가 쌓일 경우 제거
    const MAX_CHAT_MESSAGES = 12;
    while (chatMessages.children.length > MAX_CHAT_MESSAGES) {
        chatMessages.removeChild(chatMessages.firstChild);
    }

    // 새로운 메시지를 표시한 후 스크롤을 가장 아래로 이동
    chatMessages.scrollTop = chatMessages.scrollHeight;
}


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


let isSendingMessage = false;  // 전역 변수로 선언하여 메시지 전송 상태 관리

// **메시지 전송 함수에서 명령어 처리**
async function sendMessage() {
    const chatInput = document.getElementById('chat-input');
    const message = chatInput.value.trim();  // 메시지를 입력받아 공백 제거 후 저장
    const messageId = Date.now();  // 고유한 메시지 ID 생성

    if (!message || isSendingMessage) {
        return;  // 메시지가 없거나 이미 전송 중이면 중복 전송 방지
    }

    isSendingMessage = true;  // 전송 중 상태로 설정

    try {
        // 정답 여부 체크
        const normalizedMessage = message.toLowerCase();
        const isCorrectAnswer = currentAnswers.some(answer => isExactMatch(normalizedMessage, answer));

        // 명령어 처리
        if (['!스킵', '!skip'].includes(message)) {
            console.log("스킵 명령어 감지됨");

            stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
                sender: currentUserNickname,
                message: '!스킵',
                messageId: messageId
            }));

            displayChatMessage(currentUserNickname, "!스킵", false);  // 채팅창에 명령어 표시
            skipQuiz(currentUserNickname);  // 스킵 처리 함수 호출
            return;
        } else if (['!힌트', '!hint'].includes(message)) {
            console.log("힌트 명령어 감지됨");

            // 타이머가 진행 중인지 확인
            if (countdownInterval) {  // 타이머가 진행 중이면
                console.log("타이머 진행 중. 힌트를 숨깁니다.");
                hideHint();  // 타이머가 진행 중일 때 힌트를 숨김
            } else {
                // WebSocket으로 힌트 명령어 전송
                stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
                    sender: currentUserNickname,
                    message: '!힌트',
                    messageId: messageId
                }));

                displayChatMessage(currentUserNickname, "!힌트", false);  // 채팅창에 명령어 표시
                displayHint();  // 힌트 표시
            }
            return;
        }

        // WebSocket으로 일반 메시지 또는 정답 메시지 전송
        stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
            sender: currentUserNickname,
            message: message,
            messageId: messageId
        }));

        // 메시지 화면에 표시 (직접 전송한 메시지는 화면에 먼저 표시)
        if (isCorrectAnswer) {
            displayChatMessage(currentUserNickname, message, true);  // 정답인 경우 파란색 처리
            checkAnswer(currentUserNickname, message);
        } else {
            displayChatMessage(currentUserNickname, message, false);  // 일반 메시지 처리
        }

        // 메시지 전송 후 DB에 저장
        await saveChatMessage(roomId, currentUserId, message);

    } catch (error) {
        console.error("메시지 전송 중 오류:", error);
    } finally {
        chatInput.value = '';  // 입력창 초기화
        isSendingMessage = false;  // 전송 완료 후 상태 해제
    }
}

// **힌트 숨기기 함수**
function hideHint() {
    const hintDisplay = document.getElementById('hint-info');
    hintDisplay.classList.add('hidden');  // 힌트 영역을 숨김
    hintDisplay.textContent = '';  // 힌트 내용을 초기화
}


document.addEventListener('DOMContentLoaded', () => {
    const startQuizButton = document.getElementById('start-quiz-btn');
    const sendChatBtn = document.getElementById('send-chat-btn');
    const chatInput = document.getElementById('chat-input');

    // 게임 시작 버튼에 대한 이벤트 리스너 추가
    if (startQuizButton) {
        startQuizButton.addEventListener('click', startQuiz);  // 새로운 리스너 추가
    }

    // 채팅 전송 버튼에 대한 이벤트 리스너 추가
    if (sendChatBtn) {
        sendChatBtn.addEventListener('click', sendMessage);  // 새로운 리스너 추가
    }

    // 채팅 입력창에서 'Enter' 키를 눌렀을 때의 이벤트 리스너 추가
    if (chatInput) {
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
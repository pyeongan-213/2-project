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
const roomName = '방 이름';  // 방 이름 설정 (적절히 초기화 필요)

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
        if (tooltip.classList.contains('minimized')) {
            console.log('최대화 버튼 클릭됨');
            tooltip.classList.remove('minimized');  
            tooltip.classList.add('visible');  
            tooltip.style.display = 'block';  
            tooltip.style.visibility = 'visible';  
            maximizeButton.style.display = 'none';  
        } else {
            console.log('최소화 버튼 클릭됨');
            tooltip.classList.add('minimized');  
            tooltip.classList.remove('visible');  
            tooltip.style.display = 'none';  
            tooltip.style.visibility = 'hidden';  
            maximizeButton.style.display = 'block';  
        }
    }

    let isDragging = false;
    let dragStart = false;
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

// **플레이어 목록을 HTML에 동적으로 추가하는 함수**
function updatePlayerList(data) {
    const playerList = document.getElementById('players');
    playerList.innerHTML = ''; // 기존 목록 초기화

    // 서버 응답에서 players 배열 추출
    if (data.success && Array.isArray(data.players)) {
        data.players.forEach(player => {
            const li = document.createElement('li');
            li.textContent = player; // 서버에서 받은 플레이어 이름 (닉네임)
            playerList.appendChild(li); // 플레이어 목록에 추가
        });
    } else {
        console.error('서버 응답이 올바르지 않습니다:', data);
    }
}

// 서버로부터 플레이어 목록을 가져오는 함수
async function fetchPlayers(roomId) {
    try {
        const response = await fetch(`${root}/quiz/rooms/${roomId}/players`);
        const data = await response.json(); // 서버에서 응답을 받음
        updatePlayerList(data); // 플레이어 목록 업데이트
    } catch (error) {
        console.error('플레이어 목록을 가져오는 중 오류:', error);
    }
}

// 페이지 로드 시 플레이어 목록을 불러옴
document.addEventListener('DOMContentLoaded', () => {
    fetchPlayers(roomId); // 현재 방의 플레이어 목록 가져오기
});

// **WebSocket 연결 설정**
const socket = new SockJS(`${root}/ws-stomp`);
stompClient = Stomp.over(socket);

stompClient.connect({}, (frame) => {
    console.log('WebSocket 연결 성공:', frame);

    stompClient.subscribe(`/sub/quiz/${roomId}`, (message) => {
        const msg = JSON.parse(message.body);
        currentVideo = msg.code;
        currentAnswers = msg.answer.map(answer => answer.trim().toLowerCase());
        currentSongName = msg.name;
        currentAnswer = msg.answer[0];
        hintIndex = 0;
        playMusic(msg.code, msg.start);
    });

    stompClient.subscribe(`/sub/chat/${roomId}`, (message) => {
        const chat = JSON.parse(message.body);
        displayChatMessage(chat.sender, chat.content);
        processChatMessage(chat.sender, chat.content);
    });

    // **플레이어 목록 갱신 구독**
    stompClient.subscribe(`/sub/quiz/${roomId}/players`, (message) => {
        const players = JSON.parse(message.body);
        clearPlayerList();
        players.forEach(player => addPlayerToList(player.name));
    });
});

// **퀴즈 시작 함수**
async function startQuiz() {
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
            console.error('랜덤 퀴즈 가져오기 실패');
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

// **재생 아이콘 업데이트**
function updatePlayIcon() {
    const musicIcon = document.querySelector('.quiz-room-music-icon i');
    if (isPlaying) {
        musicIcon.classList.replace('bi-play-circle-fill', 'bi-pause-circle-fill');
    } else {
        musicIcon.classList.replace('bi-pause-circle-fill', 'bi-play-circle-fill');
    }
}


// **채팅 메시지 처리 함수**
function processChatMessage(sender, content) {
    const message = content.trim().toLowerCase();
    if (['!스킵', '!skip'].includes(message)) skipQuiz(sender);
    else if (['!힌트', '!hint'].includes(message)) displayHint();
    else checkAnswer(sender, content);
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

// **퀴즈 스킵 함수**
function skipQuiz(sender) {
    stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
        sender: '시스템',
        content: `${sender}님이 문제를 스킵했습니다.`
    }));
    startQuiz();
}

// **정답 확인 함수**
function checkAnswer(sender, userAnswer) {
    const normalizedUserAnswer = userAnswer.replace(/\s+/g, '').toLowerCase();
    const isCorrect = currentAnswers.some(answer =>
        answer.replace(/\s+/g, '').toLowerCase() === normalizedUserAnswer
    );

    if (isCorrect) {
        playCorrectAnswerSound();
        displayAnswerInfo(sender);
        stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
            sender: '시스템',
            content: `${sender}님이 정답을 맞췄습니다!`
        }));
        hideHint();
    } else {
        console.log('오답입니다.');
    }
}

// **정답 정보 표시 함수**
function displayAnswerInfo(sender) {
    document.getElementById('correct-player').textContent = `정답자: ${sender}`;
    document.getElementById('song-info').textContent = currentSongName;
    document.getElementById('answer-info').classList.remove('hidden');
    startCountdown();
}

// **카운트다운 시작 함수**
function startCountdown() {
    let timeLeft = 10;
    const countdownDisplay = document.getElementById('next-quiz-timer');
    countdownDisplay.textContent = `다음 퀴즈가 시작됩니다 (${timeLeft})`;
    countdownDisplay.classList.remove('hidden');

    countdownInterval = setInterval(() => {
        timeLeft--;
        countdownDisplay.textContent = `다음 퀴즈가 시작됩니다 (${timeLeft})`;

        if (timeLeft < 0) {
            clearInterval(countdownInterval);
            hideAnswerInfo();
            if (!isStoppedByUser) startQuiz();
        }
    }, 1000);
}

// **정답 정보 숨기기 함수**
function hideAnswerInfo() {
    document.getElementById('correct-player').textContent = '';
    document.getElementById('song-info').textContent = '';
    document.getElementById('answer-info').classList.add('hidden');
    document.getElementById('next-quiz-timer').classList.add('hidden');
}

// **힌트 숨기기 함수**
function hideHint() {
    const hintDisplay = document.getElementById('hint-info');
    hintDisplay.classList.add('hidden');
    hintDisplay.textContent = '';
}

// **채팅 메시지 표시 함수**
const MAX_CHAT_MESSAGES = 14;

function displayChatMessage(sender, content) {
    const chatMessages = document.getElementById('chat-messages');
    const messageElement = document.createElement('p');
    messageElement.innerHTML = `<strong>${sender}:</strong> ${content}`;
    chatMessages.appendChild(messageElement);

    while (chatMessages.children.length > MAX_CHAT_MESSAGES) {
        chatMessages.removeChild(chatMessages.firstChild);
    }
}

// **메시지 전송 함수**
function sendMessage() {
    const chatInput = document.getElementById('chat-input');
    const message = chatInput.value.trim();
    if (!message) {
        alert('메시지를 입력해주세요.');
        return;
    }

    stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
        sender: roomName,
        content: message
    }));

    displayChatMessage('나', message);
    chatInput.value = '';
    processChatMessage('나', message);

}

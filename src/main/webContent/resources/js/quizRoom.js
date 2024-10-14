let stompClient;
const roomId = 1;
let isPlaying = false;
let currentVideo = null;
let currentAnswers = [];
let currentSongName = '';
let countdownInterval = null;
let hintIndex = 0; // 힌트 진행 상태를 저장할 변수
let currentAnswer = ''; // 현재 퀴즈의 정답

document.addEventListener('DOMContentLoaded', () => {
    const socket = new SockJS(`${root}/ws-stomp`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
        console.log('WebSocket 연결 성공:', frame);

        stompClient.subscribe(`/sub/quiz/${roomId}`, (message) => {
            const msg = JSON.parse(message.body);
            console.log('퀴즈 메시지 수신:', msg);
            currentVideo = msg.code;
            currentAnswers = msg.answer.map(answer => answer.trim().toLowerCase());
            currentSongName = msg.name;
            currentAnswer = msg.answer[0]; // 첫 번째 정답 사용
            hintIndex = 0; // 힌트 초기화
            playMusic(msg.code, msg.start);
        });

        stompClient.subscribe(`/sub/chat/${roomId}`, (message) => {
            const chat = JSON.parse(message.body);
            displayChatMessage(chat.sender, chat.content);
            processChatMessage(chat.sender, chat.content);
        });

        document.getElementById('start-quiz-btn').addEventListener('click', startQuiz);
        document.querySelector('.quiz-room-music-icon').addEventListener('click', togglePlayPause);
    });

    document.getElementById('send-chat-btn').addEventListener('click', sendMessage);
    document.getElementById('chat-input').addEventListener('keydown', (event) => {
        if (event.key === 'Enter') sendMessage();
    });
});

async function startQuiz() {
    try {
        console.log(`Fetching quiz from: ${root}/quiz/rooms/${roomId}/random`);
        const response = await fetch(`${root}/quiz/rooms/${roomId}/random`);

        if (response.ok) {
            const quiz = await response.json();
            console.log('랜덤 퀴즈:', quiz);
            currentVideo = quiz.code;
            currentAnswers = quiz.answer.map(answer => answer.trim().toLowerCase());
            currentSongName = quiz.name;
            currentAnswer = quiz.answer[0]; // 첫 번째 정답 사용
            hintIndex = 0; // 힌트 초기화
            playMusic(quiz.code, quiz.start);
        } else {
            console.error('랜덤 퀴즈 가져오기 실패');
        }
    } catch (error) {
        console.error('퀴즈 가져오는 중 오류:', error);
    }
}

function playMusic(videoCode, startTime) {
    const player = document.getElementById('youtube-player');
    const embedUrl = `https://www.youtube.com/embed/${videoCode}?autoplay=1&start=${startTime}&mute=0`;
    console.log(`비디오 재생 시작: ${videoCode}, 시작 시간: ${startTime}`);
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
    } else {
        console.log('음악 재개');
        const embedUrl = `https://www.youtube.com/embed/${currentVideo}?autoplay=1`;
        player.src = embedUrl;
        isPlaying = true;
    }
    updatePlayIcon();
}

function updatePlayIcon() {
    const musicIcon = document.querySelector('.quiz-room-music-icon i');
    if (isPlaying) {
        musicIcon.classList.replace('bi-play-circle-fill', 'bi-pause-circle-fill');
    } else {
        musicIcon.classList.replace('bi-pause-circle-fill', 'bi-play-circle-fill');
    }
}

function processChatMessage(sender, content) {
    const message = content.trim().toLowerCase();
    if (message === '!힌트') {
        displayHint();
    } else if (message === '!스킵') {
        skipQuiz(sender);
    } else {
        checkAnswer(sender, content);
    }
}

function displayHint() {
    const hintDisplay = document.getElementById('hint-info');
    if (!hintDisplay) {
        console.error('힌트 표시 요소를 찾을 수 없습니다.');
        return;
    }

    if (hintIndex >= currentAnswer.length) {
        console.log('더 이상 힌트가 없습니다.');
        return;
    }

    let hint = '';
    for (let i = 0; i < currentAnswer.length; i++) {
        hint += i <= hintIndex ? currentAnswer[i] : 'O';
    }
    hintIndex++; // 다음 힌트 준비

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

    hideAnswerInfo();
    startQuiz(); // 즉시 다음 퀴즈 시작
}


function checkAnswer(sender, userAnswer) {
    const trimmedAnswer = userAnswer.trim().toLowerCase();
    if (currentAnswers.includes(trimmedAnswer)) {
        console.log('정답입니다!');
        displayAnswerInfo(sender);
        stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
            sender: '시스템',
            content: `${sender}님이 정답을 맞췄습니다!`
        }));
        hideHint();
    }
}

function displayAnswerInfo(sender) {
    const correctPlayer = document.getElementById('correct-player');
    const songInfo = document.getElementById('song-info');
    const answerDisplay = document.getElementById('answer-info');

    correctPlayer.textContent = `정답자: ${sender}`;
    songInfo.textContent = currentSongName;
    answerDisplay.classList.remove('hidden');

    startCountdown();
}

function hideHint() {
    const hintDisplay = document.getElementById('hint-info');
    hintDisplay.classList.add('hidden');
    hintDisplay.textContent = '';
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

        if (timeLeft < 0) {
            clearInterval(countdownInterval); // 타이머 종료
            countdownInterval = null; // 초기화
            hideAnswerInfo();
            startQuiz(); // 다음 퀴즈 시작
        }
    }, 1000);
}

function hideAnswerInfo() {
    const answerInfo = document.getElementById('answer-info');
    const countdownDisplay = document.getElementById('next-quiz-timer');

    document.getElementById('correct-player').textContent = '';
    document.getElementById('song-info').textContent = '';

    answerInfo.classList.add('hidden');
    countdownDisplay.classList.add('hidden');
    countdownDisplay.textContent = '';
}

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

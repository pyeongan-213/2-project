let stompClient;
const roomId = 1; // 예제 roomId
let isPlaying = false; // 음악 재생 상태
let currentVideo = null; // 현재 재생 중인 비디오 코드

document.addEventListener('DOMContentLoaded', () => {
    const socket = new SockJS(`${root}/ws-stomp`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, (frame) => {
        console.log('WebSocket 연결 성공:', frame);

        // 퀴즈 메시지 구독
        stompClient.subscribe(`/sub/quiz/${roomId}`, (message) => {
            const msg = JSON.parse(message.body);
            console.log('퀴즈 메시지 수신:', msg);
            playMusic(msg.code, msg.start); // 음악 재생 호출
        });

        // 채팅 메시지 구독
        stompClient.subscribe(`/sub/chat/${roomId}`, (message) => {
            const chat = JSON.parse(message.body);
            displayChatMessage(chat.sender, chat.content);
        });

        document.getElementById('start-quiz-btn').addEventListener('click', startQuiz);
        document.querySelector('.quiz-room-music-icon').addEventListener('click', togglePlayPause);
    });

    document.getElementById('send-chat-btn').addEventListener('click', sendMessage);
    document.getElementById('chat-input').addEventListener('keydown', (event) => {
        if (event.key === 'Enter') sendMessage();
    });
});

// **게임 시작 및 랜덤 퀴즈 가져오기**
async function startQuiz() {
    try {
        console.log(`Fetching quiz from: ${root}/quiz/rooms/${roomId}/random`);
        const response = await fetch(`${root}/quiz/rooms/${roomId}/random`);

        if (response.ok) {
            const quiz = await response.json();
            console.log('랜덤 퀴즈:', quiz);
            currentVideo = quiz.code;
            playMusic(quiz.code, quiz.start); // 음악 재생 호출
        } else {
            console.error('랜덤 퀴즈 가져오기 실패');
        }
    } catch (error) {
        console.error('퀴즈 가져오는 중 오류:', error);
    }
}

// **음악 재생 함수: 비디오 화면 없이 음악만 재생**
function playMusic(videoCode, startTime) {
    const player = document.getElementById('youtube-player');
    const embedUrl = `https://www.youtube.com/embed/${videoCode}?autoplay=1&start=${startTime}&mute=0`;

    console.log(`비디오 재생 시작: ${videoCode}, 시작 시간: ${startTime}`);

    player.src = embedUrl; // iframe의 src를 설정하여 음악 재생
    isPlaying = true;

    // 재생 상태에 따른 아이콘 업데이트
    updatePlayIcon();
}

// **재생/중지 토글 함수**
function togglePlayPause() {
    const player = document.getElementById('youtube-player');

    if (isPlaying) {
        console.log('음악 중지');
        player.src = ''; // 음악 중지
        isPlaying = false;
    } else {
        console.log('음악 재개');
        const embedUrl = `https://www.youtube.com/embed/${currentVideo}?autoplay=1`;
        player.src = embedUrl; // 음악 재개
        isPlaying = true;
    }

    // 재생 상태에 따른 아이콘 업데이트
    updatePlayIcon();
}

// **재생 상태에 따른 아이콘 업데이트 함수**
function updatePlayIcon() {
    const musicIcon = document.querySelector('.quiz-room-music-icon i');

    if (isPlaying) {
        musicIcon.classList.remove('bi-play-circle-fill');
        musicIcon.classList.add('bi-pause-circle-fill');
    } else {
        musicIcon.classList.remove('bi-pause-circle-fill');
        musicIcon.classList.add('bi-play-circle-fill');
    }
}

// **채팅 메시지 전송**
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
}

// **채팅 메시지 표시**
function displayChatMessage(sender, content) {
    const chatMessages = document.getElementById('chat-messages');
    const messageElement = document.createElement('p');
    messageElement.innerHTML = `<strong>${sender}:</strong> ${content}`;
    chatMessages.appendChild(messageElement);
}

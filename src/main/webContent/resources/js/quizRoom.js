let stompClient;
const roomId = 1;

document.addEventListener('DOMContentLoaded', () => {
    const socket = new SockJS(`${root}/ws-stomp`);
    stompClient = Stomp.over(socket); // stompClient 초기화

    stompClient.connect({}, (frame) => {
        console.log('WebSocket 연결 성공:', frame);

        // 퀴즈 메시지 수신 처리
        stompClient.subscribe(`/sub/quiz/${roomId}`, (message) => {
            const msg = JSON.parse(message.body);
            console.log('퀴즈 메시지 수신:', msg);
            displayQuiz(msg); // 수신한 퀴즈 데이터 표시
        });

        // 채팅 메시지 수신 처리
        stompClient.subscribe(`/sub/chat/${roomId}`, (message) => {
            const chat = JSON.parse(message.body);
            displayChatMessage(chat.sender, chat.content);
        });

        // 플레이어 목록 가져오기 – 연결 후 바로 호출
        fetchPlayers();
    }, (error) => {
        console.error('WebSocket 연결 실패:', error);
    });

    // 게임 시작 버튼 이벤트 등록
    document.getElementById('start-quiz-btn').addEventListener('click', startQuiz);

    // 채팅 전송 버튼 이벤트 등록
    document.getElementById('send-chat-btn').addEventListener('click', sendMessage);

    // Enter 키로 채팅 메시지 전송
    document.getElementById('chat-input').addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });
});

// **플레이어 목록 가져오기 함수**
async function fetchPlayers() {
    try {
        const response = await fetch(`${root}/quiz/rooms/${roomId}/players`);
        if (response.ok) {
            const data = await response.json();
            console.log('플레이어 목록:', data.players);
            displayPlayers(data.players);
        } else {
            console.error('플레이어 목록 가져오기 실패');
        }
    } catch (error) {
        console.error('플레이어 목록 가져오는 중 오류:', error);
    }
}

// 플레이어 목록 화면에 표시하는 함수
function displayPlayers(players) {
    const playerList = document.getElementById('players');
    playerList.innerHTML = ''; // 기존 목록 초기화

    players.forEach((player) => {
        const playerElement = document.createElement('li');
        playerElement.innerHTML = `${player} <span class="score">0</span>`; // 초기 점수 0 설정
        playerList.appendChild(playerElement);
    });
}

// **게임 시작 함수 (랜덤 퀴즈 가져오기)**
async function startQuiz() {
    try {
        console.log(`Fetching quiz from: ${root}/quiz/rooms/${roomId}/random`); // 로그 추가
        const response = await fetch(`${root}/quiz/rooms/${roomId}/random`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            const quiz = await response.json();
            console.log('랜덤 퀴즈:', quiz);
            displayQuiz(quiz); // 퀴즈 표시 함수 호출
        } else {
            const errorData = await response.json();
            console.error('랜덤 퀴즈 가져오기 실패:', errorData); // 오류 로그 추가
            alert('랜덤 퀴즈 가져오기 실패: ' + errorData.message);
        }
    } catch (error) {
        console.error('랜덤 퀴즈 가져오는 중 오류:', error);
        alert('퀴즈를 가져오는 중 오류가 발생했습니다.');
    }
}

// **퀴즈 표시 함수**
function displayQuiz(quiz) {
    const quizArea = document.querySelector('#quiz-area');
    quizArea.innerHTML = `
        <p>${quiz.musicName}</p>
        <p>태그: ${quiz.tags}</p>
        <button id="play-music">음악 재생</button>
    `;

    document.getElementById('play-music').addEventListener('click', () => {
        playMusic(quiz.videoCode, quiz.startTime);
    });
}

// **음악 재생 함수**
function playMusic(videoCode, startTime) {
    const url = `https://www.youtube.com/embed/${videoCode}?start=${startTime}&autoplay=1`;
    window.open(url, '_blank');
}

// **채팅 메시지 전송 함수**
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

// **채팅 메시지 표시 함수**
function displayChatMessage(sender, content) {
    const chatMessages = document.getElementById('chat-messages');
    const messageElement = document.createElement('p');
    messageElement.innerHTML = `<strong>${sender}:</strong> ${content}`;
    chatMessages.appendChild(messageElement);
}

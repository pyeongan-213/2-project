document.addEventListener('DOMContentLoaded', () => {
    // WebSocket 연결
    const socket = new SockJS(`${root}/ws-stomp`);
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // 퀴즈방의 모든 유저가 메시지를 수신할 수 있는 구독
        stompClient.subscribe(`/sub/quizRoom/${roomId}`, function (message) {
            const data = JSON.parse(message.body);
            
            // 퀴즈 문제 수신 시 화면에 표시
            if (data.quizQuestion) {
                document.getElementById('quiz-text').textContent = data.quizQuestion;
            }
            
            // 채팅 메시지 수신 시 채팅창에 표시
            if (data.chatMessage) {
                document.getElementById('chat-messages').innerHTML += `<p>${data.chatMessage}</p>`;
            }
            
            // 정답 여부 알림
            if (data.correct !== undefined) {
                if (data.correct) {
                    alert('정답입니다!');
                } else {
                    alert('오답입니다.');
                }
            }
        });
    }, function (error) {
        console.error('WebSocket 연결 실패: ', error);
    });

    // 게임 시작 버튼 이벤트
    const startQuizBtn = document.getElementById('start-quiz-btn');
    if (startQuizBtn) {
        startQuizBtn.addEventListener('click', () => {
            // WebSocket을 통해 퀴즈 시작 요청 전송
            stompClient.send("/pub/quiz/start", {}, JSON.stringify({
                roomId: roomId
            }));
        });
    }

    // 로비로 이동 버튼 이벤트
    const goLobbyBtn = document.getElementById('go-lobby-btn');
    if (goLobbyBtn) {
        goLobbyBtn.addEventListener('click', () => {
            leaveRoom(); // 방 나가기 요청
            window.location.href = `${root}/quiz/quizlobby`; // 로비 페이지로 이동
        });
    }

    // 페이지 떠나기 전(뒤로가기, 새로고침) 이벤트 처리
    window.addEventListener('beforeunload', () => {
        leaveRoom();
    });

    // 방을 나갈 때 서버에 요청을 보내는 함수 (AJAX 방식 유지)
    function leaveRoom() {
        fetch(`${root}/quiz/rooms/leave`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ roomId: roomId }) // 방 ID 전달
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                console.log('방을 나갔습니다.');
            }
        })
        .catch(error => {
            console.error('방 나가기 오류:', error);
        });
    }

    // 정답 제출 버튼 이벤트
    const submitAnswerBtn = document.getElementById('submit-answer-btn');
    if (submitAnswerBtn) {
        submitAnswerBtn.addEventListener('click', () => {
            const answer = document.getElementById('quiz-answer').value.trim();

            if (!answer) {
                alert('정답을 입력해주세요.');
                return;
            }

            // WebSocket을 통해 정답 제출 요청 전송
            stompClient.send("/pub/quiz/submitAnswer", {}, JSON.stringify({
                roomId: roomId,
                answer: answer
            }));
        });
    }

    // 채팅 전송 버튼 이벤트
    const sendChatBtn = document.getElementById('send-chat-btn');
    if (sendChatBtn) {
        sendChatBtn.addEventListener('click', () => {
            const message = document.getElementById('chat-input').value.trim();

            if (!message) {
                alert('메시지를 입력해주세요.');
                return;
            }

            // WebSocket을 통해 채팅 메시지 전송
            stompClient.send("/pub/quiz/sendMessage", {}, JSON.stringify({
                roomId: roomId,
                message: message
            }));

            // 채팅창에 메시지 표시 (자기 자신)
            document.getElementById('chat-messages').innerHTML += `<p>${message}</p>`;
            document.getElementById('chat-input').value = ''; // 입력 필드 초기화
        });
    }
});

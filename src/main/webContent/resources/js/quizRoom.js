document.addEventListener('DOMContentLoaded', () => {
	// 게임 시작 버튼 이벤트
	const startQuizBtn = document.getElementById('start-quiz-btn');
	if (startQuizBtn) {
		startQuizBtn.addEventListener('click', () => {
			fetch(`${root}/quiz/rooms/start`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ roomId: roomId }) // 방 ID 전달
			})
				.then(response => {
					if (!response.ok) {
						throw new Error('퀴즈 시작 요청에 실패했습니다.');
					}
					return response.json();
				})
				.then(data => {
					if (data.success) {
						alert('퀴즈가 시작되었습니다!');
						// 퀴즈 문제 표시
						document.getElementById('quiz-text').textContent = data.quizQuestion;
					} else {
						alert('퀴즈 시작에 실패했습니다.');
					}
				})
				.catch(error => {
					console.error('퀴즈 시작 오류:', error);
					alert('퀴즈 시작 중 오류가 발생했습니다.');
				});
		});
	}

	// 로비로 이동 버튼 이벤트
	const goLobbyBtn = document.getElementById('go-lobby-btn');
	if (goLobbyBtn) {
		goLobbyBtn.addEventListener('click', () => {
			leaveRoom(); // 방 나가기 요청
			console.log(`Navigating to: ${root}/quiz/quizlobby`);
			window.location.href = `${root}/quiz/quizlobby`; // 로비 페이지로 이동
		});
	}

	// 페이지 떠나기 전(뒤로가기, 새로고침) 이벤트 처리
	window.addEventListener('beforeunload', () => {
		leaveRoom();
	});

	// 방을 나갈 때 서버에 요청을 보내는 함수
	function leaveRoom() {
		fetch(`${root}/quiz/rooms/leave`, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ roomId: roomId }) // 방 ID 전달
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('방 나가기 요청에 실패했습니다.');
				}
				return response.json();
			})
			.then(data => {
				if (data.success) {
					console.log('방을 나갔습니다.');
				} else {
					console.error('방 나가기에 실패했습니다:', data.message);
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

			fetch(`${root}/quiz/rooms/submitAnswer`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ roomId: roomId, answer: answer }) // 방 ID와 정답 전달
			})
				.then(response => {
					if (!response.ok) {
						throw new Error('정답 제출 요청에 실패했습니다.');
					}
					return response.json();
				})
				.then(data => {
					if (data.correct) {
						alert('정답입니다!');
					} else {
						alert('오답입니다.');
					}
				})
				.catch(error => {
					console.error('정답 제출 오류:', error);
				});
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

			fetch(`${root}/quiz/rooms/sendMessage`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ roomId: roomId, message: message }) // 방 ID와 메시지 전달
			})
				.then(response => {
					if (!response.ok) {
						throw new Error('메시지 전송 요청에 실패했습니다.');
					}
					return response.json();
				})
				.then(data => {
					if (data.success) {
						document.getElementById('chat-messages').innerHTML += `<p>${data.message}</p>`;
						document.getElementById('chat-input').value = '';
					} else {
						alert('메시지 전송에 실패했습니다.');
					}
				})
				.catch(error => {
					console.error('메시지 전송 오류:', error);
				});
		});
	}
});

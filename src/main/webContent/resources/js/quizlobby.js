let currentRoomId = null; // 현재 방의 ID를 저장할 전역 변수

document.addEventListener('DOMContentLoaded', () => {
	// 페이지가 로드될 때 방 목록을 불러옴
	fetchRooms();

	// 방 생성 버튼 클릭 이벤트
	const createRoomBtn = document.getElementById('create-room-btn');
	if (createRoomBtn) {
		createRoomBtn.addEventListener('click', createRoom);
	}

	// 로비로 돌아가기 버튼 클릭 이벤트
	const backToLobbyBtn = document.getElementById('back-to-lobby-btn');
	if (backToLobbyBtn) {
		backToLobbyBtn.addEventListener('click', () => {
			leaveRoom(); // 방을 떠나는 요청
		});
	}
});

// 방 목록을 불러오는 함수 (프로미스 반환)
function fetchRooms() {
	return fetch(`${root}/quiz/rooms`)
		.then(response => {
			console.log('Fetch rooms response:', response); // 응답 상태 확인
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log('Fetch rooms data:', data); // 응답 데이터 확인
			if (data && data.quizRoomBeanList) {
				updateRoomList(data.quizRoomBeanList);
			} else {
				console.error('방 목록 불러오기 실패:', data.message || '알 수 없는 오류');
			}
		})
		.catch(error => {
			console.error('방 목록 불러오기 오류:', error);
		});
}

// 방 목록을 업데이트하는 함수
function updateRoomList(rooms) {
	const roomListElement = document.getElementById('room-list');
	roomListElement.innerHTML = ''; // 기존 목록을 초기화

	rooms.forEach(room => {
		const listItem = document.createElement('li');
		listItem.innerHTML = `
            <div class="room-info">
                <span class="room-name">${room.quizRoomName}</span>
                <span class="room-users">${room.memberCount} / 10명</span>
                <button class="join-room-btn" data-room-id="${room.quizRoomId}" 
                    data-requires-password="${room.quizRoomPassword ? true : false}">
                    참여
                </button>
            </div>`;
		roomListElement.appendChild(listItem);
	});

	// 동적으로 생성된 방 참여 버튼에 클릭 이벤트 추가
	addJoinRoomEventListeners();
}

// 방 생성 함수
function createRoom() {
	const roomName = prompt("생성할 방의 이름을 입력하세요:", "새로운 퀴즈방");
	if (!roomName) {
		alert('방 이름을 입력해야 합니다.');
		return;
	}

	let roomPassword = prompt("방의 비밀번호를 입력하세요 (빈칸으로 두면 비밀번호 없이 생성됩니다):");
	if (roomPassword === null) {
		roomPassword = "";
	}

	fetch(`${root}/quiz/rooms/create`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({
			quizRoomName: roomName,
			quizRoomPassword: roomPassword
		})
	})
		.then(response => {
			console.log('Create room response:', response);
			if (!response.ok) {
				// 401 에러 처리: 로그인 필요 메시지 표시
				if (response.status === 401) {
					throw new Error('로그인 후 사용해 주세요.');
				}
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log('Create room data:', data);
			if (data.success) {
				alert('방이 생성되었습니다!');
				currentRoomId = data.roomId;
				window.location.href = `${root}/quiz/rooms/${data.roomId}`;
			} else {
				alert('방 생성에 실패했습니다: ' + (data.message || '알 수 없는 오류'));
			}
		})
		.catch(error => {
			console.error('방 생성 오류:', error);
			alert('방 생성 중 오류가 발생했습니다: ' + error.message);
		});
}

// 방 참여 이벤트 리스너 추가 함수
function addJoinRoomEventListeners() {
	document.querySelectorAll('.join-room-btn').forEach(button => {
		button.addEventListener('click', () => {
			console.log('참여 버튼 클릭됨!');
			const roomId = button.getAttribute('data-room-id');
			const requiresPassword = button.getAttribute('data-requires-password') === 'true';

			if (!roomId) {
				alert('방 ID를 찾을 수 없습니다.');
				return;
			}

			let roomPassword = '';
			if (requiresPassword) {
				roomPassword = prompt("방의 비밀번호를 입력하세요:");
				if (roomPassword === null) {
					alert('방 참여가 취소되었습니다.');
					return;
				}
			}

			joinRoom(roomId, roomPassword);
		});
	});
}

// 방 참여 함수
function joinRoom(roomId, roomPassword = '') {
	console.log(`Joining room with ID: ${roomId}, Password: ${roomPassword}, UserID: ${currentUserId}`);

	if (typeof currentUserId === 'undefined' || !currentUserId) {
		console.error('currentUserId is not defined or invalid');
		alert('로그인 후 사용해 주세요.');
		return;
	}

	const requestData = {
		roomId: roomId,
		roomPassword: roomPassword,
		userId: currentUserId
	};
	console.log('Request Data:', requestData);

	fetch(`${root}/quiz/rooms/join`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(requestData)
	})
		.then(response => {
			console.log('Join room response:', response);
			if (!response.ok) {
				// 401 에러 처리: 로그인 필요 메시지 표시
				if (response.status === 401) {
					throw new Error('로그인 후 사용해 주세요.');
				}
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			console.log('Join room data:', data);
			if (data.success) {
				alert('방에 참여하였습니다!');
				currentRoomId = roomId;
				window.location.href = `${root}/quiz/rooms/${roomId}`;
			} else {
				alert('방 참여에 실패했습니다: ' + (data.message || '알 수 없는 오류'));
			}
		})
		.catch(error => {
			console.error('방 참여 오류:', error);
			alert('방 참여 중 오류가 발생했습니다: ' + error.message);
		});
}

// 방 나가기 함수
function leaveRoom() {
	if (!currentRoomId || !currentUserId) {
		console.error('방 ID 또는 사용자 ID가 없습니다.');
		return;
	}

	fetch(`${root}/quiz/rooms/leave`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ roomId: currentRoomId, userId: currentUserId })
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			if (data.success) {
				alert('로비로 돌아갑니다.');
				currentRoomId = null; // 방 나가기 성공 시 현재 방 ID 초기화

				// 지연 시간 추가 (3~5초)
				setTimeout(() => {
					// 방 목록 갱신 후 로비로 이동
					fetchRooms().then(() => {
						window.location.href = `${root}/quiz/quizlobby`;
					});
				}, 5000); // 5000ms = 5초 지연
			} else {
				alert('방 나가기에 실패했습니다: ' + (data.message || '알 수 없는 오류'));
			}
		})
		.catch(error => {
			console.error('방 나가기 오류:', error);
			alert('방 나가기 중 오류가 발생했습니다: ' + error.message);
		});
}

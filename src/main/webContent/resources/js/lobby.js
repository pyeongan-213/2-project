let stompClient = null;

function connect() {
	const socket = new SockJS('/ws'); // WebSocket 엔드포인트
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/roomList', function(message) {
			updateRoomList(JSON.parse(message.body));
		});
	});
}

function updateRoomList(rooms) {
	const roomListDiv = document.getElementById('room-list');
	roomListDiv.innerHTML = ''; // 기존 목록 초기화

	rooms.forEach(room => {
		const roomItem = document.createElement('div');
		roomItem.classList.add('room-item');
		roomItem.innerHTML = `<h3>${room.roomName} (${room.currentUsers}/${room.maxUsers})</h3>`;
		roomListDiv.appendChild(roomItem);
	});
}

function refreshRoomList() {
	fetch('/lobby/rooms')
		.then(response => response.text())
		.then(html => {
			document.getElementById('room-list').innerHTML = html;
		})
		.catch(error => console.error('Error:', error));
}

// 페이지가 로드되면 WebSocket 연결을 시작합니다.
window.onload = function() {
	connect();
};

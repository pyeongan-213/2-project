let currentRoomId = null; // 현재 방의 ID를 저장할 전역 변수

document.addEventListener('DOMContentLoaded', () => {
    fetchRooms(); // 페이지 로드 시 방 목록 불러오기

    const createRoomBtn = document.getElementById('create-room-btn');
    if (createRoomBtn) {
        createRoomBtn.addEventListener('click', openCreateRoomModal); // 방 생성 버튼 클릭 시 모달 열기
    }

    const createRoomForm = document.getElementById('create-room-form');
    if (createRoomForm) {
        createRoomForm.addEventListener('submit', createRoom); // 방 생성 폼 제출 시 방 생성
    }

    // 모달 닫기 버튼
    const closeModalBtn = document.querySelector('.close');
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', () => {
            closeModal();
        });
    }

    window.addEventListener('click', (event) => {
        const modal = document.getElementById('create-room-modal');
        if (event.target === modal) {
            closeModal();
        }
    });
});

// 방 목록을 불러오는 함수
async function fetchRooms() {
    try {
        const response = await fetch(`${root}/quiz/rooms`);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        if (data && data.quizRoomBeanList) {
            updateRoomList(data.quizRoomBeanList);
        } else {
            console.error('방 목록 불러오기 실패:', data.message || '알 수 없는 오류');
        }
    } catch (error) {
        console.error('방 목록 불러오기 오류:', error);
    }
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
                <span class="room-users">${room.memberCount} / ${room.maxCapacity}명</span>
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

// 모달 창 열기 함수 (방 생성)
function openCreateRoomModal() {
    const modal = document.getElementById('create-room-modal');
    modal.style.display = 'block';
}

// 모달 창 닫기 함수
function closeModal() {
    const modal = document.getElementById('create-room-modal');
    modal.style.display = 'none';
}

// 방 생성 함수
async function createRoom(event) {
    event.preventDefault(); // 폼 기본 제출 방지

    const roomName = document.getElementById('roomName').value;
    const maxCapacity = document.getElementById('maxCapacity').value;
    const maxMusic = document.getElementById('maxMusic').value;
    const quizType = document.getElementById('quizType').value;

    if (!roomName) {
        alert('방 이름을 입력해야 합니다.');
        return;
    }

    try {
        const response = await fetch(`${root}/quiz/rooms/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                quizRoomName: roomName,
                maxCapacity: maxCapacity,
                maxMusic: maxMusic,
                quizQuestionType: quizType
            })
        });
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        if (data.success) {
            alert('방이 생성되었습니다!');
            currentRoomId = data.roomId;
            closeModal(); // 모달 창 닫기
            window.location.href = `${root}/quiz/rooms/${data.roomId}`;
        } else {
            alert('방 생성에 실패했습니다: ' + (data.message || '알 수 없는 오류'));
        }
    } catch (error) {
        console.error('방 생성 오류:', error);
        alert('방 생성 중 오류가 발생했습니다: ' + error.message);
    }
}

// 방 참여 이벤트 리스너 추가 함수
function addJoinRoomEventListeners() {
    document.querySelectorAll('.join-room-btn').forEach(button => {
        button.addEventListener('click', () => {
            const roomId = button.getAttribute('data-room-id');
            const requiresPassword = button.getAttribute('data-requires-password') === 'true';

            if (!roomId) {
                alert('방 ID를 찾을 수 없습니다.');
                return;
            }

            if (requiresPassword) {
                openPasswordModal(roomId);
            } else {
                joinRoom(roomId);
            }
        });
    });
}

// 비밀번호 입력 모달 열기
function openPasswordModal(roomId) {
    const passwordModal = document.getElementById('password-modal');
    passwordModal.style.display = 'block';

    document.getElementById('submit-password').onclick = function() {
        const roomPassword = document.getElementById('roomPassword').value;
        joinRoom(roomId, roomPassword);
        passwordModal.style.display = 'none';
    };

    document.querySelector('.close-password-modal').onclick = function() {
        passwordModal.style.display = 'none';
    };
}

// 방 참여 함수
async function joinRoom(roomId, roomPassword = '') {
    if (!currentUserId) {
        alert('로그인 후 사용해 주세요.');
        return;
    }

    const requestData = {
        roomId: roomId,
        roomPassword: roomPassword,
        userId: currentUserId
    };

    try {
        const response = await fetch(`${root}/quiz/rooms/join`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestData)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        if (data.success) {
            alert('방에 참여하였습니다!');
            currentRoomId = roomId;
            window.location.href = `${root}/quiz/rooms/${roomId}`;
        } else {
            alert('방 참여에 실패했습니다: ' + (data.message || '알 수 없는 오류'));
        }
    } catch (error) {
        console.error('방 참여 오류:', error);
        alert('방 참여 중 오류가 발생했습니다: ' + error.message);
    }
}

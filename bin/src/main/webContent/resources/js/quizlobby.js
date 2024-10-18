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

    selectMaxMusicEventListeners(); // 곡 수 선택 버튼 이벤트 등록

    const closeModalBtn = document.querySelector('.close');
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal); // 모달 닫기 버튼 클릭 시 모달 닫기
    }

    window.addEventListener('click', (event) => {
        const modal = document.getElementById('create-room-modal');
        if (event.target === modal) {
            closeModal(); // 외부 클릭 시 모달 닫기
        }
    });
});

// 방 목록 불러오기
async function fetchRooms() {
    try {
        const response = await fetch(`${root}/quiz/rooms`);
        if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

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

// 방 목록 업데이트
function updateRoomList(rooms) {
    const roomListElement = document.getElementById('room-list');
    roomListElement.innerHTML = ''; // 기존 목록 초기화

    rooms.forEach(room => {
        const listItem = document.createElement('li');
        listItem.innerHTML = `
           <div class="room-info">
                <div class="room-details">
                    <!-- 방 이름 표시 -->
                    <span class="room-name">${room.quizRoomName}</span>
                    <!-- 방장 이름을 'owner의 방' 형식으로 표시 -->
                    <span class="room-owner">(${room.owner}의 방)</span>
                    <!-- 인원 수 표시 -->
                    <span class="room-users">${room.memberCount} / ${room.maxCapacity}명</span>
                </div>
                <button class="join-room-btn" 
                    data-room-id="${room.quizRoomId}" 
                    data-requires-password="${room.quizRoomPassword ? 'true' : 'false'}">
                    참여
                </button>
            </div>`;
        roomListElement.appendChild(listItem);
    });

    addJoinRoomEventListeners(); // 참여 버튼에 이벤트 리스너 추가
}

// 모달 열기
function openCreateRoomModal() {
    document.getElementById('create-room-modal').style.display = 'block';
}

// 모달 닫기
function closeModal() {
    document.getElementById('create-room-modal').style.display = 'none';
}

// 곡 수 선택 이벤트 리스너 추가
function selectMaxMusicEventListeners() {
    const buttons = document.querySelectorAll('.music-button');
    const maxMusicInput = document.getElementById('maxMusic');

    buttons.forEach(button => {
        button.addEventListener('click', () => {
            buttons.forEach(btn => btn.classList.remove('selected')); // 모든 버튼에서 선택 해제
            button.classList.add('selected'); // 선택된 버튼에 클래스 추가
            maxMusicInput.value = button.getAttribute('data-value'); // 선택한 값 설정
        });
    });
}

// 방 생성 처리
async function createRoom(event) {
    event.preventDefault(); // 폼 기본 제출 방지

  	const roomName = document.getElementById('roomName').value;
    const maxCapacity = document.getElementById('maxCapacity').value;
    const maxMusic = document.getElementById('maxMusic').value;
    const quizRoomType = document.getElementById('quizRoomType').value;

    if (!roomName || !maxCapacity || !maxMusic || !quizRoomType) {
        console.error('필수 입력 요소가 DOM에 없습니다.');
        alert('필수 입력 요소를 확인해주세요.');
        return;
    }
 console.log(`방 이름: ${roomName}, 최대 인원: ${maxCapacity}, 최대 곡 수: ${maxMusic}, 퀴즈 타입: ${quizRoomType}`); // 확인용 로그
   

    try {
        const response = await fetch(`${root}/quiz/rooms/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                quizRoomName: roomName,
                maxCapacity: maxCapacity,
                maxMusic: maxMusic,
                quizRoomType: quizRoomType
            })
        });

        if (response.ok) {
            const data = await response.json();
            alert('방이 생성되었습니다!');
            currentRoomId = data.roomId;
            console.log(`[INFO] 생성된 방 ID: ${currentRoomId}`); // 확인용 로그
            closeModal(); // 모달 닫기
            window.location.href = `${root}/quiz/rooms/${data.roomId}`;
        } else {
            alert('방 생성에 실패했습니다.');
        }
    } catch (error) {
        console.error('방 생성 오류:', error);
        alert('방 생성 중 오류가 발생했습니다.');
    }
}

// 참여 버튼 이벤트 리스너 추가
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
                openPasswordModal(roomId); // 비밀번호가 필요하면 모달 열기
            } else {
                joinRoom(roomId); // 아니면 바로 참여
            }
        });
    });
}

// 비밀번호 입력 모달 열기
function openPasswordModal(roomId) {
    const passwordModal = document.getElementById('password-modal');
    passwordModal.style.display = 'block';

    document.getElementById('submit-password').onclick = function () {
        const roomPassword = document.getElementById('roomPassword').value;
        joinRoom(roomId, roomPassword); // 비밀번호와 함께 참여 시도
        passwordModal.style.display = 'none';
    };

    document.querySelector('.close-password-modal').onclick = function () {
        passwordModal.style.display = 'none';
    };
}

// 방 참여 처리
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

        if (response.ok) {
            alert('방에 참여하였습니다!');
            currentRoomId = roomId;
            window.location.href = `${root}/quiz/rooms/${roomId}`;
        } else {
            alert('방 참여에 실패했습니다.');
        }
    } catch (error) {
        console.error('방 참여 오류:', error);
        alert('방 참여 중 오류가 발생했습니다.');
    }
}

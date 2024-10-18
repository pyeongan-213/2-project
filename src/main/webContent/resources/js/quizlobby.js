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
        const currentMembers = room.memberCount; // 현재 인원 수
        const maxCapacity = room.maxCapacity; // 최대 인원 수

        // 최대 인원 표시할 때 방장의 수를 고려하여 계산된 최대 인원으로 표시 (1명 추가)
        const displayedMaxCapacity = maxCapacity + 1; // 방장 포함

        const listItem = document.createElement('li');
        listItem.innerHTML = `
           <div class="room-info">
                <div class="room-details">
                    <!-- 방 이름 표시 -->
                    <span class="room-name">${room.quizRoomName}</span>
                    <!-- 방장 이름을 'owner의 방' 형식으로 표시 -->
                    <span class="room-owner">(${room.owner}의 방)</span>
                    <!-- 인원 수 표시 -->
                    <span class="room-users">${currentMembers} / ${displayedMaxCapacity}명</span>
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
    const roomNameInput = document.getElementById('roomName');
    const maxCapacityInput = document.getElementById('maxCapacity');
    const maxMusicInput = document.getElementById('maxMusic');

    // 방 이름과 최대 인원 디폴트 값 설정
    roomNameInput.value = '새로운 퀴즈방';
    maxCapacityInput.value = '10';
    
    // 100곡 버튼 기본 선택 설정
    const defaultMusicButton = document.querySelector('.music-button[data-value="100"]');
    if (defaultMusicButton) {
        defaultMusicButton.click(); // 100곡 버튼 클릭하여 선택
    }

    // 디폴트 값 드래그 상태 설정
    roomNameInput.focus();
    roomNameInput.select();
    maxCapacityInput.select();

    // 방 이름 입력창 클릭 시 빈칸으로 초기화
    roomNameInput.addEventListener('click', () => {
        if (roomNameInput.value === '새로운 퀴즈방') {
            roomNameInput.value = ''; // 기본값일 때만 빈칸으로
        }
    });

    // 최대 인원 입력창 클릭 시 빈칸으로 초기화
    maxCapacityInput.addEventListener('click', () => {
        if (maxCapacityInput.value === '10') {
            maxCapacityInput.value = ''; // 기본값일 때만 빈칸으로
        }
    });

    document.getElementById('create-room-modal').style.display = 'block';
}

// 모달 닫기
function closeModal() {
    document.getElementById('create-room-modal').style.display = 'none';
}

document.addEventListener('DOMContentLoaded', () => {
    const modalContent = document.querySelector('.modal-content');
    let isDragging = false;
    let offsetX = 0;
    let offsetY = 0;

    // 모달 상단을 드래그할 수 있도록 이벤트 추가 (모달의 헤더 또는 제목 부분)
    modalContent.addEventListener('mousedown', (e) => {
        isDragging = true;
        offsetX = e.clientX - modalContent.getBoundingClientRect().left;
        offsetY = e.clientY - modalContent.getBoundingClientRect().top;
        modalContent.style.position = 'absolute';  // 드래그 가능하도록 position 설정
        modalContent.style.cursor = 'move';  // 드래그 중 커서 변경
    });

    // 마우스 움직임에 따라 모달 이동
    document.addEventListener('mousemove', (e) => {
        if (isDragging) {
            modalContent.style.left = `${e.clientX - offsetX}px`;
            modalContent.style.top = `${e.clientY - offsetY}px`;
        }
    });

    // 마우스를 떼면 드래그 멈춤
    document.addEventListener('mouseup', () => {
        isDragging = false;
        modalContent.style.cursor = 'default';  // 드래그 끝난 후 커서 기본값으로
    });
});


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

    const roomNameInput = document.getElementById('roomName');
    const maxCapacityInput = document.getElementById('maxCapacity');
    const maxMusicInput = document.getElementById('maxMusic');
    const quizRoomType = document.getElementById('quizRoomType').value;

    // 필드 값 읽기 (디폴트 값 포함)
    let roomName = roomNameInput.value;
    let maxCapacity = maxCapacityInput.value;
    let maxMusic = maxMusicInput.value || '100'; // 기본 값으로 '100곡' 선택

    // 필수 입력 요소가 비어있지 않은지 확인
    if (!roomName) {
        roomName = '새로운 퀴즈방';  // 디폴트 값 설정
    }

    if (!maxCapacity) {
        maxCapacity = '10';  // 디폴트 값 설정
    }

    // 최대 인원 수에서 서버가 기본적으로 한 명을 추가한 것 반영하여 1명 줄이기
    const adjustedMaxCapacity = parseInt(maxCapacity, 10) - 1;

    // 필수 입력 요소 확인
    if (!maxMusic || !quizRoomType) {
        console.error('필수 입력 요소가 누락되었습니다.');
        alert('필수 입력 요소를 확인해주세요.');
        return;
    }

    console.log(`방 이름: ${roomName}, 최대 인원: ${adjustedMaxCapacity}, 최대 곡 수: ${maxMusic}, 퀴즈 타입: ${quizRoomType}`);

    try {
        const response = await fetch(`${root}/quiz/rooms/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                quizRoomName: roomName,
                maxCapacity: adjustedMaxCapacity, // 조정된 인원 수 전송
                maxMusic: maxMusic,
                quizRoomType: quizRoomType
            })
        });

        if (response.ok) {
            const data = await response.json();
            alert('방이 생성되었습니다!');
            currentRoomId = data.roomId;
            closeModal(); // 모달 닫기
            window.location.href = `${root}/quiz/rooms/${data.roomId}`;
        } else {
            alert('방 생성에 실패했습니다.');
        }
    } catch (error) {
        console.error('방 생성 중 오류:', error);
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
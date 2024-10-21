let currentRoomId = null; // 현재 방의 ID를 저장할 전역 변수

document.addEventListener('DOMContentLoaded', () => {
    fetchRooms(); // 페이지 로드 시 방 목록 불러오기

    const createRoomBtn = document.getElementById('create-room-btn');
    if (createRoomBtn) {
        createRoomBtn.addEventListener('click', openCreateRoomModal); // 방 생성 버튼 클릭 시 모달 열기
    } else {
        console.error("#create-room-btn 요소를 찾을 수 없습니다.");
    }

    const createRoomForm = document.getElementById('create-room-form');
    if (createRoomForm) {
        createRoomForm.addEventListener('submit', createRoom); // 방 생성 폼 제출 시 방 생성
    } else {
        console.error("#create-room-form 요소를 찾을 수 없습니다.");
    }

    selectMaxMusicEventListeners(); // 곡 수 선택 버튼 이벤트 등록

    const closeModalBtn = document.querySelector('.close');
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal); // 모달 닫기 버튼 클릭 시 모달 닫기
    } else {
        console.error(".close 요소를 찾을 수 없습니다.");
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
    if (!roomListElement) {
        console.error("#room-list 요소를 찾을 수 없습니다.");
        return;
    }
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
    const modalContent = document.querySelector('.modal-content');

    // roomNameInput, maxCapacityInput이 존재할 경우에만 이벤트 추가
    if (roomNameInput) {
        roomNameInput.addEventListener('click', () => {
            if (roomNameInput.value === '새로운 퀴즈방') {
                roomNameInput.value = ''; // 기본값일 때만 빈칸으로
            }
        });
    } else {
        console.error("#roomName 요소를 찾을 수 없습니다.");
    }

    if (maxCapacityInput) {
        maxCapacityInput.addEventListener('click', () => {
            if (maxCapacityInput.value === '10') {
                maxCapacityInput.value = ''; // 기본값일 때만 빈칸으로
            }
        });
    } else {
        console.error("#maxCapacity 요소를 찾을 수 없습니다.");
    }

    // 100곡 버튼 기본 선택 설정
    const defaultMusicButton = document.querySelector('.music-button[data-value="100"]');
    if (defaultMusicButton) {
        defaultMusicButton.click(); // 100곡 버튼 클릭하여 선택
    }

    // 모달을 화면 중앙에 위치시킴
    if (modalContent) {
        modalContent.style.top = '50%';
        modalContent.style.left = '50%';
        modalContent.style.transform = 'translate(-50%, -50%)'; // 중앙으로 위치 조정
        modalContent.style.position = 'fixed'; // 중앙 고정
    } else {
        console.error(".modal-content 요소를 찾을 수 없습니다.");
    }

    const modal = document.getElementById('create-room-modal');
    if (modal) {
        modal.style.display = 'block';

        // Esc 키로 모달 닫기 기능 추가
        document.addEventListener('keydown', handleEscKey);
    } else {
        console.error("#create-room-modal 요소를 찾을 수 없습니다.");
    }
}

// Esc 키로 모달 닫기 핸들러
function handleEscKey(event) {
    if (event.key === 'Escape') {
        closeModal();
    }
}

// 모달 닫기
function closeModal() {
    const modal = document.getElementById('create-room-modal');
    if (modal) {
        modal.style.display = 'none';
    }
    
    // Esc 키 이벤트 리스너 제거 (중복 방지)
    document.removeEventListener('keydown', handleEscKey);
}

document.addEventListener('DOMContentLoaded', () => {
    const modalContent = document.querySelector('.modal-content');
    let isDragging = false;
    let offsetX = 0;
    let offsetY = 0;

    if (modalContent) {
        // 모달 상단을 드래그할 수 있도록 이벤트 추가
        modalContent.addEventListener('mousedown', (e) => {
            isDragging = true;

            // 드래그 시작 시 transform을 해제하고 현재 위치 기준으로 고정
            const rect = modalContent.getBoundingClientRect();
            modalContent.style.left = `${rect.left}px`;
            modalContent.style.top = `${rect.top}px`;
            modalContent.style.transform = 'none'; // 중앙 정렬 해제
            modalContent.style.position = 'absolute';  // 드래그 가능하도록 position 설정

            offsetX = e.clientX - rect.left;
            offsetY = e.clientY - rect.top;
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
    } else {
        console.error("modal-content를 찾을 수 없습니다.");
    }
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
    const roomPasswordInput = document.getElementById('roomPassword'); // 비밀번호 입력 필드

    // 필드 값 읽기 (디폴트 값 포함)
    let roomName = roomNameInput.value;
    let maxCapacity = maxCapacityInput.value;
    let maxMusic = maxMusicInput.value || '100'; // 기본 값으로 '100곡' 선택
    let roomPassword = roomPasswordInput.value || ''; // 비밀번호가 입력되지 않으면 공백 처리

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

    console.log(`방 이름: ${roomName}, 최대 인원: ${adjustedMaxCapacity}, 최대 곡 수: ${maxMusic}, 퀴즈 타입: ${quizRoomType}, 비밀번호: ${roomPassword}`);

    try {
        const response = await fetch(`${root}/quiz/rooms/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                quizRoomName: roomName,
                maxCapacity: adjustedMaxCapacity, // 조정된 인원 수 전송
                maxMusic: maxMusic,
                quizRoomType: quizRoomType,
                roomPassword: roomPassword // 비밀번호 전송
            })
        });

        if (response.ok) {
            const data = await response.json();
            alert('방이 생성되었습니다!');
            currentRoomId = data.roomId;
            closeModal(); // 모달 닫기
            window.location.href = `${root}/quiz/rooms/${data.roomId}`;
        } else {
            alert('로그인 후 이용해주세요.');
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
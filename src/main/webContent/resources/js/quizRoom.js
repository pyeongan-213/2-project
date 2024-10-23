// WebSocket 연결 및 퀴즈 방 관리 JS
let stompClient;  // WebSocket 클라이언트 객체
const roomId = getRoomIdFromPath();  // URL 경로에서 roomId 추출
let isPlaying = false;  // 음악 재생 상태
let currentVideoTime = 0; // 재생된 시간을 저장할 변수
let currentVideo = null;  // 현재 재생 중인 비디오 코드
let currentAnswers = [];  // 현재 퀴즈 정답 배열
let currentSongName = '';  // 현재 노래 제목
let countdownInterval = null;  // 카운트다운 인터벌
let hintIndex = 0;  // 힌트 인덱스
let currentAnswer = '';  // 현재 퀴즈 정답
let isStoppedByUser = false;  // 사용자가 음악을 중지했는지 여부
const roomName = '방 이름';
let currentUserNickname = window.currentUserNickname || "유저";  // JSP에서 전달된 닉네임을 사용, 없으면 기본값
let currentUserId = window.currentUserId || 1;  // JSP에서 전달된 ID를 사용, 없으면 기본값
let chatSubscription;  // 구독을 추적할 변수
let playerSubscription = null;  // 전역 변수로 선언하여 구독 상태 관리
let currentQuizType = 'songTitle'; // 기본값을 '노래 제목 맞히기'로 설정


// 효과음 객체 생성
const correctAnswerSound = new Audio(`${root}/audio/correct_answer.mp3`);
const startEffectSound = new Audio(`${root}/audio/starteffect.mp3`);
const changeTypeSound = new Audio(`${root}/audio/changType.mp3`);
const coineffectSound = new Audio(`${root}/audio/coineffect.mp3`);



// **경로에서 roomId 추출 함수**
function getRoomIdFromPath() {
	const path = window.location.pathname;
	const pathSegments = path.split('/');
	const roomIndex = pathSegments.indexOf('rooms');
	return roomIndex !== -1 && pathSegments.length > roomIndex + 1
		? pathSegments[roomIndex + 1]
		: 1;  // 기본값 1 반환

}

// **준비완료 효과음 재생 함수**
function playcoineffectSound() {
	coineffectSound.play().catch(error => {
		console.error('오디오 재생 오류:', error);
	});
}

// **정답 효과음 재생 함수**
function playCorrectAnswerSound() {
	correctAnswerSound.play().catch(error => {
		console.error('오디오 재생 오류:', error);
	});
}

// **게임시작 효과음 재생 함수**
function playstartEffectSound() {
	startEffectSound.play().catch(error => {
		console.error('오디오 재생 오류:', error);
	});
}

// **게임타입변경 효과음 함수**
function playchangeTypeSound() {
	changeTypeSound.play().catch(error => {
		console.error('오디오 재생 오류:', error);
	});
}

// **툴팁 표시 함수 (전역으로 정의)**
function showTooltip() {
	const tooltip = document.getElementById('command-tooltip');
	const maximizeButton = document.getElementById('tooltip-maximize-btn');

	tooltip.classList.remove('hidden');
	tooltip.classList.add('visible');
	maximizeButton.classList.add('hidden');
}

document.addEventListener('DOMContentLoaded', () => {
	const tooltip = document.getElementById('command-tooltip');
	const minimizeButton = document.getElementById('tooltip-minimize-btn');
	const maximizeButton = document.getElementById('tooltip-maximize-btn');

	function toggleTooltip() {
		const tooltip = document.getElementById('command-tooltip');
		const maximizeButton = document.getElementById('tooltip-maximize-btn');

		if (tooltip.classList.contains('minimized')) {
			console.log('최대화 버튼 클릭됨');
			tooltip.classList.remove('minimized');
			tooltip.classList.add('visible');
			tooltip.style.display = 'block';
			tooltip.style.visibility = 'visible';

			// 툴팁 위치를 고정값으로 리셋
			tooltip.style.top = '12%';  // 초기 top 위치로 리셋
			tooltip.style.left = '63.5%';  // 초기 left 위치로 리셋

			// 최대화 버튼 숨기기
			maximizeButton.style.display = 'none';
		} else {
			console.log('최소화 버튼 클릭됨');
			tooltip.classList.add('minimized');
			tooltip.classList.remove('visible');
			tooltip.style.display = 'none';
			tooltip.style.visibility = 'hidden';

			// 최대화 버튼의 위치도 리셋 (최소화된 위치 고정)
			maximizeButton.style.top = '90px';
			maximizeButton.style.left = '77%';
			maximizeButton.style.display = 'block';
		}
	}

	let isDragging = false;
	let offsetX, offsetY;

	tooltip.addEventListener('mousedown', (e) => {
		isDragging = true;
		dragStart = true;
		offsetX = e.clientX - tooltip.getBoundingClientRect().left;
		offsetY = e.clientY - tooltip.getBoundingClientRect().top;
		tooltip.classList.add('grabbing');
	});

	document.addEventListener('mousemove', (e) => {
		if (isDragging) {
			tooltip.style.left = `${e.clientX - offsetX}px`;
			tooltip.style.top = `${e.clientY - offsetY}px`;
		}
	});

	document.addEventListener('mouseup', () => {
		if (isDragging) {
			isDragging = false;
			tooltip.classList.remove('grabbing');
		}
		dragStart = false;
	});

	maximizeButton.addEventListener('mousedown', (e) => {
		isDragging = true;
		dragStart = true;
		offsetX = e.clientX - maximizeButton.getBoundingClientRect().left;
		offsetY = e.clientY - maximizeButton.getBoundingClientRect().top;
		maximizeButton.classList.add('grabbing');
	});

	document.addEventListener('mousemove', (e) => {
		if (isDragging) {
			maximizeButton.style.left = `${e.clientX - offsetX}px`;
			maximizeButton.style.top = `${e.clientY - offsetY}px`;
		}
	});

	document.addEventListener('mouseup', () => {
		if (isDragging) {
			isDragging = false;
			maximizeButton.classList.remove('grabbing');
		}
		dragStart = false;
	});

	minimizeButton.addEventListener('click', toggleTooltip);
	maximizeButton.addEventListener('click', toggleTooltip);
});

// 방에 참가하는 함수
function joinRoom(roomId) {
	fetch(`${root}/quiz/rooms/join`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({ roomId: roomId })  // 서버로 roomId를 전달
	})
		.then(response => response.json())
		.then(data => {
			console.log("서버 응답 확인:", data);

			if (data.success) {
				// 방에 입장 시 서버에서 받은 플레이어 목록을 수동으로 업데이트
				if (data.players && data.players.length > 0) {
					console.log("플레이어 목록 받음:", data.players);
					updatePlayerList(data.players);  // 첫 번째로 서버에서 받은 플레이어 목록 업데이트
				} else {
					console.log("플레이어 목록이 비어 있습니다.");
				}

				// WebSocket 연결은 여기서 구독함
				connectWebSocket();  // 방에 참가한 후 WebSocket 연결
			} else {
				console.error("방 참여 실패: ", data.message);
			}
		})
		.catch(error => console.error("방 참여 중 오류 발생: ", error));  // 에러 처리
}

// WebSocket 연결 및 구독 처리
function connectWebSocket() {
	if (stompClient && stompClient.connected) {
		console.log("WebSocket 이미 연결됨");
		return;
	}

	socket = new SockJS(`${root}/ws-stomp`);
	stompClient = Stomp.over(socket);

	stompClient.connect({}, (frame) => {
		console.log('WebSocket 연결 성공:', frame);

		stompClient.subscribe(`/sub/quiz/${roomId}`, (message) => {
			const msg = JSON.parse(message.body);
			console.log('받은 메시지:', msg);  // 메시지 내용을 로그로 확인

			// 데이터가 정상적인지 확인
			if (!msg.name || !msg.answer || msg.answer.length === 0) {
				console.error('퀴즈 데이터가 올바르지 않습니다.', msg);
			}

			currentVideo = msg.code;
			currentAnswers = msg.answer.map(answer => answer.trim().toLowerCase());
			currentSongName = msg.name;
			currentAnswer = msg.answer[0];  // 첫 번째 정답을 저장
			hintIndex = 0;

			playMusic(msg.code, msg.start);
		});



		// 채팅 구독
stompClient.subscribe(`/sub/chat/${roomId}`, (message) => {
    const chatMessage = JSON.parse(message.body);

    // 본인이 보낸 메시지는 처리하지 않음
    if (chatMessage.sender === currentUserNickname) {
        return;
    }

    const normalizedMessage = chatMessage.message ? chatMessage.message.trim().toLowerCase() : '';
    if (normalizedMessage === '') return;

    // 정답 여부 확인
    const isCorrectAnswer = currentAnswers.some(answer => isExactMatch(normalizedMessage, answer));
    displayChatMessage(chatMessage.sender, chatMessage.message, isCorrectAnswer);

    // !힌트 명령어가 입력되었는지 확인
    if (['!힌트', '!hint'].includes(normalizedMessage)) {
        console.log("힌트 명령어 감지됨");

        // 힌트 표시 함수 호출
        const hint = generateHint();  // 힌트를 생성하고
        displayHint(hint);  // 화면에 힌트를 표시
        
        if (startQuizButton) {
		startQuizButton.addEventListener('click', startQuiz);  // 새로운 리스너 추가
		 hidehint();
	}

      /*  // WebSocket으로 힌트 정보를 다른 참가자에게 전송
        stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
            sender: currentUserNickname,
            message: `힌트: ${hint}`,
            messageId: Date.now()  // 고유한 메시지 ID 생성
        }));*/
    }

    // !스킵 명령어가 입력되었는지 확인
    if (['!스킵', '!skip'].includes(normalizedMessage)) {
        console.log("스킵 명령어 감지됨");
        skipQuiz(chatMessage.sender);  // 스킵 함수 호출
    }
});

// **힌트 생성 함수**
function generateHint() {
    let hint = '';
    for (let i = 0; i < currentAnswer.length; i++) {
        // 현재 힌트 인덱스까지 글자를 표시하고 나머지는 'O'로 처리
        hint += i <= hintIndex ? currentAnswer[i] : 'O';
    }
    hintIndex++;  // 다음 힌트를 위해 인덱스를 증가시킴
    return hint;
}

// **힌트를 화면에 표시하는 함수**
function displayHint(hint) {
    const hintDisplay = document.getElementById('hint-info');
    hintDisplay.textContent = `힌트: ${hint}`;  // 힌트 내용을 표시
    hintDisplay.classList.remove('hidden');  // 힌트 영역을 표시
}

		
		// 퀴즈 타입 변경 정보 구독
		stompClient.subscribe(`/sub/quiz/${roomId}/changeType`, (message) => {
			const msg = JSON.parse(message.body);
			currentQuizType = msg.newQuizType;

			const typeLabel = currentQuizType === 'songTitle' ? '노래 제목 맞히기' : '가수 이름 맞히기';
			console.log(`게임 타입이 ${typeLabel}로 변경되었습니다.`);
		});

		/*stompClient.subscribe(`/sub/quiz/${roomId}/hintMessage`, (message) => {
			const hintMessage = JSON.parse(message.body);
			console.log('수신된 힌트 메시지:', hintMessage);
			displayHint(hintMessage.hint);  // 화면에 힌트 표시
		});*/


		// WebSocket에서 정답자 정보를 구독하여 처리
		stompClient.subscribe(`/sub/quizRoom/${roomId}/correctAnswer`, (message) => {
		    const correctAnswerMessage = JSON.parse(message.body);
		
		    // 정답자 정보를 화면에 표시
		    displayAnswerInfo(correctAnswerMessage.sender, correctAnswerMessage.songName);
		 	startCountdown();
		  });  
	
		// **정답 정보 표시 함수**
		function displayAnswerInfo(sender, songName) {
		    document.getElementById('correct-player').textContent = `정답자: ${sender}`;
		    document.getElementById('song-info').textContent = songName || currentSongName;
		    document.getElementById('answer-info').classList.remove('hidden');
			hidehint();
		}
		
		// 플레이어 목록 구독 설정
		if (!playerSubscription) {
		    playerSubscription = stompClient.subscribe(`/sub/quizRoom/${roomId}/players`, (message) => {
		        const chatMessage = JSON.parse(message.body);
		        
		        // 플레이어 목록 처리
		        if (chatMessage.type === 'PLAYER_LIST') {
		            updatePlayerList(chatMessage.message);
		        } else if (chatMessage.type === 'PLAYER_JOIN') {
		            addPlayerToList(chatMessage.sender);
		        } else if (chatMessage.type === 'PLAYER_LEAVE') {
		            removePlayerFromList(chatMessage.sender);
		        } 
		        
		        // 정답자 정보 처리
		        else if (chatMessage.type === 'CORRECT_ANSWER') {
		            const correctPlayer = chatMessage.sender;  // 정답자 닉네임
		            const songName = chatMessage.songName;  // 정답 노래 제목
		            displayCorrectPlayer(correctPlayer, songName);  // 정답자 화면에 표시
		        	hidehint();
		        }
		    });
		
		    // 서버에서 플레이어 목록 가져오기
		    fetch(`${root}/quiz/rooms/${roomId}/players`)
		        .then(response => response.json())
		        .then(data => {
		            if (data.success) {
		                updatePlayerList(data.players);
		            }
		        })
		        .catch(error => console.error("플레이어 목록 업데이트 오류:", error));
		}


		// 준비완료 상태 변경 구독
		stompClient.subscribe(`/sub/quiz/${roomId}/ready`, (message) => {
			const readyMessage = JSON.parse(message.body);
			const sender = readyMessage.sender;  // 닉네임으로 설정
			const isReady = readyMessage.isReady;
			updatePlayerReadyStatus(sender, isReady);  // 닉네임을 기반으로 준비 상태 업데이트
		});

	}, (error) => {
		console.error('WebSocket 연결 실패. 재연결 시도 중...', error);
		setTimeout(connectWebSocket, 5000);
	});
}

// 페이지 로드 후 WebSocket 연결 시도
document.addEventListener('DOMContentLoaded', () => {
	connectWebSocket();
});

// 플레이어 목록 갱신 함수
function updatePlayerList(players) {
    const playerListContainer = document.getElementById('players');  // 'players' id를 가진 ul 요소 선택
    playerListContainer.innerHTML = '';  // 기존 목록 초기화

    // 플레이어 목록을 순회하며 li 요소로 추가
    players.forEach((player, index) => {
        const playerItem = document.createElement('li');  // 각 플레이어를 li 요소로 생성
        playerItem.className = 'player-item';  // li 요소에 기본 클래스 설정

        // 첫 번째 플레이어를 방장으로 설정
        if (index === 0) {
            playerItem.classList.add('host-player');  // 첫 번째 플레이어에 방장 클래스를 추가
        }

        // 플레이어 닉네임 설정
        playerItem.textContent = player;  // 닉네임으로 처리

        playerListContainer.appendChild(playerItem);  // ul 요소에 li 요소 추가
    });
}



function updatePlayerReadyStatus(playerName, isReady) {
	const playerItems = document.querySelectorAll('.player-item');

	playerItems.forEach(item => {
		const playerNickname = item.textContent.trim();  // 닉네임을 가져옴
		// playerNickname과 입력된 playerName이 일치하는지 확인
		if (playerNickname === playerName) {
			if (isReady) {
				item.classList.add('ready');
				if (!item.textContent.includes('✔')) {
					item.textContent += ' ✔';  // 준비완료 체크 표시 추가
				}
			} else {
				item.classList.remove('ready');
				item.textContent  = item.textContent.replace(' ✔', '');  // 준비취소 시 체크 표시 제거
			}
		}
	});
}


// 플레이어 목록에 새로운 항목을 추가하는 함수
function addPlayerToList(playerName, isReady = false) {
	const playerListContainer = document.getElementById('players');
	const playerItems = playerListContainer.getElementsByClassName('player-item');

	// 중복 확인
	for (let i = 0; i < playerItems.length; i++) {
		if (playerItems[i].textContent.trim() === playerName.trim()) {
			return;  // 이미 존재하는 경우 추가하지 않음
		}
	}

	// 새로운 플레이어 아이템 생성
	const playerItem = document.createElement('li');
	playerItem.className = 'player-item';
	playerItem.textContent = playerName;

	// 준비 상태일 경우 체크 표시 추가
	if (isReady) {
		const checkmark = document.createElement('span');
		checkmark.textContent = ' ✔';
		checkmark.style.color = 'green';
		playerItem.appendChild(checkmark);
	}

	playerListContainer.appendChild(playerItem);  // 리스트에 플레이어 추가
}

// 플레이어가 퇴장했을 때 목록에서 제거하는 함수
function removePlayerFromList(playerName) {
	const playerListContainer = document.getElementById('players');  // 플레이어 목록 container
	const playerItems = playerListContainer.getElementsByClassName('player-item');  // 클래스가 'player-item'인 모든 요소 선택

	for (let i = 0; i < playerItems.length; i++) {
		if (playerItems[i].textContent.trim().startsWith(playerName.trim())) {
			playerListContainer.removeChild(playerItems[i]);  // 이름이 일치하는 플레이어를 목록에서 제거
			break;
		}
	}
}

async function startQuiz() {
	console.log('게임 시작 버튼이 클릭되었습니다.');
	try {
		// 참가자 상태 확인 요청
		const participantsResponse = await fetch(`${root}/quiz/rooms/${roomId}/players`);
		if (!participantsResponse.ok) {
			console.error('참가자 정보 가져오기 실패:', participantsResponse.statusText);
			return;
		}

		const participantsData = await participantsResponse.json();
		const participants = participantsData.players;

		// 혼자 있는 경우 바로 게임 시작
		const isHostAlone = participants.length === 1;
		if (!isHostAlone) {
			// 모든 참가자의 준비 상태 확인
			const readyStatusResponse = await fetch(`${root}/quiz/rooms/${roomId}/checkReady`);
			if (!readyStatusResponse.ok) {
				console.error('참가자 준비 상태 확인 실패:', readyStatusResponse.statusText);
				return;
			}

			const readyStatusData = await readyStatusResponse.json();
			if (!readyStatusData.allReady) {
				     Swal.fire({
            title: '준비완료요망',
            text: '모든 참가자가 준비되지 않았습니다. 게임을 시작할 수 없습니다.',
            icon: 'warning',
            background: '#3A3A3A',
			color: '#fff',
			confirmButtonColor: '#1db954',
            confirmButtonText: '확인'
        });
				return;
			}
		}

		// **랜덤 퀴즈 가져오기 요청**
		const response = await fetch(`${root}/quiz/rooms/${roomId}/random`);
		if (response.ok) {
			const data = await response.json();
			const quiz = data.quiz;

			// 퀴즈 타입에 따른 처리
			if (currentQuizType === 'songTitle') {
				if (quiz.answer) {  // 퀴즈 정보가 없을 경우 처리
					currentVideo = quiz.code;
					currentAnswers = quiz.answer.map(answer => answer.trim().toLowerCase());
					currentSongName = quiz.name;
					currentAnswer = quiz.answer[0];
				} else {
					console.error('퀴즈 데이터가 올바르지 않습니다. (answer 없음)');
					return;
				}
			} else if (currentQuizType === 'artistName') {
				if (quiz.artist) {  // 퀴즈 정보가 없을 경우 처리
					currentVideo = quiz.code;
					currentAnswers = quiz.artist.map(artist => artist.trim().toLowerCase());
					currentSongName = quiz.artistName;
					currentAnswer = quiz.artist[0];
				} else {
					return;
				}
			} else {
				console.error('알 수 없는 퀴즈 타입입니다:', currentQuizType);
				return;
			}		

  			// **퀴즈 시작 시 힌트 숨기기**
            hideHint();

			// 퀴즈 시작
			hintIndex = 0;
			playMusic(quiz.code, quiz.start);
			playstartEffectSound();
			hideAnswerInfo(); // 퀴즈 시작 후 상태 초기화
		} else {
			const errorText = await response.text();  // HTML 페이지나 오류 메시지를 읽음
			console.error('퀴즈 시작 실패:', errorText);
		}

	} catch (error) {
		console.error('퀴즈 시작 중 오류:', error);
	}
}


// **음악 재생 함수**
function playMusic(videoCode, startTime) {
	const player = document.getElementById('youtube-player');
	const embedUrl = `https://www.youtube.com/embed/${videoCode}?autoplay=1&start=${startTime}&mute=0`;
	player.src = embedUrl;
	isPlaying = true;
	updatePlayIcon();
}

let isQuizCompleted = false;  // 전역 변수로 선언하여 초기값을 false로 설정

function togglePlayPause() {
	const player = document.getElementById('youtube-player');


	if (isPlaying) {
		console.log('음악 중지');
		player.src = '';  // 음악을 중지하기 위해 플레이어의 src를 비웁니다.
		isPlaying = false;
		playstartEffectSound();
		isStoppedByUser = true;  // 사용자가 음악을 수동으로 중지했음을 기록
	} else {
		console.log('음악 재개');
		playstartEffectSound();

		// 사용자가 수동으로 음악을 중지하고, 타이머가 끝난 후 다시 재생할 경우 새로운 퀴즈 시작
		if (isStoppedByUser && isQuizCompleted) {
			console.log('새로운 퀴즈로 이동합니다.');
			startQuiz();  // 새로운 퀴즈 시작
			playstartEffectSound();
		} else {
			const embedUrl = `https://www.youtube.com/embed/${currentVideo}?autoplay=1`;
			player.src = embedUrl;  // 기존 음악 재개
		}

		isPlaying = true;
		isStoppedByUser = false;  // 재생 시 상태 초기화
	}

	updatePlayIcon();  // 재생 아이콘 업데이트
}


// 음악 아이콘에 대한 클릭 이벤트 리스너 추가
document.querySelector('.quiz-room-music-icon').addEventListener('click', togglePlayPause);

// **재생 아이콘 업데이트**
function updatePlayIcon() {
    const musicIcon = document.querySelector('.quiz-room-music-icon i');
    if (isPlaying) {
        // 음악이 재생 중일 때는 일시정지 아이콘을 표시
        musicIcon.classList.remove('bi-play-circle-fill');
        musicIcon.classList.add('bi-pause-circle-fill');
    } else {
        // 음악이 정지 상태일 때는 재생 아이콘을 표시
        musicIcon.classList.remove('bi-pause-circle-fill');
        musicIcon.classList.add('bi-play-circle-fill');
    }
}


// **힌트를 화면에 표시하는 함수**
function updateHintDisplay(hint) {
	const hintDisplay = document.getElementById('hint-info');
	hintDisplay.textContent = `힌트: ${hint}`;
	hintDisplay.classList.remove('hidden');
}

function processChatMessage(sender, message, messageId) {
	// message가 문자열인지 확인
	if (typeof message !== 'string') {
		try {
			message = JSON.stringify(message); // 객체를 문자열로 변환
		} catch (error) {
			console.error("메시지를 문자열로 변환할 수 없습니다:", error);
			return;
		}
	}

	const normalizedMessage = message.trim().toLowerCase();  // 메시지를 소문자로 변환하고 공백 제거

	if (['!스킵', '!skip'].includes(normalizedMessage)) {
		stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
			sender: currentUserNickname,
			message: '!스킵',
			messageId: messageId
		}));
		skipQuiz(currentUserNickname);  // 스킵 처리 함수 호출
		return;
	}

	if (['!힌트', '!hint'].includes(normalizedMessage)) {
		// 힌트를 생성하고 내 화면에 표시
		const hint = generateHint();  
		displayHint(hint);  // 내 화면에 힌트를 표시

		// 다른 사용자에게도 힌트 정보를 전송
		stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
			sender: currentUserNickname,
			message: `힌트: ${hint}`,
			messageId: messageId
		}));

		return;
	}

	// 정답 여부 체크
	const isCorrectAnswer = currentAnswers.some(answer => isExactMatch(normalizedMessage, answer));

	// 메시지 화면에 표시
	if (isCorrectAnswer) {
		displayChatMessage(sender, message, true);  // 정답인 경우 파란색 처리
		hidehint();
		displayAnswerInfo(sender, currentSongName, currentAnswers); // currentSongName과 currentAnswers를 전달
		checkAnswer(sender, message);
	} else {
		displayChatMessage(sender, message, false);  // 일반 메시지 처리
	}
}

// **힌트 생성 함수**
function generateHint() {
    let hint = '';
    for (let i = 0; i < currentAnswer.length; i++) {
        // 현재 힌트 인덱스까지 글자를 표시하고 나머지는 'O'로 처리
        hint += i <= hintIndex ? currentAnswer[i] : 'O';
    }
    hintIndex++;  // 다음 힌트를 위해 인덱스를 증가시킴
    return hint;
}

// **힌트를 화면에 표시하는 함수**
function displayHint(hint) {
    const hintDisplay = document.getElementById('hint-info');
    hintDisplay.textContent = `힌트: ${hint}`;  // 힌트 내용을 표시
    hintDisplay.classList.remove('hidden');  // 힌트 영역을 표시
}


function displayAnswerInfo(sender, songName) {
    document.getElementById('correct-player').textContent = `정답자: ${sender}`;
    document.getElementById('song-info').textContent = songName || currentSongName;  // 노래 제목을 표시
    document.getElementById('answer-info').classList.remove('hidden');  // 정답 정보 섹션을 표시
}


// **힌트 표시 함수**
function displayHint() {
    const hintDisplay = document.getElementById('hint-info');
    
    // 힌트가 이미 최대 인덱스에 도달한 경우 더 이상 힌트를 표시하지 않음
    if (hintIndex >= currentAnswer.length) {
        console.log('더 이상 힌트를 제공할 수 없습니다.');
        return;
    }

    let hint = '';
    for (let i = 0; i < currentAnswer.length; i++) {
        // 현재 힌트 인덱스까지 글자를 표시하고 나머지는 'O'로 처리
        hint += i <= hintIndex ? currentAnswer[i] : 'O';
    }
    
    
    hintIndex++;  // 다음 힌트를 위해 인덱스를 증가시킴
    hintDisplay.textContent = `힌트: ${hint}`;  // 힌트 내용을 표시
    hintDisplay.classList.remove('hidden');  // 힌트 영역을 표시
}


let isSkipping = false; // 중복 스킵 방지를 위한 플래그

function skipQuiz(sender) {
	// 스킵이 이미 진행 중이면 함수 종료
	if (isSkipping) {
		return;
	}

	console.log(`${sender}님이 문제를 스킵했습니다.`);
	isSkipping = true; // 스킵 진행 중으로 설정

	// 타이머가 작동 중이라면 종료
	if (countdownInterval) {
		clearInterval(countdownInterval);
		countdownInterval = null; // 타이머 초기화
	}

	// 스킵 알림 메시지를 WebSocket으로 전송 (모든 참가자에게 알림)
	stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
		sender: '시스템',
		content: `${sender}님이 문제를 스킵했습니다. 다음 문제로 이동합니다!`
	}));

	// 타이머 표시 및 힌트 숨김
	hideAnswerInfo();
	hideHint();


	// **모든 참가자가 스킵 후 퀴즈 넘기기** 
	startQuiz(); // 즉시 다음 퀴즈 시작

	// 스킵이 완료되었음을 알리기 위해 플래그 초기화 (다음 퀴즈가 시작된 후 적절한 위치에서 해제)
	setTimeout(() => {
		isSkipping = false; // 스킵 완료 후 플래그 해제
	}, 2000); // 2초 후 해제
}


// 괄호 안의 문자열과 불필요한 문자를 제거하는 정규 표현식을 사용하여 정답을 처리
function normalizeAnswer(answer) {
	return answer.replace(/\(.*?\)/g, '') // 괄호 안의 내용을 제거
		.replace(/[^\w\s가-힣]|_/g, '') // 특수문자 제거, 한글 범위 추가
		.replace(/\s+/g, '') // 모든 공백 제거
		.trim();  // 양쪽 공백을 제거
}

// 정답과 사용자 입력이 정확히 일치하는지 확인하는 함수
function isExactMatch(userAnswer, correctAnswer) {
	const normalizedUserAnswer = normalizeAnswer(userAnswer);
	const normalizedCorrectAnswer = normalizeAnswer(correctAnswer);
	// 입력한 답과 정답이 정확히 일치하는지 확인
	return normalizedUserAnswer === normalizedCorrectAnswer;
}

function checkAnswer(sender, userAnswer) {
    const normalizedUserAnswer = normalizeAnswer(userAnswer);

    // 정답 리스트를 순회하며 정확한 일치를 확인
    const isCorrect = currentAnswers.some(correctAnswer => {
        const normalizedCorrectAnswer = normalizeAnswer(correctAnswer);
        return normalizedUserAnswer === normalizedCorrectAnswer;  // 전체 일치 확인
    });

    if (isCorrect) {
        // 정답자 로컬 처리
        playCorrectAnswerSound(); // 정답 맞췄을 때 효과음 재생
        displayAnswerInfo(sender); // 로컬에서 정답 정보 표시

        // WebSocket을 통해 다른 사람들에게 정답자 정보를 전송
        stompClient.send(`/pub/quiz/${roomId}/correctAnswer`, {}, JSON.stringify({
            sender: sender,
            songName: currentSongName
        }));

        hideHint(); // 힌트 숨기기
        startCountdown(); // 카운트다운 시작
    } else {
        // 오답 처리
        console.log('오답입니다.');
    }
}


function displayCorrectPlayer(correctPlayer, songName) {
    const playerItems = document.querySelectorAll('.player-item');
    const correctPlayerElement = document.getElementById('correct-player');
    const songInfoElement = document.getElementById('song-info');
    const answerInfoElement = document.getElementById('answer-info');

    // 모든 플레이어에서 정답자 강조 제거
    playerItems.forEach(item => {
        item.classList.remove('correct-player');
    });

    // 정답자를 찾아서 강조
    playerItems.forEach(item => {
        const playerNickname = item.textContent.replace(' ✔', '').trim();
        if (playerNickname === correctPlayer) {
            item.classList.add('correct-player');  // 정답자 강조
        }
    });

    // 정답자와 노래 제목을 화면에 표시
    correctPlayerElement.textContent = `정답자: ${correctPlayer}`;
    songInfoElement.textContent = `노래 제목: ${songName}`;
    answerInfoElement.classList.remove('hidden');  // 정답 정보 섹션 표시
}


function startCountdown(timeLeft = 10) {
	// 기존 타이머가 있다면 초기화
	if (countdownInterval) {
		clearInterval(countdownInterval);
		countdownInterval = null; // 타이머 초기화
	}

	const countdownDisplay = document.getElementById('next-quiz-timer');
	countdownDisplay.textContent = `다음 퀴즈가 시작됩니다 (${timeLeft})`;
	countdownDisplay.classList.remove('hidden');

	// 타이머를 WebSocket을 통해 모든 참가자에게 전송
	stompClient.send(`/pub/quiz/${roomId}/timer`, {}, JSON.stringify({
		timeLeft: timeLeft
	}));

	// 타이머 시작
	countdownInterval = setInterval(() => {
		timeLeft--;
		countdownDisplay.textContent = `다음 퀴즈가 시작됩니다 (${timeLeft})`;

		// 타이머를 모든 참가자에게 지속적으로 전송 (주석 처리된 부분은 필요 시 활성화)
		// stompClient.send(`/pub/quiz/${roomId}/timer`, {}, JSON.stringify({ timeLeft: timeLeft }));

		// !스킵 또는 !skip 명령어가 감지된 경우
		if (isSkipping) {
			clearInterval(countdownInterval); // 타이머 종료
			countdownInterval = null; // 초기화
			hideAnswerInfo();  // 정답 정보 숨기기
			hidehint();
			console.log('스킵 명령어로 타이머가 중지되었습니다.');
			startQuiz();  // 다음 퀴즈 시작
			isSkipping = false;  // 스킵 상태 초기화
			return;  // 함수 종료
		}

		// 남은 시간이 0이 되었을 때
		if (timeLeft < 0) {
			clearInterval(countdownInterval); // 타이머 종료
			countdownInterval = null; // 초기화
			hideAnswerInfo();

			// 사용자가 수동으로 음악을 중지했는지 확인
			if (!isStoppedByUser) {
				startQuiz(); // 음악 자동 재생 없이 다음 퀴즈 시작
			} else {
				console.log('사용자가 음악을 중지했습니다.');
				isStoppedByUser = false; // 상태 초기화
			}
		}
	}, 1000);
}


function hideAnswerInfo() {
	const answerInfo = document.getElementById('answer-info');
	const countdownDisplay = document.getElementById('next-quiz-timer');

	document.getElementById('correct-player').textContent = '';
	document.getElementById('song-info').textContent = '';

	// 타이머 표시 숨김
	answerInfo.classList.add('hidden');
	countdownDisplay.classList.add('hidden');
	countdownDisplay.textContent = '';
}


// **힌트 숨기기 함수**
function hideHint() {
	const hintDisplay = document.getElementById('hint-info');
	hintDisplay.classList.add('hidden');
	hintDisplay.textContent = '';
}

// **채팅 메시지 화면에 표시**
function displayChatMessage(sender, message, isCorrectAnswer) {
	const chatMessages = document.getElementById('chat-messages');
	const messageElement = document.createElement('p');

	// 메시지가 유효할 경우 출력
	if (!message) {
		console.log("정답 또는 명령어 감지", message);
		return;
	}

	// 보낸 사람이 본인인지 확인
	if (sender === "시스템") {
		messageElement.classList.add('system-chat-messages');
		messageElement.innerHTML = `<strong>${message}</strong>`;
	} else if (sender === currentUserNickname) {
		messageElement.classList.add('chat-message-right'); // 본인의 메시지일 경우 오른쪽 정렬
		messageElement.innerHTML = `<strong>${message}</strong>`;  // 본인은 메시지만 표시
	} else {
		messageElement.classList.add('chat-message-left'); // 다른 사용자의 메시지일 경우 왼쪽 정렬
		messageElement.innerHTML = `<strong>${sender}:</strong> ${message}`;  // 상대방은 이름과 메시지 표시
	}


	// 정답인 경우 파란색 배경 추가
	if (isCorrectAnswer) {
		messageElement.style.backgroundColor = '#0d6efd'; // 파란색 배경 설정
		messageElement.style.color = 'white'; // 글씨 색상은 흰색으로 변경
		messageElement.style.borderRadius = '10px'; // 둥근 모서리 추가
		messageElement.style.padding = '8px 12px'; // 패딩 추가
		messageElement.style.fontWeight = 'bold'; // 폰트 굵게  
	}

	chatMessages.appendChild(messageElement);

	// 너무 많은 메시지가 쌓일 경우 제거
	const MAX_CHAT_MESSAGES = 10;
	while (chatMessages.children.length > MAX_CHAT_MESSAGES) {
		chatMessages.removeChild(chatMessages.firstChild);
	}

	// 새로운 메시지를 표시한 후 스크롤을 가장 아래로 이동
	chatMessages.scrollTop = chatMessages.scrollHeight;
}


async function saveChatMessage(roomId, memberId, message) {
	try {
		// roomId와 memberId가 유효한지 확인
		if (!roomId || isNaN(roomId) || !memberId || isNaN(memberId)) {
			throw new Error('유효하지 않은 roomId 또는 memberId입니다.');
		}

		const response = await fetch(`${root}/saveChat`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				roomId: parseInt(roomId),  // roomId가 숫자로 전달되도록 수정
				memberId: parseInt(memberId),  // memberId도 숫자로 전달
				content: message  // message를 content로 전달
			}),
		});

		if (!response.ok) {
			const errorMsg = await response.text();
			console.error('채팅 저장 실패:', errorMsg);
			return false;
		} else {
			console.log('채팅 저장 성공');
			return true;
		}
	} catch (error) {
		console.error('채팅 저장 중 오류:', error.message);
		return false;
	}
}

let isSendingMessage = false;  // 전역 변수로 선언하여 메시지 전송 상태 관리

async function sendMessage() {
	const chatInput = document.getElementById('chat-input');
	const message = chatInput.value.trim();  // 메시지를 입력받아 공백 제거 후 저장
	const messageId = Date.now();  // 고유한 메시지 ID 생성

	if (!message || isSendingMessage) {
		return;  // 메시지가 없거나 이미 전송 중이면 중복 전송 방지
	}

	isSendingMessage = true;  // 전송 중 상태로 설정

	try {
		// 정답 여부 체크
		const normalizedMessage = message.toLowerCase();
		const isCorrectAnswer = currentAnswers.some(answer => isExactMatch(normalizedMessage, answer));

		// **명령어 처리: 스킵**
		if (['!스킵', '!skip'].includes(message)) {
			console.log("스킵 명령어 감지됨");

			stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
				sender: currentUserNickname,
				message: '!스킵',
				messageId: messageId
			}));

			//displayChatMessage(currentUserNickname, "!스킵", false);  // 채팅창에 명령어 표시
			hideAnswerInfo();
			skipQuiz(currentUserNickname);  // 스킵 처리 함수 호출
			return;
		}

		if (['!힌트', '!hint'].includes(message)) {
			console.log("힌트 명령어 감지됨");

		// WebSocket으로 힌트 명령어를 전송하여 모든 참가자에게 알림
			stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
				sender: currentUserNickname, // 힌트를 요청한 유저의 닉네임
				message: '!힌트', // 힌트 메시지 전송
				messageId: messageId  // 메시지 고유 ID
			}));
			displayChatMessage(currentUserNickname, "!힌트", false);  // 채팅창에 명령어 표시
			displayHint2();
			return;  // 메시지 저장 등의 추가 작업 없이 바로 리턴

			// **명령어 처리: 게임 타입 변경**
		} else if (['!게임타입변경'].includes(message)) {
			console.log("게임 타입 변경 명령어 감지됨");

			const newQuizType = currentQuizType === 'songTitle' ? 'artistName' : 'songTitle';

			stompClient.send(`/pub/quiz/${roomId}/changeType`, {}, JSON.stringify({
				newQuizType: newQuizType
			}));
			playchangeTypeSound();
			displayChatMessage(currentUserNickname, `게임타입변경: ${newQuizType === 'songTitle' ? '노래 제목 맞히기' : '가수 이름 맞히기'}`, false);

			return;
		}

		// **명령어 처리: 준비완료**
		else if (['!준비완료'].includes(message)) {
			console.log("준비완료 명령어 감지됨");
			console.log("준비완료 메시지 전송 시도: ", currentUserId);

			stompClient.send(`/pub/quiz/${roomId}/ready`, {}, JSON.stringify({
				sender: currentUserNickname,
				isReady: true  // 준비완료 상태로 전송
			}));

			// 채팅창에 명령어 표시
			displayChatMessage(currentUserNickname, "!준비완료", false);
			// **UI에서 준비 상태 체크표시 추가**
			updatePlayerReadyStatus(currentUserNickname, true);  // 준비완료 상태로 UI 업데이트
			// **모든 참가자가 준비되었는지 확인 후 방장에게 게임 시작 요청**
			await checkAllPlayersReadyAndNotifyHost();  // 변경된 함수
			playcoineffectSound();
			return;
		}

		// **명령어 처리: 준비취소**
		else if (['!준비취소'].includes(message)) {
			console.log("준비취소 명령어 감지됨");

			stompClient.send(`/pub/quiz/${roomId}/ready`, {}, JSON.stringify({
				sender: currentUserNickname,
				isReady: false  // 준비취소 상태로 전송
			}));

			// 채팅창에 명령어 표시
			displayChatMessage(currentUserNickname, "!준비취소", false);

			// **UI에서 준비 상태 체크표시 제거**
			updatePlayerReadyStatus(currentUserNickname, false);  // 준비취소 상태로 UI 업데이트

			return;
		}

		// WebSocket으로 일반 메시지 또는 정답 메시지 전송
		stompClient.send(`/pub/chat/${roomId}`, {}, JSON.stringify({
			sender: currentUserNickname,
			message: message,
			messageId: messageId
		}));

		if (isCorrectAnswer) {
			displayChatMessage(currentUserNickname, message, true);  // 정답인 경우 파란색 처리
			checkAnswer(currentUserNickname, message);
			hideAnswerInfo();
			hidehint();
		} else {
			displayChatMessage(currentUserNickname, message, false);  // 일반 메시지 처리
		}

		await saveChatMessage(roomId, currentUserId, message);

	} catch (error) {
	} finally {
		chatInput.value = '';  // 입력창 초기화
		isSendingMessage = false;  // 전송 완료 후 상태 해제
	}
}

let isAnswerDisplayed = false;  // 정답자 표시 상태를 추적하는 변수

// **힌트 표시 함수**
function displayHint2() {
    const hintDisplay = document.getElementById('hint-info');

    // 정답자가 이미 표시중이면 힌트를 표시하지 않음
    if (isAnswerDisplayed) {
        console.log("정답자가 표시중이므로 힌트를 표시하지 않습니다.");
        return;
    }

    // 힌트 인덱스가 이미 끝에 도달했을 때 힌트를 더 이상 표시하지 않음
    if (hintIndex >= currentAnswer.length) return;

    let hint = '';
    for (let i = 0; i < currentAnswer.length; i++) {
        hint += i <= hintIndex ? currentAnswer[i] : 'O';
    }

    hintIndex++;
    hintDisplay.textContent = `힌트: ${hint}`;
    hintDisplay.classList.remove('hidden');
}

// **정답자 정보 표시 함수 (정답자 표시 중일 때 힌트 숨김 처리)**
/*function displayAnswerInfo(sender, songName) {
    isAnswerDisplayed = true;  // 정답자가 표시되고 있음을 나타냄

    document.getElementById('correct-player').textContent = `정답자: ${sender}`;
    document.getElementById('song-info').textContent = songName || currentSongName;
    document.getElementById('answer-info').classList.remove('hidden');

    // 정답자가 표시되었으므로 힌트도 숨김
    hideHint();
}*/

// **힌트 숨기기 함수**
function hideHint() {
    const hintDisplay = document.getElementById('hint-info');
    hintDisplay.classList.add('hidden');
    hintDisplay.textContent = '';
    hintIndex = 0;  // 힌트 인덱스를 초기화
}


// **모든 참가자가 준비되었는지 확인하고, 방장에게 알림**
async function checkAllPlayersReadyAndNotifyHost() {
	try {
		const response = await fetch(`${root}/quiz/rooms/${roomId}/checkReady`);

		if (response.ok) {
			const data = await response.json();

			// **모든 참가자가 준비된 상태**
			if (data.allReady) {
				console.log("모든 참가자가 준비됨, 방장에게 게임 시작 알림.");
				enableStartButton();  // 게임 시작 버튼 활성화
			} else {
				console.log("모든 참가자가 준비되지 않음");
			}
		} else {
			console.error("준비 상태 확인 중 오류:", response.statusText);
		}
	} catch (error) {
		console.error("준비 상태 확인 중 오류:", error);
	}
}

function updatePlayerReadyStatus(currentUserNickname, isReady) {
	const playerItems = document.querySelectorAll('.player-item');

	playerItems.forEach(item => {
		// 텍스트에서 '✔'을 제거하고 닉네임만 남겨서 비교
		const playerNickname = item.textContent.replace(' ✔', '').trim();
		
		if (playerNickname === currentUserNickname) {
			if (isReady) {
				item.classList.add('ready');
				// 체크 표시가 없는 경우에만 추가
				if (!item.textContent.includes('✔')) {
					item.textContent += ' ✔';  // 준비완료 체크 표시 추가
				}
			} else {
				item.classList.remove('ready');
				// 체크 표시가 있는 경우에만 제거
				item.textContent = item.textContent.replace(' ✔', '');  // 준비취소 시 체크 표시 제거
			}
		}
	});
}


// **힌트 숨기기 함수**
function hideHint() {
	const hintDisplay = document.getElementById('hint-info');
	hintDisplay.classList.add('hidden');  // 힌트 영역을 숨김
	hintDisplay.textContent = '';  // 힌트 내용을 초기화
}


document.addEventListener('DOMContentLoaded', () => {
	const startQuizButton = document.getElementById('quiz-button-start');
	const sendChatBtn = document.getElementById('send-chat-btn');
	const chatInput = document.getElementById('chat-input');

	// 게임 시작 버튼에 대한 이벤트 리스너 추가
	if (startQuizButton) {
		startQuizButton.addEventListener('click', startQuiz);  // 새로운 리스너 추가
	}
	
	// 채팅 전송 버튼에 대한 이벤트 리스너 추가
	if (sendChatBtn) {
		sendChatBtn.addEventListener('click', sendMessage);  // 새로운 리스너 추가
	}

	// 채팅 입력창에서 'Enter' 키를 눌렀을 때의 이벤트 리스너 추가
	if (chatInput) {
		chatInput.addEventListener('keydown', handleChatInput);  // 새로운 리스너 추가
	}
});


// 채팅 입력에서 'Enter' 키를 눌렀을 때 실행될 핸들러 함수 분리
function handleChatInput(event) {
	if (event.key === 'Enter') {
		sendMessage();  // 엔터키 입력시 메시지 전송
	}
}


// 페이지를 떠나기 전에 방 나가기 처리
window.addEventListener('beforeunload', function(event) {
	leaveRoom();  // 사용자가 페이지를 떠날 때 leaveRoom 함수 호출
	event.returnValue = ''; // 메시지를 반환하면 브라우저가 경고 메시지를 띄울 수 있음
});

// 방 나가기 API 호출
async function leaveRoom() {
	try {
		const response = await fetch(`${root}/quiz/rooms/leave`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				roomId: roomId,  // 현재 방 ID
				memberId: currentUserId  // 현재 사용자 ID
			})
		});

		if (response.ok) {
			console.log('방에서 성공적으로 나갔습니다.');
		} else {
			console.error('방 나가기 실패:', response.statusText);
		}
	} catch (error) {
		console.error('방 나가기 요청 중 오류 발생:', error);
	}
}

function enableStartButton() {
	const startButton = document.getElementById('quiz-button-start');
	startButton.disabled = false;  // 게임 시작 버튼 활성화
}

// 모든 참가자가 준비되면 방장에게 알림
function checkAllPlayersReadyAndNotifyHost() {
	// 준비된 플레이어 수를 확인하고 방장에게 알림
	fetch(`${root}/quiz/rooms/${roomId}/checkReady`).then(response => response.json()).then(data => {
		if (data.allPlayersReady) {
			enableStartButton();  // 방장의 버튼 활성화
		}
	}).catch(error => console.error('플레이어 준비 상태 확인 중 오류:', error));
}

document.getElementById('play-toggle-btn').addEventListener('click', function() {
    this.classList.toggle('active');
   
/*    // 버튼의 상태에 따라 재생 아이콘 변경
     const playIcon = document.getElementById('play-icon');
    if (isPlaying) {
        // 음악이 재생 중일 때는 일시정지 아이콘을 표시
        playIcon.classList.remove('bi-play-circle-fill');
        playIcon.classList.add('bi-pause-circle-fill');
    } else {
        // 음악이 정지 상태일 때는 재생 아이콘을 표시
        playIcon.classList.remove('bi-pause-circle-fill');
        playIcon.classList.add('bi-play-circle-fill');
    }*/
});

document.querySelectorAll('.shooting_star').forEach((star) => {
  star.style.setProperty('--rand1', Math.random());
  star.style.setProperty('--rand2', Math.random() * 2 - 0.5); // -0.5에서 1.5 사이의 값을 설정하여 전체 화면을 커버
  star.style.setProperty('--rand3', Math.random());
});

//테스트
/*var RANDOM = function(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
};

var PARTICLES = document.querySelectorAll('.star');
PARTICLES.forEach(function(P) {
  P.setAttribute('style', `
    --angle: ` + RANDOM(0, 360) + `;
    --duration: ` + RANDOM(6, 20) + `;
    --delay: ` + RANDOM(1, 10) + `;
    --alpha: ` + (RANDOM(40, 90) / 100) + `;
    --size: ` + RANDOM(2, 6) + `;
    --distance: ` + RANDOM(40, 200) + `;
  `);
});*/

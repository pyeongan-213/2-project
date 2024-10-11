<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${room.quizRoomName}" /> - 퀴즈 방</title>
    <!-- SockJS 및 STOMP 클라이언트 라이브러리 추가 -->
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.2/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <!-- 탭 아이콘 추가 -->
    <link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
    <link rel="stylesheet" href="<c:out value='${root}/css/quizRoom.css'/>">
    <link rel="stylesheet" href="<c:out value='${root}/css/main.css'/>">
    <script>
        // 디버깅: room 객체 속성 값 확인 (개발 완료 후 제거 가능)
        console.log('Root: <c:out value="${root}"/>');
        console.log('Room ID: <c:out value="${room.quizRoomId}"/>');
        console.log('Room Name: <c:out value="${room.quizRoomName}"/>');
        console.log('Member Count: <c:out value="${room.memberCount}"/>');

        // root 및 방 정보를 JS로 전달
	const root = '${root}';
    const roomId = '${room.quizRoomId != null ? room.quizRoomId : 0}';
    const roomName = '${room.quizRoomName != null ? room.quizRoomName : "새로운 퀴즈방"}'; // 이중 인용부호 사용
    const memberCount = '${room.memberCount != null ? room.memberCount : 0}';
    const maxCapacity = '${room.maxCapacity != null ? room.maxCapacity : 10}'; // 이중 인용부호 사용
    const quizMode = '${room.quizMode != null ? room.quizMode : "노래 제목 맞추기"}'; // 이중 인용부호 사용
    const maxSongs = '${room.maxSongs != null ? room.maxSongs : 60}';
</script>

</head>
<body>

    <!-- 상단 메뉴 포함 -->
    <jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

    <div class="flex-container quiz-room-container">
        <!-- 슬라이드바 포함 -->
        <div class="sidebar">
            <jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
        </div>

        <!-- 퀴즈 방의 메인 콘텐츠 영역 -->
        <div class="main-content">
            <!-- 퀴즈 방의 헤더 -->
            <div class="header">
                <h1>
                    <c:out value="${room.quizRoomName}" />
                    - 퀴즈 방 (
                    <c:out value="${memberCount}" />
                    /
                    <c:out value="${maxCapacity}" />
                    )
                </h1>
                <button id="start-quiz-btn">게임 시작</button>
                <button id="go-lobby-btn">로비로 이동</button>
                <!-- 모드 및 최대 곡 수 표시 -->
                <p>모드: <c:out value="${room.quizMode}" /></p>
                <p>최대 곡 수: <c:out value="${room.maxSongs}" /></p>
            </div>

            <!-- 주제 및 퀴즈 영역 -->
            <div class="quiz-section">
                <h2 id="quiz-topic">
                    주제: <span id="current-topic">음악 퀴즈</span>
                </h2>
                <div id="quiz-area">
                    <p id="quiz-text">퀴즈 문제가 여기에 표시됩니다.</p>
                    <input type="text" id="quiz-answer" placeholder="정답 입력" />
                    <button id="submit-answer-btn">제출</button>
                </div>
            </div>

            <!-- 카메라 영역 -->
            <div class="camera-section">
                <div id="local-video-container">
                    <video id="local-video" autoplay muted></video>
                </div>
                <div id="remote-video-container"></div>
            </div>

            <!-- 채팅 영역 -->
            <div class="chat-section">
                <div id="chat-messages"></div>
                <input type="text" id="chat-input" placeholder="채팅 입력..." />
                <button id="send-chat-btn">전송</button>
            </div>
        </div>
    </div>

    <!-- JavaScript 파일을 바디 끝에 로드하여 모든 요소가 렌더링된 후에 스크립트 실행 -->
    <script src="<c:out value='${root}/js/quizRoom.js'/>"></script>

    <script>
        // 인원 초과 확인
        if (memberCount >= maxCapacity) {
            alert('인원이 초과되어 방에 입장할 수 없습니다.');
            window.location.href = root + '/lobby';
        }

        // 게임 시작 버튼 이벤트
        document.getElementById('start-quiz-btn').addEventListener('click', function() {
            // 서버로 게임 시작 요청 전송 (STOMP, WebSocket 등을 사용)
            alert('게임을 시작합니다!');
            // TODO: 서버와 연동하여 실제 게임 시작 로직 구현
        });
    </script>

</body>
</html>

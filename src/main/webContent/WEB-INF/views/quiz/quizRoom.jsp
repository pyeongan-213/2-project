<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title><c:out value="${room.quizRoomName}" /> - 퀴즈 방</title>

<!-- 페이지 아이콘 설정 -->
<link rel="icon" type="image/png" sizes="48x48"
	href="${root}/img/tabicon.png">

<!-- CSS 파일 연결 -->
<link href="${root}/css/main.css" rel="stylesheet" type="text/css">
<link href="<c:out value='${root}/css/quizRoom.css' />" rel="stylesheet">

<!-- 아이콘 라이브러리 (Bootstrap Icons) -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
	rel="stylesheet">

<!-- 필수 스크립트 로드 -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.2/dist/sockjs.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="https://www.youtube.com/iframe_api"></script>
<!-- 유튜브 API 로드 -->

<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
    <link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>


<% 
    // JSP에서 로그인된 사용자 정보 추출
    String userId = (session.getAttribute("loginMemberBean") != null)
        ? String.valueOf(((kr.co.duck.beans.MemberBean) session.getAttribute("loginMemberBean")).getMember_id())
        : "null";
    userId = userId.trim();

    String userNickname = (session.getAttribute("loginMemberBean") != null)
        ? String.valueOf(((kr.co.duck.beans.MemberBean) session.getAttribute("loginMemberBean")).getNickname())
        : "익명";
%>

<script>
	console.log('Root:', '${root}');
	window.root = '${root}';
	window.roomId = '${room.quizRoomId}';
	window.roomName = '${room.quizRoomName}';

    // 로그인된 사용자 ID와 닉네임을 JavaScript 변수로 전달
    const loggedInUserId = "<%=userId%>"; 
    const loggedInUserNickname = "<%=userNickname%>"; 
	
    console.log('Logged In User ID:', loggedInUserId);
    console.log('Logged In User Nickname:', loggedInUserNickname);
    
    // 전역 변수로 설정하여 quizroom.js에서 사용 가능하게 함
    window.currentUserId = loggedInUserId;
    window.currentUserNickname = loggedInUserNickname;
</script>
</head>
<body>
	<div class="quiz-room-container">
		<!-- 상단 메뉴 -->
		<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

		<div class="quiz-room-main">
			<!-- 왼쪽 사이드바 -->
			<div class="sidebar">
				<jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
			</div>

			<!-- 플레이어 목록 -->
			<div class="player-list-container">
				<div class="player-list">
					<ul id="players"></ul>
				</div>
			</div>
			<!-- 퀴즈 유형 안내 -->
			<div>
				<p id="quiz-instruction"></p>
			</div>
			<!-- 퀴즈 게임 영역 -->
			<div class="quiz-room-game-center">
			  <div class="noite"></div> <!-- 배경 요소 추가 -->
		
		    <div class="constelacao"></div> <!-- 별자리 요소 추가 -->
		
		    <div class="lua">
		        <div class="textura"></div>
		    </div> <!-- 달 요소 추가 -->
		
		    <div class="chuvaMeteoro"></div> <!-- 유성 요소 추가 -->
		
		    <div class="floresta">
		        <img src="https://raw.githubusercontent.com/interaminense/starry-sky/master/src/img/bgTree.png" alt="" />
		    </div> <!-- 숲 이미지 추가 -->
			
				<!-- 정답자 및 곡 정보 (재생 버튼 위) -->
				<div id="answer-info" class="answer-info hidden">
					<p id="correct-player" class="answer-player"></p>
					<!-- 정답자 -->
					<p id="song-info" class="song-info"></p>
					<!-- 곡 정보 -->
				</div>

				<!-- 힌트 영역 -->
				<div id="hintDisplay">
					<p id="hint-info" class="hidden"></p>
				</div>
				<!-- 음악 재생/일시정지 버튼 -->
				<div class="quiz-room-music-icon" id="play-toggle-btn">
					<i class="bi bi-play-circle-fill" id="play-icon"></i>
				</div>

				<!-- 타이머 -->
				<p id="next-quiz-timer" class="quiz-timer hidden"></p>

				<!-- 게임 시작 버튼 -->
				<!-- <button id="start-quiz-btn" class="quiz-room-btn">게임 시작</button> -->
				<div class="gamebtn">
					<a href="#" id="start-quiz-btn" class="quiz-room-btn"> <span
						data-attr="Game"></span> <span data-attr="Start">Now</span>
					</a>
				</div>

				<!-- 유튜브 플레이어 -->
				<div id="quiz-area" class="quiz-room-quiz-area">
					<iframe id="youtube-player" width="0" height="0" frameborder="0"
						allow="autoplay; encrypted-media" style="display: none;"></iframe>
				</div>
			</div>

			<!-- 명령어 툴팁 컨테이너 -->
			<div id="command-tooltip" class="tooltip hidden">
				<div class="tooltip-header">
					<span><strong>&nbsp;명령어 모음</strong></span>
					<button id="tooltip-minimize-btn">ㅡ</button>
				</div>
				<div id="tooltip-body" class="tooltip-body">
					<p>&nbsp;!준비완료(방장제외)</p>
					<p>&nbsp;!게임타입변경</p>
					<p>&nbsp;!힌트 or !hint</p>
					<p>&nbsp;!스킵 or !skip</p>
				</div>	
			</div>
				<!-- 최대화 버튼 (초기에는 숨김 처리) -->
				<button id="tooltip-maximize-btn" class="hidden">+</button>
			
			<!-- 채팅 영역 -->
			<div class="quiz-room-chat-section">
				<div id="chat-messages" class="quiz-room-chat-messages"></div>
				<div class="quiz-room-chat-input-wrapper">
					<input type="text" id="chat-input" class="quiz-room-chat-input"
						placeholder="채팅 입력..." />
					<button id="send-chat-btn" class="quiz-room-chat-btn">전송</button>
				</div>
			</div>

		</div>
	</div>

	<!-- quizRoom.js 스크립트 로드 -->
	<script src="<c:out value='${root}/js/quizRoom.js' />"></script>
</body>
</html>

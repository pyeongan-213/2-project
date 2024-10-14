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

<script>
	console.log('Root:', '${root}');
	window.root = '${root}';
	window.roomId = '${room.quizRoomId}';
	window.roomName = '${room.quizRoomName}';
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
					<p>플레이어 목록</p>
					<ul id="players"></ul>
				</div>
			</div>

			<!-- 퀴즈 게임 영역 -->
			<div class="quiz-room-game-center">
				<!-- 정답자 및 곡 정보 (재생 버튼 위) -->
				<div id="answer-info" class="answer-info hidden">
					<p id="correct-player" class="answer-player"></p><!-- 정답자 -->
					<p id="song-info" class="song-info"></p><!-- 곡 정보 -->		
				</div>

				<!-- 힌트 영역 -->
				<p id="hint-info" class="hidden"></p>

				<!-- 음악 재생/일시정지 버튼 -->
				<div class="quiz-room-music-icon" id="play-toggle-btn">
					<i class="bi bi-play-circle-fill" id="play-icon"></i>
				</div>

				<!-- 타이머 -->
				<p id="next-quiz-timer" class="quiz-timer hidden"></p>

				<!-- 게임 시작 버튼 -->
				<button id="start-quiz-btn" class="quiz-room-btn">게임 시작</button>

				<!-- 유튜브 플레이어 -->
				<div id="quiz-area" class="quiz-room-quiz-area">
					<iframe id="youtube-player" width="0" height="0" frameborder="0"
						allow="autoplay; encrypted-media" style="display: none;"></iframe>
				</div>
			</div>

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

	<script src="<c:out value='${root}/js/quizRoom.js' />"></script>
</body>
</html>

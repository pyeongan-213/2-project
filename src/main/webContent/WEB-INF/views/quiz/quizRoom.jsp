<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title><c:out value="${room.quizRoomName}" /> - 퀴즈 방</title>
<link rel="icon" type="image/png" sizes="48x48"
	href="${root}/img/tabicon.png">
<link href="${root}/css/main.css" rel="stylesheet" type="text/css">
<link href="<c:out value='${root}/css/quizRoom.css' />" rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
	rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.2/dist/sockjs.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="https://www.youtube.com/iframe_api"></script>
<!-- YouTube API 로드 -->
<script>
	console.log('Root:', '${root}');
	window.root = '${root}';
	window.roomId = '${room.quizRoomId}';
	window.roomName = '${room.quizRoomName}';
</script>
</head>
<body>
	<div class="quiz-room-container">
		<!-- 탑 메뉴 포함 -->
		<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

		<div class="quiz-room-main">
			<!-- 왼쪽: 사이드바 -->
			<div class="sidebar">
				<jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
			</div>

			<!-- 플레이어 목록 -->
			<div class="player-list-container">
				<div class="player-list">
					<p>플레이어 목록</p>
					<ul id="players">
						<!-- 동적으로 플레이어 목록이 추가됩니다 -->
					</ul>
				</div>
			</div>

			<!-- 중앙: 게임 시작 및 퀴즈 -->
			<div class="quiz-room-game-center">
				<div id="play-t	ggle-btn" class="quiz-room-music-icon">
					<i class="bi bi-play-circle-fill"></i>
				</div>
				<button id="start-quiz-btn" class="quiz-room-btn">게임 시작</button>
				<div id="quiz-area" class="quiz-room-quiz-area">
					<p>퀴즈 문제가 여기에 표시됩니다.</p>
					<iframe id="youtube-player" width="0" height="0" frameborder="0"
						allow="autoplay; encrypted-media" style="display: none;">
					</iframe>
				</div>
			</div>

			<!-- 오른쪽: 채팅 영역 -->
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

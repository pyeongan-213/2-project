<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>퀴즈 로비</title>

<!-- 탭 아이콘 추가 -->
<link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
<!-- CSS 및 Bootstrap 아이콘 추가 -->
<link rel="stylesheet" href="${root}/css/quizlobby.css">
<link href="${root}/css/main.css" rel="stylesheet" type="text/css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
        // root 변수를 JS로 전달
        window.root = '${pageContext.request.contextPath}';

        // 서버에서 로그인한 사용자 ID를 JavaScript 변수로 전달
      <%String userId = (session.getAttribute("loginMemberBean") != null)
		? String.valueOf(((kr.co.duck.beans.MemberBean) session.getAttribute("loginMemberBean")).getMember_id())
		: "null";
System.out.println("로그인된 사용자 ID: " + userId); // 로그로 출력
userId = userId.trim();%>
    
    const currentUserId = "<%=userId%>";
    </script>
</head>
<body>

	<!-- top_menu.jsp 포함 -->
	<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

	<!-- 전체 컨테이너 설정 -->
	<div class="flex-container quiz-lobby">
		<!-- Sidebar 포함 -->
		<div class="sidebar">
			<jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
		</div>

		<!-- 메인 콘텐츠 영역 -->
		<div class="main-content">
			<!-- Header -->
			<div class="header">
				<div class="header-left">
					<h1>퀴즈 로비</h1>
				</div>
				<button id="create-room-btn">방 생성</button>
			</div>

			<!-- Section -->
			<section class="main-container">
				<div class="room-list-container">
					<h2>방 목록</h2>
					<ul id="room-list">
						<c:forEach var="room" items="${rooms}">
							<li>
								<div class="room-info">
									<div class="room-details">
										<!-- 방장 이름을 "owner의 방" 형식으로 표시 -->
										<span class="room-name"><c:out value="${room.owner}" />의 방</span> 
										<span class="room-users"><c:out value="${room.memberCount}" /> / <c:out value="${room.maxCapacity}" />명</span>
									</div>
									<button class="join-room-btn" data-room-id="${room.quizRoomId}" data-requires-password="${room.quizRoomPassword != null && !room.quizRoomPassword.isEmpty()}">참여</button>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</section>
		</div>
	</div>

	<!-- 방 생성 모달 창 -->
	<div id="create-room-modal" class="modal">
		<div class="modal-content">
			<span class="close">&times;</span>
			<h2>방 생성</h2>
			<form id="create-room-form">
				<label for="roomName">방 이름:</label> 
				<input type="text" id="roomName" name="roomName" required><br> 
				<label for="maxCapacity">최대 인원수:</label> 
				<input type="number" id="maxCapacity" name="maxCapacity" min="1" required><br> 
				<label for="maxMusic">최대 곡 수:</label> 
				<input type="number" id="maxMusic" name="maxMusic" min="1" required><br> 
				<label for="quizType">퀴즈 타입:</label> 
				<select id="quizType" name="quizType">
					<option value="songTitle">노래 제목 맞추기</option>
					<option value="artistName">가수 이름 맞추기</option>
				</select><br>
				<button type="submit">방 생성</button>
			</form>
		</div>
	</div>

	<!-- 비밀번호 입력 모달 -->
	<div id="password-modal" class="modal">
		<div class="modal-content">
			<span class="close-password-modal">&times;</span>
			<h2>비밀번호 입력</h2>
			<form id="password-form">
				<label for="roomPassword">방의 비밀번호:</label> 
				<input type="password" id="roomPassword" name="roomPassword" required>
				<button type="button" id="submit-password">참여</button>
			</form>
		</div>
	</div>

	<!-- bottom_info.jsp 포함 -->
	<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />

	<script src="${root}/js/quizlobby.js"></script>

</body>
</html>

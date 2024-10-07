<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>퀴즈 로비</title>
<link rel="stylesheet" href="${root}/css/quizlobby.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
    // root 변수를 JS로 전달
    window.root = '${pageContext.request.contextPath}';

    // 서버에서 로그인한 사용자 ID를 JavaScript 변수로 전달
  <%
    String userId = (session.getAttribute("loginMemberBean") != null)
        ? String.valueOf(((kr.co.duck.beans.MemberBean) session.getAttribute("loginMemberBean")).getMember_id())
        : "null";
    System.out.println("로그인된 사용자 ID: " + userId); // 로그로 출력
    userId = userId.trim();
%>

const currentUserId = "<%=userId%>";
</script>
</head>
<body>
	<div class="lobby-container">
		<div class="header">
			<h1>퀴즈 로비</h1>
			<button id="create-room-btn">방 생성</button>
			<button id="home-btn">홈으로 돌아가기</button>
		</div>
		<div class="room-list-container">
			<h2>방 목록</h2>
			<ul id="room-list">
				<c:forEach var="room" items="${rooms}">
					<li>
						<div class="room-info">
							<span class="room-name"><c:out
									value="${room.quizRoomName}" /></span> <span class="room-users">
								<c:out value="${room.memberCount}" /> / 10명
							</span>
							<button class="join-room-btn" data-room-id="${room.quizRoomId}"
								data-requires-password="${room.quizRoomPassword != null && !room.quizRoomPassword.isEmpty()}">참여</button>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	<script src="${root}/js/quizlobby.js"></script>
	<script>
		// 홈으로 돌아가기 버튼 클릭 이벤트
		document.getElementById('home-btn').addEventListener('click', () => {
			window.location.href = `${root}/main`; // 홈 경로로 이동 (경로는 상황에 따라 변경)
		});
	</script>
</body>
</html>

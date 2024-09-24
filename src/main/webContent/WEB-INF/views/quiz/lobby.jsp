<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath }/" />
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로비</title>
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1.4.0/dist/sockjs.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="<c:url value='/js/lobby.js'/>"></script>
</head>

<body>
	<h1>로비</h1>

	<button onclick="refreshRoomList()">새로고침</button>
	<a href="<c:url value='/lobby/create'/>">방 생성</a>

	<div id="room-list">
		<c:forEach items="${rooms}" var="room">
			<div class="room-item">
				<h3>${room.roomName}(${room.currentUsers}/${room.maxUsers})</h3>
			</div>
		</c:forEach>
	</div>
</body>
</html>

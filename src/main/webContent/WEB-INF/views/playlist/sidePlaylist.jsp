<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:forEach var="playlist" items="${playlists}">
		<li><a
			href="${root}/playlist/playlist?playlistId=${playlist.playlist_id}">
				${playlist.playlistname} </a></li>
	</c:forEach>

	<c:if test="${empty playlists}">
		<p>플레이리스트가 없습니다.</p>
	</c:if>

</body>
</html>
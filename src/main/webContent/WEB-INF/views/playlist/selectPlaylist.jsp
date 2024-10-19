<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- selectPlaylist.jsp -->
	<div class="container">
		<h3>회원의 플레이리스트 목록</h3>
		<ul>
			<c:forEach var="playlist" items="${playlists}">
				<li>
					<!-- playlistid와 playlistname을 출력 --> 플레이리스트 ID: <a
					href="${root}/playlist/playlist?playlistId=${playlist.playlist_id}">
						${playlist.playlistname} 
				</li>
			</c:forEach>
		</ul>
	</div>


</body>
</html>
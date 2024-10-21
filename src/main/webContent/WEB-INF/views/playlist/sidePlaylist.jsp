<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<style>
.playlist-container {
	padding-top: 10px;
}

.side-my-playlist {
	padding-top: 10px;
	font-size: 12px;
}

.post-playlist-container .side-my-playlist {
	font-size: 12px;
}
.sidebar-horiz{
margin-left : -25px;
width :  268px;
 }
</style>

<div class="post-playlist-container">

	<h2>플레이리스트 목록</h2>
	<hr class="sidebar-horiz"/>

	<c:forEach var="playlist" items="${playlists}">
		<div class="side-my-playlist">
			
				<a
					href="${root}/playlist/playlist?playlistId=${playlist.playlist_id}">
					${playlist.playlistname} </a>
			
		</div>
	</c:forEach>
	<c:if test="${empty playlists}">
		<div class="side-my-playlist">플레이리스트가 없습니다.</div>
	</c:if>
</div>

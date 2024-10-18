<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var='root' value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>DuckMusic</title>

<!-- 탭 아이콘 추가 -->
<link rel="icon" type="image/png" sizes="48x48"
	href="${root}/img/tabicon.png">
<!-- CSS 및 Bootstrap 아이콘 추가 -->
<link href="${root}/css/main.css" rel="stylesheet" type="text/css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
	rel="stylesheet">

<style>
body {
	display: flex;
	justify-content: space-between;
	margin: 0;
	padding: 0;
	height: 100vh;
}

#video-container {
	flex: 1;
	padding: 10px;
}

#playlist-container {
	flex: 1;
	padding: 10px;
	overflow-y: scroll;
	background-color: #f4f4f4;
}

iframe {
	width: 100%;
	height: 100%;
}

.playlist-item {
	display: flex;
	align-items: center;
	padding: 10px;
	border-bottom: 1px solid #ccc;
}

.playlist-item img {
	width: 50px;
	height: 50px;
	margin-right: 10px;
}

.playlist-item button {
	margin-left: auto;
}
</style>

</head>

<body>
	<header>
		<!-- top_menu.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

		<!-- Sidebar 포함 -->
		<div class="sidebar">
			<jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
		</div>
	</header>

	<div id="contentContainer">
		<!-- YouTube Video 재생 영역 -->
		<div id="video-container">
			<iframe id="player" type="text/html"
				src="https://www.youtube.com/embed/${firstVideoUrl}" frameborder="0"
				allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
				allowfullscreen> </iframe>
		</div>

		<!-- 플레이리스트 영역 -->
		<div id="playlist-container">
			<h2>Playlist</h2>
			<c:forEach var="music" items="${musicList}">
				<div class="playlist-item">
					<img src="${music.thumbnailUrl}" alt="${music.musicName}"> <span>${music.musicName}
						- ${music.artist}</span>
					<button onclick="playVideo('${music.videoUrl}')">Play</button>
				</div>
			</c:forEach>
		</div>

		<script>
			// YouTube 동영상 재생 함수
			function playVideo(videoUrl) {
				var player = document.getElementById('player');
				player.src = "https://www.youtube.com/embed/" + videoUrl
						+ "?autoplay=1";
			}
		</script>

	</div>
	<!-- contentContainer -->

	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
</body>
</html>

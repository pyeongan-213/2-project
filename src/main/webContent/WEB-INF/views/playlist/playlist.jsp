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
		<div class="container">
			<div class="row">
				<!-- 왼쪽: YouTube 음악 플레이어 -->
				<div class="col-md-8">
					<h3>현재 재생 중인 곡</h3>
					<iframe id="musicPlayer" width="100%" height="500px"
						src="https://www.youtube.com/embed/${musicList[0].videoUrl}?autoplay=1"
						frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
				</div>

				<!-- 오른쪽: 플레이리스트 -->
				<div class="col-md-4">
					<h3>플레이리스트</h3>
					<table class="table">
						<thead>
							<tr>
								<th>곡 이름</th>
								<th>아티스트</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="music" items="${musicList}">
								<tr>
									<td>
										<!-- 곡 클릭 시 YouTube 재생 --> <a
										href="https://www.youtube.com/embed/${music.videoUrl}"
										target="musicPlayer"> ${music.music_Name} </a>
									</td>
									<td>${music.artist}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>



	</div>
	<!-- contentContainer -->

	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
</body>
</html>

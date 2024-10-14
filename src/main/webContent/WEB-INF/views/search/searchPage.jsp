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
	<!--  페이지 내용  -->
	<div class="main-content">
		<!-- Header -->
		<div class="header">
		<br />
			<h1 class="main-header">검색 결과</h1>
		<hr />
		</div>
		<section class="main-container">
			<div class="section">
				<div
					style="display: flex; justify-content: space-between; align-items: center;">
					
					<h2>아티스트</h2>

				</div>

				<!-- MANIADB 에서 아티스트 검색 내용 -->
				<div class="artists-grid">
					<c:forEach var="artist" items="${artistList}" varStatus="status">
						<c:if test="${status.index < 10}">
							<div class="artist">
								<!-- 이미지에 클릭 이벤트 추가 -->
								<a
									href="${root}/search/parseDetail?guid=${artist.link}&type=artist">
									<img class="artist-img" src="${artist.image}"
									onerror="${root}/img/noImageArtist.jpg">
								</a>
								<div class="artist-info">
									<h5>${artist.artistName}</h5>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</div>
			<br />
			<hr />
			<br />
			<h2>앨범</h2>
			<!-- MANIADB 에서 앨범 검색 내용 -->
			<div class="albums-grid">
				<c:forEach var="album" items="${albumList}" varStatus="status">
					<c:if test="${status.index < 10}">
						<div class="album">
							<!-- 이미지에 클릭 이벤트 추가 -->
							<a
								href="${root}/search/parseDetail?guid=${fn:escapeXml(album.guid)}&type=album">
								<img class="card-img-top" src="${album.albumimage}"
								onerror="${root}/img/img_none_01.png">
							</a>
							<div class="card-body">
								<h5 class="fw-bolder">${album.albumName}</h5>
								<p>${album.albumArtist}</p>
							</div>
						</div>
					</c:if>
				</c:forEach>
			</div>
			<!-- albums-grid -->
		</section>
		<!-- main-container -->
	</div>
	<!-- main-content -->


	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
</body>
</html>

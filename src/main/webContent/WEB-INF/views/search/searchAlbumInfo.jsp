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

		<div class="background"></div>
		<section>
			<div class="album-info">
				<div class="album-art">
					<img src="${result.image}" alt="이미지를 불러올 수 없습니다." />
					<div class="actions">
						<div class="bookmark">

						</div>
					</div>
				</div>
				<div class="album-details">
					<h2>${result.artistName}</h2> 
					<h2>${result.artistGuid}</h2> <!-- 사용해서 아티스트 페이지로 이동하기 -->
					<h1>${result.albumName}</h1>
					<br /> <br /> <span> <c:forEach var="track"
							items="${result.albumRelease}" varStatus="status">
							<span>${track}</span>
						</c:forEach>
					</span>
					<p>${result.description}</p>
				</div>
			</div>
			<div class="album-tracks">
				<ol>
					<c:forEach var="track" items="${result.trackList}"
						varStatus="status">
						<li><span>${track}</span> <span>${result.runningTimeList[status.index]}</span>
							<span> <a
								href="${pageContext.request.contextPath}/youtubeSearch?query=${fn:escapeXml(result.artistName)}+${fn:escapeXml(track)}">playlist에
									추가</a>
						</span></li>
					</c:forEach>
				</ol>
			</div>
		</section>
	</div>
	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
</body>
</html>
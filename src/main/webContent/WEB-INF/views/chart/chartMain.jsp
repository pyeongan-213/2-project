<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var='root' value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png" sizes="48x48"
	href="${root}/img/tabicon.png">
<!-- CSS 및 Bootstrap 아이콘 추가 -->
<link href="${root}/css/chart.css" rel="stylesheet" type="text/css">
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
<link
	href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>

</head>
<body>

	<h2>Melon Top 100</h2>

	<header>
		<!-- top_menu.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

		<!-- Sidebar 포함 -->
		<div class="sidebar">
			<jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
		</div>
	</header>
	<!-- body -->
	<h1>Melon Chart Top 100</h1>
	<table border="1" class="chart-chart-table">
		<thead>
			<tr>
				<th>Rank</th>
				<th>Title</th>
				<th>Artist</th>
				<th>Album</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="song" items="${songs}">
				<tr>
					<td>${song.rank}</td>
					<td>${song.title}</td>
					<td>${song.artist}</td>
					<td>${song.album}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>



	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
	<script src="${root}/js/playlist.js"></script>
</body>
</html>
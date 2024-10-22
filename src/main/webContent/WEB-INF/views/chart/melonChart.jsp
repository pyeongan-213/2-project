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


<!-- 간단한 css 추가 -->
<style>
.img_album img{
width : 120px;
height : 100px;
}
</style>

<!-- 자바스크립트 코드 추가 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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




	<!-- body -->

	<table border="1" class="chart-selector">
		<tr>
			<th class="chart-selector-selected">Top 100</th>
			<th onclick="location.href='${root}/chart/dailyChart'">일간 차트</th>
			<th onclick="location.href='${root}/chart/weeklyChart'">주간 차트</th>
			<th onclick="location.href='${root}/chart/monthlyChart'">월간 차트</th>
		</tr>
	</table>
	<table border="1" class="chart-chart-table">
		<thead>
			<tr>
				<th>Rank</th>
				<th>이미지</th>
				<th>Title</th>
				<th>Album</th>
				<th>추가하기</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="song" items="${songs}">
				<tr>
					<td>${song.rank}</td>
					<td><img src="${song.img}" alt="NaN" /></td>
					<td><h4>${song.title}</h4>
						<h6>${song.artist}</h6></td>
					<td><h6>${song.album}</h6></td>
					<td><button class="openModal" data-track="${song.title}"
							data-artist="${song.artist}">+</button></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>


	<!-- Modal 팝업 -->
	<div class="modal-wrapper">
		<div class="modal">
			<div class="head">
				<span>플레이리스트에 추가</span><span class="btn-close">×</span>
			</div>
			<div class="content" id="modalContent">
				<!-- YouTube 검색 결과가 여기에 표시됩니다 -->
			</div>
		</div>
	</div>

	<footer>
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>

	<script>
		$(document)
				.ready(
						function() {
							// 모달 열기
							$(document)
									.on(
											'click',
											'.openModal',
											function() {
												var trackName = $(this).data(
														'track');
												var artistName = $(this).data(
														'artist');

												// AJAX로 YouTube 검색 페이지를 모달에 로드
												$
														.ajax({
															url : '${root}/youtubeSearch', // 검색 요청을 보낼 경로
															type : 'GET',
															contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
															data : {
																query : artistName
																		+ " "
																		+ trackName
															},
															success : function(
																	response) {
																// YouTube 검색 결과를 모달에 표시
																$(
																		'#modalContent')
																		.html(
																				response);
																$(
																		'.modal-wrapper')
																		.addClass(
																				'open');
															},
															error : function() {
																Swal.fire({
																	icon: 'error',
																	title: '로그인이 필요합니다!',
																	text: '확인 버튼을 누르면 로그인 화면으로 이동합니다',
																	background: '#3A3A3A',
																	color: '#fff',
																	confirmButtonColor: '#1db954',
																	confirmButtonText: '확인'
																}).then((result) => {
																  if (result.isConfirmed) {
																    window.location.href = "${root}/member/login"; // 메인 페이지로 리디렉션
																  }
																});
															}
														});
											});

							// 모달 닫기
							$(document).on('click', '.btn-close', function() {
								$('.modal-wrapper').removeClass('open');
							});
						});
	</script>

	<script src="${root}/js/playlist.js"></script>
</body>
</html>
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
<link href="${root}/css/searchAlbum.css" rel="stylesheet"
	type="text/css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
	rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!-- 스타일 추가 -->
<style>
</style>

<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
<link
	href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>


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
						<div class="bookmark"></div>
					</div>
				</div>
				<div class="album-details">
					<h1>${result.albumName}</h1>
					<h4>Artist : ${result.artistName}</h4>

					<p class="album-description">
					<h6>
						<c:choose>

							<c:when test="${fn:length(result.description) > 800}">

								<span id="short-description"> <c:out
										value="${fn:substring(result.description, 0, 800)}" />...
								</span>
								<span id="full-description" style="display: none;"> <c:out
										value="${result.description}" />
								</span>
								<button id="toggle-description" onclick="toggleDescription()">더보기</button>
							</c:when>


							<c:otherwise>
								<c:out value="${result.description}" />
							</c:otherwise>
						</c:choose>
					</h6>
					</p>
				</div>
			</div>

			<table class="album-table">
				<thead>
					<tr>
						<th class="column-count"><h3>#</h3></th>
						<th class="column-title"><h3>제목</h3></th>
						<th class="column-playtime"><h3>재생시간</h3></th>
						<th class="column-action"><h3></h3></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="track" items="${result.trackList}"
						varStatus="status">
						<tr>
							<td class="column-count">${status.count}</td>
							<td class="column-title">${track}</td>
							<td class="column-playtime">${result.runningTimeList[status.index]}</td>
							<td class="column-action">
								<!-- 모달을 통해 YouTube 검색 페이지 열기 -->
								<button class="openModal" data-track="${track}"
									data-artist="${result.artistName}">+</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</section>
	</div>

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

	<script>
		function toggleDescription() {
			const shortDesc = document.getElementById('short-description');
			const fullDesc = document.getElementById('full-description');
			const toggleButton = document.getElementById('toggle-description');

			if (fullDesc.style.display === 'none') {
				// 전체 텍스트를 보여주고 버튼 텍스트를 '접기'로 변경
				shortDesc.style.display = 'none';
				fullDesc.style.display = 'inline';
				toggleButton.innerText = '접기';
			} else {
				// 전체 텍스트를 숨기고 짧은 설명과 '더보기' 버튼 표시
				shortDesc.style.display = 'inline';
				fullDesc.style.display = 'none';
				toggleButton.innerText = '더보기';
			}
		}
	</script>

</body>
</html>

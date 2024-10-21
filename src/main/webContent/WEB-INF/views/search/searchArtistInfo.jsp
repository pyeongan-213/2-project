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

<!-- Tab 아이콘 -->
<link rel="icon" type="image/png" sizes="48x48"
	href="${root}/img/tabicon.png">
<!-- Bootstrap 및 사용자 정의 CSS -->

<link rel="stylesheet" href="${root}/css/artistPage.css">
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

		<div class="artist-info">
			<!-- 가수 사진 --> 
			
			<c:choose>
				<c:when test="${not empty artistImage}">
					<img src="<c:out value='${artistImage}' />" alt="가수사진"
						class="artist-photo">
				</c:when>
				<c:when test="${not empty result.image}">
					<img src="<c:out value='${result.image}' />" alt="가수사진"
						class="artist-photo">
				</c:when>
				<c:otherwise>
					<img src="/img/default-artist.png" alt="가수사진" class="artist-photo">
					<!-- 기본 이미지 또는 대체 텍스트 -->
				</c:otherwise>
			</c:choose>
			<div class="artist-details">
				<!-- 가수 이름 -->
				<h1 class="artist-name">
					<c:out value="${result.artistName}" />
				</h1>

				<!-- 가수 설명 -->
				<p class="artist-description">
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
				</p>
			</div>
		</div>
		<br /> <br />
		<hr />
		
		<!-- 앨범 리스트 -->
		<div class="album-list">
			<c:forEach var="album" items="${result.albumNameList}"
				varStatus="status">
				<div class="album-item">
					<!-- 앨범 아트 -->
					<a
						href="${root}/search/parseDetail?guid=${result.albumGuidList[status.index]}&type=album">
						<img src="${result.albumImageList[status.index]}" alt="앨범 아트"
						class="album-art">
					</a>

					<!-- 앨범 이름 -->
					<p class="album-name">
						<c:out value="${album}" />
					</p>
				</div>
			</c:forEach>
		</div>
	</div>

	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
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

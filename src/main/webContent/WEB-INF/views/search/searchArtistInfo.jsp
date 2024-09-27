<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${root}css/searchArtistInfo.css">
</head>
<body>
	<!-- sidebar -->
	<c:import url="/WEB-INF/views/include/sidebar.jsp" />
	<%-- <!-- top_menu-->
	<c:import url="/WEB-INF/views/include/top_menu.jsp" />
	 --%><div class=container-box>
	
	<div class="artist-container">
		<!-- 가수 사진 -->
		<img src="<c:out value='${result.image}'/>" alt="가수 사진"
			class="artist-photo">

		<!-- 가수 이름 -->
		<h1 class="artist-name">
			<c:out value="${result.artistName}" />
		</h1>

		<!-- 가수 설명 -->
		<p class="artist-description">
			<c:out value="${result.description}" />
		</p>

		<!-- 앨범 리스트 -->
		<div class="album-list">
			<c:forEach var="album" items="${result.albumNameList}"
				varStatus="status">
				<div class="album-item">
					<!-- 앨범 아트 -->
					<img src="${result.albumImageList[status.index]}" alt="앨범 아트"
						class="album-art">
					<!-- 앨범 이름 -->
					<p class="album-name">
						<c:out value="${album}" />
					</p>
				</div>
			</c:forEach>

		</div>
	</div>
	
</body>
</html>

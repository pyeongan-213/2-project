<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var='root' value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>앨범 상세페이지</title>
<link href="${root}css/searchInfo.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<!-- sidebar -->
	<c:import url="/WEB-INF/views/include/sidebar.jsp" />
	<!-- top_menu-->
	<%-- <c:import url="/WEB-INF/views/include/top_menu.jsp" /> --%>
	<div class=container-box>

		<div class="background"></div>
		<section>
			<div class="album-info">
				<div class="album-art">
					<img src="${result.image}" alt="이미지를 불러올 수 없습니다." />
					<div class="actions">
						<div class="play">Playlist에 추가하기</div>
						<div class="bookmark">
							<svg xmlns="http://www.w3.org/2000/svg" fill="#faa800"
								height="24" viewbox="0 0 24 24" width="24">
            <path
									d="M17 3H7c-1.1 0-1.99.9-1.99 2L5 21l7-3 7 3V5c0-1.1-.9-2-2-2zm0 15l-5-2.18L7 18V5h10v13z"></path>
            <path d="M0 0h24v24H0z" fill="none"></path>
          </svg>
						</div>
					</div>
				</div>
				<div class="album-details">
					<h2>
						<img src="https://68.media.tumblr.com/avatar_edbd71e8c8ac_128.png" />${result.artistName}
					</h2>
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
</body>
</html>
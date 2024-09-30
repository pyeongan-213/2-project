<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var='root' value="${pageContext.request.contextPath}/" />
<html>
<head>
<title>Music Search</title>
<link rel="stylesheet" type="text/css" href="${root}css/SearchPro.css">
</head>
<body>
	<div class="container">
		<!-- 검색 창 -->
		<div class="search-form">
			<h1>Search for Music</h1>
			<form action="search" method="get">
				<input type="text" name="query"
					placeholder="Enter song, artist, or album" value="${param.query}"
					required /> <select name="searchType">
					<option value="song" ${param.searchType == 'song' ? 'selected' : ''}>Song</option>
					<option value="artist" ${param.searchType == 'artist' ? 'selected' : ''}>Artist</option>
					<option value="album" ${param.searchType == 'album' ? 'selected' : ''}>Album</option>
				</select>
				<button type="submit">Search</button>
			</form>
		</div>

		<!-- 검색 결과 -->
		<div class="search-results">
			<h2>Search Results for "${param.query}" (${param.searchType})</h2>

			<c:choose>

				<c:when test="${searchType == 'song'}">
					<c:forEach var="music" items="${searchResult}" varStatus="status">
						<c:if test="${status.index < 10}">
							<div class="result-item">
								<h3>${music.title}</h3>
								<img src="${music.albumImage}" alt="${music.albumArtist}" />
								<p><strong>Album:</strong> ${music.albumTitle}</p>
								<p><strong>Artist:</strong> ${music.albumArtist}</p>
								<a href="${music.guid}">View Details</a>
							</div>
						</c:if>
					</c:forEach>
				</c:when>

				<c:when test="${searchType == 'artist'}">
					<c:forEach var="artist" items="${searchResult}" varStatus="status">
						<c:if test="${status.index < 10}">
							<div class="result-item">
								<h3>${artist.artistName}</h3>
								<img src="${artist.image}" alt="${artist.artistName}">
								<p><strong>Period:</strong> ${artist.period}</p>
								<p><strong>Description:</strong> ${artist.description}</p>
								<a href="parseDetail?guid=${artist.link}&type=artist">View Details</a>
								<ul>
									<c:forEach var="majorSong" items="${artist.majorSongList}">
										<li>${majorSong}</li>
									</c:forEach>
								</ul>
							</div>
						</c:if>
					</c:forEach>
				</c:when>

				<c:when test="${searchType == 'album'}">
					<c:forEach var="album" items="${searchResult}" varStatus="status">
						<c:if test="${status.index < 10}">
							<div class="result-item">
								<h3>${album.albumName}</h3>
								<img src="${album.albumimage}" alt="${album.albumName}">
								<p><strong>Artist:</strong> ${album.albumArtist}</p>
								<p><strong>Release Company:</strong> ${album.releaseCompany}</p>
								<a href="parseDetail?guid=${fn:escapeXml(album.guid)}&type=album">View Details</a>
								<ul>
									<c:forEach var="track" items="${album.trackList}">
										<li>${track}</li>
									</c:forEach>
								</ul>
								<p><strong>Description:</strong> ${album.description}</p>
							</div>
						</c:if>
					</c:forEach>
				</c:when>
			</c:choose>
		</div>
	</div>
</body>
</html>

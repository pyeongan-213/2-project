<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<title>YouTube Search</title>
</head>
<body>

	<h2>Search YouTube</h2>


	<c:if test="${not empty searchResults}">
		<h3>Search Results:</h3>
		<table border="1">
			<thead>
				<tr>
					<th>Thumbnail</th>
					<th>Title</th>
					<th>Artist</th>
					<th>Duration</th>
					<th>Video</th>
					<th>Add to Playlist</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="song" items="${searchResults}">
					<tr>
						<!-- 썸네일 이미지 출력 -->
						<td><img src="${song.thumbnailUrl}" alt="Thumbnail"
							width="120" height="90" /></td>
						<!-- 노래 제목 출력 -->
						<td>${song.musicName}</td>
						<!-- 아티스트 이름 출력 -->
						<td>${song.artist}</td>
						<!-- 동영상 길이 출력 -->
						<td>${song.musicLength}</td>
						<!-- 비디오 링크 출력 -->
						<td><a href="${song.videoUrl}" target="_blank">Watch</a></td>
						<!-- playlist에 추가하는 폼 -->
						<td>
							<form action="${pageContext.request.contextPath}/addToPlaylist"
								method="post">
								<!-- YouTube URL에서 videoId 추출 -->
								<input type="hidden" name="videoId"
									value="${fn:substringAfter(song.videoUrl, 'v=')}"> <input
									type="hidden" name="musicName" value="${song.musicName}">
								<button type="submit">Add to Playlist</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty searchResults}">
		<p>No search results found.</p>
	</c:if>


</body>
</html>

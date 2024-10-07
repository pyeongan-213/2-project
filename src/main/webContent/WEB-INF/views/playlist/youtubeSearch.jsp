<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
					<th>Title</th>
					<th>Artist</th>
					<th>Thumbnail</th>
					<th>Video</th>
					<th>Add to Playlist</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="song" items="${searchResults}">
					<tr>
						<td>${song.musicName}</td>
						<td>${song.artist}</td>
						<td><img src="${song.thumbnailUrl}" alt="Thumbnail" /></td>
						<td><a href="${song.videoUrl}" target="_blank">Watch</a></td>
						<td>
							<form
								action="${pageContext.request.contextPath}/youtube/downloadMp3"
								method="post">
								<!-- 곡 정보를 hidden 필드로 전달 -->
								<input type="hidden" name="videoUrl" value="${song.videoUrl}">
								<input type="hidden" name="musicName" value="${song.musicName}">
								<input type="hidden" name="artist" value="${song.artist}">
								<input type="hidden" name="thumbnailUrl"
									value="${song.thumbnailUrl}">
								<button type="submit">MP3로 다운로드</button>
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

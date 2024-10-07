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
							<form action="${pageContext.request.contextPath}/addToPlaylist"
								method="post">
								<input type="hidden" name="videoId"
									value="${song.videoUrl.split('=')[1]}"> <input
									type="hidden" name="musicName" value="${song.musicName}">
								<button type="submit">플레이리스트에 추가</button>
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

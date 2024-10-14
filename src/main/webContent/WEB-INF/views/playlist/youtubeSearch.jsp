<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<script>

</script>
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
						<td>${song.music_Name}</td>
						<td>${song.artist}</td>
						<td><img src="${song.thumbnailUrl}" alt="Thumbnail" /></td>
						<td><a href="${song.videoUrl}" target="_blank">Watch</a></td>
						<td>
							<form
								action="${pageContext.request.contextPath}/playlist/addToPlaylist"
								method="post">
								<!-- 곡 정보를 hidden 필드로 전달 -->
								<input type="hidden" name="videoUrl" value="${song.videoUrl}">
								<input type="hidden" name="music_Name" value="${song.music_Name}">
								<input type="hidden" name="artist" value="${song.artist}">
								<input type="hidden" name="thumbnailUrl"
									value="${song.thumbnailUrl}">

								<!-- 플레이리스트를 선택할 수 있는 드롭다운 -->
								<select name="playlistId">
									<c:forEach var="playlist" items="${userPlaylists}">
										<option value="${playlist.playlistId}">${playlist.playlistName}</option>
									</c:forEach>
								</select>

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

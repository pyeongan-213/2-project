<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Your Playlist</title>
</head>
<body>

	<h2>Your Playlist</h2>
	<table border="1">
		<thead>
			<tr>
				<th>순서</th>
				<th>제목</th>
				<th>아티스트</th>
				<th>동영상</th>
				<th>삭제</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="song" items="${playlist}" varStatus="status">
				<tr>
					<td>${status.index + 1}</td>
					<td>${song.musicName}</td>
					<td>${song.artist}</td>
					<td><a href="${song.videoUrl}" target="_blank">Watch</a></td>
					<td>
						<form
							action="${pageContext.request.contextPath}/removeFromPlaylist"
							method="post">
							<input type="hidden" name="title" value="${song.musicName}">
							<button type="submit">삭제</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>


</body>
</html>

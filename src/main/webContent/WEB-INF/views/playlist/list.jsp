<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Playlist List</title>
<link href="${pageContext.request.contextPath}/css/styles.css"
	rel="stylesheet" type="text/css" />
</head>
<body>

	<h1>My Playlists</h1>

	<c:if test="${not empty playlists}">
		<table border="1">
			<thead>
				<tr>
					<th>Playlist Name</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="playlist" items="${playlists}">
					<tr>
						<td>${playlist.playlistName}</td>
						<td>
							<p>Playlist ID: ${playlist.playlistId}</p> <!-- playlistId를 출력하여 확인 -->
							<a
							href="${pageContext.request.contextPath}/playlist/view?playlistId=${playlist.playlistId}">View</a>
							|
							<form action="${pageContext.request.contextPath}/playlist/delete"
								method="post" style="display: inline;">
								<input type="hidden" name="playlistId"
									value="${playlist.playlistId}">
								<button type="submit">Delete</button>
							</form>
						</td>
					</tr>
				</c:forEach>


			</tbody>
		</table>
	</c:if>

	<c:if test="${empty playlists}">
		<p>
			You don't have any playlists yet. <a
				href="${pageContext.request.contextPath}/playlist/create">Create
				one now!</a>
		</p>
	</c:if>

</body>
</html>

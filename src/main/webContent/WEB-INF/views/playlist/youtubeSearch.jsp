<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>YouTube Search</title>
</head>
<body>

<h2>Search YouTube</h2>
<form action="${pageContext.request.contextPath}/youtubeSearch" method="get">
    <input type="text" name="query" placeholder="Search YouTube" required>
    <button type="submit">Search</button>
</form>

<c:if test="${not empty searchResults}">
    <h2>Search Results</h2>
    <table border="1">
        <thead>
            <tr>
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
                    <td>${song.musicName}</td>
                    <td>${song.artist}</td>
                    <td>${song.musicLength}</td>
                    <td><a href="${song.videoUrl}" target="_blank">Watch</a></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/addToPlaylist" method="post">
                            <input type="hidden" name="videoId" value="${song.videoUrl.split('=')[1]}">
                            <button type="submit">Add to Playlist</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>

</body>
</html>

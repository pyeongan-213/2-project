<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Your Playlist</title>
</head>
<body>

<h2>Your Playlist</h2>
<table border="1">
    <thead>
        <tr>
            <th>Title</th>
            <th>Artist</th>
            <th>Duration</th>
            <th>Remove</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="song" items="${playlist}">
            <tr>
                <td>${song.musicName}</td>
                <td>${song.artist}</td>
                <td>${song.musicLength}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/removeFromPlaylist" method="post">
                        <input type="hidden" name="title" value="${song.musicName}">
                        <button type="submit">Remove</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>

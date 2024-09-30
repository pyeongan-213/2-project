<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>YouTube Search and Playlist</title>
</head>
<body>

    <!-- 검색창 -->
    <h2>Search YouTube</h2>
    <form action="${pageContext.request.contextPath}/youtubeSearch" method="get">
        <input type="text" name="query" placeholder="Enter search term" required />
        <button type="submit">Search</button>
    </form>

    <!-- 검색 결과 -->
    <c:if test="${not empty musicBeans}">
        <h2>YouTube Search Results</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Artist</th>
                    <th>Duration</th>
                    <th>Video</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="music" items="${musicBeans}">
                    <tr>
                        <td>${music.musicName}</td>
                        <td>${music.artist}</td>
                        <td>${music.musicLength}</td>
                        <td><a href="${music.videoUrl}" target="_blank">Watch</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <!-- 검색 결과가 없을 때의 처리 -->
    <c:if test="${empty musicBeans}">
        <p>No results found. Try searching for something else.</p>
    </c:if>

</body>
</html>

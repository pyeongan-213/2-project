<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Playlist</title>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>

    <!-- 제목 -->
    <h1>Your Playlist</h1>

    <!-- 플레이리스트 테이블 -->
    <table id="playlist">
        <thead>
            <tr>
                <th>Title</th>
                <th>Artist</th>
                <th>Duration</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="song" items="${playlist}">
                <tr id="${song.musicId}">
                    <td>${song.musicName}</td>
                    <td>${song.artist}</td>
                    <td>${song.musicLength}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- YouTube 검색 폼 -->
    <h2>YouTube Search</h2>
    <form action="/youtubeSearch" method="get">
        <input type="text" name="query" placeholder="Search YouTube" required>
        <button type="submit">Search</button>
    </form>

    <!-- YouTube 검색 결과 테이블 -->
    <c:if test="${not empty results}">
        <h2>YouTube Search Results</h2>
        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Thumbnail</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="result" items="${results}">
                    <tr>
                        <td>${result.snippet.title}</td>
                        <td>${result.snippet.description}</td>
                        <td><img src="${result.snippet.thumbnails.url}" alt="Thumbnail"></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <!-- jQuery UI로 드래그 앤 드롭 기능 추가 -->
    <script>
        $(function() {
            $("#playlist tbody").sortable({
                update: function(event, ui) {
                    var order = $(this).sortable('toArray');
                    console.log(order); // 변경된 순서를 확인
                    // 여기에 Ajax 요청 추가하여 서버로 변경된 순서를 전송
                    $.ajax({
                        url: '/updatePlaylistOrder', // 서버의 플레이리스트 순서 업데이트를 처리하는 엔드포인트
                        method: 'POST',
                        data: { order: order },
                        success: function(response) {
                            console.log('Order updated:', response);
                        },
                        error: function(error) {
                            console.error('Error updating order:', error);
                        }
                    });
                }
            });
        });
    </script>

</body>
</html>

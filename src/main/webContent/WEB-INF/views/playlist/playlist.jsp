<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var='root' value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>DuckMusic</title>

    <!-- 탭 아이콘 추가 -->
    <link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
    <!-- CSS 및 Bootstrap 아이콘 추가 -->
    <link href="${root}/css/playlist.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://www.youtube.com/iframe_api"></script> <!-- YouTube Iframe API 추가 -->
</head>

<body>
    <header>
        <!-- top_menu.jsp 포함 -->
        <jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

        <!-- Sidebar 포함 -->
        <div class="sidebar">
            <jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
        </div>
    </header>

    <div id="contentContainer">
        <!-- 왼쪽: YouTube 음악 플레이어 및 재생 컨트롤 -->
        <div class="musicPlayer-container">

            <div class="player-wrapper">
                <!-- YouTube iframe 및 재생 컨트롤을 하나의 박스에 묶음 -->
                <iframe id="musicPlayer" src="https://www.youtube.com/embed/${musicList[0].videoUrl}?enablejsapi=1&autoplay=1"
                    frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe>
                
                <div class="player-controls">
                    <h4 id="currentSongTitle">${musicList[0].music_Name}</h4>
                    <h5 id="currentArtist">${musicList[0].artist}</h5>
                    
                    <!-- 재생바 -->
                    <input type="range" id="seekBar" value="0" max="100" step="0.1">
                    <div class="time-info">
                        <span id="currentTime">0:00</span>
                        <span id="durationTime">0:00</span>
                    </div>
                    
                    <!-- 컨트롤 버튼들 -->
                    <div class="controls">
                        <button id="prevBtn"><i class="bi bi-skip-backward-fill"></i></button>
                        <button id="playPauseBtn"><i class="bi bi-play-fill"></i></button>
                        <button id="nextBtn"><i class="bi bi-skip-forward-fill"></i></button>
                        <button id="shuffleBtn"><i class="bi bi-shuffle"></i></button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 오른쪽: 플레이리스트 -->
        <div class="playlist-container">
            <h3>플레이리스트</h3>
            <table class="table">
                <thead>
                    <tr>
                        <th>곡 이름</th>
                        <th>아티스트</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="music" items="${musicList}">
                        <tr>
                            <td>
                                <!-- 곡 클릭 시 YouTube 재생 --> 
                                <a href="https://www.youtube.com/embed/${music.videoUrl}"
                                   target="musicPlayer">${music.music_Name}</a>
                            </td>
                            <td>${music.artist}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script src="${root}/js/playlist.js"></script>
    <footer>
        <!-- bottom_info.jsp 포함 -->
        <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
    </footer>
</body>

</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>DuckMusic</title>
    
<!-- 탭 아이콘 추가 -->
    <link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
    <!-- CSS 및 Bootstrap 아이콘 추가 -->
    <link href="${root}/css/main.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>

    <!-- top_menu.jsp 포함 -->
    <jsp:include page="/WEB-INF/views/include/top_menu.jsp" />
    
    <!-- 전체 컨테이너 설정 -->
    <div class="flex-container">
        <!-- Sidebar 포함 -->
        <div class="sidebar">
            <jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
        </div>

        <!-- 메인 콘텐츠 영역 -->
        <div class="main-content">
            <!-- Header -->
            <div class="header">
                <h1 class="main-header"></h1>
            </div>

            <!-- Section -->
            <section class="main-container">
                <!-- 인기 아티스트 섹션 -->
                <div class="section">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <h2 class="section-title">인기 아티스트</h2>
                        <a href="${root}/artists/all" class="see-all-link">모두 표시</a> <!-- 모두 표시 링크 추가 -->
                    </div>
                    <div class="artists-grid">
                        <!-- 아티스트 블록 -->
                        <div class="artist">
                            <img class="artist-img" src="${root}/img/newjeans.jpg" alt="NewJeans">
                            <div class="artist-info">
                                <h5>NewJeans</h5>
                                <p>아티스트</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                         <div class="artist">
                            <img class="artist-img" src="${root}/img/newjeans.jpg" alt="NewJeans">
                            <div class="artist-info">
                                <h5>NewJeans</h5>
                                <p>아티스트</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                         <div class="artist">
                            <img class="artist-img" src="${root}/img/newjeans.jpg" alt="NewJeans">
                            <div class="artist-info">
                                <h5>NewJeans</h5>
                                <p>아티스트</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                         <div class="artist">
                            <img class="artist-img" src="${root}/img/newjeans.jpg" alt="NewJeans">
                            <div class="artist-info">
                                <h5>NewJeans</h5>
                                <p>아티스트</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                        <div class="artist">
                            <img class="artist-img" src="${root}/img/newjeans.jpg" alt="NewJeans">
                            <div class="artist-info">
                                <h5>NewJeans</h5>
                                <p>아티스트</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                         <div class="artist">
                            <img class="artist-img" src="${root}/img/newjeans.jpg" alt="NewJeans">
                            <div class="artist-info">
                                <h5>NewJeans</h5>
                                <p>아티스트</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 인기 앨범 섹션 -->
                <div class="section">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <h2 class="section-title">인기 앨범</h2>
                        <a href="${root}/albums/all" class="see-all-link">모두 표시</a> <!-- 모두 표시 링크 추가 -->
                    </div>
                    <div class="albums-grid">
                        <div class="album">
                            <img class="card-img-top" src="${root}/img/지민.jpg" alt="Album">
                            <div class="card-body">
                                <h5 class="fw-bolder">MUSE</h5>
                                <p>지민</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                        <div class="album">
                            <img class="card-img-top" src="${root}/img/지민.jpg" alt="Album">
                            <div class="card-body">
                                <h5 class="fw-bolder">MUSE</h5>
                                <p>지민</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                        <div class="album">
                            <img class="card-img-top" src="${root}/img/지민.jpg" alt="Album">
                            <div class="card-body">
                                <h5 class="fw-bolder">MUSE</h5>
                                <p>지민</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                        <div class="album">
                            <img class="card-img-top" src="${root}/img/지민.jpg" alt="Album">
                            <div class="card-body">
                                <h5 class="fw-bolder">MUSE</h5>
                                <p>지민</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                        <div class="album">
                            <img class="card-img-top" src="${root}/img/지민.jpg" alt="Album">
                            <div class="card-body">
                                <h5 class="fw-bolder">MUSE</h5>
                                <p>지민</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                        
                        <div class="album">
                            <img class="card-img-top" src="${root}/img/지민.jpg" alt="Album">
                            <div class="card-body">
                                <h5 class="fw-bolder">MUSE</h5>
                                <p>지민</p>
                            </div>
                            <div class="play-button">
                                <i class="bi bi-play-fill"></i> <!-- 재생 버튼 -->
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <!-- bottom_info.jsp 포함 -->
    <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
    
    <!-- JavaScript 파일 -->
    <script src="${root}/js/main.js"></script>
</body>
</html>

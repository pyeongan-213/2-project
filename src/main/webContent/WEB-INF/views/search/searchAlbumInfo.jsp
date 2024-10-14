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
    <link href="${root}/css/searchAlbum.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> 
    <!-- 스타일 추가 -->
    <style>
        /* Modal 관련 스타일 */
        html, body {
            width: 100%;
            height: 100%;
            margin: 0;
        }
        .page-wrapper {
            width: 100%;
            height: 100%;
            background: url(https://goo.gl/OeVhun) center no-repeat;
            background-size: cover;
        }
        .blur-it {
            filter: blur(4px);
        }
        a.btn {
            width: 200px;
            padding: 18px 0;
            position: absolute;
            top: 50%; 
            left: 50%;
            transform: translate(-50%, -50%);
            font-family: 'Montserrat', Arial, Helvetica, sans-serif;
            font-weight: 700;
            text-align: center;
            text-decoration: none;
            text-transform: uppercase;
            color: #fff;
            border-radius: 0;
            background: #e2525c;
        }
        .modal-wrapper {
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0; 
            left: 0;
            background: rgba(15, 15, 15, 0.9);
            visibility: hidden;
            opacity: 0;
            transition: all 0.25s ease-in-out;
        }
        .modal-wrapper.open {
            opacity: 1;
            visibility: visible;
        }
        .modal {
            width: 68%;
            height: 80%;
            display: block;
            position: relative;
            top: 50%; 
            left: 50%;
            transform: translate(-50%, -50%);
            background: #222;
            opacity: 0;
            transition: all 0.5s ease-in-out;
        }
        .modal-wrapper.open .modal {
            opacity: 1;
        }
        .head { 
            width: 100%;
            padding: 15px;
            background: #1db954;
            font-size: 40px;
            color: #fff;
            left-padding: 35px;
        }
        .btn-close {
            font-size: 40px;
            float: right;
            color: #fff;
            cursor: pointer;
            position: absolute;
            right: 26px;
            top: 8px;
        }
        .content {
            padding: 3%;
        }
    </style>
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
    <div class="background"></div>
    <section>
        <div class="album-info">
            <div class="album-art">
                <img src="${result.image}" alt="이미지를 불러올 수 없습니다." />
                <div class="actions">
                    <div class="bookmark"></div>
                </div>
            </div>
            <div class="album-details">
                <h1>${result.albumName}</h1>
                <h4>Artist : ${result.artistName}</h4>
                <p><h6>${result.description}</h6></p>
            </div>
        </div>

        <table class="album-table">
            <thead>
                <tr>
                    <th class="column-count"><h3>#</h3></th>
                    <th class="column-title"><h3>제목</h3></th>
                    <th class="column-playtime"><h3>재생시간</h3></th>
                    <th class="column-action"><h3>-</h3></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="track" items="${result.trackList}" varStatus="status">
                    <tr>
                        <td class="column-count">${status.count}</td>
                        <td class="column-title">${track}</td>
                        <td class="column-playtime">${result.runningTimeList[status.index]}</td>
                        <td class="column-action">
                            <!-- 모달을 통해 YouTube 검색 페이지 열기 -->
                            <button class="openModal" data-track="${track}" data-artist="${result.artistName}">+</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </section>
</div>

<!-- Modal 팝업 -->
<div class="modal-wrapper">
  <div class="modal">
    <div class="head">
      <span><h1 >플레이리스트에 추가</h1></span><span class="btn-close">×</span>
    </div>
    <div class="content" id="modalContent">
      <!-- YouTube 검색 결과가 여기에 표시됩니다 -->
    </div>
  </div>
</div>

<footer>
    <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
</footer>

<script>
    $(document).ready(function() {
        // 모달 열기
        $(document).on('click', '.openModal', function() {
            var trackName = $(this).data('track');
            var artistName = $(this).data('artist');

            // AJAX로 YouTube 검색 페이지를 모달에 로드
            $.ajax({
                url: '${root}/youtubeSearch',  // 검색 요청을 보낼 경로
                type: 'GET',  
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: {
                    query: artistName + " " + trackName
                },
                success: function(response) {
                    // YouTube 검색 결과를 모달에 표시
                    $('#modalContent').html(response); 
                    $('.modal-wrapper').addClass('open');
                },
                error: function() {
                    alert('Failed to load search results.');
                }
            });
        });

        // 모달 닫기
        $(document).on('click', '.btn-close', function() {
            $('.modal-wrapper').removeClass('open');
        });
    });
</script>
</body>
</html>

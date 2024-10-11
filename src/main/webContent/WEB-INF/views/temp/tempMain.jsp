<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var='root' value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>DuckMusic</title>

<!-- 탭 아이콘 추가 -->
<link rel="icon" type="image/png" sizes="48x48"
	href="${root}/img/tabicon.png">
<!-- CSS 및 Bootstrap 아이콘 추가 -->
<link href="${root}/css/main.css" rel="stylesheet" type="text/css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
	rel="stylesheet">
	
 <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        #contentContainer {
            margin: 20px;
        }

        .album-info {
            display: flex;
            margin-bottom: 30px;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        .album-art img {
            width: 200px;
            height: 200px;
            border-radius: 10px;
            object-fit: cover;
            margin-right: 20px;
        }

        .album-details {
            max-width: 800px;
        }

        .album-details h1, .album-details h2 {
            margin: 0;
            color: #333;
        }

        .album-details h1 {
            font-size: 1.8em;
            margin-bottom: 10px;
        }

        .album-details h2 {
            font-size: 1.2em;
            color: #666;
        }

        .album-details p {
            font-size: 1em;
            line-height: 1.6em;
            color: #555;
        }

        .album-tracks ol {
            padding-left: 20px;
        }

        .album-tracks ol li {
            margin-bottom: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .album-tracks li span {
            margin-right: 10px;
        }

        .album-tracks a {
            color: #306eff;
            text-decoration: none;
            font-size: 0.9em;
        }

        .album-tracks a:hover {
            text-decoration: underline;
        }

        footer {
            background-color: #333;
            padding: 20px;
            color: white;
            text-align: center;
        }

        footer p {
            margin: 0;
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
        <section>
            <div class="album-info">
                <div class="album-art">
                    <img src="http://i.maniadb.com/images/album_t/260/687/687918_1_f.jpg" alt="이미지를 불러올 수 없습니다.">
                </div>
                <div class="album-details">
                    <h2>아이유</h2>
                    <h2>/artist/153272</h2>
                    <h1>아이유 2집 - Last Fantasy</h1>
                    <p>아이유의 새 앨범 <strong>Last Fantasy</strong>는 1집 이후 2년 만에 선보이는 정규앨범으로...</p>
                    <span>2011-11-29</span>
                    <br>
                    <span>로엔</span>
                </div>
            </div>

            <div class="album-tracks">
                <ol>
                    <li>
                        <span>비밀</span>
                        <span>4:05</span>
                        <a href="/Project_2/youtubeSearch?query=아이유+비밀">playlist에 추가</a>
                    </li>
                    <li>
                        <span>잠자는 숲 속의 왕자</span>
                        <span>3:35</span>
                        <a href="/Project_2/youtubeSearch?query=아이유+잠자는 숲 속의 왕자">playlist에 추가</a>
                    </li>
                    <!-- 나머지 트랙 리스트 생략 -->
                </ol>
            </div>
        </section>
    </div> 	<!-- contentContainer -->
	
	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
</body>
</html>

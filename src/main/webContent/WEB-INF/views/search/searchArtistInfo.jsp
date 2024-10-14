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

    <!-- Tab 아이콘 -->
    <link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
    <!-- Bootstrap 및 사용자 정의 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${root}/css/artistPage.css">
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

        <div class="artist-info">
            <!-- 가수 사진 -->
            <img src="<c:out value='${result.image}' />" alt="가수 사진" class="artist-photo">

            <div class="artist-details">
                <!-- 가수 이름 -->
                <h1 class="artist-name">
                    <c:out value="${result.artistName}" />
                </h1>

                <!-- 가수 설명 -->
                <p class="artist-description">
                    <c:out value="${result.description}" />
                </p>
            </div>
        </div>
	<hr />
        <!-- 앨범 리스트 -->
        <div class="album-list">
            <c:forEach var="album" items="${result.albumNameList}" varStatus="status">
                <div class="album-item">
                    <!-- 앨범 아트 -->
                    <img src="${result.albumImageList[status.index]}" alt="앨범 아트" class="album-art">
                    <!-- 앨범 이름 -->
                    <p class="album-name">
                        <c:out value="${album}" />
                    </p>
                </div>
            </c:forEach>
        </div>
    </div>

    <footer>
        <!-- bottom_info.jsp 포함 -->
        <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
    </footer>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상단 메뉴</title>
    <link rel="stylesheet" href="${root}/css/top_menu.css">
    <!-- 필요한 CSS 및 아이콘 파일 로드 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <div class="navbar fixed-top"> <!-- 고정된 상단 메뉴 -->
        <!-- 로고 영역 -->
        <div class="navbar-logo">
            <a href="${root}/main" class="logo-link">DuckMusic</a>
        </div>

        <!-- 중앙 정렬: 홈 버튼 및 검색창 -->
        <div class="navbar-center">
            <!-- 홈 버튼 -->
            <div class="navbar-home">
                <a href="${root}/main"><i class="bi bi-house"></i></a>
            </div>
            <!-- 검색창 -->
            <div class="navbar-search">
                <input type="text" class="search-input" placeholder="어떤 콘텐츠를 감상하고 싶으세요?">
                <i class="bi bi-search search-icon"></i>
            </div>
        </div>

        <!-- 우측 로그인 및 구독 -->
        <div class="navbar-right">
            <a href="${root}/join" class="join-link">구독</a>
            
            <!-- 로그인 여부에 따라 동적으로 버튼 변경 -->
            <c:choose>
                <c:when test="${sessionScope.loginMemberBean != null}">
                    <!-- 로그인이 된 상태 -->
                    <a href="${root}/member/logout" class="logout-btn">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <!-- 로그아웃 상태 -->
                    <a href="${root}/member/login" class="login-btn">로그인</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />

<!-- 사이드바 영역 -->
<div class="sidebar-content">
    <div class="side-wrapper">
        <h2 class="sidebar-header">MENU</h2>
        <div class="side-menu">
            <a class="sidebar-link" href="${root}/board/main">Board</a>
            <a class="sidebar-link" href="${root}/temp/tempMain">Temp</a>
            <a class="sidebar-link" href="${root}/quiz/quizlobby">Quiz</a>
        </div>
    </div>
</div>

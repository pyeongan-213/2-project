<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var='root' value="${pageContext.request.contextPath }/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <h1>메인페이지입니다.</h1>

    <h2>
        <a href="${root}temp/tempMain">To tempMain</a> 
        <hr />
        <a href="${root}temp/playlist">플레이리스트로 가기</a>
        <hr />
        <a href="${root}melon-chart">Melon 차트 보기</a> <!-- Melon 차트로 가는 링크 -->
        <hr />
        <a href="${root}temp/maniadbSearch">ManiaDB 검색으로 가기</a> <!-- ManiaDB 검색으로 가는 링크 -->
        <hr />
    </h2>
</body>
</html>

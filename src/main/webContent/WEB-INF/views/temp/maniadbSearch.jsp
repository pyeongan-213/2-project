<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>ManiaDB 검색</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
    </style>
</head>
<body>
    <h1>ManiaDB 검색</h1>
    <form action="${pageContext.request.contextPath}/temp/maniadbSearch" method="get">
        <input type="text" name="query" placeholder="검색어 입력" required>
        <button type="submit">검색</button>
    </form>

    <div>
        <h2>검색 결과</h2>
        <c:if test="${not empty result}">
            <table>
                <thead>
                    <tr>
                        <th>곡 제목</th>
                        <th>아티스트</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="music" items="${result}">
                        <tr>
                            <td>${music.title}</td>
                            <td>${music.artist}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty result}">
            <p>${resultMessage}</p>  <!-- 결과가 비어 있을 때 메시지 -->
        </c:if>
    </div>
</body>
</html>

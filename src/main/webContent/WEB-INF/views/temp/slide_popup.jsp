<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제 옵션</title>
    <link rel="stylesheet" href="${root }/css/popup.css">
</head>
<body>
    <div class="payment-options">
        <label>
            <input type="radio" name="payment" id="monthly" onclick="openPopup('monthly-popup')"> 월간결제
        </label>
        <label>
            <input type="radio" name="payment" id="yearly" onclick="openPopup('yearly-popup')"> 연간결제
        </label>
    </div>

    <!-- 슬라이드 팝업 -->
    <div id="monthly-popup" class="popup">
        <h2>월 정기구독 9,900원/월</h2>
        <p>오리궁둥이 월 정기구독 9,900원/월</p>
        <button onclick="closePopup('monthly-popup')">닫기</button>
    </div>

    <div id="yearly-popup" class="popup">
        <h2>연 정기구독 99,000원/연</h2>
        <p>오리궁둥이 연 정기구독 99,000원/년</p>
        <button onclick="closePopup('yearly-popup')">닫기</button>
    </div>

    <script src="${root}/js/popup.js"></script>
</body>
</html>

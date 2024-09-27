<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>결제 옵션</title>
    <link rel="stylesheet" href="${root}/css/popup.css">
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

    <div id="monthly-popup" class="popup">
        <div class="popup-section red">
            <h2 align="center" style="margin-bottom: 30px;">선택한 구독 상품</h2>
        </div>
        <div class="popup-section yellow">
            <div class="product-info">
                <div class="product-text">
                    <h3 style="margin-bottom: 9px; margin-left: -10px;">오리궁둥이 월 정기구독</h3>
                    <p style="color: #333; margin: 0; margin-left: -10px;">9,900원/월</p>
                </div>
                <button class="close-button" onclick="closePopup('monthly-popup')" style="margin-left: -10px;">X</button>
            </div>
        </div>
        <div class="popup-section green"></div>
    </div>

    <div id="yearly-popup" class="popup">
        <div class="popup-section red">
            <h2 align="center" style="margin-bottom: 30px;">선택한 구독 상품</h2>
        </div>
        <div class="popup-section yellow">
            <div class="product-info">
                <div class="product-text">
                    <h3 style="margin-bottom: 9px; margin-left: -10px;">오리궁둥이 년 정기구독</h3>
                    <p style="color: #333; margin: 0; margin-left: -10px;">99,000원/년</p>
                </div>
                <button class="close-button" onclick="closePopup('yearly-popup')" style="margin-left: -10px;">X</button>
            </div>
        </div>
        <div class="popup-section green"></div>
    </div>

    <script src="${root}/js/popup.js"></script>
</body>
</html>
 --%>




<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<header>

	<h1>오리둥둥이와 함께 음악을 시작해보세요.</h1>

</header>

<title>결제 옵션</title>
<link rel="stylesheet" href="${root}/css/popup.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body>
	

	<h2>음악 스트리밍 구독</h2>
	<b>3000만 곡의 무제한 음악과 유튜브 커버곡까지 이용할 수 있어요.</b>


	<h3 class="title">
		<b>오리둥둥이</b> 멤버가 되어보세요
	</h3>





	<div class="btn-group">
		<!-- <button class="mocean-modal-button" data-mocean-type="slide-in-bottom">월간결제</button> -->
		<button class="mocean-modal-button" data-mocean-type="slide-in-bottom"
			data-mocean-out-type="slide-out-top">월간결제</button>
		<!-- 추가된 버튼: 2번 바뀐 모달을 위한 버튼 -->
		<button class="mocean-modal-button" data-mocean-type="slide-in-bottom"
			data-target="#mocean-modal-2">연간결제</button>


	</div>





	<div class="mocean-wrap mocean-modal-wrap" id="mocean-modal-wrap">
		<div class="mocean-content mocean-modal" id="mocean-modal">
			<h2>선택한 구독 상품</h2>
			<div class="mocean-modal-content">
				<h3>월간결제</h3>
				<p>9,900원/월</p>
				
				<button class="mocean-modal-close">Close me!</button>
			</div>
		</div>
	</div>



	<div class="mocean-wrap mocean-modal-wrap" id="mocean-modal-2">
		<!-- ID 변경 -->
		<div class="mocean-content mocean-modal">
			<h2>선택한 구독 상품</h2>
			<div class="mocean-modal-content">
				<h3>연간결제</h3>
				<p>99,900원/월</p>
				
				<button class="mocean-modal-close">Close me!</button>
			</div>
		</div>
	</div>


	<h2>결제</h2>
	<h3>
		<a href="${root }temp/slide_popup">다음</a>
	</h3>



	<script src="${root}js/popup.js"></script>
</body>
</html>






























<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>결제 옵션</title>
<link rel="stylesheet" href="${root}css/popup.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<body>
<header>

	<h1>오리둥둥이와 함께 음악을 시작해보세요.</h1>

</header>

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
				<button class="btn-pay"> 결제하기 </button>
				<button class="mocean-modal-close">Close me!</button>
			</div>
		</div>
	</div>



	<div class="mocean-wrap mocean-modal-wrap" id="mocean-modal-2">
		<!-- ID 변경 -->
		<div class="mocean-content mocean-modal">
			<h2>선택한 구독 상품</h2>
			<div class="mocean-modal-content">
				<h3 class="btn-pay">연간결제</h3>
				<p>99,900원/월</p>
				<button class="btn-pay"> 결제하기 </button>
				<button class="mocean-modal-close">Close me!</button>
			</div>
		</div>
	</div>


	<h2>결제</h2>
	<h3>
		<a href="${root }temp/slide_popup">다음</a>
	</h3>

	


	<script type="text/javascript">
		// 카카오페이 결제 팝업창 연결
		$(function() {
			$(".btn-pay").click(function(e) {
				// 아래 데이터 외에도 필요한 데이터를 원하는 대로 담고, Controller에서 @RequestBody로 받으면 됨
				let data = {
					name: '스트리밍',    // 카카오페이에 보낼 대표 상품명
					totalPrice: 9900 // 총 결제금액
				};

				$.ajax({
					type: 'post',
					url: '${root}temp/kakao/pay/ready',
					data: JSON.stringify(data),
					contentType: 'application/json',
					success: function(response) {
						location.href = response.next_redirect_pc_url;
					}
				});
			});
		});
	</script>
	<script src="${root}js/popup.js"></script>
	
</body>
</html>
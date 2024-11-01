<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 정보</title>
<!-- 탭 아이콘 추가 -->
<link rel="icon" type="image/png" sizes="80x80"
	href="${root}/img/favicon.png">
<link rel="stylesheet" href="${root}css/info.css" />
<script src="${root }js/finisher-header.es5.min.js"
	type="text/javascript"></script>
</head>
<body>
	<c:import url="/WEB-INF/views/include/top_menu.jsp" />

	<div class="header finisher-header"
		style="width: 100%; height: 1100px;">

		<div class="flex-container">

			<div class="sidebar">
				<c:import url="/WEB-INF/views/include/sidebar.jsp" />
			</div>

			<!-- 메인 콘텐츠 영역 -->
			<div class="container_info info-page">
				<div class="container_info">
					<div class="wrapper">
						<form:form action="${root}member/modify" method="post"
							modelAttribute="infoMemberBean">
							<h1>회원 정보</h1>

							<div class="input-box">
								<form:label path="membername">아이디</form:label>
								<form:input path="membername" placeholder="ID" readonly="true" />
								<i class='bx bxs-user'></i>
							</div>

							<div class="input-box">
								<form:label path="score">점수</form:label>
								<form:input path="score" readonly="true" />
								<i class='bx bxs-trophy'></i>
							</div>

							<div class="input-box">
								<form:label path="nickname">닉네임</form:label>
								<form:input path="nickname" class="editable"
									placeholder="NickName" readonly="true" />
								<i class='bx bx-edit-alt'></i>
							</div>

							<div class="input-box">
								<form:label path="real_name">실명</form:label>
								<form:input path="real_name" class="editable"
									placeholder="Real Name" readonly="true" />
								<i class='bx bxs-phone'></i>
							</div>

							<div class="input-box">
								<form:label path="email">이메일</form:label>
								<form:input path="email" placeholder="Email" readonly="true" />
								<i class='bx bx-envelope'></i>
							</div>

							<div class="input-box">
								<form:label path="birthday">생년월일</form:label>
								<form:input path="birthday" class="editable"
									placeholder="birthday" readonly="true" />
								<i class='bx bx-home'></i>
							</div>

							<div class="input-box">
								<form:label path="join_date">가입날짜</form:label>
								<form:input path="join_date" placeholder="Join Date"
									readonly="true" />
								<i class='bx bxs-phone'></i>
							</div>

							<div class="input-box">
								<form:label path="logintype">로그인타입</form:label>
								<form:input path="logintype" placeholder="LoginType"
									readonly="true" />
								<i class='bx bxs-phone'></i>
							</div>

							<div class="row justify-content-center">
								<form:button class="btn btn-outline-dark" type="submit">수정하기</form:button>
								<form:button class="btn btn-outline-dark" type="button"
									onclick="location.href='${root}member/delete_account'">회원탈퇴</form:button>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 푸터 추가 -->
	<c:import url="/WEB-INF/views/include/bottom_info.jsp" />

	<script type="text/javascript">
		new FinisherHeader({
			"count" : 100,
			"size" : {
				"min" : 1,
				"max" : 8,
				"pulse" : 0
			},
			"speed" : {
				"x" : {
					"min" : 0,
					"max" : 0.4
				},
				"y" : {
					"min" : 0,
					"max" : 0.6
				}
			},
			"colors" : {
				"background" : "#191414",
				"particles" : [ "#fbfcca", "#d7f3fe", "#ffd0a7" ]
			},
			"blending" : "overlay",
			"opacity" : {
				"center" : 1,
				"edge" : 0
			},
			"skew" : 0,
			"shapes" : [ "c" ]
		});
	</script>
</body>
</html>

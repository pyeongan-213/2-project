<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 탈퇴</title>
<link rel="stylesheet" href="${root }css/delete_account.css" />
<!-- SweetAlert CDN 추가 -->
<!-- <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script> -->
<link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
</head>
<body>

	<c:import url="/WEB-INF/views/include/top_menu.jsp" />
	<div class="flex-container">

		<div class="sidebar">
			<c:import url="/WEB-INF/views/include/sidebar.jsp" />
		</div>

		<div class="delete_container">
			<div class="wrapper">

			<!-- Spring의 form:form 태그를 사용하여 비밀번호 입력 폼을 생성 -->
			<form:form action="${root}member/delete_pro" method="post"
				modelAttribute="deleteMemberBean">
				
				<h2>회원 탈퇴</h2>
				
				<!-- 비밀번호 입력 필드 -->
				<label for="password">비밀번호 확인:</label>
				<form:password path="password" id="password" required="true" />

				<!-- 제출 버튼 -->
				<input type="submit" value="탈퇴하기" />
			</form:form>
		</div>
	</div>

		<!-- fail 값이 true일 경우 SweetAlert로 경고창 띄우기 -->
		<c:if test="${fail == true}">
			<script type="text/javascript">
				Swal.fire({
				  title: '비밀번호가 일치하지 않습니다.',
				  text: '다시 시도해주세요.',
				  icon: 'error',
				  background: '#3A3A3A',  // Spotify 짙은 회색 배경
				  color: '#fff',  // 흰색 텍스트
				  confirmButtonColor: '#1db954',  // Spotify 그린 확인 버튼
				});
			</script>
		</c:if>

		<!-- success 값이 true일 경우 SweetAlert로 성공 메시지와 리디렉션 -->
		<c:if test="${success == true}">
			<script type="text/javascript">
				Swal.fire({
				  title: '탈퇴가 완료되었습니다.',
				  text: '홈페이지로 이동합니다.',
				  icon: 'success',
				  background: '#3A3A3A',  // Spotify 짙은 회색 배경
				  color: '#fff',  // 흰색 텍스트
				  confirmButtonColor: '#1db954',  // Spotify 그린 확인 버튼
				  confirmButtonText: '확인'
				}).then((result) => {
				  if (result.isConfirmed) {
				    window.location.href = "${root}main"; // 메인 페이지로 리디렉션
				  }
				});
			</script>
		</c:if>
	</div>

	<c:import url="/WEB-INF/views/include/bottom_info.jsp" />
</body>
</html>

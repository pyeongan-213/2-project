<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="root" value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${root }css/modify.css" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
<link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
<script>
	var myVar = true;

	$(document).ready(function() {
		
		// 서버에서 전달된 상태와 메시지를 JavaScript로 처리
        var status = '${status}';  // 컨트롤러에서 전달된 status 값
        var message = '${message}';  // 컨트롤러에서 전달된 message 값

        if (status && message) {
            if (status === 'success') {
                // 성공 알림
                Swal.fire({
                    title: '성공!',
                    text: message,
                    icon: 'success',
                    confirmButtonText: '확인',
                    background: '#3A3A3A'
                }).then(function() {
                    window.location.href = "${root}member/info";  // 성공 시 리다이렉트
                });
            }
        }
		
		$('#changeBtn').click(function() {
			
			$('.wrapper').css('height', '300px');
			var email = $("#email").val();

			if (myVar) {
				myVar = false;
				$('.infoID').addClass('non');
				$('.info').removeClass('non');
			}

			$.ajax({
				url : '${root}member/modifyCertificationCode/' + email,
				dataType : 'text',
				success : function(data) {
					console.log("data : " + data);
					code = data;
					$("#authCode1").val(code);
					 // SweetAlert2 사용 - 다크 테마 적용 및 배경색 변경
			        Swal.fire({
			            title: '성공!',
			            text: '인증코드가 발송되었습니다.',
			            icon: 'success',
			            confirmButtonText: '확인',
			            background: '#3A3A3A'  // 배경색 변경
			        });
			    },
			    error: function() {
			        // SweetAlert2 에러 발생 시 - 다크 테마 적용 및 배경색 변경
			        Swal.fire({
			            title: '오류!',
			            text: '인증코드 발송에 실패했습니다.',
			            icon: 'error',
			            confirmButtonText: '확인',
			            background: '#3A3A3A'  // 배경색 변경
			        });
				}
			});
			// 기본적으로 submit 버튼을 비활성화 상태로 설정
			$('form button[type="submit"]').prop('disabled', true);

			// authCode2의 값이 변경될 때마다 체크하여 버튼 활성화/비활성화
			$('form input[name="authCode2"]').on('input', function() {
				var authCode2Value = $(this).val();

				// authCode2 값이 존재하는지 확인하여 submit 버튼 상태 변경
				if (authCode2Value.trim() !== '') {
					$('form button[type="submit"]').prop('disabled', false);
				} else {
					$('form button[type="submit"]').prop('disabled', true);
				}
			});
		});
	});
</script>
</head>
<body>
	<c:import url="/WEB-INF/views/include/top_menu.jsp" />

	<div class="flex-container">

		<div class="sidebar">
			<c:import url="/WEB-INF/views/include/sidebar.jsp" />
		</div>

		<div class="container_modify">
			<div class="wrapper">
				<form:form action="${root }member/modify_pro" method="post"
					modelAttribute="modifyMemberBean">
					<form:hidden path="authCode1" />
					<form:hidden path="membername" />
					<form:hidden path="password" />
					<form:hidden path="email" />
					<form:hidden path="join_date" />
					<form:hidden path="role" />
					<form:hidden path="logintype" />

					<div class="input-box infoID">
						<form:label path="age">나이</form:label>
						<form:input path="age" value=""></form:input>
						<form:errors path="age" style='color:red'/>
						<i class='bx bx-home'></i>
					</div>

					<div class="input-box infoID">
						<form:label path="real_name">실명</form:label>
						<form:input path="real_name" value=""></form:input>
						<form:errors path="real_name" style='color:red'/>
						<i class='bx bxs-user'></i>
					</div>

					<div class="input-box infoID">
						<form:label path="nickname">닉네임</form:label>
						<form:input path="nickname" placeholder="닉네임" value=""></form:input>
						<form:errors path="nickname" style='color:red'/>
						<i class='bx bxs-phone'></i>
					</div>

					<button type="button" id="changeBtn" class="btn infoID"
						style="width: 80%; color: #303080; margin-left: 31px;">정보수정</button>

					<div class="input-box info non">
						<form:input path="authCode2" type="text"
							placeholder="Certification Code" />
						<form:errors path="authCode2" />
						<i class='bx bx-envelope'></i>
						<form:button type="submit" class="btn"
							style=" width: 80%; margin-top: 50px; height: 40px; margin-left: 33px;">확인</form:button>
					</div>

				</form:form>
			</div>
		</div>
	</div>
	<c:import url="/WEB-INF/views/include/bottom_info.jsp" />
</body>
</html>
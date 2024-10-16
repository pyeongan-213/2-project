<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="root" value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${root }css/modifyPassword.css" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
<link
	href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
<script>

	var myVar = true;
	
    $(document).ready(function(){
      $('#changeBtn').prop('disabled', true); // 페이지 로드 시 버튼 비활성화

      $('#email').on('input', function() {
        var emailValue = $(this).val().trim();

        if (emailValue === '') {
          $('#changeBtn').prop('disabled', true); // 입력값이 없으면 버튼 비활성화
        } else {
          $('#changeBtn').prop('disabled', false); // 입력값이 있으면 버튼 활성화
        }
      });
    });
	
	$(document).ready(function(){
		$('#changeBtn').click(function(){
			if(myVar){
				myVar = false;
				$('.infoID').addClass('non');
				$('.info').removeClass('non');
			}
			var email=$("#email").val();
			
			$.ajax({
				url: '${root}member/modifyCertificationCode/' + email,
				dataType : 'text',
				success: function(data){
					console.log("data : "+data);
					code = data;
					$("#authCode1").val(code);
					console.log($("#authCode1"))
					Swal.fire({
			            title: '성공!',
			            text: '인증코드가 발송되었습니다.',
			            icon: 'success',
			            confirmButtonText: '확인',
			            background: '#3A3A3A'  // 배경색 변경
			        });

				}
			});
		});
	});
	
	$(document).ready(function(){
        // 페이지 로드 시 확인 버튼 비활성화
        $('form:button[type="submit"]').prop('disabled', true);

        // 모든 입력 필드의 유효성 검사 함수
        function validateFields() {
            var emailValue = $('#email').val().trim();
            var passwordValue = $('#password').val().trim();
            var password2Value = $('#password2').val().trim();
            var authCode2Value = $('#authCode2').val().trim();
            
            // 모든 입력 필드가 비어 있지 않으면 확인 버튼 활성화
            if(emailValue !== '' && passwordValue !== '' && password2Value !== '' && authCode2Value !== '') {
                $('form:button[type="submit"]').prop('disabled', false); // 버튼 활성화
            } else {
                $('form:button[type="submit"]').prop('disabled', true); // 버튼 비활성화
            }
        }

        // 각 입력 필드에 입력이 발생할 때마다 유효성 검사 실행
        $('.input_control, #authCode2').on('input', function() {
            validateFields();
        });
    });
</script>
</head>
<body>
	<c:import url="/WEB-INF/views/include/top_menu.jsp" />
	<div class="wrapper">
		<form:form action="${root }member/modifyPassword_pro" method="post"
			modelAttribute="modifyPasswordBean">
			<form:hidden path="authCode1" />
			<form:hidden path="membername" />
			<form:hidden path="age" />
			<form:hidden path="join_date" />
			<form:hidden path="real_name" />
			<form:hidden path="role" />
			<form:hidden path="nickname" />
			<form:hidden path="logintype" />

			<h1>비밀번호 변경</h1>

			<div class="input-box">
				<form:label path="email">이메일</form:label>
				<form:input path="email" class="input_control"></form:input>
				<form:errors path="email"></form:errors>
			</div>

			<div class="input-box">
				<form:label path="password">새 비밀번호</form:label>
				<form:password path="password" class="input_control" />
				<form:errors path="password" style='color:red' />
			</div>

			<div class="input-box">
				<form:label path="password2">새 비밀번호 확인</form:label>
				<form:password path="password2" class="input_control" />
				<form:errors path="password2" style='color:red' />
			</div>

			<div>
				<button type="button" id="changeBtn" class="btn"
					style="color: #303080">인증요청</button>
			</div>

			<div class="input-box">
				<form:input path="authCode2" type="text" placeholder="인증 코드" />
				<form:errors path="authCode2" style='color:red' />
			</div>

			<form:button type="submit" id="submit_btn" class="btn">확인</form:button>
		</form:form>
	</div>

	<c:if test="${success == true}">
		<script>
    		swal.fire({
    			 title: '비밀번호가 수정되었습니다.',
				  text: '로그인 페이지로 이동합니다.',
				  icon: 'success',
				  background: '#3A3A3A',  // Spotify 짙은 회색 배경
				  color: '#fff',  // 흰색 텍스트
				  confirmButtonColor: '#1db954',  // Spotify 그린 확인 버튼
				  confirmButtonText: '확인'
    		}).then((result) => {
				  if (result.isConfirmed) {
					    window.location.href = "${root}member/login"; // 로그인 페이지로 리디렉션
					  }
					});
    	</script>
	</c:if>

	<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
</body>
</html>

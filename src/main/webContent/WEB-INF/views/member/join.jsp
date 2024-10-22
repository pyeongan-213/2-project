<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="root" value="${pageContext.request.contextPath }/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<!-- 탭 아이콘 추가 -->
<link rel="icon" type="image/png" sizes="80x80"
	href="${root}/img/favicon.png">

<!-- jQuery 라이브러리 추가 -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="${root }css/join.css" />

<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
<link
	href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>

<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
	rel="stylesheet">
<script src="${root }js/finisher-header.es5.min.js"
	type="text/javascript"></script>
	<style>
    .required {
        color: red; /* 빨간색으로 필수 항목 표시 */
        margin-left: 3px; /* 라벨과 약간의 여백 */
    }
</style>
</head>
<script>
	function checkMemberNameExist(){
		var membername = $("#membername").val()
		
		if(membername.length == 0){
			Swal.fire({
				icon: 'warning',
				title: '아이디를 입력해주세요',
				background: '#3A3A3A',  // 배경색
				color: '#fff',  // 텍스트 색상
				confirmButtonColor: '#1db954',  // 확인 버튼 색상
				confirmButtonText: '확인'
			});
			 return
		}
		
		$.ajax({
			url : '${root}member/checkMemberNameExist/' + membername,
			type : 'get',
			dataType : 'text',
			success : function(result){
				if(result.trim() == 'true'){
					Swal.fire({
						icon: 'success',
						title: '사용할 수 있는 아이디 입니다',
						background: '#3A3A3A',  // 배경색
						color: '#fff',  // 텍스트 색상
						confirmButtonColor: '#1db954',  // 확인 버튼 색상
						confirmButtonText: '확인'
					});
					$("#memberNameExist").val('true')
				}else if(result.trim() == 'false') {
					Swal.fire({
						icon: 'error',
						title: '사용할 수 없는 아이디 입니다',
						background: '#3A3A3A',  // 배경색
						color: '#fff',  // 텍스트 색상
						confirmButtonColor: '#1db954',  // 확인 버튼 색상
						confirmButtonText: '확인'
					});
					$("#memberNameExist").val('false')
				}
			}
		})		
	}
	function resetMemberNameExist(){
		$("#memberNameExist").val('false')
	}	
	
	//사용자가 입력한 비밀번호 실시간으로 받아서 검증
	function checkPasswordSecGrade(){
		var password = document.getElementById("passwordField").value;
		var gradeElement = document.querySelector('.checkOutGrade');
		
		if(password.length === 0){
			gradeElement.textContent = "보안 등급 : ";
			return;
		}
		
		var grade = calculatePasswordGrade(password);
		
		switch(grade){
		
		case "weak":
			gradeElement.textContent = "보안 등급 : 낮음";
			break;
		case "medium":
			gradeElement.textContent = "보안 등급 : 중간";
			break;
		case "strong":
			gradeElement.textContent = "보안 등급 : 높음";
			break;
		case "very strong":
			gradeElement.textContent = "보안 등급 : 매우 높음";
			break;
		default:
			gradeElement.textContent = "보안 등급 : "
		}
		
	}
	
    function calculatePasswordGrade(password) {
        var grade = "weak";

        if (password.length < 1) {
            return grade;
        }

        var hasUpperCase = /[A-Z]/.test(password);
        var hasLowerCase = /[a-z]/.test(password);
        var hasNumber = /\d/.test(password);
        var hasSpecialChars = /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(password);

        if (hasUpperCase && hasLowerCase && hasNumber && hasSpecialChars) {
            grade = "very strong";
        } else if ((hasUpperCase && hasLowerCase && hasNumber) || (hasSpecialChars && hasLowerCase && hasNumber)
        		|| (hasUpperCase && hasLowerCase && hasSpecialChars) || (hasLowerCase && hasNumber && hasSpecialChars)) {
            grade = "strong";
        } else if ((hasUpperCase && hasLowerCase) || (hasNumber && hasSpecialChars) || (hasLowerCase && hasNumber)
        		|| (hasUpperCase && hasNumber) || (hasUpperCase && hasSpecialChars) || (hasLowerCase && hasSpecialChars)) {
            grade = "medium";
        }

        return grade;
    }	
    
 // CapsLock 체크 함수
    function checkCapsLock(event, messageId) {
        if (event.getModifierState("CapsLock")) {
            document.getElementById(messageId).innerText = "Caps Lock이 활성화된 상태입니다.";
        } else {
            document.getElementById(messageId).innerText = "";
        }
    }

    window.onload = function() {
        // 비밀번호 입력란의 CapsLock 체크
        document.getElementById("passwordField").onkeyup = function(event) {
            checkCapsLock(event, 'message');
        };
        
        // 비밀번호 확인 입력란의 CapsLock 체크
        document.getElementById("passwordField2").onkeyup = function(event) {
            checkCapsLock(event, 'message2');
        };
    };

    $(function(){
        // 눈표시 클릭 시 패스워드 보이기
        $('.eyes').on('click', function(){
            var passwordField = $('#passwordField');
            if(passwordField.attr('type') === 'password') {
                passwordField.attr('type', 'text');
                $(this).find('.fa').attr('class', 'fa fa-eye-slash fa-lg');
            } else {
                passwordField.attr('type', 'password');
                $(this).find('.fa').attr('class', 'fa fa-eye fa-lg');
            }
        });
        
     // 눈표시 클릭 시 패스워드 보이기 - 비밀번호 확인 입력란
        $('.eyes-confirm').on('click', function(){
            var passwordField2 = $('#passwordField2');
            if(passwordField2.attr('type') === 'password') {
                passwordField2.attr('type', 'text');
                $(this).find('.fa').attr('class', 'fa fa-eye-slash fa-lg');
            } else {
                passwordField2.attr('type', 'password');
                $(this).find('.fa').attr('class', 'fa fa-eye fa-lg');
            }
        });
    });
</script>

<body>

	<c:import url="/WEB-INF/views/include/top_menu.jsp" />

	<div class="header finisher-header"
		style="width: 100%; height: 1100px;">

		<div class="container_join">
			<div class="row">
				<div class="col-sm-3"></div>
				<div class="col-sm-6">
					<div class="card shadow">
						<div class="card-body">
						<h1>회원가입</h1>
							<form:form action="${root }member/join_pro" method="post"
								modelAttribute="joinMemberBean">
								<form:hidden path="memberNameExist" />
								<div class="form-group">
									<form:label path="nickname">닉네임</form:label>
									<form:input path="nickname" class="form-control" />
									<form:errors path="nickname" style='color:red' />
								</div>
								<div class="form-group">
									<form:label path="membername">아이디</form:label>
									<span class="required">*</span> <!-- 빨간색 * 추가 -->
									<div class="input-group-membername">
										<form:input path="membername" class='form-control'
											onkeypress="resetMemberNameExist()" placeholder="영문 대소문자, 숫자 또는 이메일"/>
										<div class="input-group-append">
											<button type="button" class="btn btn-primary"
												onclick="checkMemberNameExist()">중복확인</button>
										</div>
									</div>
									<form:errors path="membername" style='color:red' />
								</div>
								<div class="form-group">
									<form:label path="birthday">생년월일</form:label>
									<form:input path="birthday" class="form-control" type="date" />
									<form:errors path="birthday" style='color:red' />
								</div>
								<div class="form-group">
									<form:label path="email">이메일</form:label>
									<span class="required">*</span> <!-- 빨간색 * 추가 -->
									<form:input path="email" class="form-control" />
									<form:errors path="email" style='color:red' />
								</div>
								<div class="form-group">
									<form:label path="real_name">실명</form:label>
									<form:input path="real_name" class="form-control" />
									<form:errors path="real_name" style='color:red' />
								</div>

								<div class="form-group">
									<form:label path="password" for="passwordField">비밀번호</form:label>
									<span class="required">*</span> <!-- 빨간색 * 추가 -->
									<div class="input-group">
										<form:password path="password" class="form-control"
											id="passwordField" oninput="checkPasswordSecGrade()" placeholder="영문 대소문자, 숫자 또는 특수기호"/>
										<div class="eyes input-group-append">
											<span class="input-group-text"> <i
												class="fa fa-eye fa-lg"></i>
											</span>
										</div>
									</div>
									<div id="message" style="color: red"></div>
									<form:errors path="password" style="color:red" />
								</div>

								<div class="form-group">
									<form:label path="password2" for="passwordField2">비밀번호 확인</form:label>
									<div class="input-group">
										<form:password path="password2" class="form-control"
											id="passwordField2" />
										<div class="eyes-confirm input-group-append">
											<span class="input-group-text"> <i
												class="fa fa-eye fa-lg"></i>
											</span>
										</div>
									</div>
									<div id="message2" style="color: red"></div>
									<form:errors path="password2" style="color:red" />
								</div>

								<div class="checkOutGrade">보안 등급 :</div>

								<div class="form-group">
									<div class="text-right">
										<form:button type="submit" class="btn btn-primary">회원가입</form:button>
									</div>
								</div>
							</form:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 회원가입 성공 시 SweetAlert 표시 -->
	<c:if test="${joinSuccess == true}">
		<script>
    Swal.fire({
        title: '가입이 완료되었습니다',
        text: '로그인 화면으로 이동합니다.',
        icon: 'success',
        background: '#3A3A3A',  // 회색 배경색
        color: '#fff',  // 텍스트 색상 흰색
        confirmButtonColor: '#1db954',  // 확인 버튼 색상 (Spotify 그린)
        confirmButtonText: '확인'
    }).then((result) => {
        if (result.isConfirmed) {
            // 확인 버튼을 누르면 로그인 페이지로 리디렉션
            window.location.href = "${root}member/login";
        }
    });
</script>
	</c:if>

	<c:import url="/WEB-INF/views/include/bottom_info.jsp" />

	<script type="text/javascript">
new FinisherHeader({
  "count": 100,
  "size": {
    "min": 1,
    "max": 8,
    "pulse": 0
  },
  "speed": {
    "x": {
      "min": 0,
      "max": 0.4
    },
    "y": {
      "min": 0,
      "max": 0.6
    }
  },
  "colors": {
    "background": "#191414",
    "particles": [
      "#fbfcca",
      "#d7f3fe",
      "#ffd0a7"
    ]
  },
  "blending": "overlay",
  "opacity": {
    "center": 1,
    "edge": 0
  },
  "skew": 0,
  "shapes": [
    "c"
  ]
});
</script>

</body>
</html>
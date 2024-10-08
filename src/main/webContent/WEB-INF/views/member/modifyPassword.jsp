<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="root" value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function(){
            $('#submitBtn').prop('disabled', true); // 인증코드 입력 전까지 버튼 비활성화

            $('#authCode2').on('input', function() {
                var codeValue = $(this).val().trim();
                $('#submitBtn').prop('disabled', codeValue === ''); // 인증코드 입력되면 활성화
            });

            $('#changeBtn').click(function(){
                var email = $("#email").val();

                $.ajax({
    				url: '${root}member/modifyCertificationCode/' + email,
    				dataType : 'text',
    				success: function(data){
    					console.log("data : "+data);
    					code = data;
    					$("#authCode1").val(code);
    					alert('인증코드가 발송되었습니다.');
    				}
    			});
            });
        });
    </script>
</head>
<body>
    <c:import url="/WEB-INF/views/include/top_menu.jsp" />
    <div class="wrapper">
        <form:form action="${root }member/modifyPassword_pro" method="post" modelAttribute="modifyPasswordBean">
            <form:hidden path="authCode1"/>
            <form:hidden path="email"/>

            <h1>비밀번호 변경</h1>

            <div class="input-box">
                <form:password path="password" placeholder="새 비밀번호"/>
                <form:errors path="password" style='color:red'/>
            </div>
            <div class="input-box">
                <form:password path="password2" placeholder="새 비밀번호 확인"/>
                <form:errors path="password2" style='color:red'/>
            </div>
                
            <div class="input-box">
                <p>회원가입 시 이메일: ${modifyPasswordBean.email}</p>
                <button type="button" id="changeBtn" class="btn" style="color:#303080">인증요청</button>
            </div>
                
            <div class="input-box">
                <form:input path="authCode2" id="authCode2" type="text" placeholder="인증 코드"/>
                <form:errors path="authCode2" style='color:red'/>
            </div>

            <form:button id="submitBtn" type="submit" class="btn" style="width: 80%; margin-left: 30px; height: 40px; color: #333;" disabled="true">확인</form:button>
        </form:form>
    </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인</title>
<link rel="stylesheet" href="${root }css/login.css" />
<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
<link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
function checkCapsLock(event) {
    if (event.getModifierState("CapsLock")) {
        document.getElementById("message").innerText = "Caps Lock이 활성화된 상태입니다."
    } else {
        document.getElementById("message").innerText = ""
    }
}

window.onload = function() {
    document.getElementById("passwordField").onkeyup = function(event) {
        checkCapsLock(event);
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
});
</script>
</head>
<body>
    <!-- 상단 메뉴 부분 -->
    <c:import url="/WEB-INF/views/include/top_menu.jsp" />

    <div class="container_login">
        <div class="row">
            <div class="col-sm-3"></div>
            <div class="col-sm-6">
                <div class="card shadow">
                    <div class="card-body">                        
                        <!-- 로그인 폼 -->
                        <form:form action="${root }member/login_pro" method='post'
                            modelAttribute="tempLoginMemberBean">
                            <div class="form-group">
                                <form:label path="membername">아이디</form:label>
                                <form:input path="membername" class="form-control" />
                                <form:errors path="membername" style="color:red" />
                            </div>
                            <div class="form-group">
                                <form:label path="password">비밀번호</form:label>
                                <div class="input-group">
                                    <form:password path="password" class="form-control" id="passwordField"/>
                                    <div class="eyes input-group-append">
                                        <span class="input-group-text">
                                            <i class="fa fa-eye fa-lg"></i>
                                        </span>
                                    </div>
                                </div>
                                <div id="message" style="color:red"></div>                
                                <form:errors path="password" style="color:red" />
                            </div>

                            <!-- 숨겨진 필드로 redirectURI 추가 -->
                            <input type="hidden" name="redirectURI" value="${param.redirectURI}" />

                            <div class="form-group text-center">
                                <form:button class='btn btn-primary'>로그인</form:button>
                            </div>

                            <!-- 구글 로그인 -->
                            <div class="googleSignUp">
                                <button type="button"
                                    onclick="location.href='${root}member/getGoogleAuthUrl?redirectURI=${param.redirectURI}'">
                                    <img src="${root }img/googlelogo2.png" alt="Google logo" class="google-logo"> 
                                    Google로 계속하기
                                </button>
                            </div>

                            <div class="form-group text-center">
                                <a href="${root }member/join" class="btn-link">회원가입</a> | 
                                <a href="${root }member/modifyPassword" class="btn-link">비밀번호 찾기</a>
                            </div>

                        </form:form>
                    </div>
                </div>
            </div>
            <div class="col-sm-3"></div>
        </div>
    </div>
    
    <!-- 로그인 실패 시 SweetAlert로 알림창 띄우기 -->
    <c:if test="${fail == true}">
        <script>
            Swal.fire({
                icon: 'error',
                title: '로그인 실패',
                text: '아이디 또는 비밀번호를 확인해주세요.',
                background: '#3A3A3A',
                color: '#fff',
                confirmButtonColor: '#1db954',
                confirmButtonText: '확인'
            });
        </script>
    </c:if>

    <c:import url="/WEB-INF/views/include/bottom_info.jsp" />
</body>
</html>

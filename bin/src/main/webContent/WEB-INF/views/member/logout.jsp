<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath }/"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그아웃</title>

    <!-- SweetAlert 다크 테마 및 스크립트 추가 -->
    <link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
</head>
<body>

    <script>
        // DOMContentLoaded 이벤트로 DOM이 모두 로드된 후에 실행
        document.addEventListener('DOMContentLoaded', function () {
            Swal.fire({
                title: '로그아웃 되었습니다',
                text: '메인 페이지로 이동합니다.',
                icon: 'info',
                background: '#3A3A3A',  // 사용자 요청에 따른 회색 배경색
                color: '#fff',  // 텍스트 색상 흰색
                confirmButtonColor: '#1db954',  // 확인 버튼 색상 (Spotify 그린)
                confirmButtonText: '확인'
            }).then((result) => {
                if (result.isConfirmed) {
                    // 확인 버튼을 누르면 메인 페이지로 리디렉션
                    window.location.href = '${root}main';
                }
            });
        });
    </script>

</body>
</html>

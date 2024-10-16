<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var='root' value='${pageContext.request.contextPath}/'/>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.3/dist/sweetalert2.min.css">
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function() {
    Swal.fire({
    	icon: 'success',
        title: '수정이 완료되었습니다.',
        background: '#3A3A3A',  // 배경색
        color: '#fff',  // 텍스트 색상
        confirmButtonColor: '#1db954',  // 확인 버튼 색상
        confirmButtonText: '확인'
    }).then(() => {
        location.href = '${root}board/read?boardpost_id=${modifyContentBean.boardpost_id}';
    });
});
</script>
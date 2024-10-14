<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value='${pageContext.request.contextPath }/' />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet"> 
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
<link href="//bootswatch.com/3/darkly/bootstrap.css" rel="stylesheet">
<link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
<!-- CSS 및 Bootstrap 아이콘 추가 -->
<link href="${root}/css/main.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/board.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.3/dist/sweetalert2.min.css">
	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
	
<title>Insert title here</title>
<style>
.post-form {
    width: 100%;
    height: auto;
    overflow-y: auto;
    resize: none;
}
.note-editable img {
        max-width: 634px;
        height: auto;
}

</style>
</head>
<body>
	<header>
		<!-- top_menu.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />
		<!-- Sidebar 포함 -->
		<div class="sidebar">
		<jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
		</div>
	</header>
	<div class="board-container">
		<div style="margin-left: 30px">
		<form:form action="${root}board/write_pro" method="post" modelAttribute="writeContentBean" enctype="multipart/form-data">
			<div style="margin-bottom: 15px;">
				<span class="custom-select"> 
				<form:select path="board_id" id="drop-down">
					<form:option value="-1" disabled="disabled" selected="selected" style="color: #A9A9A9;">카테고리</form:option>
					<form:option value="1" style="color : #1ee99a;">자유게시판</form:option>
					<form:option value="2" style="color : #c783d7;">소식/정보</form:option>
					<form:option value="3" style="color : #f1cb49;">음악 추천</form:option>
				</form:select>
				</span>
				<span>
				<form:input path="content_title" style="width: 400px; font-size: 16px; padding: 3px; margin-right: 20px;" 
				placeholder="제목" />
				<form:errors path="content_title" style='color:red'/>
				</span>
			</div>
			
			<div class="post-form">
        		<textarea name="content_text" id="summernote"></textarea>
    		</div>
			
			<span style="float: right;">
				<form:button type="reset" class="write-btn"><a href="${root }board/main" style="text-decoration: none; color: black;">취소</a></form:button>
				<form:button class="write-btn" id="saveButton">작성</form:button>
			</span>
		</form:form>
		</div>
	</div>
	<footer>
	<!-- bottom_info.jsp 포함 -->
    <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
	<script>
        $(document).ready(function() {
            $('#summernote').summernote({
                height: 685,
                lang: 'ko-KR',
                placeholder: '내용을 입력해주세요.',
                toolbar: [
                    ['fontsize', ['fontsize']],
                    ['font', ['bold', 'italic', 'underline', 'clear']],
                    ['color', ['color']],
                    ['table', ['table']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['height', ['height']],
                    ['insert', ['link', 'picture', 'video']],
                    ['view', ['fullscreen', 'codeview', 'help']]
                ],
                fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', '맑은 고딕', '궁서', '굴림체', '굴림', '돋움체', '바탕체'],
                fontSizes: ['8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72', '96'],
                focus: true,
                
            });
            
            $('#saveButton').on('click', function(event) {
                event.preventDefault(); // 기본 폼 제출 방지

             	// 중복 제출 방지
                $(this).prop('disabled', true);
                
                // Summernote 에디터의 HTML 코드 가져오기
                var htmlContent = $('#summernote').summernote('code');

                // textarea에 HTML 내용 설정
                $('textarea[name="content_text"]').val(htmlContent);
             	
                // board_id 선택 여부 체크
                var boardId = $('#drop-down').val();
                if (boardId === "-1") {
                    // 에러 메시지 표시
                    swal("카테고리를 선택해 주세요.","", {icon: "warning"});
					$(this).prop('disabled', false); // 버튼 활성화
                    return; // 폼 제출 중단
                }
                
                // 폼 제출
                $(this).closest('form').submit();
            });
        });
		
        document.addEventListener('DOMContentLoaded', function() {
            var dropdown = document.getElementById('drop-down');

            function updateSelectColor() {
                var selectedOption = dropdown.options[dropdown.selectedIndex];
                dropdown.style.color = selectedOption.style.color || '#000'; // 기본 색상 설정
            }

            dropdown.addEventListener('change', updateSelectColor);
            updateSelectColor(); // 초기 색상 설정
        });
    </script>
</body>
</html>

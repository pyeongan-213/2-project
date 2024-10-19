<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value='${pageContext.request.contextPath }/' />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
    <!-- CSS 및 Bootstrap 아이콘 추가 -->
    <link href="${root}/css/main.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/board.css">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.10/dist/sweetalert2.min.js"></script>
<title>Insert title here</title>

</head>
<body>
	<header>
		<!-- top_menu.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />
		<%-- <!-- Sidebar 포함 -->
		<jsp:include page="/WEB-INF/views/include/sidebar.jsp" /> --%>
	</header>
	<div class="board-container">
		<h1>커뮤니티</h1>
		<div>
			<button class="c_btn" ><a href="${root }board/main">전체</a></button>
			<button class="c_btn" ><a href="${root }board/main_sort?board_id=1">자유게시판</a></button>
			<button class="c_btn" ><a href="${root }board/main_sort?board_id=2">소식/정보</a></button>
			<button class="c_btn" ><a href="${root }board/main_sort?board_id=3">음악 추천</a></button>
		</div>
		<div class="content-box">
			<div class="content_title">
			<span style="margin-right: 10px;">
			<c:choose>
				<c:when test="${readContentBean.board_id == 1}">
					<span style="color: #1ee99a">자유게시판</span>
				</c:when>
				<c:when test="${readContentBean.board_id == 2}">
					<span style="color: #c783d7">소식/정보</span>
				</c:when>
				<c:when test="${readContentBean.board_id == 3}">
					<span style="color: #f1cb49">음악 추천</span>
				</c:when>
			</c:choose>
			</span>
			<span>${readContentBean.content_title }</span>
			<span style="float: right; margin: 5px 40px; font-size: 13px; color: #b0b0b0;">${readContentBean.membername } / ${readContentBean.writedate }</span>
			</div>

			<div class="content_text">
			<span style="display: inline-block; width: 100%; padding: 10px 10px 35px 10px;">
			${readContentBean.content_text }
			</span>
			

			<div style="display: flex;">
				<span class="like" id="likeButton_${readContentBean.boardpost_id}"
					style="cursor: pointer;">♡&nbsp;</span> 
				<span class="like" id="likeCount">${readContentBean.like_count}</span>

				<c:if
					test="${loginMemberBean.member_id == readContentBean.member_id}">
						<span class="content-opt" style="margin-left: auto;">
							<div onclick="toggleDropdown(event)" style="cursor: pointer;">
								<img src="${root}/img/three-dots.png" alt="옵션 드롭다운"
									style="width: 17px; height: 17px; vertical-align: middle; filter: brightness(0) invert(1);"/>
							</div>
							<div class="dropdown-menu" style="display: none;">
								<a
									href="${root }board/modify?boardpost_id=${readContentBean.boardpost_id}"
									class="dropdown-item"> 
									<img src="${root}/img/modify-icon.png" alt="수정"
									class="dropdown-icon" /> <span style="font-size: 14px;">수정</span>
								</a> 
								<a href="${root }board/delete?boardpost_id=${readContentBean.boardpost_id}" 
									class="dropdown-item" onclick="confirmDelete(event, this.href); return false;">
									<img src="${root}/img/delete_icon.png" alt="삭제" 
									class="dropdown-icon" /> <span style="font-size: 14px;">삭제</span>
								</a>
							</div>
						</span>

					</c:if>
			</div>
			</div>
			<div class="comment-container">
				<c:forEach var='obj' items="${replyList }">
				<div class="comment">
					<c:if test="${loginMemberBean.member_id == obj.member_id}">
						<span class="reply-delete"> 
							<a href="${root }board/delete_rep?boardpost_id=${readContentBean.boardpost_id}&reply_id=${obj.reply_id}" onclick="confirmDelete(event, this.href); return false;">
								<img src="${root}/img/reply_delete_icon.png" alt="삭제" style="filter: brightness(0) invert(1);"/>
							</a>
							<span class="tooltip" style="display: none;">삭제하기</span>
						</span>
					</c:if>

					<div style="margin-bottom: 5px; font-size: 14px;">
						<span>${obj.reply_writer_name }</span> <span style="color: gray;">${obj.reply_date }</span>
					</div>

					<div style="padding: 0 10px;">
						${obj.reply_text }	
					</div>					
				</div>
				</c:forEach>
			</div>

			<div style="margin: 25px 0;">
			<form:form action="${root}board/write_reply_pro" method="post"
				modelAttribute="writeReplyBean">
				<input type="hidden" name="boardpost_id"
					value="${readContentBean.boardpost_id}" />
				<div style="position: relative; width: 100%; height: 100px;"
					onclick="document.querySelector('input[name=\'reply_text\']').focus();">
					
					<textarea name="reply_text" class="write-reply" placeholder="댓글을 입력하세요."></textarea>
					<form:errors path="reply_text" style="color:red"/>
					<button class="write-btn"
						style="position: absolute; right: 10px; bottom: 7px;">작성</button>
				</div>
			</form:form>
			</div>

			<button class="write-btn">
				<a href="${root }board/main" style="color: black;">목록</a>
			</button>
		</div>
	</div>
	<footer>
	<!-- bottom_info.jsp 포함 -->
    <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>
	<script>
	document.addEventListener('DOMContentLoaded', function() {
	    const urlParams = new URLSearchParams(window.location.search);
	    const boardId = urlParams.get('board_id');
	    
	    const allButton = document.querySelector('button a[href*="board/main"]');
	    const freeBoardButton = document.querySelector('button a[href*="board/main_sort?board_id=1"]');
	    const newsBoardButton = document.querySelector('button a[href*="board/main_sort?board_id=2"]');
	    const musicBoardButton = document.querySelector('button a[href*="board/main_sort?board_id=3"]');

	    // Remove active class from all buttons
	    [allButton, freeBoardButton, newsBoardButton, musicBoardButton].forEach(button => {
	        if (button) {
	            button.closest('button').classList.remove('active');
	        }
	    });

	    // Add active class based on board_id
	    switch (boardId) {
	        case '1':
	            freeBoardButton.closest('button').classList.add('active');
	            break;
	        case '2':
	            newsBoardButton.closest('button').classList.add('active');
	            break;
	        case '3':
	            musicBoardButton.closest('button').classList.add('active');
	            break;
	        default:
	            allButton.closest('button').classList.add('active');
	            break;
	    }
	});


	document.addEventListener('DOMContentLoaded', function() {
	    const member_id = "${loginMemberBean.member_id}";
	    const boardpost_id = "${readContentBean.boardpost_id}";
	    const likeButton = document.getElementById(`likeButton_${boardpost_id}`);
	    const likeKey = `liked_${loginMemberBean.member_id}_${boardpost_id}`;
	    
	    console.log(member_id);
	    console.log(boardpost_id);
	    console.log(`likeButton_${boardpost_id}`);
	    console.log(`liked_${loginMemberBean.member_id}_${boardpost_id}`);

	    if (likeButton) {
	        console.log('Like button found!');
	        if (localStorage.getItem(likeKey)) {
	            likeButton.innerText = '♥';
	        }

	        likeButton.addEventListener('click', function() {
	            console.log('Like button clicked');
	            const isLiked = localStorage.getItem(likeKey);
	            const url = isLiked ? 
	                `${root}board/remove_like` : 
	                `${root}board/add_like`;
	            const data = { boardpost_id: boardpost_id, member_id: member_id };

	            fetch(url, {
	                method: 'POST',
	                headers: { 'Content-Type': 'application/json' },
	                body: JSON.stringify(data)
	            })
	            .then(response => response.json())
	            .then(result => {
	                if (result.success) {
	                    if (isLiked) {
	                        likeButton.innerText = '♡';
	                        localStorage.removeItem(likeKey);
	                    } else {
	                        likeButton.innerText = '♥';
	                        localStorage.setItem(likeKey, 'true');
	                    }
	                    document.getElementById('likeCount').innerText = result.newLikeCount;
	                } else {
	                    console.error('Error updating like count:', result.message);
	                }
	            })
	            .catch(error => console.error('Error:', error));
	        });
	    } else {
	        console.error(`Button with ID likeButton_${boardpost_id} not found`);
	    }
	});

	function toggleDropdown(event) {
	    event.stopPropagation(); // 클릭 이벤트 전파 방지
	    const dropdownMenu = document.querySelector('.dropdown-menu');
	    dropdownMenu.style.display = dropdownMenu.style.display === 'none' || dropdownMenu.style.display === '' ? 'block' : 'none';
	}

	// 문서 전체에 클릭 이벤트를 추가하여 드롭다운 외부를 클릭하면 닫기
	document.addEventListener('click', function() {
	    const dropdownMenu = document.querySelector('.dropdown-menu');
	    dropdownMenu.style.display = 'none';
	});

	const aElements = document.querySelectorAll('.reply-delete'); // 모든 reply-delete 요소 선택
	const tooltips = document.querySelectorAll('.tooltip'); // 모든 tooltip 요소 선택

	aElements.forEach((aElement, index) => {
	    aElement.addEventListener('mouseenter', () => {
	        tooltips[index].style.display = 'block'; // 해당 툴팁 표시
	        tooltips[index].style.background = '#303030';
	    });

	    aElement.addEventListener('mouseleave', () => {
	        tooltips[index].style.display = 'none'; // 해당 툴팁 숨김
	    });
	});


	function confirmDelete(event, url) {
	    Swal.fire({
	        title: '정말로 삭제하시겠습니까?',
	        text: "삭제 후 복구할 수 없습니다!",
	        icon: 'warning',
	        showCancelButton: true,
	        background: '#3A3A3A',  // 배경색
    		color: '#fff',  // 텍스트 색상
	        confirmButtonColor: '#d33',
	        cancelButtonColor: '#3085d6',
	        confirmButtonText: '삭제',
	        cancelButtonText: '취소'
	    }).then((result) => {
	        if (result.isConfirmed) {
	            // 확인 버튼을 클릭한 경우 삭제 요청으로 리다이렉트
	            window.location.href = url;
	        }
	    });
	}
</script>

</body>
</html>
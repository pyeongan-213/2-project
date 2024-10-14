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
<title>Insert title here</title>

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
			<span style="float: right; margin: 5px 40px; font-size: 13px; color: gray;">${readContentBean.membername } / ${readContentBean.writedate }</span>
			</div>

			<div class="content_text">
			<span>
			${readContentBean.content_text }
			</span>
			</div>

			<div style="display: flex; margin: 20px 0 20px">
				<span id="likeButton_${readContentBean.boardpost_id}"
					style="cursor: pointer;">♡</span> 
				<span id="likeCount">${readContentBean.like_count}</span>

				<c:if
					test="${loginMemberBean.member_id == readContentBean.member_id}">
					<span style="margin-left: auto;"> <a
						href="${root }board/modify?boardpost_id=${readContentBean.boardpost_id}"
						class="write-btn">수정</a> | <a
						href="${root }board/delete?boardpost_id=${readContentBean.boardpost_id}"
						class="write-btn">삭제</a>
					</span>
				</c:if>
			</div>

			<div class="comment-container">
				<c:forEach var='obj' items="${replyList }">
				<div class="comment">
					<div style="font-size: 14px;">
						<span>${obj.reply_writer_name }</span> <span style="color: gray;">${obj.reply_date }</span>
					</div>

					<div>
						${obj.reply_text }
						<c:if test="${loginMemberBean.member_id == obj.member_id}">
							<span style="float: right;"> 
								<a href="${root }board/delete_rep?boardpost_id=${readContentBean.boardpost_id}&reply_id=${obj.reply_id}">🗑️</a>
							</span>
						</c:if>
					</div>
				</div>
				</c:forEach>
			</div>

			<div style=" padding: 10px;">
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

</script>

</body>
</html>
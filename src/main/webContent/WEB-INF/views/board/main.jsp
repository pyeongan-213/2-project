<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value='${pageContext.request.contextPath }/'/>
<c:set var="URI_1" value="${requestScope['javax.servlet.forward.request_uri']}"/>
<c:set var="URI_2" value="${root}board/main"/>
<c:set var="URI_3" value="${root}board/main_sort"/>
<c:set var="URI_4" value="${root}board/search"/>
<c:set var="URI_5" value="${root}board/search"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/board.css">
<style>
</style>
<title>Board</title>
<script>
	/* 문의버튼js */
	function toggleContent() {
		var content = document.querySelector('.fixed-content');
		if (content.style.display === 'block') {
			content.style.display = 'none';
		} else {
			content.style.display = 'block';
		}
	}
</script>
</head>
<body onload="setCurrentBoardIdFromURL();">
	<header>
		<!-- ============ -->
	</header>
	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>
	<div class="container">
	
		<h1>커뮤니티</h1>
		<div>
			<button class="c_btn" onclick="setBoardId(0); setTimeout(() => { location.href='${root}board/main'; }, 100);">전체</button>
			<button class="c_btn" onclick="setBoardId(1); setTimeout(() => { location.href='${root}board/main_sort?board_id=1'; }, 100);">자유게시판</button>
    		<button class="c_btn" onclick="setBoardId(2); setTimeout(() => { location.href='${root}board/main_sort?board_id=2'; }, 100);">소식/정보</button>
			<button class="c_btn" onclick="setBoardId(3); setTimeout(() => { location.href='${root}board/main_sort?board_id=3'; }, 100);">음악 추천</button>
			<p></p>
		</div>
		
		<div class="on-table">
		<form id="searchForm" action="${root}board/search" method="get" onsubmit="return searchPosts()">
			<input id="searchInput" name="query" placeholder="search">
  			<input type="hidden" id="boardId" name="board_id" value="0">
		</form>
    		<span class="right-align"> <a href="${root }board/write" class="write-btn">글쓰기</a> </span>
		</div>
		
			<div>
				<table>
					<thead>
						<tr>
							<th>카테고리</th>
							<th>제목</th>
							<th>글쓴이</th>
							<th>작성일</th>
							<th>좋아요</th>
						</tr>
					</thead>
					<tbody align="center">
						<c:forEach var='obj' items="${contentList}">
							<tr>
								<td>${obj.board_name }</td>
								<td><a href="${root }board/read?boardpost_id=${obj.boardpost_id}">${obj.content_title}</a></td>
								<td>${obj.membername }</td>
								<td>${obj.writedate }</td>
								<td>${obj.like_count }</td>
								<td style="display: none;">${obj.content_text }</td>							
							</tr>						
						</c:forEach>
					</tbody>
				</table>

			</div>
				
			<div class="pagination">
				<ul>
				
				<c:if test="${URI_1 eq URI_2}">
					<c:choose>
						<c:when test="${pageBean.prevPage <= 0 }">
							<li class="page-item-disabled"><a href="#" 
								class="page-link">←</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item">
							<a href="${root }board/main?page=${pageBean.prevPage}"
								class="page-link">←</a></li>
						</c:otherwise>
					</c:choose>

					<c:forEach var='idx' begin="${pageBean.min }" end='${pageBean.max }'>
						<c:choose>
							<c:when test="${idx == pageBean.currentPage }">
								<li class="page-item-active"><a
									href="${root }board/main?page=${idx}"
									class="page-link">${idx }</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a
									href="${root }board/main?page=${idx}"
									class="page-link">${idx }</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:choose>
						<c:when test="${pageBean.max >= pageBean.pageCnt }">
							<li class="page-item-disabled"><a href="#" 
								class="page-link">→</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a
								href="${root }board/main?page=${pageBean.nextPage}"
								class="page-link">→</a></li>
						</c:otherwise>
					</c:choose>
				</c:if>
				
				<c:if test="${URI_1 eq URI_3}"> <!-- 카테고리별로 볼때 -->
					<c:choose>
					
						<c:when test="${pageBean.prevPage <= 0 }">
							<li class="page-item-disabled"><a href="#" 
								class="page-link">←</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item">
							<a href="${root }board/main_sort?board_id=${board_id }&page=${pageBean.prevPage}"
								class="page-link">←</a></li>
						</c:otherwise>
					</c:choose>
    			
					<c:forEach var='idx' begin="${pageBean.min }" end='${pageBean.max }'>
						<c:choose>
							<c:when test="${idx == pageBean.currentPage }">
								<li class="page-item-active"><a
									href="${root }board/main_sort?board_id=${board_id }&page=${idx}"
									class="page-link">${idx }</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a
									href="${root }board/main_sort?board_id=${board_id }&page=${idx}"
									class="page-link">${idx }</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:choose>
						<c:when test="${pageBean.max >= pageBean.pageCnt }">
							<li class="page-item-disabled"><a href="#" 
								class="page-link">→</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a
								href="${root }board/main_sort?board_id=${board_id }&page=${pageBean.nextPage}"
								class="page-link">→</a></li>
						</c:otherwise>
					</c:choose>
				</c:if>
				
				<c:if test="${URI_1 eq URI_4}"> <!-- 검색했을때 -->
					<c:choose>
						<c:when test="${pageBean.prevPage <= 0 }">
							<li class="page-item-disabled"><a href="#" 
								class="page-link">←</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item">
							<a href="${root }board/search?query=${param.query }&board_id=${board_id}&page=${pageBean.prevPage}"
								class="page-link">←</a></li>
						</c:otherwise>
					</c:choose>
    			
					<c:forEach var='idx' begin="${pageBean.min }" end='${pageBean.max }'>
						<c:choose>
							<c:when test="${idx == pageBean.currentPage }">
								<li class="page-item-active"><a
									href="${root }board/search?query=${param.query }&board_id=${board_id}&page=${idx}"
									class="page-link">${idx }</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a
									href="${root }board/search?query=${param.query }&board_id=${board_id}&page=${idx}"
									class="page-link">${idx }</a></li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:choose>
						<c:when test="${pageBean.max >= pageBean.pageCnt }">
							<li class="page-item-disabled"><a href="#" 
								class="page-link">→</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a
								href="${root }board/search?query=${param.query }&board_id=${board_id}&page=${pageBean.nextPage}"
								class="page-link">→</a></li>
						</c:otherwise>
					</c:choose>
				</c:if>
				</ul>
			</div>

	</div>
	<div class="showBest">
	<%
    String userId = (session.getAttribute("loginMemberBean") != null)
        ? String.valueOf(((kr.co.duck.beans.MemberBean) session.getAttribute("loginMemberBean")).getMember_id())
        : "null";
    System.out.println("로그인된 사용자 ID: " + userId); // 로그로 출력
    userId = userId.trim();
	%>
		<h3 style="margin-right: 120px;">BEST</h3>
		<div class="bestContent">
		<c:forEach var='obj' items="${bestList}">
			<div>
			<span style="margin: 0 20px 0 0;">${obj.content_title}</span>
			<span style="margin: 0;">♡${obj.like_count }</span>
			</div>
		</c:forEach>
		</div>
	</div>
	<div class="fixed-section">
        <button class="fixed-button" onclick="toggleContent()">
        +
        </button>
        <div class="fixed-content">
        <form:form action=""></form:form>
            <h3>문의하기</h3>
            <p>이름</p>
            <textarea rows="1" cols="1">${loginMemberBean.real_name }</textarea>
            <p>메일주소</p>
            <textarea rows="1" cols="1">${loginMemberBean.email }</textarea>
            <p>문의내용</p>
			<textarea style="width: 100%; height: 250px; padding: 0; line-height: normal; margin-bottom: 7px;"></textarea>
            <div style="background:white; display: grid; justify-content: end;"><a href="${root }board/sendMail_pro">메일 보내기</a></div>
        </div>
    </div>
	<footer>
		<!-- ========== -->
	</footer>

<script>
let currentBoardId = 0; // 전역 변수로 선언

function setBoardId(boardId) {
    currentBoardId = boardId;
    document.getElementById('boardId').value = boardId;
    console.log("Current Board ID set to:", boardId); // 로그 추가
}

function setCurrentBoardIdFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    const boardId = urlParams.get('board_id');
    if (boardId) {
        currentBoardId = boardId;
        document.getElementById('boardId').value = boardId; // hidden input에 설정
        console.log("Current Board ID set from URL:", boardId); // 로그 추가
    }
}	

function searchPosts() {
    const input = document.getElementById('searchInput');
    const query = input.value.trim();

    console.log("Searching with query:", query, "and board ID:", currentBoardId); // 로그 추가

    if (query) {
        return true; 
    } else {
        alert("검색어를 입력해 주세요."); // 오류 메시지 추가
        return false;
    }
}

</script>

</body>
</html>
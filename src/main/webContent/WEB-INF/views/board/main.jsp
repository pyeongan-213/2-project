<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value='${pageContext.request.contextPath }/' />
<c:set var="URI_1"
	value="${requestScope['javax.servlet.forward.request_uri']}" />
<c:set var="URI_2" value="${root}board/main" />
<c:set var="URI_3" value="${root}board/main_sort" />
<c:set var="URI_4" value="${root}board/search" />
<c:set var="URI_5" value="${root}board/search" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/board.css">
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
		<!-- top_menu.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

		<!-- Sidebar 포함 -->
		<div class="sidebar">
			<jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
		</div>
	</header>

	<div id="contentContainer">
		<div class="container">

			<h1>커뮤니티</h1>
			<div>
				<button class="c_btn"
					onclick="setBoardId(0); setTimeout(() => { location.href='${root}board/main'; }, 100);">전체</button>
				<button class="c_btn"
					onclick="setBoardId(1); setTimeout(() => { location.href='${root}board/main_sort?board_id=1'; }, 100);">자유게시판</button>
				<button class="c_btn"
					onclick="setBoardId(2); setTimeout(() => { location.href='${root}board/main_sort?board_id=2'; }, 100);">소식/정보</button>
				<button class="c_btn"
					onclick="setBoardId(3); setTimeout(() => { location.href='${root}board/main_sort?board_id=3'; }, 100);">음악
					추천</button>
			</div>

			<div class="bestContent" style="margin-bottom: 20px;">
				<h3>✨인기글</h3>
				<hr />
				<table>
					<thead hidden="hidden">
						<tr>
							<th>카테고리</th>
							<th>제목</th>
							<th>작성일</th>
							<th>좋아요</th>
						</tr>
					</thead>
					<c:forEach var='obj' items="${bestList}">
						<tbody align="center">
							<tr class="best-list-row">
								<td style="width: 20%; text-align: center;"><c:choose>
										<c:when test="${obj.board_id == 1}">
											<span style="color: #1ee99a">자유게시판</span>
										</c:when>
										<c:when test="${obj.board_id == 2}">
											<span style="color: #c783d7">소식/정보</span>
										</c:when>
										<c:when test="${obj.board_id == 3}">
											<span style="color: #f1cb49">음악 추천</span>
										</c:when>
									</c:choose></td>
								<td style="width: 50%; text-align: left;"><a
									href="${root }board/read?boardpost_id=${obj.boardpost_id}"
									style="color: #fea443;"> 🔥${obj.content_title}</a></td>
								<td style="width: 20%; color: gray; font-size: 14px;">${obj.writedate}</td>
								<td
									style="width: 10%; text-align: left; color: gray; font-size: 14px;">♡${obj.like_count}</td>
							</tr>
						</tbody>
					</c:forEach>
				</table>
				<hr />
			</div>
			<div>
				<h3>🗨️블라블라</h3>
				<div class="on-table">
					<form id="searchForm" action="${root}board/search" method="get"
						onsubmit="return searchPosts()">
						<input id="searchInput" name="query" placeholder="search">
						<input type="hidden" id="boardId" name="board_id" value="0">
					</form>
					<span style="margin-left: auto;"> <a
						href="${root }board/write" class="write-btn">글쓰기</a>
					</span>
				</div>


				<table style="font-size: 17px;">
					<thead style="height: 60px">
						<tr style="border-bottom: 1px solid #494949">
							<th>카테고리</th>
							<th>제목</th>
							<th>글쓴이</th>
							<th>작성일</th>
							<th>좋아요</th>
						</tr>
					</thead>
					<tbody align="center">
						<c:forEach var='obj' items="${contentList}">
							<tr style="line-height: 2.0; border-bottom: 1px solid #494949">
								<td style="width: 20%; text-align: center;"><c:choose>
										<c:when test="${obj.board_id == 1}">
											<span style="color: #1ee99a">자유게시판</span>
										</c:when>
										<c:when test="${obj.board_id == 2}">
											<span style="color: #c783d7">소식/정보</span>
										</c:when>
										<c:when test="${obj.board_id == 3}">
											<span style="color: #f1cb49">음악 추천</span>
										</c:when>
									</c:choose></td>
								<td style="width: 35%; text-align: left;"><a
									href="${root}board/read?boardpost_id=${obj.boardpost_id}">
										${obj.content_title} </a></td>
								<td style="width: 15%; color: gray; font-size: 14px;">${obj.membername}</td>
								<td style="width: 15%; color: gray; font-size: 14px;">${obj.writedate}</td>
								<td style="width: 15%; color: gray; font-size: 14px;">♡${obj.like_count}</td>
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
								<li class="page-item"><a
									href="${root }board/main?page=${pageBean.prevPage}"
									class="page-link">←</a></li>
							</c:otherwise>
						</c:choose>

						<c:forEach var='idx' begin="${pageBean.min }"
							end='${pageBean.max }'>
							<c:choose>
								<c:when test="${idx == pageBean.currentPage }">
									<li class="page-item-active"><a
										href="${root }board/main?page=${idx}" class="page-link">${idx }</a></li>
								</c:when>
								<c:otherwise>
									<li class="page-item"><a
										href="${root }board/main?page=${idx}" class="page-link">${idx }</a></li>
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

					<c:if test="${URI_1 eq URI_3}">
						<!-- 카테고리별로 볼때 -->
						<c:choose>

							<c:when test="${pageBean.prevPage <= 0 }">
								<li class="page-item-disabled"><a href="#"
									class="page-link">←</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a
									href="${root }board/main_sort?board_id=${board_id }&page=${pageBean.prevPage}"
									class="page-link">←</a></li>
							</c:otherwise>
						</c:choose>

						<c:forEach var='idx' begin="${pageBean.min }"
							end='${pageBean.max }'>
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

					<c:if test="${URI_1 eq URI_4}">
						<!-- 검색했을때 -->
						<c:choose>
							<c:when test="${pageBean.prevPage <= 0 }">
								<li class="page-item-disabled"><a href="#"
									class="page-link">←</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a
									href="${root }board/search?query=${param.query }&board_id=${board_id}&page=${pageBean.prevPage}"
									class="page-link">←</a></li>
							</c:otherwise>
						</c:choose>

						<c:forEach var='idx' begin="${pageBean.min }"
							end='${pageBean.max }'>
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

		<div class="fixed-section">
			<button class="fixed-button" onclick="toggleContent()">+</button>
			<div class="fixed-content">
				<form:form
					action="${root }board/receiveEmail/${loginMemberBean.email}"
					method="post">
					<h3>문의하기</h3>

					<p>
						<label for="name">이름</label>
					</p>
					<textarea id="name" name="name" rows="1" readonly>${loginMemberBean.real_name}</textarea>

					<p>
						<label for="email">메일 주소</label>
					</p>
					<textarea id="email" name="email" rows="1" readonly>${loginMemberBean.email}</textarea>

					<p>
						<label for="subject">제목</label>
					</p>
					<textarea id="subject" name="subject" rows="1" required></textarea>

					<p>
						<label for="body">문의 내용</label>
					</p>
					<textarea id="body" name="body"
						style="width: 100%; height: 250px; padding: 5px; line-height: normal; margin-bottom: 7px;"
						required></textarea>

					<button class="send-mail" type="submit">메일보내기 ✉️</button>
				</form:form>
			</div>
		</div>
	</div>
	<!-- contentContainer -->
	<footer>
		<!-- bottom_info.jsp 포함 -->
		<jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
	</footer>

	<script>
	let currentBoardId = 0; // 전역 변수로 선언

	// 사용자가 게시판을 클릭할 때만 boardId 설정
	function setBoardId(boardId) {
	    currentBoardId = boardId;
	    document.getElementById('boardId').value = boardId; // 숨겨진 필드에 저장
	    console.log("Current Board ID set to:", boardId); // 로그 확인
	}

	// URL에서 board_id를 가져와 설정하는 함수
	function setCurrentBoardIdFromURL() {
	    const urlParams = new URLSearchParams(window.location.search);
	    const boardId = urlParams.get('board_id');
	    if (boardId) {
	        currentBoardId = boardId; // URL에서 가져온 값을 전역 변수에 저장
	        document.getElementById('boardId').value = boardId; // 숨겨진 필드에 설정
	        console.log("Current Board ID set from URL:", boardId); // URL에서 가져온 board_id 확인
	    } else {
	        // URL에 board_id가 없으면 기본값 0 설정
	        currentBoardId = 0;
	        document.getElementById('boardId').value = 0;
	    }
	}

	// 검색어 입력 시 호출되는 함수
	function searchPosts() {
	    const input = document.getElementById('searchInput');
	    const query = input.value.trim();

	    console.log("Searching with query:", query, "and board ID:", currentBoardId);

	    if (query) {
	        return true; 
	    } else {
	        alert("검색어를 입력해 주세요.");
	        return false;
	    }
	}

	// 페이지 로드 시 URL에서 board_id 추출 및 버튼 활성화
	window.onload = function() {
	    setCurrentBoardIdFromURL(); // 페이지 로드 시 URL에서 board_id 설정

	    const boardId = currentBoardId; // setCurrentBoardIdFromURL에서 설정된 값 사용
	    const buttons = document.querySelectorAll('.c_btn');

	    // 모든 버튼의 'active' 클래스를 제거
	    buttons.forEach(button => button.classList.remove('active'));

	    // URL에서 추출한 board_id에 해당하는 버튼에 'active' 클래스 추가
	    if (boardId !== null && buttons[boardId]) {
	        buttons[boardId].classList.add('active');
	    } else {
	        buttons[0].classList.add('active'); // 기본값은 첫 번째 버튼
	    }
	};

</script>

</body>
</html>
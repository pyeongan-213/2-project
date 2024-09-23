<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value='${pageContext.request.contextPath }/' />
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
<body>
	<header>
		<!-- ============ -->
	</header>
	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>
	<div class="container">
	
		<h1>커뮤니티</h1>
		<div>
			<button class="c_btn">전체</button>
			<button class="c_btn">자유게시판</button>
			<button class="c_btn">소식/정보</button>
			<button class="c_btn">음악 추천</button>
			<p></p>
		</div>
		<div class="on-table">
			<input placeholder="search">
			<%-- <form:input path="title"/> --%>
			<span class="right-align"> <a
				href="${root }board/write"
				class="write-btn">글쓰기</a>
			</span>
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
						<tr>
							<td>자유게시판</td>
							<td><a href="${root }board/read">제목1~~~~~~~~~~~~~~~~~</a></td>
							<td>글쓴이1</td>
							<td>2024-09-13</td>
							<td>5</td>
						</tr>
						<tr>
							<td>소식/정보</td>
							<td>제목2</td>
							<td>글쓴이2</td>
							<td>2024-09-13</td>
							<td>9</td>
						</tr>
						<tr>
							<td>음악추천</td>
							<td>제목3</td>
							<td>글쓴이3</td>
							<td>2024-09-13</td>
							<td>8</td>
						</tr>
						<%-- <c:forEach var='obj' items="${contentList }">
		<tr>
			<td class="text-center d-none d-md-table-cell">${obj.content_idx }</td>
			<td><a href='${root }board/read?board_info_idx=${board_info_idx}&content_idx=${obj.content_idx}&page=${page}'>${obj.content_subject }</a></td>
			<td class="text-center d-none d-md-table-cell">${obj.content_writer_name }</td>
			<td class="text-center d-none d-md-table-cell">${obj.content_date }</td>
		</tr>
		</c:forEach> --%>
					</tbody>
				</table>


			</div>


			<div class="pagination">
				<ul class="pagination justify-content-center">
					<c:choose>

						<c:when test="${pageBean.prevPage <= 0 }">
							<li class="page-item disabled"><a href="#" class="page-link">←</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a
								href="${root }board/main?board_info_idx=${board_info_idx}&page=${pageBean.prevPage}"
								class="page-link">←</a></li>
						</c:otherwise>
					</c:choose>

					<c:forEach var='idx' begin="${pageBean.min }"
						end='${pageBean.max }'>
						<c:choose>
							<c:when test="${idx == pageBean.currentPage }">
								<li class="page-item active"><a
									href="${root }board/main?board_info_idx=${board_info_idx}&page=${idx}"
									class="page-link">${idx+1 }</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a
									href="${root }board/main?board_info_idx=${board_info_idx}&page=${idx}"
									class="page-link">${idx+1 }</a></li>
							</c:otherwise>
						</c:choose>

					</c:forEach>

					<c:choose>

						<c:when test="${pageBean.max >= pageBean.pageCnt }">
							<li class="page-item disabled"><a href="#" class="page-link">→</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a
								href="${root }board/main?board_info_idx=${board_info_idx}&page=${pageBean.nextPage}"
								class="page-link">→</a></li>
						</c:otherwise>
					</c:choose>

				</ul>
			</div>
		
	</div>
	<div class="showBest">
		<h3 style="margin-right: 120px;">BEST</h3>
		<div class="bestContent">
			<div>
			<span>제목2123123</span>
			<span style="margin-left: auto; margin-right: 10px;">♡9 ⊙47</span>
			</div>
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
            <textarea rows="1" cols="1"></textarea>
            <p>메일주소</p>
            <textarea rows="1" cols="1"></textarea>
            <p>문의내용</p>
			<textarea style="width: 100%; height: 250px; padding: 0; line-height: normal; margin-bottom: 7px;"></textarea>
            <div style="background:white; display: grid; justify-content: end;"><a href="${root }board/sendMail_pro">메일 보내기</a></div>
        </div>
    </div>
	<footer>
		<!-- ========== -->
	</footer>
</body>
</html>
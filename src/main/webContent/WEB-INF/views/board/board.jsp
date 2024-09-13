<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Board</title>
<style>
* {
	background: black;
	color: white;
}

body {
	background: black;
	display: flex;
	justify-content: center;
	min-height: 100vh; /* 화면 전체 높이를 사용 */
	margin: 0;
}

.container {
	flex-direction: column; /* 수직 방향으로 정렬 */
	width: 80%; /* 원하는 너비 설정 */
	max-width: 1200px; /* 최대 너비 설정 */
	background: black; /* 배경 색상 추가 (선택 사항) */
	padding: 20px; /* 패딩 추가 */
}

button{
	margin: 10px;
    border: none; /* 테두리 제거 */
    border-radius: 20px; /* 모서리 둥글게 */
    padding: 10px 20px; /* 안쪽 여백 */
    font-weight: bolder;
    font-size: 18px; /* 글자 크기 */
    cursor: pointer; /* 커서 모양 */
    transition: background-color 0.3s, transform 0.3s; /* 효과 전환 */
}

button:hover {
	background: white;
	color: black;
	
}

input{
	border: none;
	border-bottom: 1px solid gray;
	margin-bottom: 10px;
}

input:focus{
	outline: none;
	border-bottom-color: #33FF33;
}

table{
	border-collapse: collapse;
	width: 100%; 
}

thead {
	position: relative;
	border-top: 1px solid white;
}

thead th{
	padding: 10px;
}

.on-table{
	display: flex;
}

span {
    margin-left: auto;
    margin-right: 20px;
    margin-bottom: 12px;
}
.write-btn{
	background: #74E885;
	color: black;
	border: none;
	border-radius: 3px;
	padding: 5px 20px;
	font-size: 14px;
	text-decoration: none;
	cursor: pointer;
	transition: background-color 0.3s
}

.write-btn:hover {
    background: #BC5ADC;
}
</style>
</head>
<body>
<header>
<!-- ============ -->
</header>
<div class="container">
<h1>커뮤니티</h1>
<div>
<button>전체</button>
<button>자유게시판</button>
<button>소식/정보</button>
<button>음악 추천</button>
<p></p>
</div>
<div class="on-table">
<input placeholder="search"> <%-- <form:input path="title"/> --%>
<span class="right-align">
	<a href="${root }board/write?board_info_idx=${board_info_idx}" class="write-btn">글쓰기</a>
</span>
</div>
<div>
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
		<td>제목1~~~~~~~~~~~~~~~~~</td>
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
								<li class="page-item disabled"><a href="#"
									class="page-link">이전</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item">
								<a href="${root }board/main?board_info_idx=${board_info_idx}&page=${pageBean.prevPage}" class="page-link">이전</a></li>
							</c:otherwise>
						</c:choose>

						<c:forEach var='idx' begin="${pageBean.min }" end='${pageBean.max }'>
							<c:choose>
								<c:when test="${idx == pageBean.currentPage }">
									<li class="page-item active"><a
										href="${root }board/main?board_info_idx=${board_info_idx}&page=${idx}"
										class="page-link">${idx }</a></li>
								</c:when>
								<c:otherwise>
									<li class="page-item"><a
										href="${root }board/main?board_info_idx=${board_info_idx}&page=${idx}"
										class="page-link">${idx }</a></li>
								</c:otherwise>
							</c:choose>

						</c:forEach>

						<c:choose>
						
							<c:when test="${pageBean.max >= pageBean.pageCnt }">
								<li class="page-item disabled"><a href="#"
									class="page-link">다음</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a
									href="${root }board/main?board_info_idx=${board_info_idx}&page=${pageBean.nextPage}"
									class="page-link">다음</a></li>
							</c:otherwise>
						</c:choose>

					</ul>
				</div>
		</div>
</div>
<footer>
<!-- ========== -->
</footer>
</body>
</html>
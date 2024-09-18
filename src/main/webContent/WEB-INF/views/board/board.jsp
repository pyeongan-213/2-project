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

button.c_btn {
	margin: 10px;
	border: none; /* 테두리 제거 */
	border-radius: 20px; /* 모서리 둥글게 */
	padding: 10px 20px; /* 안쪽 여백 */
	font-weight: bolder;
	font-size: 18px; /* 글자 크기 */
	cursor: pointer; /* 커서 모양 */
	transition: background-color 0.3s, transform 0.3s; /* 효과 전환 */
}

button.c_btn:hover {
	background: white;
	color: black;
}

input {
	border: none;
	border-bottom: 1px solid gray;
	margin-bottom: 10px;
}

input:focus {
	outline: none;
	border-bottom-color: #33FF33;
}

table {
	border-collapse: collapse;
	width: 100%;
}

thead {
	position: relative;
	border-top: 1px solid white;
}

thead th {
	padding: 10px;
}

.on-table {
	display: flex;
}

span {
	margin-left: auto;
	margin-right: 20px;
	margin-bottom: 12px;
}

.write-btn {
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

.pagination {
	display: flex; /* Flexbox를 사용하여 수평 정렬 */
	justify-content: center; /* 중앙 정렬 */
	padding-left: 0; /* 기본 여백 제거 */
	margin: 0; /* 기본 여백 제거 */
	margin-top: 10px;
	list-style: none; /* 기본 리스트 스타일 제거 */
}

.pagination .page-item {
	margin: 0 5px; /* 페이지 항목 간의 간격 조정 */
	text-decoration: none;
}

.page-link {
text-decoration: none;
}

/* .fixed-section: 페이지의 우측 하단에 고정된 섹션 */
.fixed-section {
    position: fixed; /* 페이지 스크롤과 관계없이 화면에 고정됨 */
    bottom: 0; /* 화면 하단에 위치 */
    right: 0; /* 화면 우측에 위치 */
    margin: 30px 50px; /* 화면 가장자리로부터 16px 거리 유지 */
    z-index: 1000; /* 다른 요소들 위에 표시되도록 설정 (높은 값일수록 위에 표시됨) */
}

/* .fixed-button: 고정 섹션 내의 버튼 스타일 */
.fixed-button {
    display: block; /* 블록 요소로 설정 (flexbox 관련 속성은 사용하지 않음) */
    width: 65px; /* 버튼의 너비를 40px로 설정 */
    height: 65px; /* 버튼의 높이를 40px로 설정 */
    /* 
    background-image: url('path/to/button-image.png');  이미지 파일의 경로 
    background-size: cover; 버튼 크기에 맞게 이미지 조절
    background-repeat: no-repeat; 이미지 반복 방지 
    */
    background-color: #007BFF; /* 버튼의 배경색을 파란색으로 설정 */
    border: none; /* 버튼의 테두리를 제거 */
    border-radius: 50%; /* 버튼을 원형으로 만듦 */
    font-size: 40px;
    cursor: pointer; /* 버튼 위에 마우스를 올렸을 때 손 모양의 커서 표시 */
    outline: none; /* 버튼의 포커스 아웃라인을 제거 */
}

/* .fixed-content: 버튼 클릭 시 표시될 고정된 콘텐츠 */
.fixed-content {
    display: none; /* 기본적으로 콘텐츠를 숨김 */
    position: absolute; /* 버튼의 절대 위치에 상대적으로 위치 설정 */
    bottom: 100px; /* 버튼 위 50px 거리에서 시작 */
    right: 0; /* 버튼과 같은 우측에 위치 */
    width: 300px; /* 콘텐츠의 너비를 200px로 설정 */
    background-color: #fff; /* 콘텐츠 배경색을 흰색으로 설정 */
    border: 1px solid #ddd; /* 콘텐츠 테두리를 연한 회색으로 설정 */
    border-radius: 4px; /* 콘텐츠의 모서리를 둥글게 설정 */
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 콘텐츠에 그림자를 추가하여 입체감을 줌 */
    padding: 10px; /* 콘텐츠 내부에 10px의 여백 추가 */
}

.fixed-content h3 {
	color: black;
	background: white;
	margin: 10px 5px;
}

.fixed-content p {
    color: black; /* 글자 색상 설정 */
    background: white; /* 배경색 설정 (필요한 경우) */
    margin: 10px 0px; /* 단락 간의 여백 조정 */
}

.fixed-content input {
    color: black; /* 글자 색상 설정 */
    background: white; /* 배경색 설정 */
    border: 1px solid gray; /* 테두리 설정 */
    padding: 5px; /* 안쪽 여백 추가 */
    width: 100%; /* 입력창 너비를 100%로 설정 */
    box-sizing: border-box; /* 패딩과 테두리를 포함한 너비 설정 */
}

</style>
<script>
        function toggleContent() {
            var content = document.querySelector('.fixed-content');
            if (content.style.display === 'block') {
                content.style.display = 'none'; // 이미 보이면 숨김
            } else {
                content.style.display = 'block'; // 숨겨져 있으면 표시
            }
        }
    </script>
</head>
<body>
	<header>
		<!-- ============ -->
	</header>
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
				href="${root }board/write?board_info_idx=${board_info_idx}"
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
	<div class="fixed-section">
        <button class="fixed-button" onclick="toggleContent()">
        +
        </button>
        <div class="fixed-content">
            <h3>문의하기</h3>
            <p>이름/메일주소/문의내용</p>
            <input>
            <button class="write-btn">메일 보내기</button>
        </div>
    </div>
	<footer>
		<!-- ========== -->
	</footer>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var='root' value='${pageContext.request.contextPath }/' />
<c:set var="URI_1" value="${requestScope['javax.servlet.forward.request_uri']}" />
<c:set var="URI_2" value="${root}board/main" />
<c:set var="URI_3" value="${root}board/main_sort" />
<c:set var="URI_4" value="${root}board/search" />
<c:set var="URI_5" value="${root}board/search" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" type="image/png" sizes="48x48" href="${root}/img/favicon.png">
    <!-- CSS 및 Bootstrap 아이콘 추가 -->
    <link href="${root}/css/main.css" rel="stylesheet" type="text/css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.3/dist/sweetalert2.min.css">
   <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<link rel="stylesheet" type="text/css"
   href="${pageContext.request.contextPath}/css/board.css">
<link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
   <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<title>Board</title>
<script>

</script>
</head>
<body onload="setCurrentBoardIdFromURL();">
   <header>
      <!-- top_menu.jsp 포함 -->
      <jsp:include page="/WEB-INF/views/include/top_menu.jsp" />
      <!-- Sidebar 포함 -->
      <jsp:include page="/WEB-INF/views/include/sidebar.jsp" />

   </header>
   
   <div id="main-content">
   <div class="board-container">

      <h1>커뮤니티</h1>
      <div>
         <button class="c_btn"
            onclick="setBoardId(0); setTimeout(() => { location.href='${root}board/main'; }, 100);">전체</button>
         <button class="c_btn"
            onclick="setBoardId(1); setTimeout(() => { location.href='${root}board/main_sort?board_id=1'; }, 100);">자유게시판</button>
         <button class="c_btn"
            onclick="setBoardId(2); setTimeout(() => { location.href='${root}board/main_sort?board_id=2'; }, 100);">소식/정보</button>
         <button class="c_btn"
            onclick="setBoardId(3); setTimeout(() => { location.href='${root}board/main_sort?board_id=3'; }, 100);">음악 추천</button>
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
                     <td style="width: 50%; text-align: left; padding: 0 20px;"><a
                        href="${root }board/read?board_id=${obj.board_id }&boardpost_id=${obj.boardpost_id}"
                        style="color: #fea443;"> 🔥${obj.content_title}</a></td>
                     <td style="width: 20%; color: gray; font-size: 14px;">${obj.writedate}</td>
                     <td style="width: 10%; text-align: left; color: gray; font-size: 14px;">♡ ${obj.like_count}</td>
                  </tr>
               </tbody>
            </c:forEach>
         </table>
         <hr />
      </div>
      <div>
         <h3>🗨️최신 게시글</h3>
         <div class="on-table">
            <form id="searchForm" action="${root}board/search" method="get">
               <input id="searchInput" name="query" placeholder="search" required>
               <input type="hidden" id="boardId" name="board_id" value="0">
            </form>
            <span style="margin-left: auto; margin-right: 20px;"> <a href="${root }board/write"
               class="write-btn">글작성</a>
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
                     <td style="width: 35%; text-align: left; padding: 0 20px;"><a
                        href="${root}board/read?board_id=${obj.board_id }&boardpost_id=${obj.boardpost_id}"
                        style="color: white;">
                           ${obj.content_title} </a></td>
                     <td style="width: 15%; color: gray; font-size: 14px;">${obj.membername}</td>
                     <td style="width: 15%; color: gray; font-size: 14px;">${obj.writedate}</td>
                     <td style="width: 15%; color: gray; font-size: 14px;">♡ ${obj.like_count}</td>
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
                     <li class="page-item-disabled"><a href="#" class="page-link">←</a></li>
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
                     <li class="page-item-disabled"><a href="#" class="page-link">→</a></li>
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
                     <li class="page-item-disabled"><a href="#" class="page-link">←</a></li>
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
                     <li class="page-item-disabled"><a href="#" class="page-link">→</a></li>
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
                     <li class="page-item-disabled"><a href="#" class="page-link">←</a></li>
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
                     <li class="page-item-disabled"><a href="#" class="page-link">→</a></li>
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
      <button class="fixed-button" onclick="toggleContent()">
         <span class="icon">+</span>
      </button>
      
      <div class="fixed-content">
         <form:form id="emailForm" action="${root }board/receiveEmail/${loginMemberBean.email}" method="post">
            <div class="message-container">
            <h3>🦆DuckMusic</h3>
            <div class="message">문의 사항 또는 불편한 점을 알려주세요!</div>
            </div>
            <p>
            <label for="name">이름</label>
            <textarea id="name" name="name" rows="1" readonly>${loginMemberBean.real_name}</textarea>
            </p>
            
            <p>
            <label for="email">메일 주소</label>
            <textarea id="email" name="email" rows="1" readonly>${loginMemberBean.email}</textarea>
            </p>
            
            <p>
            <label for="subject">제목</label>
            <textarea id="subject" name="subject" rows="1" required></textarea>
            </p>
            
            <p>
            <label for="body">문의 내용</label>
            <textarea id="body" name="body"
               style="width: 100%; height: 215px; padding: 5px; line-height: normal;"
               required></textarea>
            </p>
            
            <button class="send-mail" type="submit"> <span style="font-size: 17px;">전송하기&nbsp;</span> <img src="${root}/img/send-icon.png" alt="메일 전송" 
                  style="width: 15px; height: 15px; margin:0; filter: brightness(0) invert(1);"/>
            </button>
         </form:form>
      </div>
      
   </div>
   </div>
         
   <footer>
      <!-- bottom_info.jsp 포함 -->
      <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
   </footer>

   <div id="loading" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background: rgba(0, 0, 0, 0.7); z-index: 1000; text-align: center; padding-top: 20%;">
   <span class="back">
        <span>S</span>
        <span>e</span>
        <span>n</span>
        <span>d</span>
        <span>i</span>
        <span>n</span>
        <span>g</span>
   </span>
   </div>

<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
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
       
       // URL에서 button 파라미터 확인
       const urlParams = new URLSearchParams(window.location.search);
       const buttonParam = urlParams.get('button');
       
       if (buttonParam === 'activated') {
           // 문의 버튼 활성화
           toggleContent(); // 문의 버튼 열기
       }
       
   };

   $(document).ready(function() {
        $('#emailForm').on('submit', function(event) {
            event.preventDefault(); // 기본 제출 방지

            // 로딩 화면 표시
            $('#loading').show();

            $.ajax({
                url: $(this).attr('action'),
                method: 'POST',
                data: $(this).serialize(),
                success: function() {
                    // 성공적으로 메일이 전송되면 알림창을 띄운다.
                    Swal.fire({
                        icon: 'success',
                       title: '메일이 성공적으로 전송되었습니다!',
                        background: '#3A3A3A',  // 배경색
                        color: '#fff',  // 텍스트 색상
                        confirmButtonColor: '#1db954',  // 확인 버튼 색상
                        confirmButtonText: '확인'
                     }).then(() => {
                        // 메인 페이지로 이동
                        window.location.href = '${root}board/main';
                    });
                },
                error: function() {
                    // 에러 발생 시 알림창을 띄운다.
                    Swal.fire({
                      icon: 'error',
                      title: '메일 전송에 실패했습니다.',
                      background: '#3A3A3A',  // 배경색
                      color: '#fff',  // 텍스트 색상
                      confirmButtonColor: '#1db954',  // 확인 버튼 색상
                      confirmButtonText: '확인'
                   });
                }
            });
        });
    });

   function toggleContent() {
       var content = document.querySelector('.fixed-content');
       var button = document.querySelector('.fixed-button');
       
       if (content.style.display === 'block') {
           content.style.opacity = 0;
           content.style.transform = 'translateY(20px)'; // 아래로 이동
           setTimeout(() => {
               content.style.display = 'none';
               button.classList.remove('active');
           }, 300); // 애니메이션 시간과 일치
       } else {
           content.style.display = 'block';
           setTimeout(() => {
               content.style.opacity = 1;
               content.style.transform = 'translateY(0)'; // 원래 위치로 복귀
           }, 10);
           button.classList.add('active');
       }
   }
</script>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>퀴즈 로비</title>

    <!-- 탭 아이콘 -->
    <link rel="icon" type="image/png" sizes="48x48" href="${root}/img/tabicon.png">
    
    <!-- CSS 및 Bootstrap 아이콘 추가 -->
    <link rel="stylesheet" href="${root}/css/quizlobby.css">
    <link href="${root}/css/main.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet">
    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
	
    <script>
        // JavaScript에 root 경로 전달
        window.root = '${pageContext.request.contextPath}';

        // 로그인된 사용자 ID를 전달
        <% 
            String userId = (session.getAttribute("loginMemberBean") != null)
                ? String.valueOf(((kr.co.duck.beans.MemberBean) session.getAttribute("loginMemberBean")).getMember_id())
                : "null";
            userId = userId.trim();
        %>
        const currentUserId = "<%= userId %>";
    </script>
</head>
<body>
    <jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

    <div class="flex-container quiz-lobby">
        <div class="sidebar">
            <jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
        </div>
        
		
        <div class="main-content">
         <div class="main-line">
            <div class="header">
                <div class="header-left">
                    <h1>퀴즈 로비</h1>
                </div>
                <button id="create-room-btn">방 생성</button>
            </div>

            <section class="main-container">
                <div class="room-list-container">
                    <h2>방 목록</h2>
                    <ul id="room-list">
                        <c:forEach var="room" items="${rooms}">
                            <li>
                                <div class="room-info">
                                    <div class="room-details">
                                        <span class="room-name"><c:out value="${room.quizRoomName}" /></span>
                                        <span class="room-owner">(<c:out value="${room.owner}" />의 방)</span>
                                        <span class="room-users"><c:out value="${room.memberCount}" /> / <c:out value="${room.maxCapacity}" />명</span>
                                    </div>
                                    <button class="join-room-btn" data-room-id="${room.quizRoomId}" 
                                            data-requires-password="${room.quizRoomPassword != null && !room.quizRoomPassword.isEmpty()}">참여</button>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                
                </div>
                
            </section>
        </div>
    </div>

    <div id="create-room-modal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>방 생성</h2>
            <form id="create-room-form">
                <label for="roomName">방 이름:</label> 
                <input type="text" id="roomName" name="roomName" value="새로운 퀴즈방" required><br>

	            <label for="roomPassword">방 비밀번호(선택사항):</label> 
	            <input type="password" id="roomPassword" name="roomPassword" placeholder="비밀번호 (공백 가능)" value=""><br> <!-- 비밀번호 필드 추가 -->

                <label for="maxCapacity">최대 인원수:</label> 
                <input type="number" id="maxCapacity" name="maxCapacity" min="1" max="10" value="10" required><br>

                <label>최대 곡 수 선택:</label>
                <div class="max-music-buttons">
                    <button type="button" class="music-button" data-value="100">100곡</button>
                    <button type="button" class="music-button" data-value="200">200곡</button>
                    <button type="button" class="music-button" data-value="300">300곡</button>
                    <button type="button" class="music-button" data-value="400">400곡</button>
                    <button type="button" class="music-button" data-value="500">500곡</button>
                </div>

                <input type="hidden" id="maxMusic" name="maxMusic" value="100" required>

                <label for="quizRoomType">퀴즈 타입:</label> 
                <select id="quizRoomType" name="quizRoomType">
                    <option value="songTitle">노래 제목 맞히기</option>
                    <option value="artistName">가수 이름 맞히기</option>
                </select><br>
                <button type="submit">방 생성</button>
            </form>
        </div>
    </div>

    <div id="password-modal" class="modal">
        <div class="modal-content">
            <span class="close-password-modal">&times;</span>
            <h2>비밀번호 입력</h2>
            <form id="password-form">
                <label for="roomPassword">비밀번호:</label> 
                <input type="password" id ="roomPassword" name="roomPassword" required>
                <button type="button" id="submit-password">참여</button>
            </form>
        </div>
    </div>

    <jsp:include page="/WEB-INF/views/include/bottom_info.jsp" />
    <script src="${root}/js/quizlobby.js"></script>
    <!-- SweetAlert 다크 테마 및 스크립트 추가 -->
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
</body>
</html>

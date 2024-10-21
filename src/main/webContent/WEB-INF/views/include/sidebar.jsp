<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${root}/css/HJ_sidebar.css">
<title>Sidebar with Playlists</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
    <link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>

</head>

<body>
	<div class="overlay"></div>
	<!-- 어두운 배경 추가 -->

	<nav>
		<div class="menu-btn">
			<div class="line line__1"></div>
			<div class="line line__2"></div>
			<div class="line line__3"></div>
		</div>

		<ul class="nav-links" style="padding: 0;">
			<li class="link"><a href="${root}/board/main">Board</a></li>
			<li class="link"><a href="${root}/quiz/quizlobby">Quiz</a></li>
			<li class="link"><a href="${root}/search/randomAlbumPage">RANDOM</a></li>

		</ul>
		<div class="SideplaylistMakerWrapper">
			<h4>새 플레이리스트를 추가하세요</h4>
			<input id="newPlaylistName" class="inputplaylistPlaceholder"
				type="text" placeholder="새 플레이리스트" />
			<button id="createPlaylistBtn" type="button">생성</button>
		</div>

		<div class="Sideplaylistwrapper">
			<h2>내 플레이리스트</h2>
			<div class="playlist-container">
				<c:forEach var="playlist" items="${playlists}">
					<div class="playlist-item">
						<a
							href="${root}/playlist/playlist?playlistId=${playlist.playlist_id}">
							${playlist.playlistname} </a>
					</div>
				</c:forEach>

				<c:if test="${empty playlists}">
					<div class="playlist-item">플레이리스트가 없습니다.</div>
				</c:if>
			</div>
		</div>
	</nav>

	<script>
console.clear();

const nav = document.querySelector("nav");
const navLinksContainer = document.querySelector(".nav-links");
const navLinks = [...document.querySelectorAll(".link")];
const menuBtn = document.querySelector(".menu-btn");
const subMenuBtn = document.querySelector(".sub-menu-btn");
const overlay = document.querySelector(".overlay"); // overlay 요소 가져오기

// 사이드바 외부 클릭 감지하여 닫기 위한 함수
document.addEventListener("click", function(event) {
  if (!nav.contains(event.target) && !menuBtn.contains(event.target)) {
    // 사이드바와 메뉴 버튼 외부를 클릭하면 닫기
    if (nav.classList.contains("nav-open")) {
      nav.classList.remove("nav-open");
      menuBtn.classList.remove("close");
      overlay.classList.remove("active"); // 어두운 배경 제거
    }
    if (nav.classList.contains("sub-menu-open")) {
      nav.classList.remove("sub-menu-open");
      removeSubmenu();
    }
  }
});

function createHoverEl() {
  let hoverEl = document.createElement("div");
  hoverEl.className = "hover-el";
  hoverEl.style.setProperty("--y", "0px");
  hoverEl.style.setProperty("--mousex", "0px");
  hoverEl.style.setProperty("--mousey", "0px");
  navLinksContainer.appendChild(hoverEl);
}
createHoverEl();

menuBtn.addEventListener("click", function() {
  nav.classList.toggle("nav-open");
  menuBtn.classList.toggle("close");
  if (nav.classList.contains("nav-open")) {
    overlay.classList.add("active"); // 어두운 배경 표시
    overlay.style.opacity = "1"; // opacity 변경으로 트랜지션 효과
  } else {
    overlay.style.opacity = "0"; // opacity 변경으로 트랜지션 효과
    setTimeout(() => {
      overlay.classList.remove("active"); // 완전히 사라지면 클래스 제거
    }, 500); // 트랜지션 시간과 일치시킴
  }
});

if (subMenuBtn) {  // subMenuBtn이 존재하는 경우에만 이벤트 추가
    subMenuBtn.addEventListener("click", function() {
        nav.classList.toggle("sub-menu-open");
        removeSubmenu();
    });
}

function toggleSubmenu(el) {
  let subMenu = nav.querySelector(".sub-menu");
  if (el.children.length > 1) {
    createSubmenu(el);
  } else if (nav.contains(subMenu)) {
    removeSubmenu();
  } else {
    return;
  }
}

function createSubmenu(el) {
  let subMenuContainer = document.createElement("div");
  subMenuContainer.className = "sub-menu";
  let subMenuItem = el.children[1].cloneNode(true);
  let subMenuItemList = [...subMenuItem.children];
  subMenuItemList.forEach((item, index) => {
    item.classList.add("off-menu");
    item.style.setProperty("--delay", `${index * 40}ms`);
  });
  nav.classList.toggle("sub-menu-open");
  nav.appendChild(subMenuContainer);
  subMenuContainer.appendChild(subMenuItem);
  setTimeout(function() {
    subMenuItemList.forEach(item => {
      item.classList.remove("off-menu");
      item.classList.add("on-menu");
    });
  }, 200);
}

function removeSubmenu() {
  let subMenu = nav.querySelector(".sub-menu");
  if (nav.contains(subMenu)) {
    let subMenuItemList = [...subMenu.children[0].children];
    subMenuItemList.forEach(item => {
      item.classList.add("off-menu");
      item.classList.remove("on-menu");
    });
    setTimeout(function() {
      nav.removeChild(subMenu);
    }, 500);
  }
}


</script>
	<script>
	$(document).ready(function() {
	    $('#createPlaylistBtn').click(function() {
	        var playlistName = $('#newPlaylistName').val();
	        if (playlistName.trim() === "") {
	        	Swal.fire({
                    icon: 'error',
                    title: '플레이리스트의 이름을 입력해주세요',
                    text: '',
                    background: '#3A3A3A',
                    color: '#fff',
                    confirmButtonColor: '#1db954',
                    confirmButtonText: '확인'
                });
	            return;
	        }

	        $.ajax({
	            url: '/Project_2/playlist/create',
	            type: 'POST',
	            data: { playlistName: playlistName },
	            success: function(data) {
	            	Swal.fire({
	                    icon: 'success',
	                    title: '플레이리스트를 추가했어요',
	                    text: '',
	                    background: '#3A3A3A',
	                    color: '#fff',
	                    confirmButtonColor: '#1db954',
	                    confirmButtonText: '확인'
	                });
	                $.ajax({
	                    url: '/Project_2/playlist/selectPlaylist',
	                    type: 'GET',
	                    success: function(htmlData) {
	                        $(".Sideplaylistwrapper").html(htmlData);
	                    }
	                });
	                $('#newPlaylistName').val("");
	            },
	            error: function(xhr, status, error) {
	                console.error("플레이리스트 생성 중 오류 발생: " + error);
	            }
	        });
	    });

	    $.ajax({
	        url: '/Project_2/playlist/selectPlaylist',
	        type: 'GET',
	        cache: false,
	        success: function(htmlData) {
	            $(".Sideplaylistwrapper").empty();
	            document.querySelector(".Sideplaylistwrapper").innerHTML = htmlData;
	        },
	        error: function(xhr, status, error) {
	            console.error("플레이리스트 데이터를 가져오는 중 오류 발생: " + error);
	        }
	    });
	});
	
</script>
</body>
</html>

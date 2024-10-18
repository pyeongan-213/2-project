<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/HJ_sidebar.css">
<title>Sidebar with Playlists</title>
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
			<li class="link"><a
				href="${pageContext.request.contextPath}/board/main">Board</a></li>
			<li class="link"><a
				href="${pageContext.request.contextPath}/quiz/quizlobby">Quiz</a></li>
			<li></li>
		</ul>
		<div class="SideplaylistMakerWrapper">
			<h4>새 플레이리스트를 추가하세요</h4>
			<input class="inputplaylistPlaceholder" type="text"
				placeholder="새 플레이리스트" />
			<button type="submit">생성</button>
		</div>

		<div class="Sideplaylistwrapper">
			<h2>내 플레이리스트</h2>
			<c:forEach var="playlist" items="${playlists}">

				<td>${playlist.playlistName}</td>
			</c:forEach>

			<c:if test="${empty playlists}">
				<p>
					플레이리스트가 없습니다. 
				</p>
			</c:if>
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

subMenuBtn.addEventListener("click", function() {
  nav.classList.toggle("sub-menu-open");
  removeSubmenu();
});

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
</body>
</html>

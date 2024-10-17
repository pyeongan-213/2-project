<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/HJ_sidebar.css">
<title>Insert title here</title>
</head>
<body>
<nav>
  <div class="menu-btn">
    <div class="line line__1"></div>
    <div class="line line__2"></div>
    <div class="line line__3"></div>
  </div>

  <div class="sub-menu-btn">
    <div class="line line__1"></div>
    <div class="line line__2"></div>
  </div>

  <ul class="nav-links" style="padding: 0;">
    <li class="link">
      <a href="${pageContext.request.contextPath}/board/main">Board</a>
    </li>
    <li class="link">
      <a href="${pageContext.request.contextPath}/temp/tempMain">Temp</a>
    </li>
    <li class="link">
      <a href="${pageContext.request.contextPath}/playlist/selectPlaylist">Select Playlist</a>
    </li>
    <li class="link">
      <a href="${pageContext.request.contextPath}/quiz/quizlobby">Quiz</a>
    </li>
    <li class="link">
      <a href="${pageContext.request.contextPath}/temp/payment">payment</a>
    </li>
  </ul>
</nav>
<script>
console.clear();

const nav = document.querySelector("nav");
const navLinksContainer = document.querySelector(".nav-links");
const navLinks = [...document.querySelectorAll(".link")];
const menuBtn = document.querySelector(".menu-btn");
const subMenuBtn = document.querySelector(".sub-menu-btn");

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
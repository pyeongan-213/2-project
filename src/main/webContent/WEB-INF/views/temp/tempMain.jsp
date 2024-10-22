<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var='root' value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
   content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/locomotive-scroll@4.1.0/dist/locomotive-scroll.css" />
<script src="https://cdn.jsdelivr.net/npm/gsap@3.11.5/dist/gsap.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/gsap@3.11.5/dist/ScrollTrigger.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/locomotive-scroll@4.1.0/dist/locomotive-scroll.min.js"></script>
<script src="${pageContext.request.contextPath}/js/finisher-header.es5.min.js" type="text/javascript"></script>
<title>DuckMusic</title>
   
<style>
@keyframes wave {
  0%   { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
.wave {
  position: absolute;
  height: 750px;
  width: 750px;
  opacity: 0.6;
  left: 50%;          /* 중앙 정렬 */
  top: -125px;           /* 중앙 정렬 */
  margin-left: -375px; /* 가로 중앙 정렬 */
  margin-top: -375px;  /* 세로 중앙 정렬 */
  background: radial-gradient(#ff7f50, #ffcccb);
  border-radius: 44%;
  animation: wave 6000ms infinite linear;
  z-index: 1;
}
.wave:nth-child(2) {
  animation-duration: 4000ms;
}
.wave:nth-child(3) {
  animation-duration: 5000ms;
}

.card-container{
   display: flex;
   flex-direction: column;
     box-shadow: 0px 8px 28px -9px rgba(0,0,0,0.45);
     overflow: hidden;
     position: relative;
     width: 30vw;
     height: 60vh;
     margin: 0 55px;
     border-radius: 20px;
     text-align: center;
     object-fit: cover;
     padding: 0;
}

.card-title {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
}

.card-title img {
    max-width: 100%; /* 이미지가 카드 너비에 맞게 조절 */
    height: auto; /* 이미지 비율 유지 */
}

.card-con {
    flex: 1;
    padding: 20px;
    background-color: #f9f9f9;
}

body {
  font-family: termina, sans-serif;
  color: var(--text-color);
  background: var(--bg-color);
  transition: 0.3s ease-out;
  overflow-x: hidden;
  max-width: 100%;
  width: 100%;
  overscroll-behavior: none;
  margin: 0;
  -ms-overflow-style: none;
}

::-webkit-scrollbar{
   display: none;
}

section:not(#sectionPin) {
  min-height: 100vh;
  width: 100%;
  position: relative;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  grid-gap: 2rem;
  margin: auto;
  place-items: center;
  background: #b9b3a9;
}

section#payment{
  min-height: 100vh;
  width: 100%;
  position: relative;
  display: flex;
  flex-direction: column; /* 세로 방향으로 배치 */
  justify-content: center; /* 수직 중앙 정렬 */
  background: #b9b3a9;
}

img {
  height: 50vh;
  width: auto;
  object-fit: cover;
}

h1 {
  font-size: 5rem;
  line-height: 1;
  font-weight: 800;
  margin-bottom: 0;
  position: absolute;
  top: 15vh;
  left: 10vw;
  z-index: 4;
  overflow-wrap: break-word;
  hyphens: auto;
}

@media (max-width: 768px) {
  h1 {
    font-size: 16vw;
  }
}

h1 span {
  color:white;
  display: block;
}

h2 {
  font-size: 2rem;
  max-width: 400px;
}

.credit {
  font-family: Termina, sans-serif;
}

.credit a {
  color: var(--text-color);
}

* {
  box-sizing: border-box;
}

#sectionPin {
  height: 100vh;
  overflow: hidden;
  display: flex;
  left: 0;
  background: black;
  color: var(--bg-color);
  margin: 0;
  background: cover;
}

.pin-wrap {
  height: 100vh;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 50px 10vw;
  margin: 0;
  background: cover;
}

.pin-wrap > h2 {
  min-width: 30vw;
  padding: 0 5vw;
}

p {
  position: absolute;
  bottom: 10vh;
  right: 10vw;
  width: 200px;
  line-height: 1.5;
  color: white;
}

c-scrollbar{
   background-color: white;
}

.pay{
   display: flex;
   border-radius: 8px;
   margin-top: 30px;
}

.by-month, .by-year {
   position: relative;
   overflow: hidden;
   height: 350px;
   width: 400px;
   background: floralwhite;
   border-radius: 15px;
    flex: 1; /* 각 div가 동일한 너비를 가지도록 설정 */
    padding: 10px; /* 내부 여백 설정 */
    border: 2px solid wheat;
    text-align: center; /* 텍스트 중앙 정렬 */
    color: white;
    margin: 0 45px;
}

.btn{
   margin-top: 55PX;
}
</style>
</head>

<body style="overs">
   <header>
      <!-- top_menu.jsp 포함 -->
      <jsp:include page="/WEB-INF/views/include/top_menu.jsp" />

      <!-- Sidebar 포함 -->
         <jsp:include page="/WEB-INF/views/include/sidebar.jsp" />
   </header>
   <div class="container">
   <section>
   <div class="header finisher-header" style="width: 100%; height: 110vh; z-index: 10;">
      <h1 style="position: relative; z-index: 20;" data-scroll data-scroll-speed="1">
          <span>Horizontal</span>
          <span>scroll</span>
          <span>section</span>
          <span style="position:fixed; top: 270px; left: 5px; color: white; font-size: 20px">Hear your music in the best-in-class sound</span>
      </h1>
      
      <div class="test-pic" style="position: fixed; top: 35vh; margin-left: 45vw;">
      <img alt="예시" src="${root}/img/고양이와헤드셋여자아이.jpg">
      </div>
      <p data-scroll data-scroll-speed="2" data-scroll-delay="0.2" style="z-index: 20;">with GSAP ScrollTrigger & Locomotive Scroll</p>
   </div>
   </section>
   
   <section id="sectionPin">
      <div class="pin-wrap">
         <h2 style="color: white;">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</h2>
      
         <div class="card-container">
            <div class="card-title">
            <img src="${root}/img/미래도시,비.jpg" alt="사진" />
            </div> 
            <div class='card-con'>
            <h3>Hear more from your music.</h3>
            <p style="all:unset;">Experience your music like never before on TIDAL. Get the best-in-class sound formats like full lossless, HiRes FLAC, and Dolby Atmos.</p>
            </div>
           </div>
      
         <div class="card-container">
            <div class="card-title">
            <img src="${root}/img/미래도시,비.jpg" alt="사진" />
            </div> 
            <div class='card-con'>
            <h3>110+ million tracks and counting.</h3>
            <p style="all:unset;">Discover your next big breakout artist or favorite song in our ad-free music library of over 110+ million tracks.</p>
            </div>
           </div>
        
           <div class="card-container">
            <div class="card-title">
            <img src="${root}/img/미래도시,비.jpg" alt="사진" />
            </div> 
            <div class='card-con'>
            <h3>Mixes and Radio.</h3>
            <p style="all:unset;">Get a personalized playlist of songs curated to your tastes with My Mix, or explore new music with Artist Radio.</p>
            </div>
           </div>
        
           <div class="card-container">
            <div class="card-title">
            <img src="${root}/img/미래도시,비.jpg" alt="사진" />
            </div> 
            <div class='card-con'>
            <h3>Live on TIDAL</h3>
            <p style="all:unset;">Experience music in real-time with friends, family, or other fans with Live.</p>
            </div>
           </div>
      </div>
   </section>
   
   <section id="payment" style="background:#b9b3a9; color: black;">
      <h1 style="position: relative; z-index: 20; top:0; left: 0;" data-scroll data-scroll-speed="1">
          <span>Powerful sound for any</span>
          <span>purpose</span>
      </h1>
      <div class="pay">
         <div class="by-month">
         <div class="wave"></div>
         <div class="wave"></div>
         <div class="wave"></div>
         <div class="wave"></div>
            <h2 style="position: inherit; z-index: 2;">월간결제</h2>
            <div style="position: inherit; z-index: 2;">Experience best-in-class sound quality that opens up every detail with HiRes Free Lossless Audio Codec (HiRes FLAC). Best enjoyed on 5G or WiFi with a hardware connection.</div>
            <div style="position: inherit; z-index: 2; margin-top: 25px;">9,900원</div>
            <button class="btn">결제하기</button>
         </div>
         
         <div class="by-year">
         <div class="wave"></div>
         <div class="wave"></div>
         <div class="wave"></div>
            <h2 style="position: inherit; z-index: 2;">연간결제</h2>
            <div style="position: inherit; z-index: 2;">Get the music you love on the go without worrying about data. Useful when you have a weak signal, are reaching your data cap, or are running out of download space.</div>
            <div style="position: inherit; z-index: 2; margin-top: 25px;">99,000원</div>
            <button class="btn">결제하기</button>
         </div>
      </div>
   </section>
     </div>
   
   <script>
   new FinisherHeader({
        "count": 12,
        "size": {
          "min": 1300,
          "max": 1500,
          "pulse": 0
        },
        "speed": {
          "x": {
            "min": 3,
            "max": 4.5
          },
          "y": {
            "min": 3,
            "max": 4.5
          }
        },
        "colors": {
          "background": "#000000",
          "particles": [
            "#000000",
            "#e2cd7b",
            "#1e1ace",
            "#ce0e47"
          ]
        },
        "blending": "lighten",
        "opacity": {
          "center": 0.35,
          "edge": 0
        },
        "skew": 0,
        "shapes": [
          "c"
        ]
      });
   
   gsap.registerPlugin(ScrollTrigger);

   const pageContainer = document.querySelector(".container");

   /* SMOOTH SCROLL */
   const scroller = new LocomotiveScroll({
     el: pageContainer,
     smooth: true
   });

   scroller.on("scroll", ScrollTrigger.update);

   ScrollTrigger.scrollerProxy(pageContainer, {
     scrollTop(value) {
       return arguments.length
         ? scroller.scrollTo(value, 0, 0)
         : scroller.scroll.instance.scroll.y;
     },
     getBoundingClientRect() {
       return {
         left: 0,
         top: 0,
         width: window.innerWidth,
         height: window.innerHeight
       };
     },
     pinType: pageContainer.style.transform ? "transform" : "fixed"
   });

   ////////////////////////////////////
   ////////////////////////////////////
   window.addEventListener("load", function () {
     let pinBoxes = document.querySelectorAll(".pin-wrap > *");
     let pinWrap = document.querySelector(".pin-wrap");
     let pinWrapWidth = pinWrap.offsetWidth;
     let horizontalScrollLength = pinWrapWidth - window.innerWidth;

     // Pinning and horizontal scrolling

     gsap.to(".pin-wrap", {
       scrollTrigger: {
         scroller: pageContainer, //locomotive-scroll
         scrub: true,
         trigger: "#sectionPin",
         pin: true,
         // anticipatePin: 1,
         start: "top top",
         end: pinWrapWidth
       },
       x: -horizontalScrollLength,
       ease: "none"
     });

     ScrollTrigger.addEventListener("refresh", () => scroller.update()); //locomotive-scroll

     ScrollTrigger.refresh();
   });
   </script>
</body>
</html>

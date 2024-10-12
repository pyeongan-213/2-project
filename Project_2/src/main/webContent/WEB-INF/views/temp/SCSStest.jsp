<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var='root' value="${pageContext.request.contextPath}/" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Main Page</title>
<link rel="stylesheet" type="text/css"
	href="${root}css/SCSStest.css">
	
	
 <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<div class="container">
 <div class="sidebar">
  <span class="logo">S</span>
  <a class="logo-expand" href="#">skateboard</a>
  <div class="side-wrapper">
   <div class="side-title">MENU</div>
   <div class="side-menu">
    <a class="sidebar-link discover is-active" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path d="M9.135 20.773v-3.057c0-.78.637-1.414 1.423-1.414h2.875c.377 0 .74.15 1.006.414.267.265.417.625.417 1v3.057c-.002.325.126.637.356.867.23.23.544.36.87.36h1.962a3.46 3.46 0 002.443-1 3.41 3.41 0 001.013-2.422V9.867c0-.735-.328-1.431-.895-1.902l-6.671-5.29a3.097 3.097 0 00-3.949.072L3.467 7.965A2.474 2.474 0 002.5 9.867v8.702C2.5 20.464 4.047 22 5.956 22h1.916c.68 0 1.231-.544 1.236-1.218l.027-.009z" />
     </svg>
     Discover
    </a>
    <a class="sidebar-link trending" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M10.835 12.007l.002.354c.012 1.404.096 2.657.242 3.451 0 .015.16.802.261 1.064.16.38.447.701.809.905a2 2 0 00.91.219c.249-.012.66-.137.954-.242l.244-.094c1.617-.642 4.707-2.74 5.891-4.024l.087-.09.39-.42c.245-.322.375-.715.375-1.138 0-.379-.116-.758-.347-1.064-.07-.099-.18-.226-.28-.334l-.379-.397c-1.305-1.321-4.129-3.175-5.593-3.79 0-.013-.91-.393-1.343-.407h-.057c-.665 0-1.286.379-1.603.991-.087.168-.17.496-.233.784l-.114.544c-.13.874-.216 2.216-.216 3.688zm-6.332-1.525C3.673 10.482 3 11.162 3 12a1.51 1.51 0 001.503 1.518l3.7-.328c.65 0 1.179-.532 1.179-1.19 0-.658-.528-1.191-1.18-1.191l-3.699-.327z" />
     </svg>
     Trending
    </a>
    <a class="sidebar-link" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path d="M11.23 7.29V3.283c0-.427.34-.782.77-.782.385 0 .711.298.763.677l.007.104v4.01h4.78c2.38 0 4.335 1.949 4.445 4.38l.005.215v5.04c0 2.447-1.887 4.456-4.232 4.569l-.208.005H6.44c-2.38 0-4.326-1.94-4.435-4.379L2 16.905v-5.03c0-2.447 1.878-4.466 4.222-4.58l.208-.004h4.8v6.402l-1.6-1.652a.755.755 0 00-1.09 0 .81.81 0 00-.22.568c0 .157.045.32.14.459l.08.099 2.91 3.015c.14.155.34.237.55.237a.735.735 0 00.465-.166l.075-.071 2.91-3.015c.3-.31.3-.816 0-1.126a.755.755 0 00-1.004-.077l-.086.077-1.59 1.652V7.291h-1.54z" />
     </svg>
     Streaming
    </a>
    <a class="sidebar-link" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M12.1535 16.64L14.995 13.77C15.2822 13.47 15.2822 13 14.9851 12.71C14.698 12.42 14.2327 12.42 13.9455 12.71L12.3713 14.31V9.49C12.3713 9.07 12.0446 8.74 11.6386 8.74C11.2327 8.74 10.896 9.07 10.896 9.49V14.31L9.32178 12.71C9.03465 12.42 8.56931 12.42 8.28218 12.71C7.99505 13 7.99505 13.47 8.28218 13.77L11.1139 16.64C11.1832 16.71 11.2624 16.76 11.3515 16.8C11.4406 16.84 11.5396 16.86 11.6386 16.86C11.7376 16.86 11.8267 16.84 11.9158 16.8C12.005 16.76 12.0842 16.71 12.1535 16.64ZM19.3282 9.02561C19.5609 9.02292 19.8143 9.02 20.0446 9.02C20.302 9.02 20.5 9.22 20.5 9.47V17.51C20.5 19.99 18.5 22 16.0446 22H8.17327C5.58911 22 3.5 19.89 3.5 17.29V6.51C3.5 4.03 5.4901 2 7.96535 2H13.2525C13.5 2 13.7079 2.21 13.7079 2.46V5.68C13.7079 7.51 15.1931 9.01 17.0149 9.02C17.4333 9.02 17.8077 9.02318 18.1346 9.02595C18.3878 9.02809 18.6125 9.03 18.8069 9.03C18.9479 9.03 19.1306 9.02789 19.3282 9.02561ZM19.6045 7.5661C18.7916 7.5691 17.8322 7.5661 17.1421 7.5591C16.047 7.5591 15.145 6.6481 15.145 5.5421V2.9061C15.145 2.4751 15.6629 2.2611 15.9579 2.5721C16.7203 3.37199 17.8873 4.5978 18.8738 5.63395C19.2735 6.05379 19.6436 6.44249 19.945 6.7591C20.2342 7.0621 20.0223 7.5651 19.6045 7.5661Z" />
     </svg>
     Playlist
    </a>
    <a class="sidebar-link" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M17.769 8.382H22C22 4.985 19.964 3 16.516 3H7.484C4.036 3 2 4.985 2 8.338v7.324C2 19.015 4.036 21 7.484 21h9.032C19.964 21 22 19.015 22 15.662v-.313h-4.231c-1.964 0-3.556-1.552-3.556-3.466 0-1.915 1.592-3.467 3.556-3.467v-.034zm0 1.49h3.484c.413 0 .747.326.747.728v2.531a.746.746 0 01-.747.728H17.85c-.994.013-1.864-.65-2.089-1.595a1.982 1.982 0 01.433-1.652 2.091 2.091 0 011.576-.74zm.151 2.661h.329a.755.755 0 00.764-.745.755.755 0 00-.764-.746h-.329a.766.766 0 00-.54.213.727.727 0 00-.224.524c0 .413.34.75.764.754zM6.738 8.382h5.644a.755.755 0 00.765-.746.755.755 0 00-.765-.745H6.738a.755.755 0 00-.765.737c0 .413.341.75.765.754z" />
     </svg>
     Bookmark
    </a>
   </div>
  </div>
  <div class="side-wrapper">
   <div class="side-title">CATEGORY</div>
   <div class="side-menu">
    <a class="sidebar-link" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M15.164 6.083l.948.011c3.405 0 5.888 2.428 5.888 5.78v4.307c0 3.353-2.483 5.78-5.888 5.78A193.5 193.5 0 0112.01 22c-1.374 0-2.758-.01-4.122-.038-3.405 0-5.888-2.428-5.888-5.78v-4.307c0-3.353 2.483-5.78 5.898-5.78 1.286-.02 2.6-.04 3.935-.04v-.163c0-.665-.56-1.204-1.226-1.204h-.972c-1.109 0-2.012-.886-2.012-1.965 0-.395.334-.723.736-.723.412 0 .736.328.736.723 0 .289.246.52.54.52h.972c1.481.01 2.688 1.194 2.698 2.64v.183c.619 0 1.238.008 1.859.017zm-4.312 8.663h-1.03v1.02a.735.735 0 01-.737.723.728.728 0 01-.736-.722v-1.021H7.31a.728.728 0 01-.736-.723c0-.395.334-.722.736-.722h1.04v-1.012c0-.395.324-.723.736-.723.403 0 .736.328.736.723v1.012h1.03c.403 0 .737.327.737.722a.728.728 0 01-.736.723zm4.17-1.629h.099a.728.728 0 00.736-.722.735.735 0 00-.736-.723h-.098a.722.722 0 100 1.445zm1.679 3.315h.098a.728.728 0 00.736-.723.735.735 0 00-.736-.723H16.7a.722.722 0 100 1.445z" />
     </svg>
     Live Stream
    </a>
    <a class="sidebar-link" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M7.33 2h9.34c3.4 0 5.32 1.93 5.33 5.33v9.34c0 3.4-1.93 5.33-5.33 5.33H7.33C3.93 22 2 20.07 2 16.67V7.33C2 3.93 3.93 2 7.33 2zm4.72 15.86c.43 0 .79-.32.83-.75V6.92a.815.815 0 00-.38-.79.84.84 0 00-1.28.79v10.19c.05.43.41.75.83.75zm4.6 0c.42 0 .78-.32.83-.75v-3.28a.839.839 0 00-1.28-.79.806.806 0 00-.38.79v3.28c.04.43.4.75.83.75zm-8.43-.75a.827.827 0 01-.83.75c-.43 0-.79-.32-.83-.75V10.2a.84.84 0 01.39-.79c.27-.17.61-.17.88 0s.42.48.39.79v6.91z" />
     </svg>
     Tutorial
    </a>
    <a class="sidebar-link" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M19.761 12.001c0 .814.668 1.475 1.489 1.475.414 0 .75.333.75.743v2.677C22 19.159 20.142 21 17.858 21H6.143C3.859 21 2 19.159 2 16.896v-2.677c0-.41.336-.743.75-.743.822 0 1.49-.662 1.49-1.475 0-.793-.641-1.39-1.49-1.39a.752.752 0 01-.53-.217.74.74 0 01-.22-.525l.002-2.764C2.002 4.842 3.86 3 6.144 3h11.712c2.284 0 4.143 1.842 4.143 4.105L22 9.782a.741.741 0 01-.219.526.753.753 0 01-.531.218c-.821 0-1.489.662-1.489 1.475zm-5.509.647l1.179-1.137a.73.73 0 00-.409-1.25l-1.629-.236-.729-1.462a.737.737 0 00-.662-.41H12a.74.74 0 00-.663.409l-.729 1.463-1.626.235a.735.735 0 00-.6.498.724.724 0 00.187.753l1.179 1.137-.278 1.608a.727.727 0 00.295.719.742.742 0 00.777.054L12 14.27l1.455.757a.733.733 0 00.78-.053.723.723 0 00.296-.718l-.279-1.608z" />
     </svg>
     Competation
    </a>
    <a class="sidebar-link" href="#">
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M16.158 8.233a4.207 4.207 0 01-4.209 4.234 4.207 4.207 0 01-4.21-4.234A4.206 4.206 0 0111.95 4a4.206 4.206 0 014.21 4.233zM11.95 20c-3.431 0-6.36-.544-6.36-2.72 0-2.177 2.91-2.74 6.36-2.74 3.431 0 6.361.544 6.361 2.72S15.399 20 11.949 20zm6.008-11.69a5.765 5.765 0 01-.984 3.24.158.158 0 00.107.245 3.4 3.4 0 00.483.046c1.643.044 3.118-1.02 3.525-2.621.604-2.379-1.168-4.514-3.425-4.514-.245 0-.48.026-.708.073-.031.007-.064.021-.082.05-.022.034-.006.08.016.11a5.807 5.807 0 011.068 3.37zm2.721 5.203c1.104.217 1.83.66 2.131 1.304a1.923 1.923 0 010 1.67c-.46.998-1.944 1.319-2.52 1.402-.12.018-.215-.086-.203-.206.295-2.767-2.048-4.08-2.654-4.381-.026-.014-.032-.034-.03-.047.003-.009.013-.023.033-.026 1.312-.024 2.722.156 3.243.284zM6.438 11.84c.164-.004.325-.019.483-.046a.158.158 0 00.106-.245 5.765 5.765 0 01-.984-3.24c0-1.25.39-2.416 1.068-3.372.022-.03.037-.075.016-.11-.017-.027-.051-.042-.082-.05a3.52 3.52 0 00-.71-.072c-2.255 0-4.027 2.135-3.423 4.514.407 1.6 1.882 2.664 3.525 2.621zm.159 1.414c.003.013-.003.033-.028.047-.607.302-2.95 1.614-2.656 4.38.013.121-.082.224-.201.207-.577-.083-2.06-.404-2.52-1.402a1.917 1.917 0 010-1.67c.3-.644 1.026-1.087 2.13-1.305.522-.127 1.93-.307 3.244-.283.02.003.03.017.03.026z" />
     </svg>
     Community
    </a>
   </div>
  </div>
 </div>
 <div class="wrapper">
  <div class="header">
   <div class="search-bar">
    <input type="text" placeholder="Search">
   </div>
   <div class="user-settings">
    <img class="user-img" src="https://images.unsplash.com/photo-1587918842454-870dbd18261a?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=943&q=80" alt="">
    <div class="user-name">Thomas</div>
    <svg viewBox="0 0 492 492" fill="currentColor">
     <path d="M484.13 124.99l-16.11-16.23a26.72 26.72 0 00-19.04-7.86c-7.2 0-13.96 2.79-19.03 7.86L246.1 292.6 62.06 108.55c-5.07-5.06-11.82-7.85-19.03-7.85s-13.97 2.79-19.04 7.85L7.87 124.68a26.94 26.94 0 000 38.06l219.14 219.93c5.06 5.06 11.81 8.63 19.08 8.63h.09c7.2 0 13.96-3.57 19.02-8.63l218.93-219.33A27.18 27.18 0 00492 144.1c0-7.2-2.8-14.06-7.87-19.12z"></path>
    </svg>
    <div class="notify">
     <div class="notification"></div>
     <svg viewBox="0 0 24 24" fill="currentColor">
      <path fill-rule="evenodd" clip-rule="evenodd" d="M18.707 8.796c0 1.256.332 1.997 1.063 2.85.553.628.73 1.435.73 2.31 0 .874-.287 1.704-.863 2.378a4.537 4.537 0 01-2.9 1.413c-1.571.134-3.143.247-4.736.247-1.595 0-3.166-.068-4.737-.247a4.532 4.532 0 01-2.9-1.413 3.616 3.616 0 01-.864-2.378c0-.875.178-1.682.73-2.31.754-.854 1.064-1.594 1.064-2.85V8.37c0-1.682.42-2.781 1.283-3.858C7.861 2.942 9.919 2 11.956 2h.09c2.08 0 4.204.987 5.466 2.625.82 1.054 1.195 2.108 1.195 3.745v.426zM9.074 20.061c0-.504.462-.734.89-.833.5-.106 3.545-.106 4.045 0 .428.099.89.33.89.833-.025.48-.306.904-.695 1.174a3.635 3.635 0 01-1.713.731 3.795 3.795 0 01-1.008 0 3.618 3.618 0 01-1.714-.732c-.39-.269-.67-.694-.695-1.173z" />
     </svg>
    </div>
   </div>
  </div>
  <div class="main-container">
   <div class="main-header anim" style="--delay: 0s">Discover</div>
   <div class="main-blogs">
    <div class="main-blog anim" style="--delay: .1s">
     <div class="main-blog__title">How to do Basic Jumping and how to landing safely</div>
     <div class="main-blog__author">
      <div class="author-img__wrapper">
       <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
        <path d="M20 6L9 17l-5-5" />
       </svg>
       <img class="author-img" src="https://images.unsplash.com/photo-1560941001-d4b52ad00ecc?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80" />
      </div>
      <div class="author-detail">
       <div class="author-name">Thomas Hope</div>
       <div class="author-info">53K views <span class="seperate"></span>2 weeks ago</div>
      </div>
     </div>
     <div class="main-blog__time">7 min</div>
    </div>
    <div class="main-blog anim" style="--delay: .2s">
     <div class="main-blog__title">Skateboard Tips You need to know</div>
     <div class="main-blog__author tips">
      <div class="main-blog__time">7 min</div>
      <div class="author-img__wrapper">
       <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
        <path d="M20 6L9 17l-5-5" />
       </svg>
       <img class="author-img" src="https://images.unsplash.com/photo-1496345875659-11f7dd282d1d?ixid=MXwxMjA3fDB8MHxzZWFyY2h8Mzl8fG1lbnxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" />
      </div>
      <div class="author-detail">
       <div class="author-name">Tony Andrew</div>
       <div class="author-info">53K views <span></span>2 weeks ago</div>
      </div>
     </div>
    </div>
   </div>
   <div class="small-header anim" style="--delay: .3s">Most Watched</div>
   <div class="videos">
    <div class="video anim" style="--delay: .4s">
     <div class="video-time">8 min</div>
     <div class="video-wrapper">
      <video muted="">
       <source src="https://player.vimeo.com/external/436572488.sd.mp4?s=eae5fb490e214deb9ff532dd98d101efe94e7a8b&profile_id=139&oauth2_token_id=57447761" type="video/mp4">
      </video>
      <div class="author-img__wrapper video-author">
       <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
        <path d="M20 6L9 17l-5-5" />
       </svg>
       <img class="author-img" src="https://images.pexels.com/photos/1680172/pexels-photo-1680172.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
      </div>
     </div>
     <div class="video-by">Andy William</div>
     <div class="video-name">Basic how to ride your skateboard comfortly</div>
     <div class="video-view">54K views<span class="seperate video-seperate"></span>1 week ago</div>
    </div>
    <div class="video anim" style="--delay: .5s">
     <div class="video-time">5 min</div>
     <div class="video-wrapper">
      <video muted="">
       <source src="https://player.vimeo.com/external/449972745.sd.mp4?s=9943177fe8a6147b7bc4598259401f06ec57878a&profile_id=139&oauth2_token_id=57447761" type="video/mp4">
      </video>
      <div class="author-img__wrapper video-author">
       <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
        <path d="M20 6L9 17l-5-5" />
       </svg>
       <img class="author-img" src="https://images.pexels.com/photos/3370021/pexels-photo-3370021.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
      </div>
     </div>
     <div class="video-by offline">Gerard Bind</div>
     <div class="video-name">Prepare for your first skateboard jump</div>
     <div class="video-view">42K views<span class="seperate video-seperate"></span>1 week ago</div>
    </div>
    <div class="video anim" style="--delay: .6s">
     <div class="video-time">7 min</div>
     <div class="video-wrapper">
      <video muted="">
       <source src="https://player.vimeo.com/external/436553499.sd.mp4?s=0e44527f269278743db448761e35c5e39cfaa52c&profile_id=139&oauth2_token_id=57447761" type="video/mp4">
      </video>
      <div class="author-img__wrapper video-author">
       <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
        <path d="M20 6L9 17l-5-5" />
       </svg>
       <img class="author-img" src="https://images.pexels.com/photos/1870163/pexels-photo-1870163.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
      </div>
     </div>
     <div class="video-by offline">John Wise</div>
     <div class="video-name">Basic equipment to play skateboard safely</div>
     <div class="video-view">64K views<span class="seperate video-seperate"></span>2 week ago</div>
    </div>
    <div class="video anim" style="--delay: .7s">
     <div class="video-time">6 min</div>
     <div class="video-wrapper">
      <video muted="">
       <source src="https://player.vimeo.com/external/361861493.sd.mp4?s=19d8275ca755d653042a87ef28b2f0b2eabf57d0&profile_id=139&oauth2_token_id=57447761" type="video/mp4">
      </video>
      <div class="author-img__wrapper video-author">
       <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
        <path d="M20 6L9 17l-5-5" />
       </svg>
       <img class="author-img" src="https://images.pexels.com/photos/2889942/pexels-photo-2889942.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
      </div>
     </div>
     <div class="video-by">Budi Hakim</div>
     <div class="video-name">Tips to playing skateboard on the ramp</div>
     <div class="video-view">50K views<span class="seperate video-seperate"></span>1 week ago</div>
    </div>
   </div>
   <div class="stream-area">
    <div class="video-stream">
     <video id="my_video_1" class="video-js vjs-default-skin anim" width="640px" height="267px" controls preload="none" poster='https://images.unsplash.com/photo-1476801071117-fbc157ae3f01?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1yZWxhdGVkfDh8fHxlbnwwfHx8&w=1000&q=80' data-setup='{ "aspectRatio":"940:620", "playbackRates": [1, 1.5, 2] }'>
      <source src="https://player.vimeo.com/external/390402719.sd.mp4?s=20cfdb066c4253047562b65bd4e411b86a004bc5&profile_id=139&oauth2_token_id=57447761" type='video/mp4' />
      <source src="https://player.vimeo.com/external/390402719.sd.mp4?s=20cfdb066c4253047562b65bd4e411b86a004bc5&profile_id=139&oauth2_token_id=57447761" type='video/webm' />
     </video>
     <div class="video-detail">
      <div class="video-content">
       <div class="video-p-wrapper anim" style="--delay: .1s">
        <div class="author-img__wrapper video-author video-p">
         <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
          <path d="M20 6L9 17l-5-5" />
         </svg>
         <img class="author-img" src="https://images.pexels.com/photos/1680172/pexels-photo-1680172.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
        </div>
        <div class="video-p-detail">
         <div class="video-p-name">Andy William</div>
         <div class="video-p-sub">1,980,893 subscribers</div>
        </div>
        <div class="button-wrapper">
         <button class="like">
          <svg viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
           <path d="M21.435 2.582a1.933 1.933 0 00-1.93-.503L3.408 6.759a1.92 1.92 0 00-1.384 1.522c-.142.75.355 1.704 1.003 2.102l5.033 3.094a1.304 1.304 0 001.61-.194l5.763-5.799a.734.734 0 011.06 0c.29.292.29.765 0 1.067l-5.773 5.8c-.428.43-.508 1.1-.193 1.62l3.075 5.083c.36.604.98.946 1.66.946.08 0 .17 0 .251-.01.78-.1 1.4-.634 1.63-1.39l4.773-16.075c.21-.685.02-1.43-.48-1.943z" />
          </svg>
          Share
         </button>
         <button class="like red">
          <svg viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
           <path fill-rule="evenodd" clip-rule="evenodd" d="M15.85 2.5c.63 0 1.26.09 1.86.29 3.69 1.2 5.02 5.25 3.91 8.79a12.728 12.728 0 01-3.01 4.81 38.456 38.456 0 01-6.33 4.96l-.25.15-.26-.16a38.093 38.093 0 01-6.37-4.96 12.933 12.933 0 01-3.01-4.8c-1.13-3.54.2-7.59 3.93-8.81.29-.1.59-.17.89-.21h.12c.28-.04.56-.06.84-.06h.11c.63.02 1.24.13 1.83.33h.06c.04.02.07.04.09.06.22.07.43.15.63.26l.38.17c.092.05.195.125.284.19.056.04.107.077.146.1l.05.03c.085.05.175.102.25.16a6.263 6.263 0 013.85-1.3zm2.66 7.2c.41-.01.76-.34.79-.76v-.12a3.3 3.3 0 00-2.11-3.16.8.8 0 00-1.01.5c-.14.42.08.88.5 1.03.64.24 1.07.87 1.07 1.57v.03a.86.86 0 00.19.62c.14.17.35.27.57.29z" />
          </svg>
          Liked
         </button>
        </div>
       </div>
       <div class="video-p-title anim" style="--delay: .2s">Basic how to ride your Skateboard</div>
       <div class="video-p-subtitle anim" style="--delay: .3s">Lorem ipsum dolor sit amet consectetur adipisicing elit. Repellendus illum tempora consequuntur. Lorem ipsum dolor sit amet consectetur adipisicing elit. Debitis earum velit accusantium maiores qui sit quas, laborum voluptatibus vero quidem tempore facilis voluptate tempora deserunt! </div>
       <div class="video-p-subtitle anim" style="--delay: .4s">Lorem ipsum dolor sit amet consectetur adipisicing elit. Temporibus laborum qui dolorum fugiat eius accusantium repellendus illum tempora consequuntur. Lorem ipsum dolor, sit amet consectetur adipisicing elit.</div>
      </div>
     </div>
    </div>
    <div class="chat-stream">
     <div class="chat">
      <div class="chat-header anim">Live Chat<span><svg viewBox="0 0 24 24" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
         <path fill-rule="evenodd" clip-rule="evenodd" d="M14.212 7.762c0 2.644-2.163 4.763-4.863 4.763-2.698 0-4.863-2.119-4.863-4.763C4.486 5.12 6.651 3 9.35 3c2.7 0 4.863 2.119 4.863 4.762zM2 17.917c0-2.447 3.386-3.06 7.35-3.06 3.985 0 7.349.634 7.349 3.083 0 2.448-3.386 3.06-7.35 3.06C5.364 21 2 20.367 2 17.917zM16.173 7.85a6.368 6.368 0 01-1.137 3.646c-.075.107-.008.252.123.275.182.03.369.048.56.052 1.898.048 3.601-1.148 4.072-2.95.697-2.675-1.35-5.077-3.957-5.077a4.16 4.16 0 00-.818.082c-.036.008-.075.025-.095.055-.025.04-.007.09.019.124a6.414 6.414 0 011.233 3.793zm3.144 5.853c1.276.245 2.115.742 2.462 1.467a2.107 2.107 0 010 1.878c-.531 1.123-2.245 1.485-2.912 1.578a.207.207 0 01-.234-.232c.34-3.113-2.367-4.588-3.067-4.927-.03-.017-.036-.04-.034-.055.002-.01.015-.025.038-.028 1.515-.028 3.145.176 3.747.32z" />
        </svg>
        15,988 people
       </span>
      </div>
      <div class="message-container">
       <div class="message anim" style="--delay: .1s">
        <div class="author-img__wrapper video-author video-p">
         <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
          <path d="M20 6L9 17l-5-5" />
         </svg>
         <img class="author-img" src="https://images.unsplash.com/photo-1560941001-d4b52ad00ecc?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80" />
        </div>
        <div class="msg-wrapper">
         <div class="msg__name video-p-name"> Wijaya Adabi</div>
         <div class="msg__content video-p-sub"> Lorem ipsum clor sit, ame conse quae debitis</div>
        </div>
       </div>
       <div class="message anim" style="--delay: .2s">
        <div class="author-img__wrapper video-author video-p">
         <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
          <path d="M20 6L9 17l-5-5" />
         </svg>
         <img class="author-img" src="https://images.pexels.com/photos/2889942/pexels-photo-2889942.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
        </div>
        <div class="msg-wrapper">
         <div class="msg__name video-p-name offline"> Johny Wise</div>
         <div class="msg__content video-p-sub"> Suscipit eos atque voluptates labore</div>
        </div>
       </div>
       <div class="message anim" style="--delay: .3s">
        <div class="author-img__wrapper video-author video-p">
         <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
          <path d="M20 6L9 17l-5-5" />
         </svg>
         <img class="author-img" src="https://images.unsplash.com/photo-1496345875659-11f7dd282d1d?ixid=MXwxMjA3fDB8MHxzZWFyY2h8Mzl8fG1lbnxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" />
        </div>
        <div class="msg-wrapper">
         <div class="msg__name video-p-name offline"> Budi Hakim</div>
         <div class="msg__content video-p-sub">Dicta quidem sunt adipisci</div>
        </div>
       </div>
       <div class="message anim" style="--delay: .4s">
        <div class="author-img__wrapper video-author video-p">
         <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
          <path d="M20 6L9 17l-5-5" />
         </svg>
         <img class="author-img" src="https://images.pexels.com/photos/1870163/pexels-photo-1870163.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
        </div>
        <div class="msg-wrapper">
         <div class="msg__name video-p-name"> Thomas Hope</div>
         <div class="msg__content video-p-sub"> recusandae doloremque aperiam alias molestias</div>
        </div>
       </div>
       <div class="message anim" style="--delay: .5s">
        <div class="author-img__wrapper video-author video-p">
         <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
          <path d="M20 6L9 17l-5-5" />
         </svg>
         <img class="author-img" src="https://images.pexels.com/photos/1680172/pexels-photo-1680172.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
        </div>
        <div class="msg-wrapper">
         <div class="msg__name video-p-name"> Gerard Will</div>
         <div class="msg__content video-p-sub">Dicta quidem sunt adipisci</div>
        </div>
       </div>
       <div class="message anim" style="--delay: .6s">
        <div class="author-img__wrapper video-author video-p">
         <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" class="feather feather-check">
          <path d="M20 6L9 17l-5-5" />
         </svg>
         <img class="author-img" src="https://images.pexels.com/photos/2889942/pexels-photo-2889942.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500" />
        </div>
        <div class="msg-wrapper">
         <div class="msg__name video-p-name offline">Johny Wise</div>
         <div class="msg__content video-p-sub"> recusandae doloremque aperiam alias molestias</div>
        </div>
       </div>
      </div>
      <div class="chat-footer anim" style="--delay: .1s">
       <input type="text" placeholder="Write your message">
      </div>
     </div>
     <div class="chat-vid__container">
      <div class="chat-vid__title anim" style="--delay: .3s">Related Videos</div>
      <div class="chat-vid anim" style="--delay: .4s">
       <div class="chat-vid__wrapper">
        <img class="chat-vid__img" src="https://cdn.nohat.cc/thumb/f/720/3b55eddcfffa4e87897d.jpg" />
        <div class="chat-vid__content">
         <div class="chat-vid__name">Prepare for your first skateboard jump</div>
         <div class="chat-vid__by">Jordan Wise</div>
         <div class="chat-vid__info">125.908 views <span class="seperate"></span>2 days ago</div>
        </div>
       </div>
      </div>
      <div class="chat-vid anim" style="--delay: .5s">
       <div class="chat-vid__wrapper">
        <img class="chat-vid__img" src="https://iamaround.it/wp-content/uploads/2015/02/pexels-photo-4663818.jpeg" />
        <div class="chat-vid__content">
         <div class="chat-vid__name">Prepare for your first skateboard jump</div>
         <div class="chat-vid__by">Jordan Wise</div>
         <div class="chat-vid__info">125.908 views <span class="seperate"></span>2 days ago</div>
        </div>
       </div>
      </div>
      <div class="chat-vid__button anim" style="--delay: .6s">See All related videos (32)</div>
     </div>
	<script src="${root}js/SCSSscript.js"></script>
</body>
</html>

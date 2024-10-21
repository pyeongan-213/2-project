<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<div class="playlist-container">
   <c:forEach var="playlist" items="${playlists}">
      <div class="playlist-item">
         <a href="${root}/playlist/playlist?playlistId=${playlist.playlist_id}">
            ${playlist.playlistname}
         </a>
      </div>
   </c:forEach>
   <c:if test="${empty playlists}">
      <div class="playlist-item">플레이리스트가 없습니다.</div>
   </c:if>
</div>

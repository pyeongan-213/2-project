<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var='root' value="${pageContext.request.contextPath}" />
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- SweetAlert 다크 테마 및 스크립트 추가 -->
<link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-dark@4/dark.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.js"></script>
<title>YouTube Search</title>
<style>
.album-table {
	width: calc(100% - 10px); /* 좌우 여백 포함한 전체 너비 */
	min-width: 80%; /* 표의 최소 너비 설정 */
	margin: 0 auto; /* 중앙 정렬 */
}

.img_album {
	height: 90px;
	width: 120px;
}
</style>
</head>
<body>

	<c:if test="${not empty searchResults}">

		<table border="1" class="album-table">
			<thead>
				<tr>
					<th>앨범</th>
					<th>제목</th>
					<th>Add to Playlist</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="song" items="${searchResults}">
					<tr>
						<td class="img_album"><img src="${song.thumbnailUrl}"
							alt="Thumbnail" /></td>
						<td>${song.music_Name}</td>
						<td>
							<!-- 드롭다운 선택과 곡 정보를 포함한 AJAX로 추가 -->
							<select class="playlist-select" 
									data-video-url="${song.videoUrl}"
									data-music-name="${song.music_Name}" 
									data-artist="${song.artist}"
									data-thumbnail-url="${song.thumbnailUrl}">
								<c:forEach var="playlist" items="${userPlaylists}">
									<option value="${playlist.playlistId}">${playlist.playlistName}</option>
								</c:forEach>
							</select>
							<button class="add-to-playlist">플레이리스트에 추가</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<c:if test="${empty searchResults}">
		<p>No search results found.</p>
	</c:if>

	<!-- 스크립트 부분 -->
	<script>
    $(document).ready(function() {
        // 기존 모달 열기 기능
        $(document).on('click', '.openModal', function() {
            var trackName = $(this).data('track');
            var artistName = $(this).data('artist');

            // AJAX로 YouTube 검색 페이지를 모달에 로드
            $.ajax({
                url: '${root}/youtubeSearch',  // 검색 요청을 보낼 경로
                type: 'GET',
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: {
                    query: artistName + " " + trackName
                },
                success: function(response) {
                    // YouTube 검색 결과를 모달에 표시
                    $('#modalContent').html(response); 
                    $('.modal-wrapper').addClass('open');
                },
                error: function() {
                    alert('Failed to load search results.');
                }
            });
        });

        // 모달 닫기 기능
        $(document).on('click', '.btn-close', function() {
            $('.modal-wrapper').removeClass('open');
        });

        // 새로운 추가: 플레이리스트에 곡 추가하는 AJAX 요청
        $(document).on('click', '.add-to-playlist', function() {
            var $row = $(this).closest('tr');  // 버튼이 있는 행을 찾음
            var playlistId = $row.find('.playlist-select').val();  // 선택된 플레이리스트 ID
            var videoUrl = $row.find('.playlist-select').data('video-url');
            var musicName = $row.find('.playlist-select').data('music-name');
            var artist = $row.find('.playlist-select').data('artist');
            var thumbnailUrl = $row.find('.playlist-select').data('thumbnail-url');

            // AJAX 요청으로 데이터 전송
            $.ajax({
                url: '${root}/playlist/addToPlaylist',  // 서버에 보낼 URL
                type: 'POST',
                data: {
                    playlistId: playlistId,
                    videoUrl: videoUrl,
                    music_Name: musicName,
                    artist: artist,
                    thumbnailUrl: thumbnailUrl
                },
                success: function(response) {
                    // 성공적으로 추가되었을 경우
                    Swal.fire({
                icon: 'success',
                title: '플레이리스트에 추가되었어요',
                text: '',
                background: '#3A3A3A',
                color: '#fff',
                confirmButtonColor: '#1db954',
                confirmButtonText: '확인'
            });
                    $('.modal-wrapper').removeClass('open');  // 모달 닫기
                },
                error: function() {
                	Swal.fire({
                        icon: 'error',
                        title: '플레이리스트에 추가할 수 없어요',
                        text: '',
                        background: '#3A3A3A',
                        color: '#fff',
                        confirmButtonColor: '#1db954',
                        confirmButtonText: '확인'
                    });
                }
            });
        });
    });
    </script>
</body>
</html>

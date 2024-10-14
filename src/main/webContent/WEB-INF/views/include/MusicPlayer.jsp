<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YouTube Music Player</title>
</head>
<body>

    <!-- 플레이어가 보이도록 width, height 설정 -->
    <iframe id="player" type="text/html" width="640" height="390"
        src="https://www.youtube.com/embed/M7lc1UVf-VE?enablejsapi=1"
        frameborder="0"></iframe>

    <!-- 재생, 일시정지, 다음, 이전 버튼 -->
    <button id="play-button">Play</button>
    <button id="pause-button">Pause</button>
    <button id="next-button">Next</button>
    <button id="previous-button">Previous</button>

    <!-- URL에서 비디오 ID를 추출하고 동영상을 재생하는 input 필드와 버튼 -->
    <input type="text" id="video-url" placeholder="YouTube URL 입력">
    <button id="load-video">Load Video</button>

    <!-- YouTube Iframe API 로드 -->
    <script src="https://www.youtube.com/iframe_api"></script>
    
    <!-- JavaScript 코드 -->
    <script>
        var player;

        // YouTube Iframe API가 로드되면 호출되는 함수
        function onYouTubeIframeAPIReady() {
            player = new YT.Player('player', {
                height: '390',
                width: '640',
                videoId: 'M7lc1UVf-VE', // 초기 비디오 ID
                events: {
                    'onReady': onPlayerReady,
                    'onStateChange': onPlayerStateChange
                }
            });
        }

        // 플레이어가 준비되면 버튼 이벤트 리스너 등록
        function onPlayerReady(event) {
            document.getElementById("play-button").addEventListener("click", function() {
                player.playVideo();
            });

            document.getElementById("pause-button").addEventListener("click", function() {
                player.pauseVideo();
            });

            document.getElementById("load-video").addEventListener("click", function() {
                var videoUrl = document.getElementById("video-url").value;
                var videoId = getYouTubeVideoId(videoUrl);
                if (videoId) {
                    player.loadVideoById(videoId);
                } else {
                    alert("유효한 YouTube URL을 입력해주세요.");
                }
            });
        }

        // 플레이어 상태 변경 이벤트 핸들러
        function onPlayerStateChange(event) {
            if (event.data == YT.PlayerState.ENDED) {
                alert("동영상이 끝났습니다.");
            }
        }

        // URL에서 비디오 ID를 추출하는 함수
        function getYouTubeVideoId(url) {
            var videoId = null;
            try {
                var urlObj = new URL(url);

                // 일반적인 YouTube URL에서 v 파라미터로부터 비디오 ID 추출
                if (urlObj.searchParams.has('v')) {
                    videoId = urlObj.searchParams.get('v');
                }
                // embed 형식의 URL에서 비디오 ID 추출
                else if (urlObj.pathname.includes('/embed/')) {
                    videoId = urlObj.pathname.split('/embed/')[1];
                }
            } catch (error) {
                console.error("유효하지 않은 URL 형식입니다.");
            }
            return videoId;
        }
    </script>

</body>
</html>

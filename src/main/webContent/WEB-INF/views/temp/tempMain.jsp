<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>YouTube Music Player</title>

    <!-- Font Awesome 아이콘을 사용하기 위한 CDN -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    
    <!-- 간단한 CSS 스타일링 -->
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background-color: #f0f0f0;
        }

        /* 플레이어 컨테이너 */
        .player-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        /* 재생 버튼 스타일 */
        .player-buttons {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        .player-buttons button {
            background-color: #4caf50;
            border: none;
            color: white;
            padding: 10px 20px;
            margin: 0 10px;
            border-radius: 50%;
            font-size: 18px;
            cursor: pointer;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }

        .player-buttons button:hover {
            background-color: #45a049;
            transform: scale(1.1);
        }

        .player-buttons button:focus {
            outline: none;
        }

        /* 비디오 URL 입력 및 로드 버튼 */
        .video-input {
            margin-top: 20px;
        }

        #video-url {
            padding: 10px;
            width: 300px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        #load-video {
            padding: 10px 20px;
            background-color: #2196f3;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-left: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        #load-video:hover {
            background-color: #1976d2;
        }

        iframe {
            margin-top: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>

    <div class="player-container">
        <!-- YouTube Iframe -->
        <iframe id="player" type="text/html" width="360" height="240"
            src="https://www.youtube.com/embed/hRzsvFtOo1A?&controls=0&startSeconds=65"
            frameborder="0"></iframe>

        <!-- 재생/일시정지, 다음, 이전 버튼 -->
        <div class="player-buttons">
            <button id="toggle-play-pause"><i class="fas fa-play"></i></button> <!-- 재생/일시정지 토글 버튼 -->
            <button id="previous-button"><i class="fas fa-backward"></i></button>
            <button id="next-button"><i class="fas fa-forward"></i></button>
        </div>

        <!-- 비디오 URL 입력 필드 및 로드 버튼 -->
        <div class="video-input">
            <input type="text" id="video-url" placeholder="YouTube URL 입력">
            <button id="load-video">Load Video</button>
        </div>
    </div>

    <!-- YouTube Iframe API 로드 -->
    <script src="https://www.youtube.com/iframe_api"></script>
    
    <!-- JavaScript 코드 -->
    <script>
        var player;
        var playlist = ['M7lc1UVf-VE', 'LXb3EKWsInQ', 'ScMzIvxBSi4']; // 임시로 사용할 비디오 ID 목록
        var currentIndex = 0;
        var isPlaying = false; // 재생 상태를 추적하는 변수
        var isMuted = true; // 음소거 상태를 추적하는 변수

        // YouTube Iframe API가 로드되면 호출되는 함수
        function onYouTubeIframeAPIReady() {
            player = new YT.Player('player', {
                height: '20',
                width: '20',
                videoId: playlist[currentIndex], // 초기 비디오 ID
                playerVars: {
                    autoplay: 1, // 자동 재생
                    mute: 0     // 음소거 상태로 시작
                },
                events: {
                    'onReady': onPlayerReady,
                    'onStateChange': onPlayerStateChange
                }
            });
        }

        // 플레이어가 준비되면 버튼 이벤트 리스너 등록
        function onPlayerReady(event) {
            document.getElementById("toggle-play-pause").addEventListener("click", function() {
                togglePlayPause(); // 재생/일시정지 토글
            });

            document.getElementById("next-button").addEventListener("click", function() {
                playNextSong(); // 다음 곡 재생
            });

            document.getElementById("previous-button").addEventListener("click", function() {
                playPreviousSong(); // 이전 곡 재생
            });

            document.getElementById("load-video").addEventListener("click", function() {
                var videoUrl = document.getElementById("video-url").value;
                var videoId = getYouTubeVideoId(videoUrl);
                if (videoId) {
                    player.loadVideoById(videoId); // 입력한 비디오 ID 로드
                    isPlaying = true;
                    updatePlayPauseButton(); // 아이콘 갱신
                } else {
                    alert("유효한 YouTube URL을 입력해주세요.");
                }
            });
        }

        // 재생/일시정지 토글 기능
        function togglePlayPause() {
            if (isPlaying) {
                player.pauseVideo(); // 일시정지
                isPlaying = false;
            } else {
                if (isMuted) {
                    player.unMute(); // 음소거 해제
                    isMuted = false;
                }
                player.playVideo(); // 재생
                isPlaying = true;
            }
            updatePlayPauseButton(); // 버튼 아이콘을 상태에 따라 변경
        }

        // 재생 상태에 따라 버튼 아이콘을 업데이트
        function updatePlayPauseButton() {
            var button = document.getElementById("toggle-play-pause");
            button.innerHTML = isPlaying ? '<i class="fas fa-pause"></i>' : '<i class="fas fa-play"></i>';
        }

        // 플레이어 상태 변경 이벤트 핸들러
        function onPlayerStateChange(event) {
            if (event.data == YT.PlayerState.ENDED) {
                playNextSong(); // 동영상이 끝나면 자동으로 다음 곡 재생
            } else if (event.data == YT.PlayerState.PLAYING) {
                isPlaying = true;
                updatePlayPauseButton(); // 재생 중일 때 아이콘을 일시정지로 변경
            } else if (event.data == YT.PlayerState.PAUSED) {
                isPlaying = false;
                updatePlayPauseButton(); // 일시정지 중일 때 아이콘을 재생으로 변경
            }
        }

        // 다음 곡 재생
        function playNextSong() {
            currentIndex = (currentIndex + 1) % playlist.length; // 다음 곡으로 이동
            player.loadVideoById(playlist[currentIndex]);
            isPlaying = true;
            updatePlayPauseButton(); // 다음 곡 재생 시 아이콘 업데이트
        }

        // 이전 곡 재생
        function playPreviousSong() {
            currentIndex = (currentIndex - 1 + playlist.length) % playlist.length; // 이전 곡으로 이동
            player.loadVideoById(playlist[currentIndex]);
            isPlaying = true;
            updatePlayPauseButton(); // 이전 곡 재생 시 아이콘 업데이트
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

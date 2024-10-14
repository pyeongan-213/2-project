// YouTube Iframe API를 비동기로 로드
function onYouTubeIframeAPIReady() {
    // 플레이어 객체 생성
    player = new YT.Player('player', {
        videoId: 'M7lc1UVf-VE', // 초기 재생할 비디오 ID
        events: {
            'onReady': onPlayerReady,
            'onStateChange': onPlayerStateChange
        }
    });
}

// 플레이어가 준비되면 호출되는 함수
function onPlayerReady(event) {
    // 버튼에 이벤트 리스너 추가
    document.getElementById("play-button").addEventListener("click", function() {
        player.playVideo();
    });

    document.getElementById("pause-button").addEventListener("click", function() {
        player.pauseVideo();
    });

    document.getElementById("next-button").addEventListener("click", function() {
        // 다음 곡 로직 - 다음 비디오 ID로 변경
        playNextSong('다음_비디오_ID');
    });

    document.getElementById("previous-button").addEventListener("click", function() {
        // 이전 곡 로직 - 이전 비디오 ID로 변경
        playNextSong('이전_비디오_ID');
    });
}

// 플레이어 상태 변경 이벤트
function onPlayerStateChange(event) {
    if (event.data == YT.PlayerState.ENDED) {
        // 비디오가 끝나면 다음 곡 자동 재생
        playNextSong('다음_비디오_ID');
    }
}

// 비디오 ID를 변경하여 곡 재생
function playNextSong(videoId) {
    player.loadVideoById(videoId);
}

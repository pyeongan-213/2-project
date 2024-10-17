let player;
let isPlaying = false;
let duration = 0;

// YouTube Iframe API 로드 후 player 객체 생성
function onYouTubeIframeAPIReady() {
    player = new YT.Player('musicPlayer', {
        events: {
            'onReady': onPlayerReady,
            'onStateChange': onPlayerStateChange
        }
    });
}

// 플레이어 준비 후 재생시간 및 이벤트 설정
function onPlayerReady(event) {
    duration = player.getDuration();
    $('#durationTime').text(formatTime(duration));

    // 재생바 및 시간 업데이트
    setInterval(() => {
        if (isPlaying) {
            const currentTime = player.getCurrentTime();
            $('#currentTime').text(formatTime(currentTime));
            $('#seekBar').val((currentTime / duration) * 100);
        }
    }, 1000);

    // 재생바 조작 시 비디오 이동
    $('#seekBar').on('input', function() {
        const seekToTime = (this.value / 100) * duration;
        player.seekTo(seekToTime);
    });
}

// 재생/일시정지 버튼 처리
$('#playPauseBtn').on('click', function() {
    if (isPlaying) {
        player.pauseVideo();
    } else {
        player.playVideo();
    }
    isPlaying = !isPlaying;
    updatePlayPauseIcon();
});

function updatePlayPauseIcon() {
    if (isPlaying) {
        $('#playPauseBtn i').removeClass('bi-play-fill').addClass('bi-pause-fill');
    } else {
        $('#playPauseBtn i').removeClass('bi-pause-fill').addClass('bi-play-fill');
    }
}

// 포맷 시간 함수 (초를 MM:SS 형식으로 변환)
function formatTime(time) {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
}

// 플레이어 상태 변경 이벤트 처리
function onPlayerStateChange(event) {
    if (event.data === YT.PlayerState.PLAYING) {
        isPlaying = true;
        duration = player.getDuration();
        $('#durationTime').text(formatTime(duration));
    } else {
        isPlaying = false;
    }
}

// 이전, 다음, 셔플 버튼들에 대한 기능 추가
$('#prevBtn').on('click', function() {
    // 이전 곡으로 넘어가는 로직 작성
});

$('#nextBtn').on('click', function() {
    // 다음 곡으로 넘어가는 로직 작성
});

$('#shuffleBtn').on('click', function() {
    // 셔플 기능 로직 작성
});

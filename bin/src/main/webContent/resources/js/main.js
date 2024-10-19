$(function () {
    // 사이드바 링크 클릭 시 활성화 클래스 적용
    $(".sidebar-link").click(function () {
        $(".sidebar-link").removeClass("is-active");
        $(this).addClass("is-active");
    });
});

// 비디오 관련 이벤트 처리
const allVideos = document.querySelectorAll(".video");

allVideos.forEach((v) => {
    v.addEventListener("mouseover", () => {
        const video = v.querySelector("video");
        video.play();
    });
    v.addEventListener("mouseleave", () => {
        const video = v.querySelector("video");
        video.pause();
    });
});

// 메인 컨테이너 처리 및 비디오 클릭 이벤트
$(function () {
    // 로고 및 디스커버 클릭 시 메인 컨테이너 숨김 처리
    $(".logo, .logo-expand, .discover").on("click", function (e) {
        $(".main-container").removeClass("show");
        $(".main-container").scrollTop(0);
    });

    // 트렌딩 또는 비디오 클릭 시 메인 컨테이너 표시
    $(".trending, .video").on("click", function (e) {
        $(".main-container").addClass("show");
        $(".main-container").scrollTop(0);
        $(".sidebar-link").removeClass("is-active");
        $(".trending").addClass("is-active");
    });

    // 비디오 클릭 시 상세 정보 업데이트
    $(".video").click(function () {
        var source = $(this).find("source").attr("src");
        var title = $(this).find(".video-name").text();
        var person = $(this).find(".video-by").text();
        var img = $(this).find(".author-img").attr("src");
        $(".video-stream video").stop();
        $(".video-stream source").attr("src", source);
        $(".video-stream video").load();
        $(".video-p-title").text(title);
        $(".video-p-name").text(person);
        $(".video-detail .author-img").attr("src", img);
    });
});

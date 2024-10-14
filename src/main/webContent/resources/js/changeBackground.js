// 변경할 배경 이미지 배열
const images = [
    "./../img/커플기타연주.jpg",
    "./../img/고양이와헤드셋여자아이.jpg",    
    "./../img/6875483.jpg"   
];

let currentIndex = 0;  // 현재 이미지 인덱스

// 배경 이미지를 변경하는 함수
const changeBackground = () => {
    document.body.classList.remove("fade-in"); // 기존 클래스 제거
    void document.body.offsetWidth; // reflow to restart animation
    document.body.style.backgroundImage = `url('${images[currentIndex]}')`; // 새로운 배경 이미지 설정
    document.body.classList.add("fade-in"); // 페이드 인 클래스 추가
    currentIndex = (currentIndex + 1) % images.length;  // 이미지 배열을 순환
};

// 30초마다 배경 이미지 변경 (30초 = 30000ms)
setInterval(changeBackground, 20000);

// 페이지 로드 시 첫 이미지 설정
changeBackground();

// 화면 크기에 따라 배경 이미지 크기 조절
const resizeBackground = () => {
    if (window.innerWidth < 800) {
        document.body.style.backgroundSize = 'contain';  // 작은 화면에서는 이미지가 잘리지 않도록
    } else {
        document.body.style.backgroundSize = 'cover';    // 큰 화면에서는 꽉 차게 표시
    }
};

// 화면 크기가 변경될 때마다 호출
window.addEventListener('resize', resizeBackground);

// 페이지 로드 시 초기 설정
resizeBackground();

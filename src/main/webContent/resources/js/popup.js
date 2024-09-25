// DOM 로드 후 이벤트 리스너 추가
document.addEventListener('DOMContentLoaded', () => {
    // 월간결제 라디오 버튼 클릭 시 팝업 열기
    document.getElementById('monthly').addEventListener('click', () => {
        openPopup('monthly-popup');
    });

    // 연간결제 라디오 버튼 클릭 시 팝업 열기
    document.getElementById('yearly').addEventListener('click', () => {
        openPopup('yearly-popup');
    });

    // 팝업 닫기 버튼에 이벤트 추가
    document.querySelectorAll('.popup button').forEach(button => {
        button.addEventListener('click', (event) => {
            const popupId = event.target.closest('.popup').id;
            closePopup(popupId);
        });
    });
});

// 팝업 열기 함수
function openPopup(popupId) {
    const popups = document.querySelectorAll('.popup');
    popups.forEach(popup => {
        popup.classList.remove('active');  // 모든 팝업 닫기
    });
    document.getElementById(popupId).classList.add('active');  // 선택된 팝업 열기
}

// 팝업 닫기 함수
function closePopup(popupId) {
    const popup = document.getElementById(popupId);
    popup.classList.remove('active');  // 팝업 닫기
}

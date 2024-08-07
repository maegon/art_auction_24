/*이미지 돋보기 기능 시작*/
$(document).ready(function(){
            $('.auctionProductTarget').okzoom({
                width: 150,
                height: 150,
                scaleWidth: 400,
                round: true,
                background: "#fff",
                // backgroundRepeat: "repeat",
                shadow: "0 0 5px #000",
                border: "1px solid black"
                });
})
/*이미지 돋보기 기능 끝*/

/*정보 부분 fixed 기능 시작*/
window.addEventListener('scroll', function() {
    const infoSection = document.getElementById('infoSection');
    const container = document.getElementById('container');
    const scrollPosition = window.scrollY || window.pageYOffset;
    const triggerPoint = infoSection.offsetTop;
    const maxScrollPosition = container.offsetTop + container.offsetHeight - infoSection.offsetHeight;

    // 모바일 환경에서는 fixed-info-section 클래스를 제거
    if (window.innerWidth < 1024) {
        infoSection.classList.remove('fixed-info-section');
        return;
    }

    // 스크롤 제한 및 fixed-info-section 클래스 추가/제거
    if (scrollPosition > triggerPoint && scrollPosition < maxScrollPosition && scrollPosition < 1000) {
        infoSection.classList.add('fixed-info-section');
    } else {
        infoSection.classList.remove('fixed-info-section');
    }

    // 스크롤 위치가 1000px 이하로 제한되도록 설정
    if (scrollPosition > 1000) {
        window.scrollTo(0, 1000);
    }
});

/*정보 부분 fixed 기능 끝*/
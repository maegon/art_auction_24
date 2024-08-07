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

            if (scrollPosition > triggerPoint && scrollPosition < maxScrollPosition && scrollPosition < 1000) {
                infoSection.classList.add('fixed-info-section');
            } else {
                infoSection.classList.remove('fixed-info-section');
            }
        });
/*정보 부분 fixed 기능 끝*/
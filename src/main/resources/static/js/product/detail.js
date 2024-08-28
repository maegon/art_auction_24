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
    const productDetailContainer = document.getElementById('productDetailContainer');
    const scrollPosition = window.scrollY || window.pageYOffset;
    const triggerPoint = infoSection.offsetTop;
    const maxScrollPosition = productDetailContainer.offsetTop + productDetailContainer.offsetHeight - infoSection.offsetHeight;

    // 모바일 환경에서는 fixed-info-section 클래스를 제거
    if (window.innerWidth < 1024) {
        infoSection.classList.remove('fixed-info-section');
        return;
    }

    // 스크롤 제한 및 fixed-info-section 클래스 추가/제거
    if (scrollPosition > triggerPoint && scrollPosition < maxScrollPosition) {
        infoSection.classList.add('fixed-info-section');
    } else {
        infoSection.classList.remove('fixed-info-section');
    }


});

/*정보 부분 fixed 기능 끝*/

/*삭제 확인 요청 메세지 호출 start*/
const delete_elements = document.getElementsByClassName("productDetail-delete");
Array.from(delete_elements).forEach(function(element) {
    element.addEventListener('click', function() {
        if(confirm("정말로 삭제하시겠습니까?")) {
            location.href = this.dataset.uri;
        };
    });
});
/*삭제 확인 요청 메세지 호출 end*/

var swiper1 = new Swiper(".mySwiper", {
      spaceBetween: 30,
      pagination: {
        el: ".swiper-pagination",
        clickable: true,
      },
    });

/*ongoing js start*/
 var swiper2 = new Swiper(".ongoingAuc", {

    slidesPerView: 1,
    spaceBetween: 10,
    pagination: {
      el: ".ongoing-pagination",
      clickable: true,
    },
    // 반응형 breakpoints : px 기준
    breakpoints: {
      // window 넓이 640px ~ 767px
      640: {
        slidesPerView: 2,
        spaceBetween: 40,
      },
      // window 넓이 768px ~ 1023px
      768: {
        slidesPerView: 3,
        spaceBetween: 60,
      },
      // window 넓이 1024px ~
      1024: {
        slidesPerView: 4,
        spaceBetween: 80,
      },
    },
    });


/*ongoing js end*/

/*upcoming js start*/
 var swiper3 = new Swiper(".upcomingAuc", {

    slidesPerView: 1,
    spaceBetween: 10,
    pagination: {
      el: ".upcoming-pagination",
      clickable: true,
    },
    // 반응형 breakpoints : px 기준
    breakpoints: {
      // window 넓이 640px ~ 767px
      640: {
        slidesPerView: 2,
        spaceBetween: 40,
      },
      // window 넓이 768px ~ 1023px
      768: {
        slidesPerView: 3,
        spaceBetween: 60,
      },
      // window 넓이 1024px ~
      1024: {
        slidesPerView: 4,
        spaceBetween: 80,
      },
    },
    });


/*upcoming js end*/

/*guide js start*/
var $owl = $('.loop');

$owl.owlCarousel({
    autoplay: true,
    autoplayHoverPause: true,
    autoplayTimeout: 3000,
    autoplaySpeed: 800,
    center: true,
    items: 1.4,
    stagePadding: 15,
    loop: true,
    margin: 15,
  animateOut: 'slide-up',
animateIn: 'slide-down',
});
/*guide js end*/

/*current bid 단위 표시 start*/
document.addEventListener('DOMContentLoaded', function() {
        const currentBidElement = document.getElementById('currentBid');
        const currentBidText = currentBidElement.textContent.trim(); // 공백 제거
        console.log("Original text content:", currentBidText); // 원본 텍스트 확인

        // 숫자 추출 및 변환
        const currentBid = parseInt(currentBidText.replace(/,/g, ''), 10); // ',' 제거하고 숫자로 변환
        console.log("Parsed current bid:", currentBid); // 숫자 변환 결과 확인

        // 변환된 숫자가 유효한지 확인
        if (!isNaN(currentBid)) {
            currentBidElement.textContent = '₩ ' + currentBid.toLocaleString('ko-KR'); // 천 단위 구분 기호 추가
        } else {
            console.error("Invalid bid value:", currentBidText); // 잘못된 값 오류 처리
        }
    });
/*current bid 단위 표시 end*/
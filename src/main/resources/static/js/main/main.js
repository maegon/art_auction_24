var swiper1 = new Swiper(".mySwiper", {
  spaceBetween: 30, // 슬라이드 사이의 간격
  pagination: {
    el: ".swiper-pagination", // 페이지네이션 요소
    clickable: true, // 페이지네이션 클릭 가능
  },
  loop: true, // 무한 루프 설정
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

/*up animation js start*/


/*up animation js end*/


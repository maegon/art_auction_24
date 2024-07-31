// JavaScript 파일 또는 스크립트 태그 내
document.addEventListener('DOMContentLoaded', function() {
  const scrollTopBtn = document.querySelector('.btn-scroll_top');

  window.addEventListener('scroll', function() {
    if (window.scrollY > 100) { // 100px 이상 스크롤 시 버튼을 표시
      scrollTopBtn.classList.add('show');
    } else { // 상단으로 오면 버튼을 숨김
      scrollTopBtn.classList.remove('show');
    }
  });
});

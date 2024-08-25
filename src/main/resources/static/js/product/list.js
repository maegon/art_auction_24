
document.addEventListener('DOMContentLoaded', () => {
    const productPageLinks = document.querySelectorAll('.productList-page-link');

    productPageLinks.forEach(link => {
        link.addEventListener('click', function() {
            // 클릭된 링크에 'data-page'가 없는 경우 클릭 이벤트를 무시
            const pageNumber = this.getAttribute('data-page');
            if (pageNumber === null) return; // 링크가 비활성화된 경우 아무 동작도 하지 않음

            // 모든 페이지 링크에서 active 클래스 제거
            productPageLinks.forEach(l => l.classList.remove('productList-active', 'text-white', 'bg-black', 'border-black'));

            // 클릭된 링크에 active 클래스 추가
            this.classList.add('productList-active', 'text-white', 'bg-black', 'border-black');

            // 현재 URL 설정 및 페이지 번호 추가
            const currentUrl = new URL(window.location.href);
            currentUrl.searchParams.set('page', pageNumber);  // 'page' 파라미터 설정

            // 새로운 URL로 이동
            window.location.href = currentUrl.toString();     // 새로운 URL로 리다이렉트
        });
    });
});



/* list 검색 부분 시작 */

$(document).ready(function () {
    // 페이지 링크 클릭 시 페이지 번호 설정 후 폼 제출


       // 경매 작품만 보기 버튼 클릭 시 토글 및 텍스트 변경
       $("#auctionOnlyButton").on("click", function () {
           const auctionInput = $("#auctionInput");
           const isChecked = auctionInput.val() === 'true';

           // 토글 상태 및 텍스트 업데이트
           auctionInput.val(isChecked ? 'false' : 'true');
           $(this).find('span').text(isChecked ? '경매 작품만 보기' : '전체보기');

           // 검색 폼 제출
           $("#product-searchForm").submit();
       });

       // 페이지 로드 시 경매 작품만 보기 버튼 상태 초기화
       const auctionInputValue = $("#auctionInput").val();
       if (auctionInputValue === 'true') {
           $("#auctionOnlyButton").find('span').text('전체보기');
       } else {
           $("#auctionOnlyButton").find('span').text('경매 작품만 보기');
       }


});

/* list 검색 부분 끝 */

/* list 드롭다운 메뉴 부분 시작 */
document.addEventListener('DOMContentLoaded', function () {
    const sortButton = document.getElementById('product-sortButton');
    const sortDropdown = document.getElementById('product-sortDropdown');
    const sortText = document.getElementById('product-sortText');
    const sortInput = document.getElementById('product-sortInput');
    const searchForm = document.getElementById('product-searchForm');
    const sortLinks = sortDropdown.querySelectorAll('a');

    // 페이지 로드 시 정렬 옵션 버튼 텍스트 설정
    const sortOptions = {
        'price-asc': '가격 낮은 순',
        'price-desc': '가격 높은 순',
        'latest': '최신 순'
    };

    const currentSort = sortInput.value || '정렬하기';
    sortText.textContent = sortOptions[currentSort] || '정렬하기';

    sortButton.addEventListener('click', function () {
        sortDropdown.classList.toggle('hidden');
    });

    sortLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const sortOption = this.getAttribute('data-sort');
            const sortOptionText = this.textContent;

            // 버튼 텍스트 업데이트
            sortText.textContent = sortOptionText;

            // 숨겨진 입력 필드 업데이트 및 폼 제출
            sortInput.value = sortOption;
            searchForm.submit();
        });
    });

    // 드롭다운 외 클릭 시 닫기
    document.addEventListener('click', function (e) {
        if (!sortButton.contains(e.target) && !sortDropdown.contains(e.target)) {
            sortDropdown.classList.add('hidden');
        }
    });
});
/* list 드롭다운 메뉴 부분 끝 */

/*버튼 기능 수정 시작*/

/*버튼 기능 수정 끝*/
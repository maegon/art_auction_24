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
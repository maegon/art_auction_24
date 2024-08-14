/* list 검색 부분 시작 */

$(document).ready(function () {
    // 페이지 링크 클릭 시 페이지 번호 설정 후 폼 제출
       $(".page-link").on("click", function () {
           $("#page").val($(this).data("page"));
           $("#auction-searchForm").submit();
       });

});

/* list 검색 부분 끝 */

/* list 드롭다운 메뉴 부분 시작 */
document.addEventListener('DOMContentLoaded', function () {
    const sortButton = document.getElementById('auction-sortButton');
    const sortDropdown = document.getElementById('auction-sortDropdown');
    const sortText = document.getElementById('auction-sortText');
    const sortInput = document.getElementById('auction-sortInput');
    const searchForm = document.getElementById('auction-searchForm');
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

/*category 정렬 시작*/
document.addEventListener('DOMContentLoaded', function() {
    const categoryLinks = document.querySelectorAll('.AuctionListCategory-link');

    categoryLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault(); // 기본 링크 클릭 동작 방지

            // 데이터 속성 읽기
            const auctionName = this.dataset.category;
            console.log('Selected category:', auctionName); // 콘솔에서 값 확인

            // URL 생성 및 페이지 이동
            const encodedCategory = encodeURIComponent(auctionName);
            const url = `/auction/list?kw=&sort=latest&viewAll=false&auctionName=${encodedCategory}`;
            window.location.href = url;
        });
    });
});

/*category 정렬 끝*/
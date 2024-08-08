/* list 검색 부분 시작 */

$(document).ready(function () {
    // 페이지 링크 클릭 시 페이지 번호 설정 후 폼 제출
       $(".page-link").on("click", function () {
           $("#page").val($(this).data("page"));
           $("#auction-searchForm").submit();
       });

       // 검색 버튼 클릭 시 검색어와 페이지 번호를 초기화 후 폼 제출
       $("#btn_search").on("click", function () {
           $("#kw").val($("#search_kw").val());
           $("#page").val(0);
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

/*list 검색 부분 시작*/

 $(document).ready(function () {
            $(".page-link").on("click", function () {
                $("#page").val($(this).data("page"));
                $("#searchForm").submit();
            });

            $("#btn_search").on("click", function () {
                $("#kw").val($("#search_kw").val());
                $("#page").val(0);
                $("#searchForm").submit();
            });
        });

/*list 검색 부분 끝*/


/*list 드롭다운 메뉴 부분 시작*/
  document.addEventListener('DOMContentLoaded', function () {
        const sortButton = document.getElementById('sortButton');
        const sortDropdown = document.getElementById('sortDropdown');
        const sortText = document.getElementById('sortText');
        const sortInput = document.getElementById('sortInput');
        const searchForm = document.getElementById('searchForm');
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

                // Update the button text
                sortText.textContent = sortOptionText;

                // Update hidden input and submit form
                sortInput.value = sortOption;
                searchForm.submit();
            });
        });

        // Close dropdown if clicked outside
        document.addEventListener('click', function (e) {
            if (!sortButton.contains(e.target) && !sortDropdown.contains(e.target)) {
                sortDropdown.classList.add('hidden');
            }
        });
    });
/*list 드롭다운 메뉴 부분 끝*/
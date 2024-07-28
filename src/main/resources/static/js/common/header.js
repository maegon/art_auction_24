function toggleMenu() {
    document.querySelector('.nav__main').classList.toggle('active');
    document.querySelector('.nav__login').classList.toggle('active');
    document.querySelector('.header_nav').classList.toggle('active');
}



/*$(document).ready(function() {
    $('#search_button').click(function() {
        var $searchInput = $('#search_input');
        var $searchContainer = $('#search_container');

        $searchInput.toggleClass('show');
        $searchInput.focus(); // 검색창이 보일 때 포커스 설정

        // `show` 클래스에 따라 `border-bottom`을 추가하거나 제거
        if ($searchInput.hasClass('show')) {
            $searchContainer.css('border-bottom', '1px solid black');
        } else {
            $searchContainer.css('border-bottom', 'none');
        }
    });
});*/

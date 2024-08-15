
document.addEventListener('DOMContentLoaded', function() {
    // 드롭다운 관련 설정
    const profileElement = document.getElementById('header_profile');
    const dropdownContent = document.getElementById('profileDropdown');

    profileElement.addEventListener('click', function(event) {
        event.preventDefault();
        event.stopPropagation();
        dropdownContent.style.display = dropdownContent.style.display === 'block' ? 'none' : 'block';
    });

    // 드롭다운 외부를 클릭하면 닫히도록 설정
    window.addEventListener('click', function(event) {
        if (!profileElement.contains(event.target)) {
            dropdownContent.style.display = 'none';
        }
    });

    // 네비게이션 메뉴 토글 함수
    function toggleNavigationMenu() {
        document.querySelector('.nav__main').classList.toggle('header_active');
        document.querySelector('.nav__login').classList.toggle('header_active');
        document.querySelector('.header_nav').classList.toggle('header_active');
    }

    // 메뉴 버튼 클릭 시 네비게이션 메뉴 토글
    document.querySelector('.nav__bar').addEventListener('click', toggleNavigationMenu);
});


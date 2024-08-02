function toggleMenu() {
    document.querySelector('.nav__main').classList.toggle('active');
    document.querySelector('.nav__login').classList.toggle('active');
    document.querySelector('.header_nav').classList.toggle('active');
}


 document.addEventListener('DOMContentLoaded', function() {
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
        });


document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.querySelector('.nav__bar');
    const navMain = document.querySelector('.nav__main');
    const navLogin = document.querySelector('.nav__login');

    toggleBtn.addEventListener('click', () => {
        navMain.classList.toggle('active');
        navLogin.classList.toggle('active');
    });
});

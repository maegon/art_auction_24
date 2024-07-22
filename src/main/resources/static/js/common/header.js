document.addEventListener('DOMContentLoaded', () => {
    const overlay = document.getElementById('overlay');
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');

    searchBtn.addEventListener('click', () => {
        searchInput.classList.toggle('visible');
    });

    overlay.addEventListener('click', () => {
        loginForm.classList.remove('visible');
        setTimeout(() => {
            loginForm.classList.add('hidden');
            overlay.classList.add('hidden');
        }, 500);
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const colors = ['#FF38A9', '#67F0E0'];
    const randomColor = colors[Math.floor(Math.random() * colors.length)];
    const drop = document.querySelector('.drop');
    drop.style.backgroundColor = randomColor;

    window.onload = function() {
        const loader = document.querySelector('.loader');
        const drop = document.querySelector('.drop');

        drop.addEventListener('animationend', (event) => {
            if (event.animationName === 'shapeChange') {
                loader.style.transition = 'opacity 1s ease';
                loader.style.opacity = '0';
                setTimeout(() => {
                    loader.style.display = 'none';
                    document.querySelector('.content-area').style.display = 'block';
                    document.body.classList.add('scrolled');
                }, 500);
            }
        });
        drop.style.opacity = '1';
    };
});
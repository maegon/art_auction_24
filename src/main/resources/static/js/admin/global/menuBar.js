document.addEventListener("DOMContentLoaded", function () {
    const menuToggleBtn = document.querySelector(".admin-manage-menu-toggle-btn");
    const sidebar = document.querySelector(".admin-manage-sidebar");

    menuToggleBtn.addEventListener("click", function (event) {
        event.stopPropagation(); // 이벤트 버블링을 막아서 버튼 클릭 시 닫히지 않도록
        sidebar.classList.toggle("active");
    });

    document.addEventListener("click", function (event) {
        // 사이드바가 열려있고 클릭한 부분이 사이드바와 메뉴 버튼이 아닌 경우
        if (sidebar.classList.contains("active") && !sidebar.contains(event.target) && !menuToggleBtn.contains(event.target)) {
            sidebar.classList.remove("active");
        }
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const loginButton = document.querySelector(".login-submit");
    const usernameInput = document.getElementById("component-input-id");
    const passwordInput = document.getElementById("component-input-password");
    const rememberMeCheckbox = document.getElementById("saveId");

    const savedUsername = localStorage.getItem("savedUsername");

    if (savedUsername) {
        usernameInput.value = savedUsername;
        rememberMeCheckbox.checked = true;
    }

    function updateLoginButtonState() {
        if (usernameInput.value.trim() !== "" && passwordInput.value.trim() !== "") {
            loginButton.removeAttribute("disabled");
        } else {
            loginButton.setAttribute("disabled", "true");
        }
    }

    function handleLogin(event) {
        event.preventDefault();
        if (rememberMeCheckbox.checked) {
            localStorage.setItem("savedUsername", usernameInput.value);
        } else {
            localStorage.removeItem("savedUsername");
        }
    }
    usernameInput.addEventListener("input", updateLoginButtonState);
    passwordInput.addEventListener("input", updateLoginButtonState);
    loginButton.addEventListener("click", handleLogin);

    updateLoginButtonState();
});
document.addEventListener("DOMContentLoaded", function() {
    const loginButton = document.querySelector(".login-submit");
    const loginUsernameInput = document.getElementById("component-input-id");
    const loginPwInput = document.getElementById("component-input-password");
    const rememberMeCheckbox = document.getElementById("saveId");

    const savedUsername = localStorage.getItem("savedUsername");

    if (savedUsername) {
        loginUsernameInput.value = savedUsername;
        rememberMeCheckbox.checked = true;
    }

    function updateLoginButtonState() {
        if (loginUsernameInput.value.trim() !== "" && loginPwInput.value.trim() !== "") {
            loginButton.removeAttribute("disabled");
        } else {
            loginButton.setAttribute("disabled", "true");
        }
    }

    function handleLogin(event) {
        if (rememberMeCheckbox.checked) {
            localStorage.setItem("savedUsername", loginUsernameInput.value);
        } else {
            localStorage.removeItem("savedUsername");
        }
    }
    loginUsernameInput.addEventListener("input", updateLoginButtonState);
    loginPwInput.addEventListener("input", updateLoginButtonState);
    loginButton.addEventListener("click", handleLogin);

    updateLoginButtonState();
});
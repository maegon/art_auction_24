document.addEventListener("DOMContentLoaded", function() {
    const loginButton = document.querySelector(".login-submit");
    const usernameInput = document.getElementById("component-input-id");
    const passwordInput = document.getElementById("component-input-password");
    const rememberMeCheckbox = document.getElementById("remember-me");

    function updateLoginButtonState() {
        if (usernameInput.value.trim() !== "" && passwordInput.value.trim() !== "") {
            loginButton.removeAttribute("disabled");
        } else {
            loginButton.setAttribute("disabled", "true");
        }
    }


    usernameInput.addEventListener("input", updateLoginButtonState);
    passwordInput.addEventListener("input", updateLoginButtonState);
    loginButton.addEventListener("click", handleLogin);

    updateLoginButtonState();
});
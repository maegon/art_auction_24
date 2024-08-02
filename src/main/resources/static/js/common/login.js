document.addEventListener("DOMContentLoaded", function() {
    const loginButton = document.querySelector(".login-submit");
    const component-input-id_Input = document.getElementById("component-input-id");
    const component-input-password_Input = document.getElementById("component-input-password");
    const rememberMeCheckbox = document.getElementById("saveId");

    const savedUsername = localStorage.getItem("savedUsername");

    if (savedUsername) {
        component-input-id_Input.value = savedUsername;
        rememberMeCheckbox.checked = true;
    }

    function updateLoginButtonState() {
        if (component-input-id_Input.value.trim() !== "" && component-input-password_Input.value.trim() !== "") {
            loginButton.removeAttribute("disabled");
        } else {
            loginButton.setAttribute("disabled", "true");
        }
    }

    function handleLogin(event) {
        event.preventDefault();
        if (rememberMeCheckbox.checked) {
            localStorage.setItem("savedUsername", component-input-id_Input.value);
        } else {
            localStorage.removeItem("savedUsername");
        }
    }
    component-input-id_Input.addEventListener("input", updateLoginButtonState);
    component-input-password_Input.addEventListener("input", updateLoginButtonState);
    loginButton.addEventListener("click", handleLogin);

    updateLoginButtonState();
});
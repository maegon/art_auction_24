document.addEventListener("DOMContentLoaded", function() {
    const loginButton = document.querySelector(".login-submit");
    const usernameInput = document.getElementById("component-input-id");
    const passwordInput = document.getElementById("component-input-password");
    const rememberMeCheckbox = document.getElementById("saveId");
    const autoLoginCheckbox = document.getElementById("autoLogin");

    const savedUsername = localStorage.getItem("savedUsername");
    const autoLoginEnabled = localStorage.getItem("autoLoginEnabled");

    if (savedUsername) {
        usernameInput.value = savedUsername;
        rememberMeCheckbox.checked = true;
    }
    if (autoLoginEnabled === 'true') {
        autoLoginCheckbox.checked = true;
        if (usernameInput.value && passwordInput.value) {
            handleLogin();
        }
    }

    function updateLoginButtonState() {
        if (usernameInput.value.trim() !== "" && passwordInput.value.trim() !== "") {
            loginButton.removeAttribute("disabled");
        } else {
            loginButton.setAttribute("disabled", "true");
        }
    }

    function handleLogin(event) {
        event.preventDefault(); // 기본 폼 제출 동작 방지
        if (rememberMeCheckbox.checked) {
            localStorage.setItem("savedUsername", usernameInput.value);
        } else {
            localStorage.removeItem("savedUsername");
        }
        if (autoLoginCheckbox.checked) {
            localStorage.setItem("autoLoginEnabled", 'true');
        } else {
            localStorage.removeItem("autoLoginEnabled");
        }

        const loginData = {
            username: usernameInput.value,
            password: passwordInput.value
        };

        try {
            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log("로그인 성공:", result);
                window.location.href = '/';
            } else {
                const error = await response.json();
                console.error("로그인 실패:", error.message);
                alert("로그인 실패: " + error.message);
            }
        } catch (error) {
            console.error("로그인 중 오류 발생:", error);
            alert("로그인 중 오류가 발생했습니다. 나중에 다시 시도해주세요.");
        }
    }

    usernameInput.addEventListener("input", updateLoginButtonState);
    passwordInput.addEventListener("input", updateLoginButtonState);
    loginButton.addEventListener("click", handleLogin);

    updateLoginButtonState();
});
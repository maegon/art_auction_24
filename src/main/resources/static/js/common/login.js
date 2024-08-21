document.addEventListener("DOMContentLoaded", function() {
    const loginButton = document.querySelector(".login-submit");
    const loginUsernameInput = document.getElementById("component-input-id");
    const loginPwInput = document.getElementById("component-input-password");
    const rememberMeCheckbox = document.getElementById("saveId");
    const accountRestoration = document.getElementById("accountRestoration");

    const savedUsername = localStorage.getItem("savedUsername");


    document.querySelector('.accountRestoration_section').style.display = 'none';

    // '계정을 잊어버리셨나요?' 버튼 클릭 시 계정찾기 페이지 표시
    accountRestoration.addEventListener('click', function() {
        if (!accountRestoration.disabled) {
            document.querySelector('.accountRestoration_section').style.display = 'block';
            document.getElementById('loginForm').style.display = 'none';
        }
    });

    // backLink2 버튼 클릭 시 동작 추가 (계정 찾기 페이지 이전 버튼)
    const backLink2Button = document.querySelector('.backLink2');
    backLink2Button.addEventListener('click', function(event) {
        event.preventDefault(); // 기본 링크 동작을 막음
        document.querySelector('.accountRestoration_section').style.display = 'none';
        document.getElementById('loginForm').style.display = 'block';
    });


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

    // 아이디 찾기, 패스워드 찾기
    document.getElementById('findUsernameForm').addEventListener('submit', function(event) {
        const email = document.getElementById('findUsernameEmail').value;

        if (!validateEmail(email)) {
            event.preventDefault();
            document.getElementById('usernameEmailError').textContent = '유효한 이메일 주소를 입력하세요.';
        } else {
            document.getElementById('usernameEmailError').textContent = '';
        }
    });

    document.getElementById('findPasswordForm').addEventListener('submit', function(event) {
        const username = document.getElementById('findPasswordUsername').value;
        const email = document.getElementById('findPasswordEmail').value;

        if (!username) {
            event.preventDefault();
            document.getElementById('passwordEmailError').textContent = '아이디를 입력하세요.';
        } else if (!validateEmail(email)) {
            event.preventDefault();
            document.getElementById('passwordEmailError').textContent = '유효한 이메일 주소를 입력하세요.';
        } else {
            document.getElementById('passwordEmailError').textContent = '';
        }
    });

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }

});


document.addEventListener("DOMContentLoaded", function() {
    // 약관 토글 버튼
    const togglePrivacyBtn = document.getElementById('togglePrivacyBtn');
    const privacyContent = document.getElementById('privacyContent');

    togglePrivacyBtn.addEventListener('click', function() {
        privacyContent.classList.toggle('hidden');
    });

    // 저작권 토글 버튼
    const toggleCopyrightBtn = document.getElementById('toggleCopyrightBtn');
    const copyrightContent = document.getElementById('copyrightContent');

    toggleCopyrightBtn.addEventListener('click', function() {
        copyrightContent.classList.toggle('hidden');
    });
});
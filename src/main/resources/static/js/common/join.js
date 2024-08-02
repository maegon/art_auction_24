document.addEventListener("DOMContentLoaded", function() {
    const phoneNumberInput = document.getElementById("phoneNumber");
    const joinButton = document.querySelector(".join-submit");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const passwordConfirmInput = document.getElementById("passwordConfirm");
    const emailInput = document.getElementById("domain-txt");
    const nicknameInput = document.getElementById("nickname");
    const addressInput = document.getElementById("sample6_postcode");
    const addressInput2 = document.getElementById("sample6_detailAddress");
    const profileImageInput = document.getElementById("profileImage");
    const domainListEl = document.querySelector('#domain-list');
    const joinError = document.getElementById("joinError");
    const nicknameError = document.getElementById("nicknameError");
    const passwordMatchError = document.getElementById("passwordMatchError");
    const usernameMatchError = document.getElementById("usernameMatchError");
    const usernameCheckButton = document.getElementById("usernameCheckButton"); // 아이디 중복 확인 버튼
    const nicknameCheckButton = document.getElementById("nicknameCheckButton"); // 닉네임 중복 확인 버튼

    // 전화번호 형식 자동 포맷
    phoneNumberInput.addEventListener("input", function(event) {
        let input = event.target.value.replace(/\D/g, ''); // 숫자 이외의 문자 제거
        let formattedInput = '';

        if (input.length > 3 && input.length <= 7) {
            formattedInput = `${input.slice(0, 3)}-${input.slice(3)}`;
        } else if (input.length > 7) {
            formattedInput = `${input.slice(0, 3)}-${input.slice(3, 7)}-${input.slice(7, 11)}`;
        } else {
            formattedInput = input;
        }

        event.target.value = formattedInput;
    });

    // 이메일 도메인 선택
    domainListEl.addEventListener('change', (event) => {
        if (event.target.value !== "type") {
            emailInput.value = event.target.value;
            emailInput.disabled = true;
        } else {
            emailInput.value = "";
            emailInput.disabled = false;
        }
    });

    // 회원가입 버튼 상태 업데이트
    function updateJoinButtonState() {
        if (
            usernameInput.value.trim() !== "" &&
            passwordInput.value.trim() !== "" &&
            passwordConfirmInput.value.trim() !== "" &&
            emailInput.value.trim() !== "" &&
            nicknameInput.value.trim() !== "" &&
            phoneNumberInput.value.trim() !== "" &&
            addressInput.value.trim() !== "" &&
            addressInput2.value.trim() !== "" &&
            profileImageInput.files.length > 0
        ) {
            joinButton.removeAttribute("disabled");
        } else {
            joinButton.setAttribute("disabled", "true");
        }
    }
    // 입력 필드 이벤트 리스너 추가
    const inputs = [
        usernameInput,
        passwordInput,
        passwordConfirmInput,
        emailInput,
        nicknameInput,
        phoneNumberInput,
        addressInput,
        addressInput2,
        profileImageInput
    ];

    inputs.forEach(input => {
        input.addEventListener("input", updateJoinButtonState);
        if (input === passwordInput || input === passwordConfirmInput) {
            input.addEventListener("input", checkPasswords);
        }
    });

    // 중복 확인 버튼 상태 업데이트
    function updateCheckButtonState() {
        if (usernameInput.value.trim().length !== "") {
            usernameCheckButton.removeAttribute("disabled");
        } else {
            usernameCheckButton.setAttribute("disabled", "true");
        }

        if (nicknameInput.value.trim().length !== "") {
            nicknameCheckButton.removeAttribute("disabled");
        } else {
            nicknameCheckButton.setAttribute("disabled", "true");
        }
    }

    // 아이디, 닉네임 입력 시 중복 확인 버튼 상태 업데이트
    usernameInput.addEventListener("input", updateCheckButtonState);
    nicknameInput.addEventListener("input", updateCheckButtonState);

    // 폼 유효성 검사
    function validateForm() {
        const username = usernameInput.value.trim();
        const nickname = nicknameInput.value.trim();
        const checkButton = document.querySelectorAll(".checkBtn");

        // 사용자 아이디 검사
        if (username.length < 4 || username.length > 24) {
            setErrorMessage('username', 'size');
            joinButton.disabled = true;
            usernameCheckButton.disabled = false;
        } else {
            fetch("/member/check-username?username=" + username)
                .then(response => response.json())
                .then(data => {
                    if (data.exists) {
                        usernameMatchError.innerText = "중복된 아이디가 있습니다.";
                        usernameMatchError.classList.remove("success");
                        usernameMatchError.classList.add("error");
                        joinButton.disabled = true;
                        usernameCheckButton.disabled = false;
                    } else {
                        usernameMatchError.innerText = "사용 가능한 아이디입니다.";
                        usernameMatchError.classList.remove("error");
                        usernameMatchError.classList.add("success");
                        usernameCheckButton.disabled = true;
                        updateJoinButtonState();
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        // 닉네임 검사
        if (nickname.length === 0) {
            setErrorMessage('nickname', 'notBlank');
            joinButton.disabled = true;
            nicknameCheckButton.disabled = false;
        } else {
            fetch("/member/check-nickname?nickname=" + nickname)
                .then(response => response.json())
                .then(data => {
                    if (data.exists) {
                        nicknameError.innerText = "(이미 존재하는 닉네임입니다.)";
                        nicknameError.classList.remove("success");
                        nicknameError.classList.add("error");
                        joinButton.disabled = true;
                        nicknameCheckButton.disabled = false;
                    } else {
                        nicknameError.innerText = "(입력하신 닉네임은 사용 가능합니다.)";
                        nicknameError.classList.remove("error");
                        nicknameError.classList.add("success");
                        nicknameCheckButton.disabled = true;
                        updateJoinButtonState();
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    }

    // 비밀번호 확인
    function checkPasswords() {
        const password = passwordInput.value;
        const passwordConfirm = passwordConfirmInput.value;
        const checkButton = document.querySelectorAll(".checkBtn");

        if (password && passwordConfirm) {
            if (password !== passwordConfirm) {
                setErrorMessage('password', 'pattern');
                passwordMatchError.innerText = "비밀번호가 일치하지 않습니다.";
                passwordMatchError.classList.remove("success");
                passwordMatchError.classList.add("error");
                joinButton.disabled = true;
            } else {
                passwordMatchError.innerText = "비밀번호가 일치합니다.";
                passwordMatchError.classList.remove("error");
                passwordMatchError.classList.add("success");
                updateJoinButtonState();
            }
        } else {
            passwordMatchError.innerText = "";
        }
    }

});

// 에러 메시지 객체
const errMsg = {
    username: {
        size: "아이디는 최소 4자리 ~ 최대 24자리여야 합니다.",
        notBlank: "아이디를 입력해주세요."
    },
    password: {
        size: "비밀번호는 최소 6자리 ~ 최대 24자리여야 합니다.",
        notBlank: "비밀번호를 입력해주세요.",
        pattern: "각각 하나 이상의 숫자, 특수문자(!,@,#)를 포함하여 최소 6자리 ~ 최대 24자리 까지 입력해주세요."
    },
    email: {
        notBlank: "이메일 주소를 입력해주세요.",
        pattern: "유효한 이메일 주소를 입력해주세요."
    },
    nickname: {
        notBlank: "닉네임을 입력해주세요.",
        pattern: "부적절한 단어가 포함되어 있습니다."
    },
    phoneNumber: {
        notBlank: "전화번호를 입력해주세요.",
        pattern: "전화번호를 - 없이 작성해주세요."
    }
};

// 각 필드에 대한 에러 메시지를 설정하는 함수
function setErrorMessage(field, errorType) {
    const errorElement = document.getElementById(`${field}Error`);
    if (errMsg[field] && errMsg[field][errorType]) {
        errorElement.innerText = errMsg[field][errorType];
        errorElement.classList.remove("success");
        errorElement.classList.add("error");
    } else {
        errorElement.innerText = "";
        errorElement.classList.remove("error");
        errorElement.classList.add("success");
    }
}
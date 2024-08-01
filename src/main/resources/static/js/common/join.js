document.addEventListener("DOMContentLoaded", function() {
    const phoneNumberInput = document.getElementById("phoneNumber");

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

    // 회원가입 버튼 상태 업데이트
    const joinButton = document.querySelector(".join-submit");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const passwordConfirmInput = document.getElementById("passwordConfirm");
    const emailInput = document.getElementById("domain-txt");
    const nicknameInput = document.getElementById("nickname");
    const phoneNumberInput = document.getElementById("phoneNumber");
    const addressInput = document.getElementById("address");
    const profileImageInput = document.getElementById("profileImage");

    // 이메일 직접 입력/이메일 선택
    const domainListEl = document.querySelector('#domain-list');
    const domainInputEl = document.querySelector('#domain-txt');

    domainListEl.addEventListener('change', (event) => {
        if (event.target.value !== "type") {
            domainInputEl.value = event.target.value;
            domainInputEl.disabled = true;
        } else {
            domainInputEl.value = "";
            domainInputEl.disabled = false;
        }
    });

    function updateJoinButtonState() {
        if (
            usernameInput.value.trim() !== "" &&
            passwordInput.value.trim() !== "" &&
            passwordConfirmInput.value.trim() !== "" &&
            emailInput.value.trim() !== "" &&
            nicknameInput.value.trim() !== "" &&
            phoneNumberInput.value.trim() !== "" &&
            addressInput.value.trim() !== "" &&
            profileImageInput.files.length > 0
        ) {
            joinButton.removeAttribute("disabled");
        } else {
            joinButton.setAttribute("disabled", "true");
        }
    }

    const inputs = [
        usernameInput,
        passwordInput,
        passwordConfirmInput,
        emailInput,
        nicknameInput,
        phoneNumberInput,
        addressInput,
        profileImageInput
    ];

    inputs.forEach(input => {
        input.addEventListener("input", updateJoinButtonState);
    });

    // 폼 유효성 검사
    function validateForm() {
        const joinError = document.getElementById("joinError");
        const nicknameError = document.getElementById("nicknameError");
        const submitButton = document.querySelector(".join-submit");
        const checkButton = document.getElementsByClassName("checkBtn");
        const username = usernameInput.value.trim();
        const nickname = nicknameInput.value.trim();

        // 사용자 아이디 검사
        if (username !== "") {
            fetch("/member/check-username?username=" + username)
                .then(response => response.json())
                .then(data => {
                    if (data.exists) {
                        joinError.innerText = "(이미 존재하는 아이디입니다.)";
                        joinError.classList.remove("success");
                        joinError.classList.add("error");
                        submitButton.disabled = true;
                        checkButton.disabled = false;
                    } else {
                        joinError.innerText = "(입력하신 아이디는 사용 가능합니다.)";
                        joinError.classList.remove("error");
                        joinError.classList.add("success");
                        checkButton.disabled = true;
                        updateJoinButtonState();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }

        // 닉네임 검사
        if (nickname !== "") {
            fetch("/member/check-nickname?nickname=" + nickname)
                .then(response => response.json())
                .then(data => {
                    if (data.exists) {
                        nicknameError.innerText = "(이미 존재하는 닉네임입니다.)";
                        nicknameError.classList.remove("success");
                        nicknameError.classList.add("error");
                        submitButton.disabled = true;
                        checkButton.disabled = false;
                    } else {
                        nicknameError.innerText = "(입력하신 닉네임은 사용 가능합니다.)";
                        nicknameError.classList.remove("error");
                        nicknameError.classList.add("success");
                        checkButton.disabled = true;
                        updateJoinButtonState();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    }

    // 비밀번호 확인
    function checkPasswords() {
        const password = document.getElementById("password").value;
        const passwordConfirm = document.getElementById("passwordConfirm").value;
        const passwordMatchError = document.getElementById("passwordMatchError");
        const submitButton = document.querySelector(".join-submit");

        if (password && passwordConfirm) {
            if (password !== passwordConfirm) {
                passwordMatchError.innerText = "비밀번호가 일치하지 않습니다.";
                passwordMatchError.classList.remove("success");
                passwordMatchError.classList.add("error");
                submitButton.disabled = true;
                checkButton.disabled = false;
            } else {
                passwordMatchError.innerText = "비밀번호가 일치합니다.";
                passwordMatchError.classList.remove("error");
                passwordMatchError.classList.add("success");
                checkButton.disabled = true;
                updateJoinButtonState();
            }
        } else {
            passwordMatchError.innerText = "";
        }
    }

    // 비밀번호 입력 시 확인
    passwordInput.addEventListener("input", checkPasswords);
    passwordConfirmInput.addEventListener("input", checkPasswords);

    // 주소 검색
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                if (data.userSelectedType === 'R') {
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    document.getElementById("sample6_extraAddress").value = extraAddr;
                } else {
                    document.getElementById("sample6_extraAddress").value = '';
                }

                document.getElementById('sample6_postcode').value = data.zonecode;
                document.getElementById("sample6_address").value = addr;
                document.getElementById("sample6_detailAddress").focus();
            }
        }).open();
    }

    // 비밀번호 확인 기능 추가
    passwordInput.addEventListener("input", checkPasswords);
    passwordConfirmInput.addEventListener("input", checkPasswords);
});
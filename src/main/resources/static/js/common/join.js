document.addEventListener("DOMContentLoaded", function() {
    const joinButton = document.querySelector(".join-submit");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const passwordConfirmInput = document.getElementById("passwordConfirm");
    const phoneNumberInput = document.getElementById("phoneNumber");
    const emailTextInput = document.getElementById('email-txt');
    const domainInput = document.getElementById("domain-txt");
    const nicknameInput = document.getElementById("nickname");
    const addressInput = document.getElementById("sample6_postcode");
    const addressInput2 = document.getElementById("sample6_detailAddress");
    const domainListEl = document.getElementById('domain-list');
    const joinError = document.getElementById("joinError");
    const passwordMatchError = document.getElementById("passwordMatchError");
    const usernameMatchError = document.getElementById("usernameMatchError");
    const nicknameError = document.getElementById("nicknameError");
    const usernameCheckButton = document.getElementById("usernameCheckButton"); // 아이디 중복 확인 버튼
    const nicknameCheckButton = document.getElementById("nicknameCheckButton"); // 닉네임 중복 확인 버튼
    const emailCheckButton = document.getElementById('emailCheckButton');
    const emailConfirmInput = document.getElementById('emailConfirm');
    const emailError = document.getElementById('emailError');

    // 약관 동의
    const agreeButton = document.getElementById("agreeButton"); // '다음' 버튼
    const privacyCheckbox = document.getElementById('privacy');
    const copyrightCheckbox = document.getElementById('copyright');

    // 약관 동의 체크박스 상태에 따라 버튼 활성화
    function updateAgreeButtonState() {
        if (privacyCheckbox.checked && copyrightCheckbox.checked) {
            agreeButton.removeAttribute("disabled");
        } else {
            agreeButton.setAttribute("disabled", "true");
        }
    }

    // 체크박스 상태가 변경될 때마다 버튼 상태 업데이트
    privacyCheckbox.addEventListener('change', updateAgreeButtonState);
    copyrightCheckbox.addEventListener('change', updateAgreeButtonState);

    // '다음' 버튼 클릭 시 회원가입 폼 표시
    agreeButton.addEventListener('click', function() {
        if (!agreeButton.disabled) {
            document.querySelector('.privacyCheckbox').style.display = 'none';
            document.querySelector('.copyrightCheckbox').style.display = 'none';
            document.getElementById('joinForm').style.display = 'block';
        }
    });

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
            domainInput.value = event.target.value;
            domainInput.disabled = true;
        } else {
            domainInput.value = "";
            domainInput.disabled = false;
        }
    });

    // 회원가입 버튼 상태 업데이트
    function updateJoinButtonState() {
        let allFilled = true;
        for (let input of inputs) {
            if (input.type === "file") {
                if (input.files.length === 0) {
                    allFilled = false;
                    break;
                }
            } else {
                if (input.value.trim() === "") {
                    allFilled = false;
                    break;
                }
            }
        }
        if (allFilled) {
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
        emailTextInput,
        domainInput,
        nicknameInput,
        phoneNumberInput,
        addressInput,
        addressInput2
    ];

    inputs.forEach(input => {
        input.addEventListener("input", updateJoinButtonState);
        if (input === passwordInput || input === passwordConfirmInput) {
            input.addEventListener("input", checkPasswords);
        }
    });

    // 중복 확인 버튼 상태 업데이트
    function updateCheckButtonState() {
        // 아이디 중복 확인 버튼 활성화 여부
        if (usernameInput.value.trim().length !== "") {
            usernameCheckButton.removeAttribute("disabled");
        } else {
            usernameCheckButton.setAttribute("disabled", "true");
        }
        // 닉네임 중복 확인 버튼 활성화 여부
        if (nicknameInput.value.trim().length !== "") {
            nicknameCheckButton.removeAttribute("disabled");
        } else {
            nicknameCheckButton.setAttribute("disabled", "true");
        }
    }
    usernameCheckButton.addEventListener("click", validateUsername);
    nicknameCheckButton.addEventListener("click", validateNickname);

    // 아이디, 닉네임 입력 시 중복 확인 버튼 상태 업데이트
    usernameInput.addEventListener("input", updateCheckButtonState);
    nicknameInput.addEventListener("input", updateCheckButtonState);

    // 아이디 중복 확인 함수
    function validateUsername() {
        // 아이디에 공백, 한글, 특수문자가 있는지 확인하고 있으면 경고 표시
        // 영문자와 숫자만 허용
        const validUsernameRegex = /^[a-zA-Z0-9]+$/;
        const username = usernameInput.value.trim();

        const digitCounts = {};

        // 동일한 숫자가 4번 이상 포함되어 있는지 검사
        for (const char of username) {
            if (/\d/.test(char)) { // 숫자일 경우
                digitCounts[char] = (digitCounts[char] || 0) + 1;
                if (digitCounts[char] > 3) {
                    usernameMatchError.innerText = "동일한 숫자를 4번 이상 사용할 수 없습니다.";
                    usernameMatchError.classList.remove("success");
                    usernameMatchError.classList.add("error");
                    return;
                }
            }
        }

        if (username.length < 4 || username.length > 24) {
            usernameMatchError.innerText = "아이디는 최소 4자리 ~ 최대 24자리여야 합니다.";
            usernameMatchError.classList.remove("success");
            usernameMatchError.classList.add("error");
            // 경고 문구 표시 후 중복확인 버튼 초기화
            usernameCheckButton.disabled = true;
        } else if (!validUsernameRegex.test(username)) {
            usernameMatchError.innerText = "사용할 수 없는 문자(한글, 공백, 특수문자)가 포함되어 있습니다.";
            usernameMatchError.classList.remove("success");
            usernameMatchError.classList.add("error");
            // 경고 문구 표시 후 중복확인 버튼 초기화
            usernameCheckButton.disabled = true;
        } else {
            fetch("/member/check-username?username=" + username)
                .then(response => response.json())
                .then(data => {
                    if (data.exists) {
                        usernameMatchError.innerText = "사용 중인 아이디입니다.";
                        usernameMatchError.classList.remove("success");
                        usernameMatchError.classList.add("error");
                    } else {
                        usernameMatchError.innerText = "사용 가능한 아이디입니다.";
                        usernameMatchError.classList.remove("error");
                        usernameMatchError.classList.add("success");
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    }

    // 닉네임 중복 확인 함수
    function validateNickname() {
        const nickname = nicknameInput.value.trim();

        // 유효하지 않은 닉네임을 검사하는 정규식
        const validNicknameRegex = /^(?!.*(admin|관리자|운영자|fuck|shit|bitch|cunt|sex|porn|xxx|rape|asshole|slut|whore|nigger|chink|gook|spic|beaner|faggot|cocksucker|motherfucker|bastard|idiot|stupid|retard|dumb|jew|nazi|fascist|terrorist|prick|dick|cunt|ass|pussy|clit|femdom|gay|lesbian|homosexual|transgender|sexist|misogynist|racist|hate|bigot|xenophobe|bully|harass|abuse|정신병|미친|병신|년|개새끼|후리|놈|걸레|창녀|쓰레기|짱깨|좆|빡대가리|대가리|찌질이|천민|한남|패륜|불륜|강간|성폭력|토착왜구|매국노|일베|새끼|지랄|까발리|씨발|걸레|핵쓰레기|개돼지|쪽발이|찌질이|강간범|성희롱|성추행|성폭행|성희롱범|좆|좆같은|섹스)).*[a-zA-Z0-9가-힣]+$/;

        if (nickname.length < 4 || nickname.length > 24) {
            nicknameError.innerText = "닉네임은 최소 4자리 ~ 최대 24자리여야 합니다.";
            nicknameError.classList.remove("success");
            nicknameError.classList.add("error");
            nicknameCheckButton.disabled = true;
        } else if (!validNicknameRegex.test(nickname)) {
            nicknameError.innerText = "사용할 수 없는 문자가 포함되어 있습니다.";
            nicknameError.classList.remove("success");
            nicknameError.classList.add("error");
            nicknameCheckButton.disabled = true;
        } else {
            fetch("/member/check-nickname?nickname=" + encodeURIComponent(nickname))
                .then(response => response.json())
                .then(data => {
                    if (data.exists) {
                        nicknameError.innerText = "이미 존재하는 닉네임입니다.";
                        nicknameError.classList.remove("success");
                        nicknameError.classList.add("error");
                    } else {
                        nicknameError.innerText = "사용 가능한 닉네임입니다.";
                        nicknameError.classList.remove("error");
                        nicknameError.classList.add("success");
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    }

    // 비밀번호 일치 여부 확인 함수
    function checkPasswords() {
        const password = passwordInput.value.trim();
        const passwordConfirm = passwordConfirmInput.value.trim();

        // 비밀번호 유효성 검사 정규 표현식
        const validPasswordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#])[a-zA-Z\d!@#]{6,24}$/;

        // 비밀번호 길이 검사
        if (password.length < 6 || password.length > 24) {
            passwordMatchError.innerText = "비밀번호는 최소 6자리 ~ 최대 24자리여야 합니다.";
            passwordMatchError.classList.remove("success");
            passwordMatchError.classList.add("error");
            joinButton.disabled = true;
        }
        // 비밀번호 유효성 검사
        else if (!validPasswordRegex.test(password)) {
            passwordMatchError.innerText = "하나 이상의 숫자와 특수문자(!,@,#)를 포함해야 합니다.";
            passwordMatchError.classList.remove("success");
            passwordMatchError.classList.add("error");
            joinButton.disabled = true;
        }
        // 비밀번호와 비밀번호 확인 일치 검사
        else if (password && passwordConfirm) {
            if (password === passwordConfirm) {
                passwordMatchError.innerText = "비밀번호가 일치합니다.";
                passwordMatchError.classList.remove("error");
                passwordMatchError.classList.add("success");
                joinButton.disabled = false;
            } else {
                passwordMatchError.innerText = "비밀번호가 일치하지 않습니다.";
                passwordMatchError.classList.remove("success");
                passwordMatchError.classList.add("error");
                joinButton.disabled = true;
            }
        } else {
            // 비밀번호 입력이 비어 있을 경우
            passwordMatchError.innerText = "";
            joinButton.disabled = true;
        }
    }

    // 비밀번호 입력 필드에 이벤트 리스너 추가
    passwordInput.addEventListener("input", checkPasswords);
    passwordConfirmInput.addEventListener("input", checkPasswords);

    // 이메일 인증
    let generatedCode = "";

    document.getElementById("emailCheckButton").addEventListener("click", function() {
        const email = `${emailTextInput.value.trim()}@${domainInput.value.trim()}`;

        if (!emailTextInput.value.trim() || !domainInput.value.trim()) {
            emailError.innerText = "이메일 주소를 입력하세요.";
            emailError.classList.add("error");
            joinButton.disabled = true;
            return;
        }

        if (emailTextInput.value.trim()) {
            emailError.innerText = "인증번호를 입력 해주세요.";
            emailError.classList.remove("error");
            emailError.classList.add("success");
        }

        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        console.log('CSRF Token:', csrfToken);
        console.log('CSRF Header:', csrfHeader);

        fetch("/sendmail/confirmCode", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ email: email })
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('응답되지 않습니다.');
            }
        })
         .then(data => {
            console.log("")
            if (data.code) {
                generatedCode = data.code;
                emailError.innerText = "인증 메일이 발송되었습니다. 이메일을 확인하세요.";
                emailError.classList.remove("error");
                emailError.classList.add("success");
            } else {
                emailError.innerText = "이메일 발송에 실패했습니다.";
                emailError.classList.remove("success");
                emailError.classList.add("error");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            emailError.innerText = "이메일 발송 중 오류가 발생했습니다.";
            emailError.classList.remove("success");
            emailError.classList.add("error");
        });
    });

    emailConfirmInput.addEventListener("input", function() {
        const userEnteredCode = emailConfirmInput.value.trim();

        if (userEnteredCode === "") {
            emailError.innerText = "인증번호를 입력해 주세요.";
            emailError.classList.remove("success");
            emailError.classList.add("error");
            joinButton.disabled = true;
        } else if (userEnteredCode === generatedCode) {
            emailError.innerText = "이메일 인증이 완료되었습니다.";
            emailError.classList.remove("error");
            emailError.classList.add("success");
            joinButton.disabled = false;
        } else {
            emailError.innerText = "인증 번호가 일치하지 않습니다.";
            emailError.classList.remove("success");
            emailError.classList.add("error");
            joinButton.disabled = true;
        }
    });
});
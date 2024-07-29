function toggleCertificateDiv() {
    var userType = document.getElementById("userType").value;
    var certificateDiv = document.getElementById("certificateDiv");
    if (userType === "artist") {
        certificateDiv.style.display = "block";
    } else {
        certificateDiv.style.display = "none";
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const joinButton = document.querySelector(".join-submit");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const emailInput = document.getElementById("email");
    const nicknameInput = document.getElementById("nickname");
    const phoneNumberInput = document.getElementById("phoneNumber");
    const addressInput = document.getElementById("address");
    const profileImageInput = document.getElementById("profileImage");

    function updateJoinButtonState() {
        if (
            usernameInput.value.trim() !== "" &&
            passwordInput.value.trim() !== "" &&
            emailInput.value.trim() !== "" &&
            nicknameInput.value.trim() !== "" &&
            phoneNumberInput.value.trim() !== "" &&
            addressInput.value.trim() !== "" &&
            profileImageInput.value.trim() !== ""
        ) {
            joinButton.removeAttribute("disabled");
        } else {
            joinButton.setAttribute("disabled", "true");
        }
    }

    const inputs = [
        usernameInput,
        passwordInput,
        emailInput,
        nicknameInput,
        phoneNumberInput,
        addressInput,
        profileImageInput
    ];

    inputs.forEach(input => {
        input.addEventListener("input", updateJoinButtonState);
    });
});

function validateForm() {
    const joinError = document.getElementById("joinError");
    const joinError = document.getElementById("nicknameError");
    const submitButton = document.getElementById("join-submit");
    const usernameInput = document.getElementById("username");
    const nicknameInput = document.getElementById("nickname");
    const username = usernameInput.value.trim();
    const nickname = nicknameInput.value.trim();
    if (username !== "") {
        fetch("/member/check-username?username=" + username)
            .then(response => response.json())
            .then(data => {
                if (data.exists) {
                    joinError.innerText = "(이미 존재하는 아이디입니다.)";
                    joinError.classList.remove("success");
                    joinError.classList.add("error");
                    submitButton.disabled = true;
                } else {
                    joinError.innerText = "(입력하신 아이디는 사용 가능합니다.)";
                    joinError.classList.remove("error");
                    joinError.classList.add("success");
                    submitButton.disabled = false;
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    if (nickname !== "") {
            fetch("/member/check-nickname?nickname=" + nickname)
                .then(response => response.json())
                .then(data => {
                    if (data.exists) {
                        nicknameError.innerText = "(이미 존재하는 닉네임입니다.)";
                        nicknameError.classList.remove("success");
                        nicknameError.classList.add("error");
                        submitButton.disabled = true;
                    } else {
                        nicknameError.innerText = "(입력하신 닉네임은 사용 가능합니다.)";
                        nicknameError.classList.remove("error");
                        nicknameError.classList.add("success");
                        submitButton.disabled = false;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
}

    function checkPasswords() {
        const password = document.getElementById("password").value;
        const passwordConfirm = document.getElementById("password_confirm").value;
        const passwordMatchError = document.getElementById("passwordMatchError");
        const submitButton = document.getElementById("join-submit");

        if (password && passwordConfirm) {
            if (password !== passwordConfirm) {
                passwordMatchError.innerText = "비밀번호가 일치하지 않습니다.";
                passwordMatchError.classList.remove("success");
                passwordMatchError.classList.add("error");
                submitButton.disabled = true;
            } else {
                passwordMatchError.innerText = "비밀번호가 일치합니다.";
                passwordMatchError.classList.remove("error");
                passwordMatchError.classList.add("success");
                submitButton.disabled = false;
            }
        } else {
            passwordMatchError.innerText = "";
        }
    }


// 주소 검색
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;

            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}
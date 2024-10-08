document.addEventListener('DOMContentLoaded', function() {
    // 검색 입력 필드에 대한 이벤트 리스너 추가
    const searchInput = document.getElementById('memberSearch');
    const memberListItems = document.querySelectorAll('.mb_list_each');

    searchInput.addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();

        memberListItems.forEach(item => {
            const fields = [
                '.mb_list_un', '.mb_list_nm', '.mb_list_e',
                '.mb_list_cd', '.mb_list_md', '.mb_list_pnb',
                '.mb_list_ad', '.mb_list_ia select',
                '.mb_list_ptc', '.mb_list_rl select'
            ];

            const isMatch = fields.some(selector =>
                item.querySelector(selector).textContent.toLowerCase().includes(searchTerm)
            );

            item.style.display = isMatch ? 'flex' : 'none';
        });
    });

    // 상태나 역할 변경 시 저장 버튼 활성화
    document.querySelectorAll('.isActive, .role').forEach(selectElement => {
        selectElement.addEventListener('change', function() {
            const parent = this.closest('li');
            const saveButton = parent.querySelector('.btn_save');
            saveButton.disabled = false; // 변경이 감지되면 저장 버튼 활성화
        });
    });

    // 페이지 이탈 시 변경 사항 확인
    window.addEventListener('beforeunload', function (e) {
        const unsavedChanges = Array.from(document.querySelectorAll('.btn_save'))
            .some(button => !button.disabled);

        if (unsavedChanges) {
            e.preventDefault();
            e.returnValue = '';
        }
    });
});

// 페이지가 로드될 때 저장 버튼을 비활성화하고 초기 값을 저장
document.querySelectorAll('.mb_list_each').forEach(item => {
    const isActiveSelect = item.querySelector('.isActive');
    const roleSelect = item.querySelector('.role');
    const saveButton = item.querySelector('.btn_save');

    // 초기 값 저장
    isActiveSelect.setAttribute('data-original-value', isActiveSelect.value);
    roleSelect.setAttribute('data-original-value', roleSelect.value);

    // 저장 버튼 비활성화
    saveButton.disabled = true;
});
document.querySelectorAll('.btn_save').forEach(button => {
    button.addEventListener('click', function() {
        const memberId = this.getAttribute('data-memberid'); // member.id 가져오기
        const parent = this.closest('li');

        const isActiveSelect = parent.querySelector('.isActive');
        const roleSelect = parent.querySelector('.role');
        const newStatus = isActiveSelect.value;
        const newRole = roleSelect.value;

        // 기존 값과 비교
        const originalStatus = isActiveSelect.getAttribute('data-original-value');
        const originalRole = roleSelect.getAttribute('data-original-value');

        // 변경 사항이 없으면 저장하지 않음
        if (newStatus === originalStatus && newRole === originalRole) {
           alert('변경된 내용이 없습니다.');
           return;
        }

        // 변경된 데이터를 담을 객체 생성
        let updatedData = {};

        if (newStatus !== originalStatus) {
           updatedData.isActive = newStatus;
        }

        if (newRole !== originalRole) {
           updatedData.role = newRole;
        }

        // URL 문자열 보간을 올바르게 사용
        const url = `/admin/member/${memberId}`;

        // confirm 대화상자 표시
        if (confirm(`변경사항을 저장하시겠습니까?\n활성 상태: ${updatedData.isActive || originalStatus}\n역할: ${updatedData.role || originalRole}`)) {
           // Fetch API를 이용한 비동기 데이터 전송
           fetch(url, {
               method: 'PUT',
               headers: {
                   'Content-Type': 'application/json'
               },
               body: JSON.stringify(updatedData)
           })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Error: ${response.status} - ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert('회원 상태가 변경되었습니다.');
                    location.reload();
                } else {
                    alert('업데이트 중 오류가 발생했습니다: ' + data.message);
                }
            })
            .catch(error => {
                console.error('네트워크 오류 또는 서버 오류 발생:', error);
                alert('회원 정보 업데이트 중 오류가 발생했습니다.' + error.message);
            });
        } else {
            // 사용자가 취소를 누른 경우 버튼을 비활성화하고 기본 상태로 유지
            isActiveSelect.value = isActiveSelect.getAttribute('data-original-value');
            roleSelect.value = roleSelect.getAttribute('data-original-value');
            this.disabled = true; // 저장 버튼 비활성화
        }
    });
});

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

    // 저장 버튼
    document.querySelectorAll('.btn_save').forEach(button => {
        button.addEventListener('click', function() {
            const memberId = this.getAttribute('data-memberid');
            const parent = this.closest('li');

            const isActiveSelect = parent.querySelector('.isActive');
            const roleSelect = parent.querySelector('.role');
            const newStatus = isActiveSelect.value === 'true';
            const newRole = roleSelect.value;

            if (confirm(`변경사항을 저장하시겠습니까?\n활성 상태: ${newStatus ? 'true' : 'false'}\n역할: ${newRole}`)) {
                fetch(`/admin/member/${memberId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        isActive: newStatus,
                        role: newRole
                    })
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('네트워크 응답이 올바르지 않습니다.');
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
                    alert('회원 정보 업데이트 중 오류가 발생했습니다.');
                });
            } else {
                // 사용자가 취소를 누른 경우 버튼을 비활성화하고 기본 상태로 유지
                isActiveSelect.value = isActiveSelect.getAttribute('data-original-value');
                roleSelect.value = roleSelect.getAttribute('data-original-value');
                this.disabled = true; // 저장 버튼 비활성화
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

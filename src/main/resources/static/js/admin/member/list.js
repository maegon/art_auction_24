document.addEventListener('DOMContentLoaded', function() {
    // 검색 입력 필드에 대한 이벤트 리스너 추가
    const searchInput = document.getElementById('memberSearch');
    const memberListItems = document.querySelectorAll('.mb_list_each');

    searchInput.addEventListener('input', function() {
        const searchTerm = this.value.toLowerCase();

        memberListItems.forEach(item => {
            const username = item.querySelector('.mb_list_un').textContent.toLowerCase();
            const nickname = item.querySelector('.mb_list_nm').textContent.toLowerCase();
            const email = item.querySelector('.mb_list_e').textContent.toLowerCase();
            const createDate = item.querySelector('.mb_list_cd').textContent.toLowerCase();
            const modifyDate = item.querySelector('.mb_list_md').textContent.toLowerCase();
            const phoneNumber = item.querySelector('.mb_list_pnb').textContent.toLowerCase();
            const address = item.querySelector('.mb_list_ad').textContent.toLowerCase();
            const isActive = item.querySelector('.mb_list_ia select').value.toLowerCase();
            const providerType = item.querySelector('.mb_list_ptc').textContent.toLowerCase();
            const role = item.querySelector('.mb_list_rl select').value.toLowerCase();

            // 검색어와 일치하는 항목이 있는지 확인
            const isMatch =
                username.includes(searchTerm) ||
                nickname.includes(searchTerm) ||
                email.includes(searchTerm) ||
                createDate.includes(searchTerm) ||
                modifyDate.includes(searchTerm) ||
                phoneNumber.includes(searchTerm) ||
                address.includes(searchTerm) ||
                isActive.includes(searchTerm) ||
                providerType.includes(searchTerm) ||
                role.includes(searchTerm);

            // 일치하는 항목이 있으면 보여주고, 아니면 숨김
            if (isMatch) {
                item.style.display = 'flex'; // 일치하면 보이도록 설정
            } else {
                item.style.display = 'none'; // 일치하지 않으면 숨김
            }
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

            if (confirm(`변경사항을 저장하시겠습니까?\n활성 상태: ${newStatus ? '활성화' : '비활성화'}\n역할: ${newRole}`)) {
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
                // 사용자가 취소를 누른 경우 기본값으로 복원
                isActiveSelect.value = 'true'; // isActive를 '활성화'로 설정
                roleSelect.value = 'USER'; // role을 '일반'으로 설정
            }
        });
    });
});
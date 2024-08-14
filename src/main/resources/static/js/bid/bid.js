window.addEventListener('load', function() {
    try {
        const urlParams = new URLSearchParams(window.location.search);
        const error = urlParams.get('error');

        if (error) {
            // URL 디코딩된 오류 메시지를 알림으로 표시
            Swal.fire({
                icon: 'error',
                title: '오류',
                text: decodeURIComponent(error),
                confirmButtonText: '확인'
            });
        }
    } catch (e) {
        console.error('Error in script execution:', e);
    }
});


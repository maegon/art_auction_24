 document.addEventListener('DOMContentLoaded', () => {


        if (startTimeElement && remainingTimeElement) {
            const startDateString = startTimeElement.dataset.start;

            // 날짜 문자열을 ISO 8601 형식으로 변환 (시작 날짜)
            const startDate = new Date(startDateString.replace(' ', 'T'));
            const now = new Date();

            // 날짜 유효성 검사
            if (isNaN(startDate.getTime())) {
                console.error('Invalid date format');
                remainingTimeElement.textContent = 'Invalid start date';
                return;
            }

            // 남은 시간 계산 함수
            function calculateRemainingTime(startDate, now) {
                const remainingTime = startDate - now;
                if (remainingTime <= 0) return 'Auction has started';
                const hours = Math.floor((remainingTime / (1000 * 60 * 60)) % 24);
                const minutes = Math.floor((remainingTime / (1000 * 60)) % 60);
                return `${hours}h ${minutes}m`;
            }

            // 남은 시간 업데이트
            function updateTime() {
                const remainingTime = calculateRemainingTime(startDate, now);
                remainingTimeElement.textContent = `Starts in: ${remainingTime}`;
            }

            updateTime();

            // 주기적으로 남은 시간 업데이트 (1분마다)
            setInterval(() => {
                now = new Date(); // 현재 시간 업데이트
                updateTime();
            }, 60000); // 1분 간격으로 업데이트
        }
    });
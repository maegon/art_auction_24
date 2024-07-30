async function handleRecharge(event) {
    event.preventDefault();

    const memberId = document.getElementById('memberId').value;
    const amount = document.getElementById('amount').value;

    try {
        const response = await fetch(`/api/recharge/${memberId}?amount=${amount}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const data = await response.json();
            alert(`충전 완료! ID: ${data.id}, 금액: ${data.amount}`);
        } else {
            alert('충전 실패');
        }
    } catch (error) {
        console.error('충전 중 오류 발생:', error);
        alert('충전 중 오류 발생');
    }
}

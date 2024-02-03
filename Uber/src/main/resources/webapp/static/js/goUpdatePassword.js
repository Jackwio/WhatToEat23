function updatePassword() {
    var password = $('#passwordInput').val()
    var confirmPassword = $('#confirmPasswordInput').val()
    if (password != confirmPassword) {
        alert("密碼和確認密碼不同!!!")
        return
    }
    fetch('/member/password/' + password, {
        method: 'PATCH',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message)
            window.location.href = 'http://localhost:8080/member/'
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
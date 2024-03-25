function phoneNumValid() {
    var memName = $.trim($('#nameInput').val())
    var password = $.trim($('#passwordInput').val())
    var confirmPasswordInput = $.trim($('#confirmPasswordInput').val())
    if (password !== confirmPasswordInput) {
        alert("密碼和確認密碼不同")
        return
    }
    var phone = $.trim($('.phoneNumberContainer').text());
    phone = phone.substr(phone.indexOf("+886") + 5)
    fetch('/member/phoneNumAndElse/' + phone + '/' + memName + '/' + password, {
        method: "POST"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message)
            if (data.message.indexOf("成功") !== -1) {
                window.location.href = 'http://localhost:8080/'
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
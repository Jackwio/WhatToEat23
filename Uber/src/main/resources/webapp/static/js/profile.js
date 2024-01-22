$(document).ready(function () {
    $('#directories a').click(function (e) {
        e.preventDefault();
        var targetSection = $(this).attr('href'); //獲取href跳轉對應的值
        $('.section').removeClass('active');
        $(targetSection).addClass('active');
    });
});

function showPasswordBlock() {
    $('#passwordBlock').toggle();
}

function confirmChange() {

    var memEmail = $('#emailInput').val()
    var memName = $('#nameInput').val()
    var memPhoneNum = $('#phoneInput').val()
    var password = $('#passwordInput').val()
    var confirmPassword = $('#confirmPasswordInput').val()
    if (password != confirmPassword) {
        alert('密碼和確認密碼不一樣，更改失敗')
        return false
    }

    const formData = new FormData();
    formData.append('memEmail', memEmail);
    formData.append('memName', memName);
    formData.append('memPhoneNum', memPhoneNum);
    formData.append('password', password);

    fetch('/editMemberInfo', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            alert(data)
            window.location.href = 'http://localhost:8080/goToProfile'
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


function phoneNumValid() {
    var phone = $.trim($('.phoneNumberContainer').text());
    phone = phone.substr(phone.indexOf(".886")+5)
    fetch('/member/phoneNum/' + phone, {
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
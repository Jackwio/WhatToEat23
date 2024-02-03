function addAdmin() {
    var adminEmail = $('#email').val()
    var password = $('#password').val()
    var confirmPassword = $('#confirmPassword').val()
    if(password!=confirmPassword){
        alert("密碼和確認密碼不同")
        return
    }

    fetch('/admin/' + adminEmail + '/' + password, {
        method: 'GET',
    })
        .then(response => {
            if(!response.ok){
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message)
            if(data.message.indexOf("成功")!==-1){
                window.location.href = 'http://localhost:8080/goToAdmin'
            }
        })
        .catch(error => {

        });

}
function login() {

    var memEmail = $('#memEmail').val()
    var password = $('#password').val()
    const formData = new FormData()
    formData.append('memEmail', memEmail)
    formData.append('password', password)
    fetch('/member/', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok')
            }
            return response.json();
        })
        .then(data => {
            alert(data.message)
            if (data.message === "餐廳業者登入成功") {
                sessionStorage.setItem('acceptedOrder', 'true')
                window.location.href = 'http://localhost:8080/goToRestBack'
            } else if (data.message === "會員登入成功") {
                sessionStorage.setItem('finishOrder', 'true')
                window.location.href = 'http://localhost:8080/'
            } else {
                window.location.href = 'http://localhost:8080/loginUser'
            }
        })
        .catch(error => {
        });
}
function login() {

    var memEmail = $('#memEmail').val();
    var password = $('#password').val();
    const formData = new FormData();

    formData.append('memEmail', memEmail);
    formData.append('password', password);


    fetch('/login', {
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
            if(data==="餐廳業者登入成功"){
                sessionStorage.setItem('acceptedOrder', 'true');
                window.location.href='http://localhost:8080/goToRestBack'
            }else if(data==="會員登入成功"){
                sessionStorage.setItem('finishOrder', 'true');
                console.log(sessionStorage);
                window.location.href='http://localhost:8080/'
            }else{
                window.location.href='http://localhost:8080/loginUser'
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
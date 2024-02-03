function login(event) {
    event.preventDefault()
    var adminEmail = $('#adminEmail').val();
    var password = $('#password').val().trim();

    fetch('/admin?adminEmail=' + adminEmail + "&password=" + password, {
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
function phoneNumValid(){
    fetch('/validPhoneNum')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            alert(data)
            if(data === "註冊成功"){
                window.location.href = 'http://localhost:8080/';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
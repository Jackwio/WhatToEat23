function register() {
    var memEmail = $('#memEmail').val();
    fetch('/register?memEmail='+memEmail)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            if(data.length!=0){
                alert(data)
            }else{
                window.location.href = 'http://localhost:8080/goValidCode';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
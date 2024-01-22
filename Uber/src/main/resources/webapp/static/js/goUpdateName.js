function updateName(){
    var memName = $('#nameInput').val();
    fetch('/updateName?memName='+memName)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            alert(data)
            if(data.endsWith("成功")){
                window.location.href = 'http://localhost:8080/goToProfile';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
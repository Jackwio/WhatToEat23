function register() {
    var memEmail = $('#memEmail').val();
    fetch('/member/email/' + memEmail, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json()
        })
        .then(data => {
            if (data.message.length != 0) {
                alert(data.message)
            } else {
                window.location.href = 'http://localhost:8080/goValidCode'
            }
        })
        .catch(error => {
        });
}
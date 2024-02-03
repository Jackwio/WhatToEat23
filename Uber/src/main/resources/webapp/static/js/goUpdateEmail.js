function updateEmail() {
    var memEmail = $('#emailInput').val()
    fetch('/member/email/' + memEmail, {
        method: "PATCH"
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
                window.location.href = 'http://localhost:8080/member/';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
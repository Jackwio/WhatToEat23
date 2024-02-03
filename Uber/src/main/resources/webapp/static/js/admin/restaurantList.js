function stopRest(button) {
    var restId = button.getAttribute('data-rest-id')

    fetch('/restaurants/' + restId, {
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
        })
        .catch(error => {

        });
}
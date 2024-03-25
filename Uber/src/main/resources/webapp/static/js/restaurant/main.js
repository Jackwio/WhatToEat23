$(document).ready(function () {
    //獲取登入時儲存為會員的標記，可以再搭配後端判斷是否有需要評分的訂單
    const finishOrder = sessionStorage.getItem('finishOrder');
    if (finishOrder === 'true') {
        $('#commentModal').modal('show');
    }
})

function goToComment(button) {
    var orderId = button.getAttribute('data-order-id');
    const comment = $('#commentContent').val()
    const rating = document.querySelector('input[name="rating"]:checked').value;
    fetch('/member/comment?ratingsContent=' + comment + '&ratingsStar=' + rating + '&orderId=' + orderId, {
        method: "PUT"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            window.location.href = 'http://localhost:8080' + data.url;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function cancelComment(button) {
    var orderId = button.getAttribute('data-order-id');
    window.location.href = 'http://localhost:8080/member/comment?orderId=' + orderId;
}

function useTurntable() {
    window.location.href = 'http://localhost:8080/restaurant/turntable'
}
function changePage(pageNumber) {
    window.location.href = 'http://localhost:8080/restaurant/?pageNumber=' + pageNumber
}
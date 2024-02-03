function login() {
    window.location.href = 'http://localhost:8080/loginUser'
}

function register() {
    window.location.href = 'http://localhost:8080/registerUser'
}

function addOwner() {
    window.location.href = 'http://localhost:8080/loginUser'
}

function addRest() {
    window.location.href = 'http://localhost:8080/goToRegisterRest'
}

// 購物車相關
function editItem(change, mId, event) {

    fetch('/cart/food/' + mId+'/'+change, {
        method: "PATCH"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok')
            }
            return response.text()
        })
        .then(data => {
            $('#cartItemBlock').html(data)
            fetch('/cart/numbers', {
                method: "PATCH"
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok')
                    }
                    return response.text()
                })
                .then(data => {
                    $('#cartNumber').html(data)
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        })
        .catch(error => {
            console.error('Error:', error);
        });

}

function addItem(button) {
    var mId = button.getAttribute("data-menu-id")
    var rest = button.getAttribute("data-cartItem-rest")
    var currentRest = button.getAttribute('data-currentRest')
    if (parseInt(rest) != 0 && parseInt(rest) != parseInt(currentRest)) {
        alert("你已加入另一個商品!")
        return
    }
    $('#exampleModal').modal('show');
    fetch("/cart/food/" + mId, {
        method: "POST"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            $('#cartItemBlock').html(data)
            fetch("/cart/numbers", {
                method: "PATCH"
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(data => {
                    $('#cartNumber').html(data)
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function goToPay(button) {
    var quantity = button.getAttribute("data-cartItem")
    if (parseInt(quantity) == 0) {
        alert("請先加入商品到購物在進行付款")
        return
    }
    window.location.href = 'http://localhost:8080/order/'
}

function deleteItem(foodId) {
    $.get("/deleteCart?foodId=" + foodId, function (data) {
        $('#cartItemBlock').html(data)
        $.get("/changeCartNumber", function (data) {
            $('#cartNumber').html(data)
        })
    })
}

function collectRest(restId, event, pageNumber) {
    event.stopPropagation()
    event.preventDefault();
    var heartId = event.target.getAttribute("id")
    var bool = $('#' + heartId).hasClass('heart-red')
    if (heartId === 'heart' + restId) {
        if (!bool) {
            $('#' + heartId).addClass("heart-red")
            fetch('/member/restaurant/' + restId+'/'+pageNumber, {
                method: "POST"
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {})
                .catch(error => {
                    console.error('Error:', error);
                });
        } else {
            $('#' + heartId).removeClass("heart-red")
            fetch('/member/restaurant/' + restId+'/'+pageNumber, {
                method: "DELETE"
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {})
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    }

}
function order() {
    // 前往付款的相應動作
    fetch('/order/payment', {
        method: "PUT"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message);
            window.location.href='http://localhost:8080/main'
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function goAddFood(restId) {
    window.location.href = 'http://localhost:8080/food/' + restId;
}
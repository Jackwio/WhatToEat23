function toggleOptions() {
    // 點完選項後可以消失
    $('#customOptions').toggle()
}

function handleOptionSelect(event) {
    //透過jquery中的html更改點擊完選項後的文字
    $('.select-button').html(`<i class="bi bi-stopwatch-fill"></i> ${event.target.innerText} <i class="bi bi-caret-down-fill down"></i>`);
    // 點完選項後可以消失
    $('#customOptions').toggle()
}

function reserve(){
    $('#reserveModal').modal('show')
}

function adminLogin(){
    window.location.href = 'http://localhost:8080/goLoginAdmin'
}

function findRest() {
    var pos = $('#siteInput').val()
    console.log(pos,'@@')
    window.location.href = 'http://localhost:8080/lookRestaurant?position=' + pos
}
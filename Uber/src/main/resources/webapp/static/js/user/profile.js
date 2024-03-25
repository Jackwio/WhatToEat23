$(document).ready(function () {
    $('#directories a').click(function (e) {
        e.preventDefault();
        var targetSection = $(this).attr('href'); //獲取href跳轉對應的值
        $('.section').removeClass('active');
        $(targetSection).addClass('active');
    });
});

function changePhoto() {
    var fileInput = $('#fileInput')[0] // 获取原生的 DOM 元素
    var previewImg = $('.image-preview')
    var imgContainer = $('.img-container')
    var file = fileInput.files[0]

    if (file) {
        var reader = new FileReader();
        var formData = new FormData()
        formData.append("memPhoto",file)
        reader.onload = function (e) {
            previewImg.attr('src', e.target.result);
        };
        reader.readAsDataURL(file);

        fetch('/member/photo', {
            method: "PATCH",
            body: formData
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                alert(data.message)
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}

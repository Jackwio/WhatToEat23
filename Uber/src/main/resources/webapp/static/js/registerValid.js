
$(document).ready(function() {
    $('.codeInput').on('input', function() {
        var currentValue = $(this).val();

        // 使用正則表達式檢查是否為數字
        if (/^[0-9]*$/.test(currentValue)) {
            // 確保在用戶輸入完成一個字符時才執行相應的代碼
            if (currentValue.length === 1) {
                var nextInputIndex = $('.codeInput').index(this) + 2;
                if (nextInputIndex <= $('.codeInput').length) {
                    // 移動到下一個輸入框
                    var nextInput = $('.codeInput').eq(nextInputIndex - 1);
                    //（cursor）設置到下一個輸入框 nextInput 上的
                    nextInput.focus();
                }
            }
         }
    });
});

function validCode() {
    var inputs = $('.codeInput')
    var inputValues = [
        inputs[0].value,
        inputs[1].value,
        inputs[2].value,
        inputs[3].value
    ];
    var code = inputValues.join("")

    fetch('/validCode?code='+code)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            alert(data)
            if(data === "驗證成功"){
                window.location.href = 'http://localhost:8080/goPhoneNumInput';
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
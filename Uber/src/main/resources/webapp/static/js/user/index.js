function toggleOptions() {
    // 點完選項後可以消失
    $('#customOptions').toggle()
    var newWidth = $('#time').outerWidth();
    $('.custom-options').css('width', newWidth + 'px');
}

function handleOptionSelect(event) {
    //透過jquery中的html更改點擊完選項後的文字
    if ('預約運送' !== $.trim(event.target.innerText)) {
        $('#time').html(`<i class="bi bi-stopwatch-fill"></i> ${event.target.innerText} <i class="bi bi-caret-down-fill down"></i>`);
    }
    // 點完選項後可以消失
    $('#customOptions').toggle()
}

function reserve() {
    $('#reserveModal').modal('show')
}

function adminLogin() {
    window.location.href = 'http://localhost:8080/goLoginAdmin'
}

function findRest() {
    var pos = $('#siteInput').val()
    var time = $('#time').text()
    var t
    if ($.trim(time) === "立即運送") {
        var tDate = new Date()
        t = tDate.getHours()

    } else {
        t = parseInt(time.substring(time.indexOf(':00') - 2, time.indexOf(':00')))
        if (time.indexOf('下午') !== -1) {
            t = (t + 12) % 24
            t = t == 0 ? t + 24 : t;
        }
        if (time.indexOf('上午') !== -1 && t == 12) {
            t = t + 12
        }
    }

    window.location.href = 'http://localhost:8080/restaurant/?restLocation=' + pos + '&time=' + t
}

$(document).ready(function () {
    var nowDate = new Date()
    nowDate.setDate(nowDate.getDate() - 1)
    var dayNames = ['日', '一', '二', '三', '四', '五', '六']
    var str;
    for (var i = 0; i < 7; i++) {
        nowDate.setDate(nowDate.getDate() + 1);
        str = ''
        if (i === 0) {
            str = '今天，'
        } else if (i === 1) {
            str = '明天，'
        }
        var option = $('<option>', {
            value: i,
            text: str + (nowDate.getMonth() + 1) + '月/' + nowDate.getDate() + '日 周' + dayNames[nowDate.getDay()]
        })
        $('#chooseDate').append(option)
    }
    $('#chooseDate').val(0)

    updateTimes($('#chooseDate').children().eq(0))

    $("#chooseDate").on("change", function () {
        var selectedDate = $(this).children(":selected");
        console.log(selectedDate)
        updateTimes(selectedDate);
    });
    var customSelect = $('#customSelect');
    var selectButton = $('.select-button');
    customSelect.width(selectButton.outerWidth());
    $(window).on('resize', function () {
        customSelect.width(selectButton.outerWidth());
    });
})


function updateTimes(selectedDate) {
    $('#chooseTime').empty();
    var nowDate = new Date()
    var str = selectedDate.text()
    if (str.indexOf(nowDate.getDate() + '日') !== -1) {
        nowDate.setHours(nowDate.getHours() - 1)
        var i = 0
        var str1, str2
        do {
            nowDate.setHours(nowDate.getHours() + 1);
            str1 = nowDate.getHours() >= 12 ? '下午 ' : '上午 ';
            str2 = nowDate.getHours() + 1 != 12 && nowDate.getHours() + 1 != 24 ? str1 : nowDate.getHours() + 1 == 12 ? '下午' : str1
            var hourStart = nowDate.getHours() % 12 || 12;
            var hourEnd = (nowDate.getHours() + 1) % 12 || 12;
            var option = $('<option>', {
                value: i++,
                text: str1 + hourStart + ':00 - ' + str2 + hourEnd + ':00'
            });
            $('#chooseTime').append(option);
        } while (nowDate.getHours() !== 23)
        $('#chooseTime').val(0);
    } else {
        var i = 0
        nowDate.setHours(24)
        while (nowDate.getHours() !== 23) {
            var hour = nowDate.getHours()
            nowDate.setHours(nowDate.getHours() + 1);
            var str = (hour >= 12) ? '下午 ' : '上午 ';
            var hourStart = hour % 12 || 12;
            var hourEnd = (hour + 1) % 12 || 12;
            var option = $('<option>', {
                value: i++,
                text: str + hourStart + ':00 - ' + str + hourEnd + ':00'
            });
            $('#chooseTime').append(option);
        }
        $('#chooseTime').val(0);
    }
}

function assignTime() {
    var date = $('#chooseDate option:selected').text()
    var time = $('#chooseTime option:selected').text().replace('-', ' 至 ')
    $('#time').html(date + ' ' + time)
    var newWidth = $('#time').outerWidth();
    $('.custom-options').css('width', newWidth + 'px');
}
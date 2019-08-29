$(function () {
    $.ajax({
        url: '/dongfeng/jobData',
        type: 'get',
        dataType: 'json',
        success: function (data) {
            var button = '<div class="btn-group">\n' +
                '  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\n' +
                '    操作 <span class="caret"></span>\n' +
                '  </button>\n' +
                '  <ul class="dropdown-menu">\n' +
                '    <li><a href="#" >编辑</a></li>\n' +
                '    <li><a href="#" >删除</a></li>\n' +
                '  </ul>\n' +
                '</div>';
            $.each(data.respDto, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.jobName + '</td>' + '<td>' + item.jobType + '</td>' + '<td>' + item.shardingCnt + '</td>'
                    + '<td>' + item.dagId + '</td>' + '<td>' + item.scheduleType + '</td>' + '<td>' + item.launchCommand + '</td>'+ '<td>' + item.assignIp + '</td>'
                    + '<td>' + item.parentJobIds + '</td>' + '<td>' + button + '</td>';
                $("#tbody").append('<tr>' + tr + '</tr>')
            })
        }
    })
})

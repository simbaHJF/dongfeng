$(function () {
    /*$.ajax({
        url: '/dongfeng/jobData',
        type: 'get',
        dataType: 'json',
        success: function (data) {
            var button = '<div class="btn-group">\n' +
                '  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\n' +
                '    操作 <span class="caret"></span>\n' +
                '  </button>\n' +
                '  <ul class="dropdown-menu">\n' +
                '    <li><a href="#" onclick="edit_click(this)">编辑</a></li>\n' +
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
    })*/
    getTableDate(1);
})

function getTableDate(pageNum) {
    $.ajax({
        url: '/dongfeng/jobData',
        type: 'get',
        data:{
            'page':pageNum
        },
        dataType: 'json',
        success: function (data) {
            var button = '<div class="btn-group">\n' +
                '  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\n' +
                '    操作 <span class="caret"></span>\n' +
                '  </button>\n' +
                '  <ul class="dropdown-menu">\n' +
                '    <li><a href="#" onclick="edit_click(this)">编辑</a></li>\n' +
                '    <li><a href="#" onclick="delete_click(this)">删除</a></li>\n' +
                '  </ul>\n' +
                '</div>';
            var tableStr = '';
            $.each(data.respDto.list, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.jobName + '</td>' + '<td>' + item.jobType + '</td>' + '<td>' + item.shardingCnt + '</td>'
                    + '<td>' + item.dagId + '</td>' + '<td>' + item.scheduleType + '</td>' + '<td>' + item.launchCommand + '</td>'+ '<td>' + item.assignIp + '</td>'
                    + '<td>' + item.parentJobIds + '</td>' + '<td>' + button + '</td>';
                tableStr = tableStr + '<tr>' + tr + '</tr>';
            })
            $("#tbody").empty();
            $("#tbody").append(tableStr);

            var pages = '';
            for (var i = 1;i <= data.respDto.pages;i++){
                if (i === data.respDto.pageNum) {
                    pages = pages + '<li class="active"><a onclick="page_on_click(this)">' + i + '</a></li>';
                } else {
                    pages = pages + '<li><a onclick="page_on_click(this)">' + i + '</a></li>';
                }
            }
            $("#pagination").empty();
            $("#pagination").append(pages);
        }
    })
}
function page_on_click(data) {
    var page = $(data).html();
    getTableDate(page);
}


function edit_click(data)
{
    var jobId = $(data).parent().parent().parent().parent().parent().children().first().html();
    $.ajax({
        url: '/dongfeng/updateJobPage',
        type: 'get',
        data:{
            'jobId':jobId
        },
        success: function (data) {
            $("#updateJobModal").empty();
            $("#updateJobModal").append(data);
            $("#updateJobModal").modal({
                show: true,
                backdrop:'static'
            })
        }
    })
}


function checkDisable() {
    $(":radio").each(function() {
        $(this).attr("disabled", false);
    });
    return true;
}

function delete_click(data) {
    var jobId = $(data).parent().parent().parent().parent().parent().children().first().html();
    $.ajax({
        url: '/dongfeng/deleteJobInfo',
        type: 'get',
        data:{
            'jobId':jobId
        },
        success: function (data) {
            if (data.code === 200) {
                self.location.reload();
            } else {
                alert(data.msg);
            }
        }
    })
}
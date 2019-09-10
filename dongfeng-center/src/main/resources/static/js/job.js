$(function () {
    getTableData(1,0);
})

function getTableData(pageNum, dagId) {
    $.ajax({
        url: '/dongfeng/admin/jobData',
        type: 'post',
        data:{
            'page':pageNum,
            'dagId':dagId
        },
        dataType: 'json',
        success: function (data) {
            var button = '<div class="btn-group">\n' +
                '  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\n' +
                '    操作 <span class="caret"></span>\n' +
                '  </button>\n' +
                '  <ul class="dropdown-menu">\n' +
                '    <li><a href="#" onclick="edit_job_page(this)">编辑</a></li>\n' +
                '    <li><a href="#" onclick="delete_job_submit(this)">删除</a></li>\n' +
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
                    pages = pages + '<li class="active"><a onclick="page_on_click(this,' + dagId + ')">' + i + '</a></li>';
                } else {
                    pages = pages + '<li><a onclick="page_on_click(this,' + dagId + ')">' + i + '</a></li>';
                }
            }
            $("#pagination").empty();
            $("#pagination").append(pages);
        }
    })
}
function page_on_click(data,dagId) {
    var page = $(data).html();
    getTableData(page,dagId);
}

function add_job_submit() {
    $.ajax({
        type: "post",   //提交的方法
        url:"/dongfeng/admin/addJob", //提交的地址
        data:$('#addJobForm').serialize(),// 序列化表单值
        async: false,
        error: function(request) {  //失败的话
            alert("Connection error");
        },
        success: function(data) {  //成功
            if (data.code === 200) {
                self.location.reload();
            } else {
                alert(data.msg);
            }
        }
    });
}

function update_job_submit() {
    checkDisable();
    $.ajax({
        type: "post",   //提交的方法
        url:"/dongfeng/admin/updateJobInfo", //提交的地址
        data:$('#updateJobForm').serialize(),// 序列化表单值
        async: false,
        error: function(request) {  //失败的话
            alert("Connection error");
        },
        success: function(data) {  //成功
            if (data.code === 200) {
                self.location.reload();
            } else {
                alert(data.msg);
            }
        }
    });

}

function edit_job_page(data)
{
    var jobId = $(data).parent().parent().parent().parent().parent().children().first().html();
    $.ajax({
        url: '/dongfeng/admin/updateJobPage',
        type: 'post',
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

function delete_job_submit(data) {
    var jobId = $(data).parent().parent().parent().parent().parent().children().first().html();
    $.ajax({
        url: '/dongfeng/admin/deleteJobInfo',
        type: 'post',
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

function searchJobsByDagId() {
    var dagId;
    if ($("#searchJobsByDagId").val() == '') {
        dagId = 0;
    } else {
        dagId = $("#searchJobsByDagId").val();
    }
    getTableData(1, dagId);
}
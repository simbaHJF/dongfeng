$(function () {
    getTableDate(1);
})

function getTableDate(pageNum) {
    $.ajax({
        url: '/dongfeng/admin/dagData',
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
                '    <li><a href="#" onclick="edit_dag_page(this)">编辑</a></li>\n' +
                '    <li><a href="#" onclick="delete_dag_submit(this)">删除</a></li>\n' +
                '    <li><a href="#" onclick="manual_trigger_click(this)">手动触发</a></li>\n' +
                '  </ul>\n' +
                '</div>';
            var tableStr = '';
            $.each(data.respDto.list, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.dagName + '</td>' + '<td>' + item.dagGroup + '</td>' + '<td>' + item.dagCron + '</td>'
                    + '<td>' + item.status + '</td>'+ '<td>' + item.triggerTime + '</td>' + '<td>' + item.param + '</td>' + '<td>' + button + '</td>';
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

function add_dag_submit() {
    $.ajax({
        type: "get",   //提交的方法
        url:"/dongfeng/admin/addDag", //提交的地址
        data:$('#addDagForm').serialize(),// 序列化表单值
        async: false,
        error: function(request) {  //失败的话
            alert("Connection error");
        },
        success: function(data) {  //成功
            self.location.reload();
        }
    });
}

function edit_dag_submit() {
    $.ajax({
        type: "get",   //提交的方法
        url:"/dongfeng/admin/updateDagInfo", //提交的地址
        data:$('#updateDagForm').serialize(),// 序列化表单值
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

function edit_dag_page(data)
{
    var dagId = $(data).parent().parent().parent().parent().parent().children().first().html();

    $.ajax({
        url: '/dongfeng/admin/updateDagPage',
        type: 'get',
        data:{
            'dagId':dagId
        },
        success: function (data) {
            $("#updateDagModal").empty();
            $("#updateDagModal").append(data);
            $("#updateDagModal").modal({
                show: true,
                backdrop:'static'
            })
        }
    })
}


function delete_dag_submit(data) {
    var dagId = $(data).parent().parent().parent().parent().parent().children().first().html();
    $.ajax({
        url: '/dongfeng/admin/deleteDagInfo',
        type: 'get',
        data:{
            'dagId':dagId
        },
        success: function(data) {  //成功
            if (data.code === 200) {
                self.location.reload();
            } else {
                alert(data.msg);
            }
        }
    })
}

function manual_trigger_click(data) {
    var dagId = $(data).parent().parent().parent().parent().parent().children().first().html();
    $("#manualTriggerDagId").val(dagId);
    $("#manualTriggerDagParam").val('');
    $("#manualTriggerDagModal").modal({
        show: true,
        backdrop:'static'
    })
}

function manual_trigger_submit() {
    $.ajax({
        url: '/dongfeng/admin/manualTrigger',
        type: 'get',
        data:$('#manualTriggerDagForm').serialize(),
        success: function(data) {  //成功
            if (data.code === 200) {
                self.location.reload();
            } else {
                alert(data.msg);
            }
        }
    })
}
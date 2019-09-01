$(function () {
    /*$.ajax({
        url: '/dongfeng/dagData',
        type: 'get',
        data:{
            'page':1
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
                '    <li><a href="#">手动触发</a></li>\n' +
                '  </ul>\n' +
                '</div>';
            $.each(data.respDto.list, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.dagName + '</td>' + '<td>' + item.dagGroup + '</td>' + '<td>' + item.dagCron + '</td>'
                    + '<td>' + item.status + '</td>' + '<td>' + item.param + '</td>' + '<td>' + button + '</td>';
                $("#tbody").append('<tr>' + tr + '</tr>')
            })
        }
    })*/
    getTableDate(1);
})

function getTableDate(pageNum) {
    $.ajax({
        url: '/dongfeng/dagData',
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
                '    <li><a href="#">手动触发</a></li>\n' +
                '  </ul>\n' +
                '</div>';
            var tableStr = '';
            $.each(data.respDto.list, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.dagName + '</td>' + '<td>' + item.dagGroup + '</td>' + '<td>' + item.dagCron + '</td>'
                    + '<td>' + item.status + '</td>' + '<td>' + item.param + '</td>' + '<td>' + button + '</td>';
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
    var dagId = $(data).parent().parent().parent().parent().parent().children().first().html();

    $.ajax({
        url: '/dongfeng/updateDagPage',
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


function delete_click(data) {
    var dagId = $(data).parent().parent().parent().parent().parent().children().first().html();
    $.ajax({
        url: '/dongfeng/deleteDagInfo',
        type: 'get',
        data:{
            'dagId':dagId
        },
        success: function (data) {
            self.location.reload();
        }
    })
}
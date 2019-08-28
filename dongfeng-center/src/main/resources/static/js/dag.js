$(function () {
    $.ajax({
        url: '/dongfeng/dagData',
        type: 'get',
        dataType: 'json',
        success: function (data) {
            var button = '<div class="btn-group">\n' +
                '  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">\n' +
                '    操作 <span class="caret"></span>\n' +
                '  </button>\n' +
                '  <ul class="dropdown-menu">\n' +
                '    <li><a href="#" onclick="edit_click(this)">编辑</a></li>\n' +
                '    <li><a href="#">删除</a></li>\n' +
                '    <li><a href="#">手动触发</a></li>\n' +
                '  </ul>\n' +
                '</div>';
            $.each(data.respDto, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.dagName + '</td>' + '<td>' + item.dagGroup + '</td>' + '<td>' + item.dagCron + '</td>'
                    + '<td>' + item.status + '</td>' + '<td>' + item.param + '</td>' + '<td>' + button + '</td>';
                $("#tbody").append('<tr>' + tr + '</tr>')
            })
        }
    })
})


function edit_click(data)
{
    var dagId = $(data).parent().parent().parent().parent().parent().children().first().html();

    $.ajax({
        url: '/dongfeng/dagData',
        type: 'get',
        data:{
            'dagId':dagId
        },
        success: function (data) {
            $("#updateDagModal").empty();
            $("#updateDagModal").append(data);
            $("#updateDagModal").attr("aria-hidden", true);
        }
    })
}
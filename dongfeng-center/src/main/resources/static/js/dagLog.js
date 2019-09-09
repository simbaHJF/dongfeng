$(function () {
    getTableData(1,0);
})

function getTableData(pageNum, dagId) {

    var dagdag = dagId;
    $.ajax({
        url: '/dongfeng/admin/dagLogData',
        type: 'post',
        data:{
            'page':pageNum,
            'dagId':dagId
        },
        dataType: 'json',
        success: function (data) {

            var tableStr = '';
            $.each(data.respDto.list, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.dagId + '</td>'+ '<td>' + item.dagName + '</td>' + '<td>' + item.triggerType + '</td>' +
                    '<td>' + item.startTime + '</td>' + '<td>' + item.endTime + '</td>' + '<td>' + item.status + '</td>' + '<td>' + item.param + '</td>';
                tableStr = tableStr + '<tr>' + tr + '</tr>';
            })
            $("#tbody").empty();
            $("#tbody").append(tableStr);

            var pages = '';
            for (var i = 1;i <= data.respDto.pages;i++){
                if (i === data.respDto.pageNum) {
                    pages = pages + '<li class="active"><a onclick="page_on_click(this,' + dagdag + ')">' + i + '</a></li>';
                } else {
                    pages = pages + '<li><a onclick="page_on_click(this,' + dagdag + ')">' + i + '</a></li>';
                }
            }
            $("#pagination").empty();
            $("#pagination").append(pages);
        }
    });
}

function page_on_click(data,dagId) {
    var page = $(data).html();
    getTableData(page,dagId);
}

function searchDagLogsByDagId() {
    var dagId;
    if ($("#searchDagLogsByDagId").val() == '') {
        dagId = 0;
    } else {
        dagId = $("#searchDagLogsByDagId").val();
    }
    getTableData(1, dagId);
}
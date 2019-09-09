$(function () {
    getTableData(1,0);
})

function getTableData(pageNum,dagLogId) {

    $.ajax({
        url: '/dongfeng/admin/jobLogData',
        type: 'post',
        data:{
            'page':pageNum,
            'dagLogId':dagLogId
        },
        dataType: 'json',
        success: function (data) {

            var tableStr = '';
            $.each(data.respDto.list, function (index, item) {
                var tr;
                tr = '<td>' + item.id + '</td>' + '<td>' + item.jobId + '</td>' + '<td>' + item.jobName + '</td>' + '<td>' + item.dagId + '</td>' + '<td>' + item.dagTriggerId + '</td>'
                    + '<td>' + item.startTime + '</td>' + '<td>' + item.endTime + '</td>' + '<td>' + item.status + '</td>' + '<td>' + item.centerIp + '</td>'
                    + '<td>' + item.executorIp + '</td>' + '<td>' + item.shardingIdx + '</td>' + '<td>' + item.shardingCnt + '</td>' + '<td>' + item.param + '</td>';
                tableStr = tableStr + '<tr>' + tr + '</tr>';
            })
            $("#tbody").empty();
            $("#tbody").append(tableStr);

            var pages = '';
            for (var i = 1;i <= data.respDto.pages;i++){
                if (i === data.respDto.pageNum) {
                    pages = pages + '<li class="active"><a onclick="page_on_click(this,' + dagLogId + ')">' + i + '</a></li>';
                } else {
                    pages = pages + '<li><a onclick="page_on_click(this,' + dagLogId + ')">' + i + '</a></li>';
                }
            }
            $("#pagination").empty();
            $("#pagination").append(pages);
        }
    })
}
function page_on_click(data,dagLogId) {
    var page = $(data).html();
    getTableData(page,dagLogId);
}

function searchJobLogsByDagLogId() {

    var dagLogId;
    if ($("#searchJobLogsByDagLogId").val() == '') {
        dagLogId = 0;
    } else {
        dagLogId = $("#searchJobLogsByDagLogId").val();
    }
    getTableData(1, dagLogId);

}
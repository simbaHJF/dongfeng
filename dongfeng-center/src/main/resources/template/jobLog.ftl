<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>dagLog</title>

    <script type="text/javascript" src="/dongfeng/js/jquery-3.4.1.js"></script>

    <link rel="stylesheet" type="text/css" href="/dongfeng/css/bootstrap.min.css">
    <script type="text/javascript" src="/dongfeng/js/bootstrap.min.js"></script>


</head>
<body>

<div class="row">
    <div class="col-lg-6" style="padding: 65px;">
        <div class="input-group">
            <input type="text" class="form-control" placeholder="Search by dagId" id="searchJobLogsByDagLogId">
            <span class="input-group-btn">
        <button class="btn btn-primary btn-default" type="button" onclick="searchJobLogsByDagLogId()">Go!</button>
      </span>
        </div><!-- /input-group -->
    </div><!-- /.col-lg-6 -->
</div><!-- /.row -->

<div class="row" style="padding-left: 60px;padding-right: 60px;">
    <p class="bg-info" style="font-size:20px"> status : 1为运行中 ; 2为成功 ; 3为失败 </p>
</div>

<div class="row" style="padding-left: 50px;padding-right: 50px;">

    <div class="col-xs-12">
        <div class="box">
            <h3 class="box-title">DagLog列表</h3>
        </div>
        <div class="box-body">
            <table id="job_log_list" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                    <th name="id">id</th>
                    <th name="jobId">jobId</th>
                    <th name="jobName">jobName</th>
                    <th name="dagId">dagId</th>
                    <th name="dagLogId">dagLogId</th>
                    <th name="startTime">startTime</th>
                    <th name="endTime">endTime</th>
                    <th name="status">status</th>
                    <th name="centerIp">centerIp</th>
                    <th name="executorIp">executorIp</th>
                    <th name="shardingIdx">shardingIdx</th>
                    <th name="shardingCnt">shardingCnt</th>
                    <th name="param">param</th>
                </tr>
                </thead>
                <tbody id="tbody"></tbody>
                <tfoot></tfoot>
            </table>
        </div>
    </div>
</div>
<div style="padding-left: 50px;padding-right: 50px;">
    <nav aria-label="Page navigation">
        <ul class="pagination" id="pagination">
        </ul>
    </nav>
</div>
</div>
<script type="text/javascript" src="/dongfeng/js/jobLog.js"></script>
</body>
</html>

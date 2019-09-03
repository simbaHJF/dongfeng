<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>dagLog</title>
    <script type="text/javascript" src="js/jquery-3.4.1.js"></script>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <#--<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">-->
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <#--<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>-->
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <script type="text/javascript" src="js/bootstrap.min.js"></script>


</head>
<body>

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
                    <th name="dagId">dagId</th>
                    <th name="dagTriggerId">dagTriggerId</th>
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
<script type="text/javascript" src="js/jobLog.js"></script>
</body>
</html>
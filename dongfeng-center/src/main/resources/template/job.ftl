<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>job配置</title>


    <script type="text/javascript" src="/dongfeng/js/jquery-3.4.1.js"></script>


    <link rel="stylesheet" type="text/css" href="/dongfeng/css/bootstrap.min.css">
    <script type="text/javascript" src="/dongfeng/js/bootstrap.min.js"></script>


</head>
<body>

<div class="row">
    <div class="col-lg-6" style="padding: 65px;">
        <div class="input-group">
            <input type="text" class="form-control" placeholder="Search by dagId" id="searchJobsByDagId">
            <span class="input-group-btn">
        <button class="btn btn-primary btn-default" type="button" onclick="searchJobsByDagId()">Go!</button>
      </span>
        </div><!-- /input-group -->
    </div><!-- /.col-lg-6 -->
</div><!-- /.row -->

<div style="padding: 50px;">
    <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#addJobModal">新增job配置</button>
</div>

<div class="modal fade bs-example-modal-lg" id="addJobModal" tabindex="-1" role="dialog" aria-labelledby="addJobModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content modal-div">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="addJobModalLabel">新增job配置</h4>
            </div>
            <div class="modal-body">新增job配置</div>


            <div id="modal-div" style="margin: 50px">
                <form method="post" id="addJobForm">
                    <div class="form-group">
                        <label for="jobName" class="text-danger">* Job name</label>
                        <input type="text" class="form-control" name="jobName" placeholder="Job name">
                    </div>
                    <div class="form-group">
                        <label for="jobType" class="text-danger">* 任务节点类型</label>
                        <div class="radio">
                            <label>
                                <input type="radio" name="jobType" value="1" checked> 开始节点
                            </label>
                            &emsp;&emsp;
                            <label>
                                <input type="radio" name="jobType" value="2" > 任务节点
                            </label>
                            &emsp;&emsp;
                            <label>
                                <input type="radio" name="jobType" value="3" > 结束节点
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="shardingCnt" class="text-danger">* Sharding cnt</label>
                        <input type="text" class="form-control" name="shardingCnt" value="1" readonly>
                    </div>
                    <div class="form-group">
                        <label for="dagId" class="text-danger">* Dag id</label>
                        <input type="text" class="form-control" name="dagId" placeholder="所属dag的id">
                    </div>
                    <div class="form-group">
                        <label for="scheduleType" class="text-danger">* 调度方式</label>
                        <div class="radio">
                            <label>
                                <input type="radio" name="scheduleType"  value="random" checked> 随机
                            </label>
                            &emsp;&emsp;
                            <label>
                                <input type="radio" name="scheduleType"  value="assign"> 指定ip
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="assignIp">Assign ip</label>
                        <input type="text" class="form-control" name="assignIp" placeholder="指定ip">
                    </div>
                    <div class="form-group">
                        <label for="launchCommand">Launch command</label>
                        <input type="text" class="form-control" name="launchCommand" placeholder="启动命令">
                    </div>
                    <div class="form-group">
                        <label for="parentJobIds">Parent job ids</label>
                        <input type="text" class="form-control" name="parentJobIds" placeholder="父job的id,多个用英文逗号分割;开始节点没有父job">
                    </div>


                    <button type="button" class="btn btn-default" id="addDag" onclick="add_job_submit()">Submit</button>
                    <button type="reset" class="btn btn-default">Reset</button>
                </form>
            </div>
        </div>
    </div>
</div>



<div class="modal fade bs-example-modal-lg" id="updateJobModal" tabindex="-1" role="dialog" aria-labelledby="updateJobModalLabel"
     aria-hidden="true">

</div>



<div class="row" style="padding: 50px;">

    <div class="col-xs-12">
        <div class="box">
            <h3 class="box-title">Job列表</h3>
        </div>
        <div class="box-body">
            <table id="job_list" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                    <th name="id">id</th>
                    <th name="jobName">jobName</th>
                    <th name="jobType">jobType</th>
                    <th name="shardingCnt">shardingCnt</th>
                    <th name="dagId">dagId</th>
                    <th name="scheduleType">scheduleType</th>
                    <th name="launchCommand">launchCommand</th>
                    <th name="assignIp">assignIp</th>
                    <th name="parentJobIds">parentJobId</th>
                    <th name="operation">operation</th>
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
<script type="text/javascript" src="/dongfeng/js/job.js"></script>
</body>
</html>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>dag配置</title>

    <script type="text/javascript" src="/dongfeng/js/jquery-3.4.1.js"></script>


    <link rel="stylesheet" type="text/css" href="/dongfeng/css/bootstrap.min.css">
    <script type="text/javascript" src="/dongfeng/js/bootstrap.min.js"></script>


</head>
<body>

<div style="padding: 50px;">
    <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#addDagModal">新增dag配置</button>
</div>

<div class="modal fade bs-example-modal-lg" id="addDagModal" tabindex="-1" role="dialog"
     aria-labelledby="addDagModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content modal-div">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="addDagModalLabel">新增dag配置</h4>
            </div>
            <div class="modal-body">新增dag配置,开关务必先置位关闭,待任务job配置完成再开启</div>


            <div id="modal-div" style="margin: 50px">
                <form method="post" id="addDagForm">
                    <div class="form-group">
                        <label for="dagName" class="text-danger">* Dag name</label>
                        <input type="text" class="form-control" id="addDagName" name="dagName" placeholder="Dag name">
                    </div>
                    <div class="form-group">
                        <label for="dagGroup" class="text-danger">* Dag group</label>
                        <input type="text" class="form-control" id="addDagGroup" name="dagGroup"
                               placeholder="Dag group">
                    </div>
                    <div class="form-group">
                        <label for="dagCron" class="text-danger">* Dag cron</label>
                        <input type="text" class="form-control" id="addDagCron" name="dagCron" placeholder="Dag cron">
                    </div>
                    <div class="form-group">
                        <label for="dagParam">Dag param</label>
                        <input type="text" class="form-control" id="addDagParam" name="param" placeholder="Dag param">
                    </div>
                    <div class="form-group">
                        <label for="dagAlarm">Dag alarm</label>
                        <input type="text" class="form-control" id="addDagAlarm" name="alarm" placeholder="Dag alarm">
                    </div>
                    <div class="form-group">
                        <label for="status" class="text-danger">* dag开关</label>
                        <div class="radio">
                            <label>
                                <input type="radio" name="status" id="addDagstatusOn" value="1" checked> 关闭
                            </label>
                            &emsp;&emsp;
                            <label>
                                <input type="radio" name="status" id="addDagstatusOff" value="2"> 开启
                            </label>
                        </div>
                    </div>


                    <button type="button" class="btn btn-default" id="addDag" onclick="add_dag_submit()">Submit</button>
                    <button type="reset" class="btn btn-default">Reset</button>
                </form>
            </div>
        </div>
    </div>
</div>


<div class="modal fade bs-example-modal-lg" id="updateDagModal" tabindex="-1" role="dialog"
     aria-labelledby="updateDagModalLabel"
     aria-hidden="true">
</div>

<div class="modal fade bs-example-modal-lg" id="manualTriggerDagModal" tabindex="-1" role="dialog"
     aria-labelledby="manualTriggerDagModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content modal-div">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="manualTriggerDagModalLabel">手动触发dag</h4>
            </div>


            <div id="modal-div" style="margin: 50px">
                <form method="post" id="manualTriggerDagForm">
                    <div class="form-group">
                        <label for="id">Dag id</label>
                        <input type="text" class="form-control" id="manualTriggerDagId" name="dagId" readonly>
                    </div>
                    <div class="form-group">
                        <label for="manualTriggerDagParam" class="text-danger">Param</label>
                        <input type="text" class="form-control" id="manualTriggerDagParam" name="manualTriggerDagParam">
                    </div>
                    <button type="button" class="btn btn-default" onclick="manual_trigger_submit()">launch</button>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="row" style="padding-left: 60px;padding-right: 60px;">
    <p class="bg-info" style="font-size:20px"> status : 1为关闭 ; 2为开启 </p>
</div>

<div class="row" style="padding-left: 50px;padding-right: 50px;">

    <div class="col-xs-12">
        <div class="box">
            <h3 class="box-title">Dag列表</h3>
        </div>
        <div class="box-body">
            <table id="dag_list" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                    <th name="id">id</th>
                    <th name="dagName">dagName</th>
                    <th name="dagGroup">dagGroup</th>
                    <th name="dagCron">dagCron</th>
                    <th name="status">status</th>
                    <th name="triggerTime">triggerTime</th>
                    <th name="param">param</th>
                    <th name="operation">operation</th>
                    <th name="alarm">alarm</th>
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
<script type="text/javascript" src="/dongfeng/js/dag.js"></script>
</body>
</html>

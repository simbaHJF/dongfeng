<div class="modal-dialog modal-lg">
    <div class="modal-content modal-div">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" id="updateDagModalLabel">更新dag配置</h4>
        </div>


        <div id="modal-div" style="margin: 50px">
            <form method="post" id="updateDagForm">
                <div class="form-group">
                    <label for="id">Dag id</label>
                    <input type="text" class="form-control" id="updateDagId" name="id" placeholder="Dag id"
                           value="${dagInfo.id}" readonly>
                </div>
                <div class="form-group">
                    <label for="dagName" class="text-danger">* Dag name</label>
                    <input type="text" class="form-control" id="updateDagName" name="dagName" placeholder="Dag name"
                           value="${dagInfo.dagName}">
                </div>
                <div class="form-group">
                    <label for="dagGroup" class="text-danger">* Dag group</label>
                    <input type="text" class="form-control" id="updateDagGroup" name="dagGroup" placeholder="Dag group"
                           value="${dagInfo.dagGroup}">
                </div>
                <div class="form-group">
                    <label for="dagCron" class="text-danger">* Dag cron</label>
                    <input type="text" class="form-control" id="updateDagCron" name="dagCron" placeholder="Dag cron"
                           value="${dagInfo.dagCron}">
                </div>
                <div class="form-group">
                    <label for="dagParam">Dag param</label>
                    <input type="text" class="form-control" id="updateDagParam" name="param" placeholder="Dag param"
                           value="${dagInfo.param}">
                </div>
                <div class="form-group">
                    <label for="dagAlarm">Dag alarm</label>
                    <input type="text" class="form-control" id="updateDagAlarm" name="alarm" placeholder="Dag alarm"
                            value="${dagInfo.alarm}">
                </div>

                <div class="form-group">
                    <label for="status" class="text-danger">* dag开关</label>
                    <div class="radio">
                        <label>
                            <input type="radio" name="status" id="updateDagStatusOn" value="1"
                                   <#if  dagInfo.status== 1>checked </#if>> 关闭
                        </label>
                        &emsp;&emsp;
                        <label>
                            <input type="radio" name="status" id="updateDagStatusOn" value="2"
                                   <#if  dagInfo.status== 2>checked </#if>> 开启
                        </label>
                    </div>
                </div>

                <button type="button" class="btn btn-default" onclick="edit_dag_submit()">Submit</button>
            </form>
        </div>
    </div>
</div>
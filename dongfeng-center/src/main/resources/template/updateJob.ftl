<div class="modal-dialog modal-lg">
    <div class="modal-content modal-div">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title" id="addJobModalLabel">新增job配置</h4>
        </div>
        <div class="modal-body">新增job配置</div>


        <div id="modal-div" style="margin: 50px">
            <form method="post" action="updateJobInfo" onsubmit="return checkDisable();">
                <div class="form-group">
                    <label for="id">* Job id</label>
                    <input type="text" class="form-control" name="id" placeholder="Job id" value="${jobInfo.id}" readonly>
                </div>
                <div class="form-group">
                    <label for="jobName" class="text-danger">* Job name</label>
                    <input type="text" class="form-control" name="jobName" placeholder="Job name" value="${jobInfo.jobName}">
                </div>
                <div class="form-group">
                    <label for="jobType" class="text-danger">* 任务节点类型</label>
                    <div class="radio">
                        <label>
                            <input type="radio" name="jobType" value="1" <#if  jobInfo.jobType == 1>checked </#if> disabled> 开始节点
                        </label>
                        &emsp;&emsp;
                        <label>
                            <input type="radio" name="jobType" value="2" <#if  jobInfo.jobType == 2>checked </#if> disabled> 任务节点
                        </label>
                        &emsp;&emsp;
                        <label>
                            <input type="radio" name="jobType" value="3" <#if  jobInfo.jobType == 3>checked </#if> disabled> 结束节点
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="shardingCnt" class="text-danger">* Sharding cnt</label>
                    <input type="text" class="form-control" name="shardingCnt" value="${jobInfo.shardingCnt}" readonly>
                </div>
                <div class="form-group">
                    <label for="dagId" class="text-danger">* Dag id</label>
                    <input type="text" class="form-control" name="dagId" placeholder="所属dag的id" value="${jobInfo.dagId}" readonly>
                </div>
                <div class="form-group">
                    <label for="scheduleType" class="text-danger">* 调度方式</label>
                    <div class="radio">
                        <label>
                            <input type="radio" name="scheduleType"  value="random" <#if jobInfo.scheduleType == 'random'>checked </#if>> 随机
                        </label>
                        &emsp;&emsp;
                        <label>
                            <input type="radio" name="scheduleType"  value="assign" <#if jobInfo.scheduleType == 'assign'>checked </#if>> 指定ip
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="assignIp">Assign ip</label>
                    <input type="text" class="form-control" name="assignIp" placeholder="指定ip" value="${jobInfo.assignIp}">
                </div>
                <div class="form-group">
                    <label for="launchCommand" class="text-danger">* Launch command</label>
                    <input type="text" class="form-control" name="launchCommand" placeholder="启动命令" value="${jobInfo.launchCommand}">
                </div>
                <div class="form-group">
                    <label for="parentJobIds">Parent job ids</label>
                    <input type="text" class="form-control" name="parentJobIds" value="${jobInfo.parentJobIds}" placeholder="父job的id,多个用英文逗号分割;开始节点没有父job" readonly>
                </div>


                <button type="submit" class="btn btn-default">Submit</button>
            </form>
        </div>
    </div>
</div>
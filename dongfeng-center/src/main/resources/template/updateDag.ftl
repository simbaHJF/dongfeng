    <div class="modal-dialog modal-lg">
        <div class="modal-content modal-div">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="updateDagModalLabel">更新dag配置</h4>
            </div>


            <div id="modal-div" style="margin: 50px">
                <form>
                    <div class="form-group">
                        <label for="dagName">Dag name</label>
                        <input type="text" class="form-control" id="updateDagName" name="dagName" placeholder="Dag name" value="${dag.}">
                    </div>
                    <div class="form-group">
                        <label for="dagGroup">Dag group</label>
                        <input type="text" class="form-control" id="updateDagGroup" name="dagGroup" placeholder="Dag group">
                    </div>
                    <div class="form-group">
                        <label for="dagCron">Dag cron</label>
                        <input type="text" class="form-control" id="updateDagCron" name="dagCron" placeholder="Dag cron">
                    </div>
                    <div class="form-group">
                        <label for="dagParam">Dag param</label>
                        <input type="text" class="form-control" id="updateDagParam" name="dagParam" placeholder="Dag param">
                    </div>
                    <div class="radio">
                        <label>
                            <input type="radio" name="status" id="updateDagStatusOn" value="1" checked> 关闭
                        </label>
                        &emsp;&emsp;
                        <label>
                            <input type="radio" name="status" id="updateDagStatusOff" value="2"> 开启
                        </label>
                    </div>

                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
        </div>
    </div>
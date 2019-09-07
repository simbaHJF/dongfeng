### 当前问题列表
1.  执行器心跳更新sql是replace,造成executor表的id一直上涨
2.  dag拉取的间隔是30秒,所以有可能配置触发时间距离配置时间很近的任务时,拉取不到,无法触发
3.  目前job配置时填写的是launch command,导致可以执行执行器节点下的任意脚本和命令,有不安全风险,
后面会考虑改为job配置时填写启动脚本的文件名,执行器节点只执行dongfeng.executor.sh.repo.path路径下
的文件.



### 注意事项
1.  executor中的任务,需要保证幂等性(调度中心来保证exactly-once太难了)
2.  

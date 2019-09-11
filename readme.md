### 当前问题列表
1.  执行器心跳更新sql是replace,造成executor表的id一直上涨
2.  dag拉取的间隔是30秒,所以有可能配置触发时间距离配置时间很近的任务时,拉取不到,无法触发



### 注意事项
1.  executor中的任务,需要保证幂等性(调度中心来保证exactly-once太难了)
2.  logback配置文件中的日志文件地址要写绝对路径
3.  执行脚本中启动java jar时,-cp或-jar后续路径参数要写绝对路径
4.  center拉取dag的窗口为60秒,若center拉取完dag,还未进行调度,此时宕机,则任务将无法完成调度.
5.  对于java任务,如果认为任务执行失败,一定要在最外层抛出异常,不要吞掉异常,
否则调度系统认为任务执行成功.对于shell同理,也要确保如果shell执行失败,异常会抛出.
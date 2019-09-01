CREATE TABLE `dag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `dag_name` varchar(50) NOT NULL COMMENT 'dag名称',
  `dag_group` varchar(50) NOT NULL COMMENT 'dag业务组',
  `dag_cron` varchar(20) NOT NULL COMMENT '触发cron',
  `status` int(11) NOT NULL COMMENT 'dag流是否开启',
  `trigger_time` datetime DEFAULT NULL COMMENT '下次触发时间',
  `param` varchar(200) DEFAULT '' COMMENT '任务参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Dag表';


CREATE TABLE `dag_trigger_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `dag_id` bigint(20) NOT NULL COMMENT 'dag的id',
  `trigger_type` int(11) NOT NULL COMMENT '触发类型',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` int(11) NOT NULL COMMENT 'dag流状态',
  `param` varchar(200) DEFAULT '' COMMENT '执行参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='dag流调度log表';


CREATE TABLE `dependency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `job_id` bigint(20) NOT NULL COMMENT 'job表id',
  `parent_job_id` bigint(20) NOT NULL COMMENT '依赖父job的id',
  `dag_id` bigint(20) NOT NULL COMMENT '所属dagId',
  PRIMARY KEY (`id`),
  KEY `idx_job_id` (`job_id`),
  KEY `idx_parent_id` (`parent_job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务依赖表';


CREATE TABLE `executor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `executor_name` varchar(100) NOT NULL COMMENT '执行器名称',
  `executor_ip` varchar(20) NOT NULL COMMENT '执行器ip',
  `executor_port` varchar(10) NOT NULL COMMENT '执行器端口',
  `executor_group` varchar(50) NOT NULL COMMENT '执行器分组',
  `active_time` datetime NOT NULL COMMENT '执行器上次活跃时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_executor_ip` (`executor_ip`),
  KEY `idx_executor_group` (`executor_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='执行器表';


CREATE TABLE `job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `job_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_type` int(11) NOT NULL COMMENT '任务类型',
  `sharding_cnt` int(11) NOT NULL COMMENT '分片数',
  `dag_id` bigint(20) NOT NULL COMMENT '对应dag流id',
  `schedule_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度策略',
  `launch_command` varchar(200) NOT NULL COMMENT '任务启动命令',
  `assign_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '指定的执行ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';


CREATE TABLE `job_trigger_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `dag_id` bigint(20) NOT NULL COMMENT 'dag的id',
  `dag_trigger_id` bigint(20) NOT NULL COMMENT 'dag触发id',
  `start_time` datetime NOT NULL COMMENT '任务开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  `status` int(11) NOT NULL COMMENT '任务状态',
  `center_ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度中心ip',
  `executor_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务执行节点ip',
  `sharding_idx` int(11) NOT NULL COMMENT '分片序号',
  `sharding_cnt` int(11) NOT NULL COMMENT '分片总数',
  `param` varchar(200) DEFAULT '' COMMENT '执行参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_job_dag_trigger` (`job_id`,`dag_trigger_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Job触发日志表';
package com.simba.dongfeng.executor.pojo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * DATE:   2019/9/6 15:49
 * AUTHOR: simba.hjf
 * DESC:    job执行记录池,用于保存在本executor上执行过的任务,以便有可能的中断操作.
 **/
public class JobRecordPool {
    private Map<Long, JobRecord> map = new HashMap<>();
    private Lock poolLock = new ReentrantLock();

    public void lockPool() {
        poolLock.lock();
    }

    public void unlockPool() {
        poolLock.unlock();
    }

    public JobRecord putJobRecord(JobRecord jobRecord) {
        try {
            poolLock.lock();
            return map.put(jobRecord.getJobInfo().getJobTriggerLogId(), jobRecord);
        } finally {
            poolLock.unlock();
        }
    }

    public JobRecord getJobRecord(long jobLogId) {
        try {
            poolLock.lock();
            return map.get(jobLogId);
        } finally {
            poolLock.unlock();
        }
    }

    public JobRecord removeJobRecord(long jobLogId) {
        try {
            poolLock.lock();
            return map.remove(jobLogId);
        }finally {
            poolLock.unlock();
        }
    }


    public void deleteExpireJobRecord() {
        try {
            poolLock.lock();
            LocalDateTime localDateTime = LocalDateTime.now();
            Date deadLine = Timestamp.valueOf(localDateTime.minusHours(1));
            Iterator<Map.Entry<Long, JobRecord>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                JobRecord jobRecord = iterator.next().getValue();
                if (jobRecord.getEndTime() != null && jobRecord.getEndTime().before(deadLine)) {
                    iterator.remove();
                }
            }
        } finally {
            poolLock.unlock();
        }
    }
}

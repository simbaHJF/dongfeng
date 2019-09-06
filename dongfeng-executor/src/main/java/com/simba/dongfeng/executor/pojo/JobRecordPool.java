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
 * DESC:
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
            return map.put(jobRecord.getJobLogId(), jobRecord);
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


    public void deleteExpireJobRecord() {
        try {
            poolLock.lock();
            Date deadLine = Timestamp.valueOf(LocalDateTime.now().minusHours(1));
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

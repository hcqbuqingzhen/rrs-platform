package com.rrs.common.core.lock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * 锁对象
 * DistributedLock提供对不同分布式锁的包装,会有redis实现和zk实现,DistributedLock将第三方的实现返回的锁包装.提供对外统一的形式.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DistributedLock {
    private String key;
    private long waitTime;
    private long leaseTime;
    private  TimeUnit unit;
    private boolean isFair;
    /**
     * 具体实现类
     */
    private  DistributedLockAdapter locker;

    public boolean tryLock() throws Exception{
        return locker.tryLock(key,waitTime,leaseTime,unit,isFair);
    }

    public void lock() throws Exception{
        locker.lock(key,waitTime,leaseTime,unit,isFair);
    }

    public void unlock() throws Exception {
        locker.unlock();
    }


}

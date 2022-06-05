package com.rrs.common.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁具体实现
 */
public interface DistributedLockAdapter {
    /**
     * 尝试加锁
     * @param key
     * @param waitTime
     * @param leaseTime
     * @param unit
     */
    boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws InterruptedException;

    /**
     * 枷锁
     * @param key
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @param isFair
     */
    void lock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair);

    /**
     * 解锁
     */
    void unlock() ;
}

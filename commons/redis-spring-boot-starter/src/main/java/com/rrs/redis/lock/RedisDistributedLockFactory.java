package com.rrs.redis.lock;

import com.rrs.common.core.lock.DistributedLock;

import java.util.concurrent.TimeUnit;

/**
 * 获取redis分布式锁
 */
public class RedisDistributedLockFactory {
    public static DistributedLock getDistributedLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair){
        return new DistributedLock(key,waitTime,leaseTime,unit,isFair,new RedissonDistributedLockAdapter());
    }
}

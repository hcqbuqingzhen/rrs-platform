package com.rrs.redis.lock;

import com.rrs.common.core.lock.DistributedLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 获取redis分布式锁
 */
@Component
@ConditionalOnProperty(prefix = "rrs.lock", name = "lockerType", havingValue = "REDIS",matchIfMissing = true)
public class RedisDistributedLockFactory {
    public DistributedLock getDistributedLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair){
        return new DistributedLock(key,waitTime,leaseTime,unit,isFair,new RedissonDistributedLockAdapter());
    }
}

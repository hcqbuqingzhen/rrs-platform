package com.rrs.redis.lock;

import com.rrs.common.core.constants.CommonConstant;
import com.rrs.common.core.lock.DistributedLockAdapter;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RedissonDistributedLockAdapter implements DistributedLockAdapter {
    private static RedissonClient redisson=Redisson.create();

    private RLock rLock;

    private void getLock(String key, boolean isFair) {
        if(Objects.nonNull(rLock)){
            return;
        }
        if (isFair) {
            rLock = redisson.getFairLock(CommonConstant.LOCK_KEY_PREFIX + ":" + key);
        } else {
            rLock =  redisson.getLock(CommonConstant.LOCK_KEY_PREFIX + ":" + key);
        }
    }
    @Override
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws InterruptedException {
        getLock(key,isFair);
        return rLock.tryLock(waitTime, leaseTime, unit);
    }

    @Override
    public void lock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) {
        getLock(key,isFair);
        rLock.lock(leaseTime,unit);
    }

    @Override
    public void unlock() {
        if(Objects.nonNull(rLock)){
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }
    }
}

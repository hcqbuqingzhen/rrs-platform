package com.rrs.zk.lock;

import com.rrs.common.core.constants.CommonConstant;
import com.rrs.common.core.lock.DistributedLockAdapter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
public class ZookeeperDistributedLock implements DistributedLockAdapter {
    public ZookeeperDistributedLock(CuratorFramework client){
        this.client=client;
    }
    private CuratorFramework client;
    private InterProcessMutex lock;

    private void getLock(String key) {
        if(Objects.isNull(lock)){
            InterProcessMutex lock = new InterProcessMutex(client, getPath(key));
        }
    }
    @Override
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        getLock(key);
        if(lock.acquire(waitTime,unit)){
            return true;
        }
        return false;
    }

    @Override
    public void lock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        lock.acquire();
    }

    @Override
    public void unlock() throws Exception {
        if (lock.isAcquiredInThisProcess()) {
            lock.release();
        }
    }

    private String getPath(String key) {
        return CommonConstant.PATH_SPLIT + CommonConstant.LOCK_KEY_PREFIX + CommonConstant.PATH_SPLIT + key;
    }
}

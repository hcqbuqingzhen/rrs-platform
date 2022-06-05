package com.rrs.zk.lock;


import com.rrs.common.core.lock.DistributedLock;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(prefix = "rrs.lock", name = "lockerType", havingValue = "ZK")
public class ZookeeperDistributedLockFactory {
    @Autowired
    private static CuratorFramework client;

    public DistributedLock getLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair){
        ZookeeperDistributedLock Locker=new ZookeeperDistributedLock(client);
        return new DistributedLock(key,waitTime,leaseTime,unit,isFair, Locker);
    }
}

package com.rrs.common.ribbon.rule;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.rrs.common.core.constants.CommonConstant;
import com.rrs.common.core.context.LbIsolationContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public class VersionIsolationRule extends RoundRobinRule {
    private final static String KEY_DEFAULT = "default";

    /**
     * 优先根据版本号取实例
     */
    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        String version;
        if (key != null && !KEY_DEFAULT.equals(key)) {
            version = key.toString();
        } else {
            version = LbIsolationContextHolder.getVersion();
        }

        List<Server> targetList = null;
        List<Server> upList = lb.getReachableServers();
        if (StrUtil.isNotEmpty(version)) {
            //取指定版本号的实例
            targetList = upList.stream().filter(
                    server -> version.equals(
                            ((NacosServer) server).getMetadata().get(CommonConstant.METADATA_VERSION)
                    )
            ).collect(Collectors.toList());
        }

        if (CollUtil.isEmpty(targetList)) {
            //只取无版本号的实例
            targetList = upList.stream().filter(
                    server -> {
                        String metadataVersion = ((NacosServer) server).getMetadata().get(CommonConstant.METADATA_VERSION);
                        return StrUtil.isEmpty(metadataVersion);
                    }
            ).collect(Collectors.toList());
        }

        if (CollUtil.isNotEmpty(targetList)) {
            return getServer(targetList);
        }
        return super.choose(lb, key);
    }

    /**
     * 随机取一个实例
     */
    private Server getServer(List<Server> upList) {
        int nextInt = RandomUtil.randomInt(upList.size());
        return upList.get(nextInt);
    }
}

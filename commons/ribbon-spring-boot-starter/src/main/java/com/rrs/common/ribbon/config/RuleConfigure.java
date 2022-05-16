package com.rrs.common.ribbon.config;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.IRule;
import com.rrs.common.ribbon.rule.VersionIsolationRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 *  自定义负载均衡策略,可以使用.解决此文章中说的问题.
 *   https://mp.weixin.qq.com/s/9XQ-SIbYsov3KBx9TGFN0g
 *   这个@Configurable不能被spring扫描到。
 *   why? https://segmentfault.com/a/1190000038625324
 */

public class RuleConfigure {
    @Bean
    @ConditionalOnClass(NacosServer.class)
    @ConditionalOnMissingBean
    public IRule versionIsolationRule() {
        return new VersionIsolationRule();
    }
}

package com.rrs.scgateway.config;

import com.alibaba.nacos.client.config.NacosConfigService;
import com.rrs.scgateway.route.NacosRouteDefinitionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将动态路由实现类注入
 * ConditionalOnProperty 当rrs.sc-gateway.dynamicRoute 的enable值为 true时，此类才有效。
 */
@Configuration
@ConditionalOnProperty(prefix = "rrs.sc-gateway.dynamicRoute", name = "enabled", havingValue = "true")
public class DynamicRouteConfig {
    /**
     * @ConditionalOnProperty 当rrs.sc-gateway.dynamicRoute 的from 值为nacos ，此配置类才有效。
     * nacos动态路由实现
     */
    @Configuration
    @ConditionalOnProperty(prefix = "rrs.sc-gateway.dynamicRoute", name = "from", havingValue = "nacos",matchIfMissing = true)
    public class nacosDynamicRoute{
        @Value("${spring.cloud.nacos.config.server-addr}")
        private String serverAddr;
        @Bean
        public NacosRouteDefinitionRepository nacosRouteDefinitionRepository(ApplicationEventPublisher publisher ) {
            return new NacosRouteDefinitionRepository(publisher,serverAddr);
        }
    }
}

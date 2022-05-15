package com.rrs.scgateway.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeRoute {
    //通过代码配置路由，返回一个RouteLocator 类即可。
    //所有的RouteLocator 最终都被CachingRouteLocator集成。
    @Bean
    public RouteLocator customizeRoute(RouteLocatorBuilder builder){
        return builder.routes().route(
                //id
                "code",
                //path  -> url  参照配置文件
                r->r.path("/coderoute/**").uri("lb://provider-hello")
        ).build();
    }
}

package com.rrs.scgateway.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrs.scgateway.fliter.rewrite.RequestBodyRewrite;
import com.rrs.scgateway.fliter.rewrite.ResponseBodyRewrite;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置路由信息，添加修改请求体响应体的过滤器。
 */
@Configuration
public class ModifyBodyRoute {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, ObjectMapper objectMapper) {
        return builder
                .routes()
                .route("path_route_change",
                        r -> r.path("/change")
                                .filters(f -> f
                                        .modifyRequestBody(String.class,String.class,new RequestBodyRewrite(objectMapper))
                                        .modifyResponseBody(String.class, String.class, new ResponseBodyRewrite(objectMapper))
                                )
                                .uri("http://127.0.0.1:8082"))
                .build();
    }
}

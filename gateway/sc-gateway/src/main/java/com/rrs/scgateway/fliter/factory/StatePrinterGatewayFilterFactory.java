package com.rrs.scgateway.fliter.factory;

import com.rrs.scgateway.fliter.StatePrinterGatewayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义过滤器工厂类
 * 过滤器实现打印断路器状态信息
 */
//@Component
public class StatePrinterGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Autowired
    ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory;

    @Override
    public String name() {
        return "CircuitBreakerStatePrinter";
    }

    @Override
    public GatewayFilter apply(Object config)
    {
        return new StatePrinterGatewayFilter(reactiveResilience4JCircuitBreakerFactory);
    }
}

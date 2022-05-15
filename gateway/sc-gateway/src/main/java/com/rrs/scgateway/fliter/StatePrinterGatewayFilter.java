package com.rrs.scgateway.fliter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.collection.Seq;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * 自定义过滤器 打印断路器状态
 */
public class StatePrinterGatewayFilter implements GatewayFilter, Ordered {
    private ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory;
    private CircuitBreaker circuitBreaker = null;
    // 通过构造方法取得reactiveResilience4JCircuitBreakerFactory实例
    public StatePrinterGatewayFilter(ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory) {
        this.reactiveResilience4JCircuitBreakerFactory = reactiveResilience4JCircuitBreakerFactory;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //反射获取CircuitBreaker
        if (null==circuitBreaker) {
            CircuitBreakerRegistry circuitBreakerRegistry = null;
            try {
                Method method = reactiveResilience4JCircuitBreakerFactory.getClass().getDeclaredMethod("getCircuitBreakerRegistry", (Class[]) null);
                // 用反射将getCircuitBreakerRegistry方法设置为可访问
                method.setAccessible(true);
                CircuitBreakerRegistry invoke = (CircuitBreakerRegistry)method.invoke(reactiveResilience4JCircuitBreakerFactory);
            }catch (Exception e){
                e.printStackTrace();
            }
            // 得到所有断路器实例
            Seq<CircuitBreaker> seq = circuitBreakerRegistry.getAllCircuitBreakers();
            // 用名字过滤，myCircuitBreaker来自路由配置中
            circuitBreaker = seq.filter(breaker -> breaker.getName().equals("myCircuitBreaker"))
                    .getOrNull();
        }
        // 取断路器状态，再判空一次，因为上面的操作未必能取到circuitBreaker
        String state = (null==circuitBreaker) ? "unknown" : circuitBreaker.getState().name();

        System.out.println("state : " + state);

        // 继续执行后面的逻辑
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}

package com.rrs.scgateway.route;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.rrs.common.core.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * nacos动态路由类 ，详细原理见。
 * RouteDefinitionRepository 继承了RouteDefinitionLocator ,
 * 1.因此也会被纳入到CompositeRouteDefinitionLocator中。
 * 2.然后经过RouteDefinitionRouteLocator，最终路由都被集成到CachingRouteLocator
 * 3.被RoutePredicateHandlerMapping所使用
 */
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
    //通过此id和组，获取唯一配置文件，
    private static final String SCG_DATA_ID = "sc-gateway-routes";
    private static final String SCG_GROUP_ID = "DEFAULT_GROUP";
    //NacosConfigService 类，通过此类获取配置中心的路由。
    private  String serverAddr;
    private ConfigService nacosConfigService;
    private ApplicationEventPublisher publisher;

    //构造方法注入
    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher,String serverAddr)  {
        try {
            this.nacosConfigService= NacosFactory.createConfigService(serverAddr);
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
        this.publisher=publisher;
        addListener();//1
    }
    //给nacosConfigService添加监听器，当配置文件更新时触发。
    private void addListener(){
        try {
            //添加一个Listener，
            nacosConfigService.addListener(SCG_DATA_ID,SCG_GROUP_ID,new Listener(){

                @Override
                public Executor getExecutor() {
                    return null;
                }
                //主要重写的方法，当配置更新后，发布一个事件，此事件会触发更新路由。
                @Override
                public void receiveConfigInfo(String s) {
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        }catch (Exception e){
            log.error("NacosRouteDefinitionRepository|addListener:error",e);
        }


    }
    //重写的主要方法，通过此方法刷新路由。
    @SneakyThrows
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        //构造方法中为ConfigService
        //获取注册中心上的配置。
        String config = nacosConfigService.getConfig(SCG_DATA_ID,SCG_GROUP_ID,5000);
        //解析json
        List<RouteDefinition> list = JsonUtils.getList(config, RouteDefinition.class);
        //返回
        for (RouteDefinition routeDefinition : list) {
            System.out.println(routeDefinition);
        }
        return  Flux.fromIterable(list);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}

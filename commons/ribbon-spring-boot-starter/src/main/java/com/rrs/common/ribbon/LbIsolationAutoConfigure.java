package com.rrs.common.ribbon;

import com.rrs.common.ribbon.config.RuleConfigure;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * 并通过配置开关控制是否开启自定义负载规则
 * 自定义负载均衡配置
 * - 1. 此方法为官方推荐
 *   如果针对不同的服务需要不同的策略，则可以参考官方实例的配置。
 *   https://huburt-hu.github.io/2019/09/05/Spring-Cloud-Ribbon-%E8%87%AA%E5%AE%9A%E4%B9%89%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1%E7%AD%96%E7%95%A5/
 * - 2.简单来说就是注入我们自己实现的IRule，然后配置给RioonClient。
 * - 3. @RibbonClient的value/name属性设置的是被调用的服务名（不是当前正在配置的服务名）
 * - 4. 需要注意的是RuleConfig类需要放在启动类的上层（或者不同包名），避免Spring默认扫描到。
 *   否则会出现“简单配置”效果，即所有服务都使用这个策略，无法实现不同服务不同策略的效果。
 */
@ConditionalOnProperty(value = "rrs.ribbon.isolation.enabled", havingValue = "true")
@RibbonClients(defaultConfiguration = {RuleConfigure.class})
public class LbIsolationAutoConfigure {
}

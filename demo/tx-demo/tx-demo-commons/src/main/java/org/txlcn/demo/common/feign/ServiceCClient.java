package org.txlcn.demo.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "txlcn-demo-spring-service-c")
public interface ServiceCClient {
    @GetMapping("/rpc")
    String rpc(@RequestParam("value") String name);
}

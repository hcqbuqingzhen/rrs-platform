package com.rrs.merchant.controller;


import com.rrs.merchant.service.BusinessService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author rrs
 */
@RestController
public class BusinessController {
    @Resource
    private BusinessService merchantService;

    /**
     * 下单场景测试-正常
     */
    @RequestMapping(path = "/placeOrder")
    public Boolean placeOrder() {
        merchantService.placeOrder("U001");
        return true;
    }

    /**
     * 下单场景测试-回滚
     */
    @RequestMapping(path = "/placeOrderFallBack")
    public Boolean placeOrderFallBack() {
        merchantService.placeOrder("U002");
        return true;
    }
}

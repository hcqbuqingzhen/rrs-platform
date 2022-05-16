package com.rrs.order.service;

import com.rrs.order.feign.AccountFeignClient;
import com.rrs.order.mapper.OrderMapper;
import com.rrs.order.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author rrs
 */
@Slf4j
@Service
public class OrderService {
    @Resource
    private AccountFeignClient accountFeignClient;

    @Resource
    private OrderMapper orderMapper;

    //@Transactional(rollbackFor = Exception.class)
    public void create(String userId, String commodityCode, Integer count) {
        //订单金额
        Integer orderMoney = count * 2;

        Order order = new Order()
                .setUserId(userId)
                .setCommodityCode(commodityCode)
                .setCount(count)
                .setMoney(orderMoney);
        orderMapper.insert(order);

        accountFeignClient.reduce(userId, orderMoney);
    }
}

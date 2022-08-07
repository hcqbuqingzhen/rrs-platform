package com.springboot.demo.controller;

import com.rrs.common.core.lock.DistributedLock;
import com.rrs.redis.lock.RedisDistributedLockFactory;
import com.springboot.demo.bean.BeanList;
import com.springboot.demo.bean.Mybean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class DemoController {
    @Autowired
    private ApplicationContext applicationContext;
    @Resource
    RedisDistributedLockFactory factory;
    @GetMapping("/demo")
    public String getBeanInfo(){
        log.info("测试干阿嘎嘎嘎嘎"+System.currentTimeMillis());
        log.info("测试干阿嘎嘎嘎嘎"+System.currentTimeMillis());
        log.info("测试干阿嘎嘎嘎嘎"+System.currentTimeMillis());
        log.info("测试干阿嘎嘎嘎嘎"+System.currentTimeMillis());
        log.info("测试干阿嘎嘎嘎嘎"+System.currentTimeMillis());
        //bean 的名字由方法名确定
        BeanList beanList = (BeanList)applicationContext.getBean("getBeanList");
        for (Mybean mybean : beanList.getList()) {
            System.out.println(mybean.toString());
        }
        return "";
    }

    /**
     * 测试redis分布式锁
     * @param id
     * @return
     */
    @GetMapping("/lock/{id}")
    public String testLock(@PathVariable Integer id) throws Exception {
        DistributedLock distributedLock = factory.getDistributedLock("aaa", 10, 10, TimeUnit.SECONDS, true);

        try {
            distributedLock.lock();
            System.out.println("拿到锁了:"+System.currentTimeMillis());
            Thread.sleep(3000l);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(id==2){
                distributedLock.unlock();
            }

        }
        return "success";
    }

}

package com.rrs.scgateway.controller;

import com.rrs.scgateway.exception.CustomizeErrorWebExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 网关自己使用，用于断路器短路后的降级
 */
@RestController
public class FallbackController {
    private String dateStr(){
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * 返回字符串类型
     * @return
     */
    @GetMapping("/myfallback")
    public String helloStr() {
        //验证是否是替换了类
        CustomizeErrorWebExceptionHandler errorWebExceptionHandler = (CustomizeErrorWebExceptionHandler)applicationContext.getBean("errorWebExceptionHandler");
        errorWebExceptionHandler.print();
        return "myfallback, " + dateStr();
    }
}

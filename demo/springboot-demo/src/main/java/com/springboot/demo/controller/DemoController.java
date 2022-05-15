package com.springboot.demo.controller;

import com.springboot.demo.bean.BeanList;
import com.springboot.demo.bean.Mybean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/demo")
    public String getBeanInfo(){
        //bean 的名字由方法名确定
        BeanList beanList = (BeanList)applicationContext.getBean("getBeanList");
        for (Mybean mybean : beanList.getList()) {
            System.out.println(mybean.toString());
        }
        return "";
    }

}

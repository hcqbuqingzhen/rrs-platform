package com.springboot.demo.config;


import com.springboot.demo.bean.BeanList;
import com.springboot.demo.bean.FoodBean;
import com.springboot.demo.bean.Mybean;
import com.springboot.demo.bean.UserBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DemoConfig {
    @Bean //当参数为List<Mybean> springboot会把之前所有的Mybean 封装成list注入。
    public BeanList getBeanList(List<Mybean> mybeans){
        ArrayList<Mybean> arrayList=new ArrayList<Mybean>();
        arrayList.addAll(mybeans);
        return new BeanList(arrayList);
    }

    @Bean
    public Mybean getFoodBean(){
        return new FoodBean("鱼");
    }

    @Bean
    public Mybean getUserBean(){
        return new UserBean("maozedong");
    }
}

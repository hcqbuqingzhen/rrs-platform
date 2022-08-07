package com.rrs.scgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;


@EnableDiscoveryClient
@SpringBootApplication
public class SCGatewayApplication {
    @Autowired
    private static ApplicationContext applicationContext;
    public static void main(String[] args) {
        SpringApplication.run(SCGatewayApplication.class,args);

    }
}

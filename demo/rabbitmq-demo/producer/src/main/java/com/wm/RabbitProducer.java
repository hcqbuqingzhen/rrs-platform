package com.wm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitProducer {
    public static void main(String[] args) {
        SpringApplication.run(RabbitProducer.class,args);
    }
}

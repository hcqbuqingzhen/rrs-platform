package com.wm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wm.sender.RabbitSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestRabbitController {

    @Autowired
    private RabbitSend msgProducer;


    @GetMapping("/testConfirm/{msg}")
    public void sendMessage(@PathVariable("msg") String msg) throws JsonProcessingException {
        System.out.println("xxxxx");
        msgProducer.testConfirm(msg);
    }

    @GetMapping("/testReturn/{msg}")
    public void sendMessage2(@PathVariable("msg") String msg) throws JsonProcessingException {
        System.out.println("xxxxx");
        msgProducer.testReturn(msg);
    }

    @GetMapping("/testTtl/{msg}")
    public void sendMessage3(@PathVariable("msg") String msg) throws JsonProcessingException {
        System.out.println("xxxxx");
        msgProducer.testTtl(msg);
    }
}

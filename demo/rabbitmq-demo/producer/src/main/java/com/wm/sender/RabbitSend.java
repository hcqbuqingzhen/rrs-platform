package com.wm.sender;

import com.wm.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitSend {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * Confirm模式
     * 没找到交换机
     */
    public void testConfirm(String s){
        CorrelationData correlationData=new CorrelationData();
        correlationData.setId("123");
        if(s==null){
            return;
        }
        if(s.contains("1")){
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A,RabbitConfig.ROUTING_KEY_A,s,correlationData);
        }else {
            rabbitTemplate.convertAndSend("sasasa",RabbitConfig.ROUTING_KEY_A,s,correlationData);
        }
    }

    public void testReturn(String s){
        /**
         * 回退模式
         * 没找到queue
         *  1.丢弃
         *  2.返回给发送方
         */
        rabbitTemplate.setMandatory(true);

        if(s.contains("1")){
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A,RabbitConfig.ROUTING_KEY_A,s);
        }else {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A,"sasasa",s);
        }
    }

    public void testBack(String s){
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("return 执行");
            }
        });
        if(s.contains("1")){
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A,RabbitConfig.ROUTING_KEY_A,s);
        }else {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A,"sasasa",s);
        }
    }

    /**
     * ttl队列
     * @param s
     */
    public void testTtl(String s){
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A,
                RabbitConfig.ROUTING_KEY_B,s);

    }
}

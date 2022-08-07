package com.wa.consumer01.listener;

import com.rabbitmq.client.Channel;
import com.wa.consumer01.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class TestListener implements ChannelAwareMessageListener {

    @Override
    //@RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println(new String(message.getBody()));
        //
        Thread.sleep(1000);
        try {
            System.out.println("处理业务逻辑");
            int i=3/0;
            //手动签收
            channel.basicAck(deliveryTag,true);
        }catch ( Exception e){
            channel.basicNack(deliveryTag,true,true);
        }

    }

    //测试限流
    @RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void testQos(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println(new String(message.getBody()));
        /*1.单条
          2.每次多少
          3.channel还是consumer
        * */
        channel.basicQos(0,1,false);
        //业务逻辑
        //签收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);


    }

    //测试死信
    @RabbitListener(queues = RabbitConfig.QUEUE_C)
    public void testDead(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println(new String(message.getBody()));
        //业务逻辑
        //签收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);

    }
}

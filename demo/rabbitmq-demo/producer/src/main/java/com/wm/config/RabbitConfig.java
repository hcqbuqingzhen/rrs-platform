package com.wm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*配置类*/
@Configuration
@Slf4j
public class RabbitConfig {
    /**
     * MQ地址
     */
    @Value("${spring.rabbitmq.host}")
    private String host;
    /**
     * MQ端口
     */
    @Value("${spring.rabbitmq.port}")
    private int port;

    /**
     * 用户名
     */
    @Value("${spring.rabbitmq.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${spring.rabbitmq.password}")
    private String password;
    /**
     * 虚拟主机
     */
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    // 定义一个或多个交换机
    // 用于开发之前测试
    public static final String EXCHANGE_A = "wm.test.topic";
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    public static final String EXCHANGE_C = "wm.test.black";
    // 定义队列
    public static final String QUEUE_A = "queue-wm.test";
    public static final String QUEUE_B = "queue-wm.ttl";
    public static final String QUEUE_C = "queue-wm.dead";
    public static final String BACKUP_QUEUE_NAME = "backup.queue"; //备份队列
    public static final String WARNING_QUEUE_NAME = "warning.queue";//警告队列
    // 定义routing-key
    public static final String ROUTING_KEY_A = "routing.wm.test";
    public static final String ROUTING_KEY_B = "routing.wm.ttl";
    public static final String ROUTING_KEY_C = "routing.wm.dead";

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     HeadersExchange ：通过添加属性key-value匹配
     DirectExchange:按照routingkey分发到指定队列
     TopicExchange:多关键字匹配
     **/
    /*************test-start******************/
    //死信交换机
    @Bean("testDeadExchange")
    public TopicExchange testDeadExchange(){
        //alternate-exchange 声明备用交换机
        return ExchangeBuilder.topicExchange(EXCHANGE_C).build();
    }
    //备份交换机
    @Bean("testBackExchange")
    public FanoutExchange testBackExchange(){
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE_NAME).build();
    }
    //topic交换机
    @Bean("testExchange")
    public TopicExchange testExchange(){
        //alternate-exchange 声明备用交换机
        return ExchangeBuilder.topicExchange(EXCHANGE_A).
                withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }
    //test队列
    @Bean("testQueue")
    public Queue testQueue() {
        return new Queue(QUEUE_A, true); //队列持久
    }
    @Bean("testBackupQueue")
    public Queue testBackupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    @Bean("testWarnQueue")
    public Queue testWarnQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }
    //正常队列配置
    @Bean("testTtlQueue")
    public Queue testTtlQueue(){
        return QueueBuilder.durable(QUEUE_B).deadLetterExchange(EXCHANGE_C).
                deadLetterRoutingKey(ROUTING_KEY_C).ttl(10000).maxLength(10).build();
    }

    @Bean("testDeadQueue") //死信对列
    public Queue testDeadQueue(){
        return QueueBuilder.durable(QUEUE_C).build();
    }

    //绑定
    @Bean
    public Binding binding(@Qualifier("testExchange") TopicExchange topicExchange,@Qualifier("testQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(RabbitConfig.ROUTING_KEY_A);
    }
    //备份队列
    @Bean
    public Binding backBinding(@Qualifier("testBackExchange") FanoutExchange fanoutExchange,@Qualifier("testBackupQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
    //警告队列
    @Bean
    public Binding warnBind(@Qualifier("testBackExchange") FanoutExchange fanoutExchange,@Qualifier("testWarnQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }
    //死信交换机和死信队列绑定
    @Bean
    public Binding deadBind(@Qualifier("testDeadExchange") TopicExchange topicExchange,@Qualifier("testDeadQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_KEY_C);
    }
    //ttl队列帮定原有交换机
    @Bean
    public Binding ttlBind(@Qualifier("testExchange") TopicExchange topicExchange,@Qualifier("testTtlQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_KEY_B);
    }
    /*************test-end******************/
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    // 创建连接工厂,获取MQ的连接
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        return connectionFactory;
    }

    // 创建rabbitTemplate
    @Bean(name = "rabbitTemplate")
    public RabbitTemplate rabbitTemplate(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //默认使用simpleMessageConverter  在此处更改为json序列化方案
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("return 执行");
            }
        });

        rabbitTemplate.setConfirmCallback((CorrelationData data, boolean ack, String cause)->{
            if(ack){
                System.out.println("success");
                //数据库执行状态更改
            }else {
                System.out.println("cause:"+cause);
            }
        });
        return rabbitTemplate;
    }

}

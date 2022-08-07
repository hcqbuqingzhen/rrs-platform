package com.wa.consumer01.config;

public class RabbitConfig {
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




}

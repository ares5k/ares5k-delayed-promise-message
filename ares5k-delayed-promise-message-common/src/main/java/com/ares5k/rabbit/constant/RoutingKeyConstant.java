package com.ares5k.rabbit.constant;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: Rabbit MQ 路由信息常量类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
public class RoutingKeyConstant {

    /**
     * 可靠性投递交换机和可靠性投递队列的绑定 Key
     */
    public static final String PROMISE_MESSAGE_QUEUE_ROUTING_KEY = "queue.routing.key";

    /**
     * 可靠性投递交换机和可靠性投递检查队列的绑定 Key
     */
    public static final String PROMISE_MESSAGE_CHECK_QUEUE_ROUTING_KEY = "queue.check.routing.key";

    /**
     * 可靠性投递延迟交换机和可靠性投递延迟队列的绑定 Key
     */
    public static final String PROMISE_MESSAGE_DELAY_QUEUE_ROUTING_KEY = "queue.delay.routing.key";

}

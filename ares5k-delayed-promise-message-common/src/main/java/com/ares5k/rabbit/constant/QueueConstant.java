package com.ares5k.rabbit.constant;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: Rabbit MQ 队列信息常量类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
public class QueueConstant {

    //******************* 可靠性消息投递 普通队列常量定义 start *******************

    /**
     * 可靠性投递队列是否持久化
     */
    public static final boolean PROMISE_MESSAGE_QUEUE_DURABLE = false;

    /**
     * 可靠性投递队列是否通信管道独享
     */
    public static final boolean PROMISE_MESSAGE_QUEUE_EXCLUSIVE = false;

    /**
     * 可靠性投递队列没有消息时是否自动删除
     */
    public static final boolean PROMISE_MESSAGE_QUEUE_AUTO_DELETE = false;

    /**
     * 可靠性投递队列名称
     */
    public static final String PROMISE_MESSAGE_QUEUE_NAME = "promise.message.queue";

    //******************* 可靠性消息投递 普通队列常量定义 end *******************

    //******************* 可靠性消息投递 检查队列常量定义 start *******************

    /**
     * 可靠性投递检查队列是否持久化
     */
    public static final boolean PROMISE_MESSAGE_CHECK_QUEUE_DURABLE = false;

    /**
     * 可靠性投递检查队列是否通信管道独享
     */
    public static final boolean PROMISE_MESSAGE_CHECK_QUEUE_EXCLUSIVE = false;

    /**
     * 可靠性投递检查队列没有消息时是否自动删除
     */
    public static final boolean PROMISE_MESSAGE_CHECK_QUEUE_AUTO_DELETE = false;

    /**
     * 可靠性投递检查队列名称
     */
    public static final String PROMISE_MESSAGE_CHECK_QUEUE_NAME = "promise.message.check.queue";

    //******************* 可靠性消息投递 检查队列常量定义 end *******************

    //******************* 可靠性消息投递 延迟队列常量定义 start *******************

    /**
     * 可靠性投递延迟队列是否持久化
     */
    public static final boolean PROMISE_MESSAGE_DELAY_QUEUE_DURABLE = false;

    /**
     * 可靠性投递延迟队列是否通信管道独享
     */
    public static final boolean PROMISE_MESSAGE_DELAY_QUEUE_EXCLUSIVE = false;

    /**
     * 可靠性投递延迟队列没有消息时是否自动删除
     */
    public static final boolean PROMISE_MESSAGE_DELAY_QUEUE_AUTO_DELETE = false;

    /**
     * 可靠性投递延迟队列名称
     */
    public static final String PROMISE_MESSAGE_DELAY_QUEUE_NAME = "promise.message.delay.queue";

    //******************* 可靠性消息投递 延迟队列常量定义 end *******************

}

package com.ares5k.rabbit.constant;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: Rabbit MQ 日志信息常量类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
public class RabbitLogConstant {

    /**
     * 消费失败
     */
    public static final String CONSUMER_ERROR = "消费失败";

    /**
     * json转换时发生异常
     */
    public static final String JSON_ERROR = "json转换时发生异常";

    /**
     * 数据同步成功
     */
    public static final String RABBIT_SYNC_SUCCESS = "消息同步成功, 消息ID: {}";

    /**
     * 发送到检查交换机
     */
    public static final String SEND_TO_CHECK_EXCHANGE = "发送消息到检查队列, 消息ID: {}";

    /**
     * 幂等性
     */
    public static final String REDIS_PROCESSED = "消息已经被处理过, 不能重复处理, 消息ID: {}";

    /**
     * 收到队列投递的消息
     */
    public static final String RABBIT_QUEUE_RECEIVED = "收到Rabbit Broker投递消息: 交换机:{}, 队列: {}, 消息ID: {}, 消息在队列中的位置: {}";

    /**
     * 已记录消费成功的消息状态
     */
    public static final String RABBIT_CONSUME_SUCCESS = "已记录消费成功的消息状态, 消息ID: {}";

    /**
     * Rabbit Broker消息发送到交换机失败格式
     */
    public static final String RABBIT_MQ_CONFIRM_NACK = "消息发送到rabbit broker交换机失败, 消息ID: {}";

    /**
     * 发送消息到延迟队列格式
     */
    public static final String RABBIT_MQ_SEND_TO_DELAY = "发送消息到延迟队列, 消息ID: {}, 延迟时间: {}ms";


    /**
     * 消息消费异常, 请求重试。
     */
    public static final String RABBIT_MQ_RETRY = "消息消费异常, 请求重试。";

    /**
     * 发送到业务消息队列失败, 延迟消息已经发出
     */
    public static final String SYNC_FIND_QUEUE_ERROR = "开始更新数据同步状态：异常状态, 发送到业务消息队列失败, 延迟消息已经发出";

    /**
     * 发送到业务消息交换机失败, 延迟消息未发出
     */
    public static final String SYNC_FIND_EXCHANGE_ERROR = "开始更新数据同步状态：异常状态, 发送到业务消息交换机失败, 延迟消息未发出";

    /**
     * Rabbit Broker消息找不到队列格式
     */
    public static final String RABBIT_MQ_RETURN_MESSAGE_FORMAT = "\r\n消息发送到rabbit broker队列失败:\r\n{\r\n  消息ID: {}\r\n  错误码: {}\r\n  错误原因: {}\r\n  交换机: {}\r\n  路由: {}\r\n}";
}

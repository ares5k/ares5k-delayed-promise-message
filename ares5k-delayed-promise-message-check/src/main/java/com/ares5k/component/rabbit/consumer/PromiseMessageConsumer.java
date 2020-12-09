package com.ares5k.component.rabbit.consumer;

import com.ares5k.modules.sync.service.MessageDeliverService;
import com.ares5k.rabbit.constant.ExchangeConstant;
import com.ares5k.rabbit.constant.QueueConstant;
import com.ares5k.rabbit.constant.RabbitLogConstant;
import com.ares5k.rabbit.constant.RabbitMsgHeaderConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: RabbitMQ 消费端监听类 - 监听延迟队列和检查队列
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Slf4j
@Component
public class PromiseMessageConsumer {


    /**
     * 消息投递表业务处理对象
     */
    @Autowired
    private MessageDeliverService messageDeliverService;

    /**
     * 监听延时队列
     *
     * @param message 消息对象
     * @param channel 通信管道
     * @throws IOException http请求或 ack出错
     * @author ares5k
     */
    @RabbitHandler
    @RabbitListener(
            //监听的队列
            queues = {QueueConstant.PROMISE_MESSAGE_DELAY_QUEUE_NAME},
            //指定监听容器工厂
            containerFactory = "promiseMsgListenerContainerFactory")
    public void delayCheckHandle(Message message, Channel channel) throws IOException {

        //消息ID
        String correlationId = message.getMessageProperties()
                .getHeader(RabbitMsgHeaderConstant.RABBIT_MQ_RETURN_HEADER_CORRELATION_ID_KEY);

        //获取消息在队列的位置
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        //收到投递消息日志
        log.info(RabbitLogConstant.RABBIT_QUEUE_RECEIVED,
                ExchangeConstant.PROMISE_MESSAGE_EXCHANGE_NAME,
                QueueConstant.PROMISE_MESSAGE_QUEUE_NAME,
                correlationId,
                deliveryTag);
        try {
            //消息检查
            messageDeliverService.delayCheckHandle(correlationId, new String(message.getBody(), StandardCharsets.UTF_8));
        } finally {
            //此处使用手动 ack的目的仅仅是为了限流
            //哪怕消费失败了也不需要将消息重回队列, 既然消息
            //不重回队列, 那么此处用 ack nack reject都无所谓的,
            //只是为了告诉 broker可以继续发消息了
            channel.basicAck(deliveryTag, false);
        }
    }

    /**
     * 监听检查队列
     *
     * @param message 消息对象
     * @param channel 通信管道
     * @throws IOException http请求或 ack出错
     * @author ares5k
     */
    @RabbitHandler
    @RabbitListener(
            //监听的队列
            queues = {QueueConstant.PROMISE_MESSAGE_CHECK_QUEUE_NAME},
            //指定监听容器工厂
            containerFactory = "promiseMsgListenerContainerFactory")
    public void okHandler(Message message, Channel channel) throws IOException {

        //消息ID
        String correlationId = message.getMessageProperties()
                .getHeader(RabbitMsgHeaderConstant.RABBIT_MQ_RETURN_HEADER_CORRELATION_ID_KEY);

        //获取消息在队列的位置
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        //收到投递消息日志
        log.info(RabbitLogConstant.RABBIT_QUEUE_RECEIVED,
                ExchangeConstant.PROMISE_MESSAGE_EXCHANGE_NAME,
                QueueConstant.PROMISE_MESSAGE_QUEUE_NAME,
                correlationId,
                deliveryTag);

        try {
            //将消息状态保存至消息表
            messageDeliverService.okHandle(correlationId, new String(message.getBody(), StandardCharsets.UTF_8));
        } finally {
            //此处使用手动 ack的目的仅仅是为了限流
            //哪怕消费失败了也不需要将消息重回队列, 既然消息
            //不重回队列, 那么此处用 ack nack reject都无所谓的,
            //只是为了告诉 broker可以继续发消息了
            channel.basicAck(deliveryTag, false);
        }
    }
}

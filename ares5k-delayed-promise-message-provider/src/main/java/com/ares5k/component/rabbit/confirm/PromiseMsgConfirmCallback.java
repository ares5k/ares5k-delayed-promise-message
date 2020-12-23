package com.ares5k.component.rabbit.confirm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ares5k.entity.provider.BizProvider;
import com.ares5k.modules.bizprovider.mapper.BizProviderMapper;
import com.ares5k.rabbit.constant.ExchangeConstant;
import com.ares5k.rabbit.constant.RabbitLogConstant;
import com.ares5k.rabbit.constant.RoutingKeyConstant;
import com.ares5k.rabbit.data.MsgData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: Rabbit Broker消息发送到交换机的结果的回调处理类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Slf4j
@Component
public class PromiseMsgConfirmCallback implements RabbitTemplate.ConfirmCallback {

    /**
     * Object <=> Json 转换的对象
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 提供端业务表 Mybatis-plus Mapper
     */
    @Autowired
    private BizProviderMapper bizProviderMapper;

    /**
     * 可靠性消息投递专用 Rabbit MQ操作对象 - 延迟用
     */
    @Autowired
    @Qualifier(value = "promiseMsgRabbitTemplate")
    private RabbitTemplate promiseMsgRabbitTemplate;

    /**
     * Rabbit Broker消息发送到交换机的结果的回调方法
     *
     * @param correlationData 发送消息时发送的关联数据
     * @param ack             结果
     * @param cause           错误原因
     * @author ares5k
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        //消息ID
        String correlationId = ObjectUtil.isNotEmpty(correlationData) ? correlationData.getId() : StrUtil.EMPTY;

        //判断发送到交换机的结果
        if (ack) {
            //发送消息到延迟队列
            sendDelayMessage(correlationData);

        } else {
            //异常日志
            log.warn(RabbitLogConstant.RABBIT_MQ_CONFIRM_NACK, correlationId);
            try {
                //从消息关联中拿到消息对象
                if (correlationData.getReturnedMessage() != null) {
                    //json转换
                    BizProvider bizProvider = objectMapper.readValue(correlationData.getReturnedMessage().getBody(), MsgData.class).getBizProvider();

                    //更新数据状态
                    log.warn(RabbitLogConstant.SYNC_FIND_EXCHANGE_ERROR);
                    bizProviderMapper.updateById(bizProvider.setSyncStatus(BizProvider.SendSyncEnum.FIND_EXCHANGE_ERROR.ordinal()));
                }
            } catch (IOException e) {
                log.error(RabbitLogConstant.JSON_ERROR, e);
            }
        }
    }

    /**
     * 发送消息到延迟队列
     *
     * @param correlationData 消息关联数据
     * @author ares5k
     */
    private void sendDelayMessage(CorrelationData correlationData) {

        //消息ID
        String correlationId = ObjectUtil.isNotEmpty(correlationData) ? correlationData.getId() : StrUtil.EMPTY;
        log.info(RabbitLogConstant.RABBIT_MQ_SEND_TO_DELAY, correlationId, 5000);

        if (correlationData.getReturnedMessage() != null) {
            try {
                //发送消息到延迟队列
                promiseMsgRabbitTemplate.convertAndSend(
                        //延迟交换机名
                        ExchangeConstant.PROMISE_MESSAGE_DELAY_EXCHANGE_NAME,
                        //路由
                        RoutingKeyConstant.PROMISE_MESSAGE_DELAY_QUEUE_ROUTING_KEY,
                        //消息体
                        objectMapper.readValue(correlationData.getReturnedMessage().getBody(), MsgData.class),
                        //RabbitTemplate创建消息对象后的回调, 让我们自定义完善消息对象
                        message -> {
                            //延迟时间-单位毫秒
                            message.getMessageProperties().setDelay(5000);
                            return message;
                        },
                        new CorrelationData(correlationId == null ? StrUtil.EMPTY : correlationId));
            } catch (IOException e) {
                log.error(RabbitLogConstant.JSON_ERROR, e);
            }
        }
    }
}

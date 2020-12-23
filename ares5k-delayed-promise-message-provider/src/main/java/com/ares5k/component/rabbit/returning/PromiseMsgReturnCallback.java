package com.ares5k.component.rabbit.returning;

import com.ares5k.entity.provider.BizProvider;
import com.ares5k.modules.bizprovider.mapper.BizProviderMapper;
import com.ares5k.rabbit.constant.RabbitLogConstant;
import com.ares5k.rabbit.constant.RabbitMsgHeaderConstant;
import com.ares5k.rabbit.data.MsgData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 消息找不到队列回调处理类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Slf4j
@Component
public class PromiseMsgReturnCallback implements RabbitTemplate.ReturnCallback {

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
     * 投递的消息找不到队列时的回调方法
     * 此时延迟队列的消息也已经发出
     *
     * @param message    发送的消息对象
     * @param replyCode  错误码
     * @param replyText  错误原因
     * @param exchange   交换机
     * @param routingKey 路由
     * @author ares5k
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {

        //消息ID
        String correlationId = message.getMessageProperties().getHeader(RabbitMsgHeaderConstant.RABBIT_MQ_RETURN_HEADER_CORRELATION_ID_KEY);

        //异常日志
        log.warn(RabbitLogConstant.RABBIT_MQ_RETURN_MESSAGE_FORMAT,
                correlationId,
                replyCode,
                replyText,
                exchange,
                routingKey);
        try {
            //json转换
            BizProvider bizProvider = objectMapper.readValue(message.getBody(), MsgData.class).getBizProvider();

            //更新数据状态
            log.warn(RabbitLogConstant.SYNC_FIND_QUEUE_ERROR);
            bizProviderMapper.updateById(bizProvider.setSyncStatus(BizProvider.SendSyncEnum.FIND_QUEUE_ERROR.ordinal()));

        } catch (IOException e) {
            log.error(RabbitLogConstant.JSON_ERROR, e);
        }
    }
}

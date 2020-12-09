package com.ares5k.modules.bizprovider.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ares5k.entity.provider.BizProvider;
import com.ares5k.modules.bizprovider.mapper.BizProviderMapper;
import com.ares5k.modules.bizprovider.service.BizProviderService;
import com.ares5k.rabbit.constant.ExchangeConstant;
import com.ares5k.rabbit.constant.RoutingKeyConstant;
import com.ares5k.rabbit.data.MsgData;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 提供端业务表对应的业务接口类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Service
public class BizProviderServiceImpl extends ServiceImpl<BizProviderMapper, BizProvider> implements BizProviderService {

    /**
     * rabbit mq 操作对象
     */
    @Autowired
    @Qualifier(value = "promiseMsgWithCallbackRabbitTemplate")
    private RabbitTemplate promiseMsgWithCallbackRabbitTemplate;

    /**
     * 添加数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    @Override
    public String addBizProvider(BizProvider provider) {
        // mybatis-plus提供的新增数据方法, 并且会将生成主键填充到参数对象内
        if (!super.save(provider)) {
            //插入失败
            return ERROR;
        } else {
            //插入成功后投递数据
            send(MsgData.DataOperationEnum.INSERT_UPDATE, provider);
            //操作结果
            return SUCCESS;
        }
    }

    /**
     * 删除数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    @Override
    public String delBizProvider(BizProvider provider) {
        //主键不能是空
        if (StrUtil.isEmpty(provider.getProviderId())) {
            return ERROR;
        }
        //删除数据
        if (!super.removeById(provider.getProviderId())) {
            //删除失败
            return ERROR;
        } else {
            //投递数据
            send(MsgData.DataOperationEnum.DELETE, provider);
            //操作结果
            return SUCCESS;
        }
    }

    /**
     * 修改数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    @Override
    public String changeBizProvider(BizProvider provider) {
        //主键不能是空
        if (StrUtil.isEmpty(provider.getProviderId())) {
            return ERROR;
        }
        //修改数据
        if (!super.updateById(provider)) {
            //修改失败
            return ERROR;
        } else {
            //投递数据
            send(MsgData.DataOperationEnum.INSERT_UPDATE, provider);
            //操作结果
            return SUCCESS;
        }

    }

    /**
     * 消息重发
     *
     * @param msgId      消息ID
     * @param providerId 消息提供端业务表ID
     * @author arese5k
     */
    @Override
    public void retry(String msgId, String providerId) {
        //根据主键查询
        BizProvider provider = super.getById(providerId);
        //重发
        if (ObjectUtil.isEmpty(provider)) {
            send(MsgData.DataOperationEnum.DELETE, provider, msgId);
        } else {
            send(MsgData.DataOperationEnum.INSERT_UPDATE, provider, msgId);
        }
    }

    /**
     * 投递消息
     *
     * @param operation 此次数据的操作类型
     * @param provider  内容对象
     */
    private void send(MsgData.DataOperationEnum operation, BizProvider provider) {
        this.send(operation, provider, null);
    }

    /**
     * 投递消息
     *
     * @param operation 此次数据的操作类型
     * @param msgId     消息ID
     * @param provider  内容对象
     */
    private void send(MsgData.DataOperationEnum operation, BizProvider provider, String msgId) {

        //设置消息投递载体对象
        MsgData msgData = new MsgData();
        msgData.setBizProvider(provider);
        msgData.setOperation(operation);

        // mq消息投递
        // mq 使用了 publisher confirm工作模式, 投递后会回调成功或失败的监听方法
        promiseMsgWithCallbackRabbitTemplate.convertAndSend(
                //交换机名
                ExchangeConstant.PROMISE_MESSAGE_EXCHANGE_NAME,
                //路由
                RoutingKeyConstant.PROMISE_MESSAGE_QUEUE_ROUTING_KEY,
                //投递内容
                msgData,
                //RabbitTemplate创建消息对象后的回调, 让我们自定义完善消息对象
                //此处不能使用lambda, 因为要重写 postProcessMessage的重载方法
                new MessagePostProcessor() {
                    /**
                     * 不需要重写
                     */
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        return null;
                    }

                    /**
                     * 重写
                     * @param message 消息对象
                     * @param correlation 消息关联信息
                     */
                    @Override
                    public Message postProcessMessage(Message message, Correlation correlation) {
                        //将消息对象放入消息关联信息中
                        CorrelationData correlationData = (CorrelationData) correlation;
                        correlationData.setReturnedMessage(message);
                        return message;
                    }
                }
                //消息关联数据
                , new CorrelationData(StrUtil.isEmpty(msgId) ? IdUtil.simpleUUID() : msgId));
    }

    /**
     * 操作失败
     */
    private static final String ERROR = "操作失败";

    /**
     * 操作成功
     */
    private static final String SUCCESS = "操作成功";

}

package com.ares5k.modules.sync.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ares5k.entity.check.MessageDeliver;
import com.ares5k.modules.sync.mapper.MessageDeliverMapper;
import com.ares5k.modules.sync.service.MessageDeliverService;
import com.ares5k.rabbit.constant.RoutingKeyConstant;
import com.ares5k.rabbit.data.MsgData;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 消息投递表 接口类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Service
public class MessageDeliverServiceImpl extends ServiceImpl<MessageDeliverMapper, MessageDeliver> implements MessageDeliverService {

    /**
     * Object <=> Json 转换的对象
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 发送 http请求的对象
     */
    @Autowired
    private CloseableHttpClient httpClient;

    /**
     * 新增消息投递表数据
     *
     * @param id   消息ID
     * @param data 数据
     * @return 操作结果
     * @author ares5k
     */
    @Override
    public boolean okHandle(String id, String data) {

        //查询是否存在
        MessageDeliver deliver;

        //判断是否是重试的场合
        if (ObjectUtil.isNotEmpty(deliver = super.getById(id))) {
            //更新
            deliver.setContent(data);
            deliver.setMsgStatus(MessageDeliver.MessageStatus.CONSUMER_OK.ordinal());
            return super.updateById(deliver);

        } else {
            //新增
            return super.save(setMessageDeliver(id, data, MessageDeliver.MessageStatus.CONSUMER_OK.ordinal(), 0));
        }
    }

    /**
     * 验证是否成功同步
     *
     * @param id   消息ID
     * @param data 数据
     * @return 操作结果
     * @author ares5k
     */
    @SuppressWarnings("all")
    @Override
    public boolean delayCheckHandle(String id, String data) throws IOException {

        //是否重试
        boolean retry = false;

        //获取投递消息对象
        MessageDeliver deliver = super.getById(id);

        //消费端已经成功处理
        if (ObjectUtil.isNotEmpty(deliver)
                && deliver.getMsgStatus() == MessageDeliver.MessageStatus.CONSUMER_OK.ordinal()) {
            //删除消息
            super.removeById(id);
            return true;
        }
        //消费端处理失败的消息
        if (ObjectUtil.isNotEmpty(deliver) && deliver.getMsgStatus() != MessageDeliver.MessageStatus.CONSUMER_OK.ordinal()) {
            if (deliver.getMaxRetry() > deliver.getCurrentRetry()) {
                //更新重试次数
                deliver.setCurrentRetry(deliver.getCurrentRetry() + 1);
                super.updateById(deliver);
                retry = true;
            }
        }
        //消费表压根没有消息
        if (ObjectUtil.isEmpty(deliver)) {
            //保存一份消息, 状态设置为异常, 当前重试次数设为 1
            super.save(this.setMessageDeliver(id, data, MessageDeliver.MessageStatus.CONSUMER_FAIL.ordinal(), 1));
            retry = true;
        }
        //需要重试
        if (retry) {
            //发送 get请求
            MsgData msgData = objectMapper.readValue(data, MsgData.class);
            httpClient.execute(new HttpGet(String.format(RETRY_URL, new String[]{id, msgData.getBizProvider().getProviderId()})));
        }
        //正常返回
        return true;
    }

    /**
     * 设置消息投递对象
     *
     * @param id     消息ID
     * @param data   数据
     * @param status 消息状态
     * @param retry  当前重试次数
     * @return 操作结果
     * @author ares5k
     */
    private MessageDeliver setMessageDeliver(String id, String data, int status, int retry) {
        //消息对象
        MessageDeliver deliver = new MessageDeliver();
        //消息ID
        deliver.setMsgId(id);
        //消息状态
        deliver.setMsgStatus(status);
        //routing-key
        deliver.setRoutingKey(RoutingKeyConstant.PROMISE_MESSAGE_QUEUE_ROUTING_KEY);
        //错误原因
        deliver.setErrorCause(StrUtil.EMPTY);
        //消息内容
        deliver.setContent(data);
        //最大重试次数
        deliver.setMaxRetry(5);
        //当前重试次数
        deliver.setCurrentRetry(retry);
        //消息对象
        return deliver;
    }

    /**
     * 重试地址
     */
    private static final String RETRY_URL = "http://192.168.3.25:8080/provider/retry?msgId=%s&providerId=%s";
}

package com.ares5k.modules.bizconsumer.service;

import com.ares5k.entity.consumer.BizConsumer;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 业务表-从提供端同步数据 接口类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
public interface BizConsumerService {

    /**
     * 添加数据
     *
     * @param consumer 业务表-从提供端同步数据
     * @author arese5k
     */
    void saveBizConsumer(BizConsumer consumer);

    /**
     * 删除数据
     *
     * @param consumer 业务表-从提供端同步数据
     * @author arese5k
     */
    void delBizConsumer(BizConsumer consumer);

}

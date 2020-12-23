package com.ares5k.modules.sync.service;

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
public interface MessageDeliverService {

    /**
     * 新增消息投递表数据
     *
     * @param id   消息ID
     * @param data 数据
     * @author ares5k
     */
    void consumerHandleSuccess(String id, String data);

    /**
     * 验证是否成功同步
     *
     * @param id   消息ID
     * @param data 数据
     * @author ares5k
     */
    void delayCheckHandle(String id, String data) throws IOException;
}

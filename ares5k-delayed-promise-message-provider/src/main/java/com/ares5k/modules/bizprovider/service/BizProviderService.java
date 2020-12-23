package com.ares5k.modules.bizprovider.service;

import com.ares5k.entity.provider.BizProvider;

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
public interface BizProviderService {

    /**
     * 添加数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    String addBizProvider(BizProvider provider);

    /**
     * 删除数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    String delBizProvider(BizProvider provider);

    /**
     * 修改数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    String changeBizProvider(BizProvider provider);

    /**
     * 消息重发
     *
     * @param msgId      消息ID
     * @param providerId 消息提供端业务表ID
     * @author arese5k
     */
    void retry(String msgId, String providerId);
}

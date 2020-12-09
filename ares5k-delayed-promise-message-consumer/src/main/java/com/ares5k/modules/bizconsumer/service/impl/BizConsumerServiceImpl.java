package com.ares5k.modules.bizconsumer.service.impl;

import com.ares5k.entity.consumer.BizConsumer;
import com.ares5k.modules.bizconsumer.mapper.BizConsumerMapper;
import com.ares5k.modules.bizconsumer.service.BizConsumerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
@Service
public class BizConsumerServiceImpl extends ServiceImpl<BizConsumerMapper, BizConsumer> implements BizConsumerService {

    /**
     * 添加数据
     *
     * @param consumer 业务表-从提供端同步数据
     * @author arese5k
     */
    @Override
    public void saveBizConsumer(BizConsumer consumer) {
        //不存在新增
        if (ObjectUtils.isEmpty(super.getById(consumer.getProviderId()))) {
            super.save(consumer);
        } else {
            //存在更新
            super.updateById(consumer);
        }
    }

    /**
     * 删除数据
     *
     * @param consumer 业务表-从提供端同步数据
     * @author arese5k
     */
    @Override
    public void delBizConsumer(BizConsumer consumer) {
        super.removeById(consumer.getProviderId());
    }
}

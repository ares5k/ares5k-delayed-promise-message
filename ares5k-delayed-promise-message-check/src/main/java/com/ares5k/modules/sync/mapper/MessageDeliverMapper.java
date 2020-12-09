package com.ares5k.modules.sync.mapper;

import com.ares5k.entity.check.MessageDeliver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 消息投递表 Mybatis-plus Mapper接口
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Mapper
public interface MessageDeliverMapper extends BaseMapper<MessageDeliver> {
}

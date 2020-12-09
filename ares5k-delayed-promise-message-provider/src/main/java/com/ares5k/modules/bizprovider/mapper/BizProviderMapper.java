package com.ares5k.modules.bizprovider.mapper;

import com.ares5k.entity.provider.BizProvider;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 提供端业务表 Mybatis-plus Mapper接口
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Mapper
public interface BizProviderMapper extends BaseMapper<BizProvider> {

}

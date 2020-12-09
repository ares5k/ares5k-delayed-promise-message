package com.ares5k.rabbit.data;

import com.ares5k.entity.provider.BizProvider;
import lombok.Data;

import java.io.Serializable;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 消息投递载体
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Data
public class MsgData {

    /**
     * 对数据操作的类型
     */
    private DataOperationEnum operation;

    /**
     * 提供端业务表对象
     */
    private BizProvider bizProvider;

    /**
     * 对数据操作的类型
     */
    public enum DataOperationEnum {
        INSERT_UPDATE, DELETE
    }
}

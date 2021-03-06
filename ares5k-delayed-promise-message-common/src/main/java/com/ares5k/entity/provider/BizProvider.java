package com.ares5k.entity.provider;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 提供端业务表
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Data
@Accessors(chain = true)
public class BizProvider {

    /**
     * 消息提供端业务表ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String providerId;

    /**
     * 消息提供端业务表名称
     */
    private String providerName;

    /**
     * 记录同步状态:
     * 0: 正常
     * 1: 发送到业务消息交换机失败, 延迟消息未发出
     * 2. 发送到业务消息队列失败, 延迟消息已经发出
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer syncStatus;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyUser;

    /**
     * 乐观锁
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 同步状态
     */
    public enum SendSyncEnum {
        SUCCESS, FIND_EXCHANGE_ERROR, FIND_QUEUE_ERROR
    }
}

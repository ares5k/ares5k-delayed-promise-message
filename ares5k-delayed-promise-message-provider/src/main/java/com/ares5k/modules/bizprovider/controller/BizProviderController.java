package com.ares5k.modules.bizprovider.controller;

import com.ares5k.entity.provider.BizProvider;
import com.ares5k.modules.bizprovider.service.BizProviderService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 提供端业务表对应的Controller
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@RestController
@RequestMapping("/provider")
public class BizProviderController {

    /**
     * 提供端业务表对应的业务对象
     */
    @Autowired
    private BizProviderService bizProviderService;

    /**
     * 添加数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    @PostMapping(path = "/add")
    public String addBizProvider(@RequestBody BizProvider provider) {
        return bizProviderService.addBizProvider(provider);
    }

    /**
     * 删除数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    @PostMapping(path = "/del")
    public String delBizProvider(@RequestBody BizProvider provider) {
        return bizProviderService.delBizProvider(provider);
    }

    /**
     * 修改数据
     *
     * @param provider 提供端业务表实体对象
     * @return 操作结果
     * @author arese5k
     */
    @PostMapping(path = "/change")
    public String changeBizProvider(@RequestBody BizProvider provider) {
        return bizProviderService.changeBizProvider(provider);
    }

    /**
     * 重试发送
     *
     * @param msgId 消息ID
     * @param providerId 消息提供端业务表ID
     * @author arese5k
     */
    @GetMapping(path = "/retry")
    public void retry(@Param("msgId") String msgId, @Param("providerId") String providerId) {
        bizProviderService.retry(msgId, providerId);
    }
}

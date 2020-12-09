package com.ares5k.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: Mybatis plus配置类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Configuration
public class MybatisConfig {

    /**
     * 配置乐观锁插件
     *
     * @return 乐观锁处理对象
     * @author ares5k
     */
    @Bean
    public OptimisticLockerInterceptor lockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }
}

package com.ares5k;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: 消息提供端启动类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@SpringBootApplication
public class Application {
    /**
     * 程序入口
     *
     * @author ares5k
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}

package com.ares5k.config.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 可靠性消息投递 - 延迟队列
 * <p>
 * 类说明: Http Client 配置类
 *
 * @author ares5k
 * @since 2020-12-01
 * qq: 16891544
 * email: 16891544@qq.com
 */
@Configuration
public class HttpClientConfig {

    /**
     * 创建发送 http请求的对象
     *
     * @return httpclient对象
     * @author ares5k
     */
    @Bean
    public CloseableHttpClient httpClient() {

        //创建连接池管理对象
        PoolingHttpClientConnectionManager poolManager = new PoolingHttpClientConnectionManager();
        //连接池对最连接数
        poolManager.setMaxTotal(50);
        //对同一主机的并发最大数量
        poolManager.setDefaultMaxPerRoute(50);

        //设置连接配置信息
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000).build();

        //创建一个建造器对象
        HttpClientBuilder builder = HttpClientBuilder.create();
        //设置连接池
        builder.setConnectionManager(poolManager);
        //设置连接配置信息
        builder.setDefaultRequestConfig(config);

        //从建造器中获取一个连接对象
        return builder.build();
    }
}

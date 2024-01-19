/*
 * 版权信息: © fusionfintrade
 */ 
package com.zaqbest.core.redis.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * redis配置
 *
 * @author allan
 * @date 2022/3/23
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfigurationProperties {

    private Set<String> nodes;

    private String master;
    /**
     * 启用redis集群
     */
    private Boolean sentinelCluster = false;
    /**
     * 启用redis 防重复提交
     */
    private Boolean enableDuplicate = true;

    @Value("${spring.redis.host:127.0.0.1}")
    private String host;
    @Value("${spring.redis.port:6379}")
    private int port;
    @Value("${spring.redis.database:1}")
    private int database;
    @Value("${spring.redis.password:null}")
    private String password;
    @Value("${spring.redis.lettuce.pool.max-idle:8}")
    private int maxIdle;
    @Value("${spring.redis.lettuce.pool.min-idle:0}")
    private int minIdle;
    @Value("${spring.redis.lettuce.pool.max-wait:-1}")
    private long maxWait;
    @Value("${spring.redis.lettuce.pool.max-active:8}")
    private int maxActive;
    @Value("${spring.redis.lettuce.shutdown-timeout:100}")
    private int shutdownTimeout;
}

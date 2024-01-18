/*
 * 版权信息: © fusionfintrade
 */ 
package com.zaqbest.core.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.zaqbest.core.redis.helper.RedisHelper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * redis配置
 *
 * @author allan
 * @date 2022/3/23
 */
@Configuration
@EnableConfigurationProperties(RedisConfigurationProperties.class)
public class RedisAutoConfiguration extends CachingConfigurerSupport {

    @Autowired
    private RedisConfigurationProperties redisConfigurationProperties;

    /**
     * @return 自定义策略生成的key
     * @description 自定义的缓存key的生成策略 若想使用这个key
     * 只需要讲注解上keyGenerator的值设置为keyGenerator即可</br>
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 配置自定义redisTemplate
     *
     * @return
     */

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean("lettuceConnectionFactory")
    @ConditionalOnProperty(name = "spring.redis.sentinel.sentinelCluster", havingValue = "true")
    public LettuceConnectionFactory getLettuceConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(getSentinelConfig(),
                getLettucePoolingClientConfiguration());
        lettuceConnectionFactory.setDatabase(redisConfigurationProperties.getDatabase());
        return lettuceConnectionFactory;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.redis.sentinel.sentinelCluster", havingValue = "false")
    LettuceConnectionFactory lettuceConnectionFactory() {
        // 连接池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(redisConfigurationProperties.getMaxIdle() == 0 ? 8 :
                redisConfigurationProperties.getMaxIdle());
        poolConfig.setMinIdle(redisConfigurationProperties.getMinIdle() == 0 ? 1 :
                redisConfigurationProperties.getMinIdle());
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration =
                LettucePoolingClientConfiguration.builder()
                        .poolConfig(poolConfig)
                        .build();
        // 单机redis
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisConfigurationProperties.getHost());
        redisConfig.setPort(redisConfigurationProperties.getPort());
        if (redisConfigurationProperties.getPassword() != null && !"".equals(redisConfigurationProperties.getPassword())) {
            redisConfig.setPassword(redisConfigurationProperties.getPassword());
        }

        return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
    }

    /**
     * lettuce连接池配置
     *
     * @return LettucePoolingClientConfiguration
     */
    private LettucePoolingClientConfiguration getLettucePoolingClientConfiguration() {
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =
                LettucePoolingClientConfiguration.builder();
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(redisConfigurationProperties.getMaxActive());
        genericObjectPoolConfig.setMaxIdle(redisConfigurationProperties.getMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisConfigurationProperties.getMinIdle());
        genericObjectPoolConfig.setMaxWaitMillis(redisConfigurationProperties.getMaxWait());
        builder.poolConfig(genericObjectPoolConfig);
        builder.shutdownTimeout(Duration.ofMillis(redisConfigurationProperties.getShutdownTimeout()));
        return builder.build();
    }

    /**
     * sentinel配置
     *
     * @return RedisSentinelConfiguration
     */
    private RedisSentinelConfiguration getSentinelConfig() {
        RedisSentinelConfiguration redisSentinelConfiguration =
                new RedisSentinelConfiguration(redisConfigurationProperties.getMaster(),
                        redisConfigurationProperties.getNodes());
        redisSentinelConfiguration.setPassword(RedisPassword.of(redisConfigurationProperties.getPassword().toCharArray()));
        redisSentinelConfiguration.setDatabase(redisConfigurationProperties.getDatabase());
        return redisSentinelConfiguration;
    }

    @Bean
    RedisHelper getRedisHelper(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        return new RedisHelper(redisTemplate, stringRedisTemplate);
    }

}

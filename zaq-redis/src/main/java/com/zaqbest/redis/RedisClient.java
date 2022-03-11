package com.zaqbest.redis;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisClient {
    private final static String REDIS_SEPARATOR = ":";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void removeRedisKey(String key, String prefix) {
        String rediskey = buildRedisKey(prefix, key);
        redisTemplate.delete(rediskey);
    }

    public Object get(final String key, final String prefix) {
        String redisKey = buildRedisKey(prefix, key);
        return redisTemplate.opsForValue().get(redisKey);
    }

    public boolean set(final String key, final Object value, final String prefix, final Long expireTime) {
        boolean result = false;
        try {
            String redisKey = buildRedisKey(prefix, key);
            log.info("构建的rediskey:{}, 存入的val为:{}", redisKey, value);
            if (expireTime == null) {
                redisTemplate.opsForValue().set(redisKey, value);
            } else {
                redisTemplate.opsForValue().set(redisKey, value, expireTime, TimeUnit.SECONDS);
            }
            result = true;
        } catch (Exception e) {
            log.error("cause=[{}]", e);
        }
        return result;
    }

    public String buildRedisKey(String prefix, String key) {

        StringBuffer sb = new StringBuffer();

        if (StrUtil.isNotBlank(prefix)) {
            sb.append(prefix).append(REDIS_SEPARATOR).append(key);
        } else {
            sb.append(key);
        }
        return sb.toString();
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}

package com.zaqbest.comm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zaqbest.comm.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作实现类
 * Created by macro on 2020/3/3.
 */
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Value("${redis.database:zaq}")
    private String database;
    
    @Override
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(makeKey(key), value, time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(makeKey(key), value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(makeKey(key));
    }

    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(makeKey(key));
    }

    @Override
    public Long del(List<String> keys) {
        return redisTemplate.delete(makeKeys(keys));
    }

    @Override
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(makeKey(key), time, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(makeKey(key), TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(makeKey(key));
    }

    @Override
    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(makeKey(key), delta);
    }

    @Override
    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().increment(makeKey(key), -delta);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(makeKey(key), hashKey);
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        redisTemplate.opsForHash().put(makeKey(key), hashKey, value);
        return expire(key, time);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(makeKey(key), hashKey, value);
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(makeKey(key));
    }

    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(makeKey(key), map);
        return expire(key, time);
    }

    @Override
    public void hSetAll(String key, Map<String, ?> map) {
        redisTemplate.opsForHash().putAll(makeKey(key), map);
    }

    @Override
    public void hDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(makeKey(key), hashKey);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(makeKey(key), hashKey);
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(makeKey(key), hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(makeKey(key), hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(makeKey(key));
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(makeKey(key), values);
    }

    @Override
    public Long sAdd(String key, long time, Object... values) {
        Long count = redisTemplate.opsForSet().add(makeKey(key), values);
        expire(key, time);
        return count;
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(makeKey(key), value);
    }

    @Override
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(makeKey(key));
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(makeKey(key), values);
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(makeKey(key), start, end);
    }

    @Override
    public Long lSize(String key) {
        return redisTemplate.opsForList().size(makeKey(key));
    }

    @Override
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(makeKey(key), index);
    }

    @Override
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(makeKey(key), value);
    }

    @Override
    public Long lPush(String key, Object value, long time) {
        Long index = redisTemplate.opsForList().rightPush(makeKey(key), value);
        expire(key, time);
        return index;
    }

    @Override
    public Long lPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(makeKey(key), values);
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        Long count = redisTemplate.opsForList().rightPushAll(makeKey(key), values);
        expire(makeKey(key), time);
        return count;
    }

    @Override
    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(makeKey(key), count, value);
    }
    
    private String makeKey(String key){
        return StrUtil.format("{}:{}", database, key);
    }

    private List<String> makeKeys(List<String> keys){
        if (CollUtil.isEmpty(keys)) {
            return keys;
        }
        List<String> ans = new ArrayList<>();
        keys.stream().forEach(x->ans.add(StrUtil.format("{}:{}", database, x)));
        return ans;
    }
}

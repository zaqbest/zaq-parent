/*
 * 版权信息: © fusionfintrade
 */ 
package com.zaqbest.core.redis.helper;

import cn.hutool.core.util.StrUtil;
import com.zaqbest.core.utils.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author allan
 * @date 2022/3/23
 */
@Slf4j
@ConditionalOnMissingBean(RedisHelper.class)
public class RedisHelper {

    /**
     * 构建redis key的分隔符
     */
    private final static String REDIS_SEPARATOR = ":";

    private RedisTemplate<String, Object> redisTemplate;

    private StringRedisTemplate stringRedisTemplate;

    public RedisHelper(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 查询key列表
     *
     * @param pattern 匹配表达式
     * @return
     */
    public Set<String> getKeys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return new HashSet<>();
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     *            登保要求不可做删除操作
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.opsForValue().set(key[0], "", 1, TimeUnit.MILLISECONDS);
            } else {
                for (String k : key) {
                    redisTemplate.opsForValue().set(k, "", 1, TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    /**
     * TODO
     *
     * @param prefix
     * @return
     * @author allan
     * @date 2023/2/11
     */
    public int delPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        for (String k : keys) {
            redisTemplate.opsForValue().set(k, "", 1, TimeUnit.MILLISECONDS);
        }
        return keys.size();
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String getString(String key) {
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }

    }

    /**
     * 如果{@code key}不存在，则设置{@code key}以保存字符串{@code value}和过期{@code timeout}。
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setIfAbsent(String key, Object value, long time) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 如果存在{@code key}，则设置{@code key}以保存字符串{@code value}和过期{@code timeout}。
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setIfPresent(String key, Object value, long time) {
        try {
            redisTemplate.opsForValue().setIfPresent(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {

        return redisTemplate.opsForHash().get(key, item);
    }

    public Object hGetAll(String key) {
        final Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

        System.out.println(entries);
        return entries;
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param map 值
     * @return true 成功 false失败
     */
    public boolean hsetAll(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return null;
        }
    }

    /**
     * 裁剪list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public void lTrim(String key, long start, long end) {
        try {
            this.redisTemplate.opsForList().trim(key, start, end);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("程序异常:", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return 0;
        }
    }

    /**
     * 新增key 并设置过期时间
     *
     * @param key
     * @param value
     * @param expireTime 时间长度
     * @param unit       单位
     * @return
     */
    public boolean set(String key, Object value, long expireTime, TimeUnit unit) {

        try {
            redisTemplate.opsForValue().set(key, value, expireTime, unit);
            return true;
        } catch (Exception e) {
            log.error("程序异常:", e);
            return false;
        }

    }

    /**
     * redis锁
     *
     * @param k
     * @param v
     * @param time
     * @param unit
     * @return
     */
    public boolean setIfAbsent(String k, String v, long time, TimeUnit unit) {

        return redisTemplate.opsForValue().setIfAbsent(k, v, time, unit);
    }

    /**
     * redis锁
     *
     * @param k
     * @param v
     * @param time
     * @param unit
     * @return
     */
    public boolean setIfAbsent(String k, Object v, long time, TimeUnit unit) {

        return redisTemplate.opsForValue().setIfAbsent(k, v, time, unit);
    }


    /**
     * 获取锁
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时时间
     * @return
     */
    public boolean tryLock(String key, String value, long timeout) {
        try {
            // 对应setnx命令，可以成功设置,也就是key不存在，获得锁成功
            return stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("tryLock {}", key, e);
        }
        return false;
    }

    /**
     * 释放锁 因为不支持删除操作，将时间调小
     *
     * @param key 键
     */
    public void releaseLock(String key) {
        try {
            stringRedisTemplate.opsForValue().set(key, "UN LOCK", 1, TimeUnit.MICROSECONDS);
            log.info("释放锁资源完成：{}", key);
        } catch (Exception e) {
            log.error("redis release key 异常：", e);
        }
    }

    /**
     * 获取字符串值
     *
     * @param key 键
     * @return 值
     */
    public String getCacheString(String key) {
        return redisTemplate.opsForValue().get(key) == null ? "" : String.valueOf(redisTemplate.opsForValue().get(key));
    }

    /**
     * 查询带有前缀的内容
     *
     * @param key
     * @param prefix
     * @return java.lang.Object
     * @author allan
     * @date 2022/3/27
     */
    public Object get(final String key, final String prefix) {
        String redisKey = this.buildRedisKey(prefix, key);
        return this.redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 根据key和prefix， 将value写入缓存，并设置超时时间
     *
     * @param key
     * @return
     */
    public boolean set(final String key, final Object value, final String prefix, final Long expireTime) {
        boolean result = false;
        try {
            String redisKey = buildRedisKey(prefix, key);
            log.debug("构建的rediskey:{}, 存入的val为:{}", redisKey, value);
            if (expireTime == null) {
                redisTemplate.opsForValue().set(redisKey, value);
            } else {
                redisTemplate.opsForValue().set(redisKey, value, expireTime, TimeUnit.SECONDS);
            }
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 使用scan命令 查询某些前缀的key
     *
     * @param key
     * @return
     */
    public Set<String> scan(String key) {
        Set<String> res = this.redisTemplate.execute(new RedisCallback<Set<String>>() {

            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {

                Set<String> binaryKeys = new HashSet<>();

                Cursor<byte[]> cursor =
                        connection.scan(new ScanOptions.ScanOptionsBuilder().match(key).count(1000).build());
                while (cursor.hasNext()) {
                    binaryKeys.add(new String(cursor.next()));
                }
                return binaryKeys;
            }
        });
        return res;
    }

    /**
     * 使用scan命令 查询某些前缀的key 有多少个 用来获取当前session数量,也就是在线用户
     *
     * @param key
     * @return
     */
    public Long scanSize(String key) {
        long dbSize = this.redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long count = 0L;
                Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(key).count(1000).build());
                while (cursor.hasNext()) {
                    cursor.next();
                    count++;
                }
                return count;
            }
        });
        return dbSize;
    }


    /**
     * 构建redis存储key,没有前缀则返回key
     *
     * @param prefix
     * @param key
     * @return
     */
    public String buildRedisKey(String prefix, String key) {
        StringBuffer sb = new StringBuffer();
        if (!Objects.isNull(prefix)) {
            sb.append(prefix).append(REDIS_SEPARATOR).append(key);
        } else {
            sb.append(key);
        }
        return sb.toString();
    }


    /**
     * 轮询获取锁
     *
     * @param k
     * @param v                systemTime
     * @param time             默认1000s
     * @param unit             默认ms
     * @param retryCount       重试次数 默认三次
     * @param singleTimeLength 单次睡眠时长 默认单次100ms
     * @throws InterruptedException
     */
    public void setPollingLockByKey(String k, String v, long time, TimeUnit unit, int retryCount, long singleTimeLength) {
        TimeUnit timeUnit = unit == null ? TimeUnit.MILLISECONDS : unit;
        singleTimeLength = singleTimeLength == 0 ? 100 : singleTimeLength;
        retryCount = retryCount == 0 ? 3 : retryCount;
        v = StrUtil.isBlank(v) ? String.valueOf(System.currentTimeMillis()) : v;
        try {
            pollingLockByKey(k, v, time, timeUnit, retryCount, singleTimeLength);
        } catch (InterruptedException e) {
            final String msg = " key: " + k + " 加锁失败";
            log.error(msg);
            releaseLock(k);
        }
    }

    public void pollingLockByKey(String k, String v, long time, TimeUnit unit, Integer retryCount, Long singleTimeLength) throws InterruptedException {
        log.info("开始给key:{}加锁处理,加锁时长:{}", k, time);
        time = time == 0 ? 1000 : time;
        Boolean result = redisTemplate.opsForValue().setIfAbsent(k, v, time, unit);
        while (result != null && !result) {
            if (retryCount == 0) {
                final String msg = "获取 key: " + k + " 锁失败";
                log.error(msg);
                Exceptions.throwBusinessException("服务繁忙，请稍后再试！");
            }
            Thread.sleep(singleTimeLength);
            result = redisTemplate.opsForValue().setIfAbsent(k, v, time, unit);
            log.warn("获取 key: {} 锁失败,剩余次数：{}", k, retryCount);
            retryCount--;
        }
    }

}

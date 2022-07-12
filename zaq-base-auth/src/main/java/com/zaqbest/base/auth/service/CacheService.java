package com.zaqbest.base.auth.service;

/**
 * 由应用程序具体实现
 */
public interface CacheService {
    Object get(final String key, final String prefix);

    void set(final String key, final Object value, final Long expireTime);

    void set(final String key, final Object value, final String prefix, final Long expireTime);
}

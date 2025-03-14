package com.hosecloud.hab.plugin.cache;


public interface CacheService {
    /**
     * 根据key获取缓存
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置缓存
     * @param key
     * @param value
     * @param expireSeconds 过期时间，单位秒
     */
    void set(String key, String value, int expireSeconds);
}

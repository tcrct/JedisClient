package com.duang.jedisclient.plugin;

import com.duang.jedisclient.common.RedisConfig;
import com.duang.jedisclient.core.IJedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis工厂类
 *
 * @author Laotang
 * @since 1.0
 * @date 2020-09-07
 */
public class RedisFactory {

    private static Map<String, RedisConfig.RedisType> jedisClientTypeMap = new ConcurrentHashMap<String,RedisConfig.RedisType>();
    private static Map<String, IJedisClient> jedisClientMap = new ConcurrentHashMap<String,IJedisClient>();
    private static String DEFAULT_APPKEY;
    /***/
    public final static ThreadLocal<Jedis> THREAD_LOCAL_JEDIS = new ThreadLocal<Jedis>();
    /***/
    public final static ThreadLocal<JedisCluster> THREAD_LOCAL_JEDIS_CLUSTER = new ThreadLocal<JedisCluster>();

    private RedisFactory() {
    }

    private static String getDefaultAppkey() {
        if (null == DEFAULT_APPKEY) {
            DEFAULT_APPKEY = jedisClientMap.keySet().iterator().next();
        }
        return DEFAULT_APPKEY;
    }

    public static IJedisClient getClient() {
        return getClient(getDefaultAppkey());
    }

    public static IJedisClient getClient(String appId) {
        return jedisClientMap.get(appId);
    }

    public static RedisConfig.RedisType getClientType() {
        return getClientType(getDefaultAppkey());
    }
    public static RedisConfig.RedisType getClientType(String appId) {
        return jedisClientTypeMap.get(appId);
    }

    protected static void setClient(IJedisClient jedisClient, RedisConfig redisConfig) {
        RedisFactory.jedisClientMap.put(redisConfig.getAppId(), jedisClient);
        RedisFactory.jedisClientTypeMap.put(redisConfig.getAppId(), redisConfig.getRedisType());
    }
}

package com.duang.jedisclient.plugin;

import com.duang.jedisclient.core.IJedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import javax.xml.bind.annotation.XmlType;
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

    private static Map<String, IJedisClient> jedisClientMap = new ConcurrentHashMap<String,IJedisClient>();
    private static String DEFAULT_APPKEY;
    /***/
    public final static ThreadLocal<Jedis> THREAD_LOCAL_JEDIS = new ThreadLocal<Jedis>();
    /***/
    public final static ThreadLocal<JedisCluster> THREAD_LOCAL_JEDIS_CLUSTER = new ThreadLocal<JedisCluster>();

    private RedisFactory() {
    }

    public static IJedisClient getClient() {
        if (null == DEFAULT_APPKEY) {
            DEFAULT_APPKEY = jedisClientMap.keySet().iterator().next();
        }
        return jedisClientMap.get(DEFAULT_APPKEY);
    }

    public static IJedisClient getClient(String appId) {
        return jedisClientMap.get(appId);
    }

    protected static void setClient(String appId, IJedisClient jedisClient) {
        RedisFactory.jedisClientMap.put(appId, jedisClient);
    }
}

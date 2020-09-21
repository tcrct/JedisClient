package com.duang.jedisclient.heandle;

import com.duang.jedisclient.common.RedisConfig;
import com.duang.jedisclient.plugin.RedisFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 单实例Redis链接池处理器，目前只对单StandalOne模式进行管理
 *      将jedis放入到threadLocal中，加到入拦截器中，让一个请求共用一个jedis对象，请求处理完成后销毁
 *      能有效地提高效率，减少资源开销
 * Redis Cluster模式下的是共用同一个jedisCluster对象，无需再自行封装管理
 *
 * @author Laotang
 * @since 1.0
 * @date 2020-09-16
 */
public class StandalOneRedisHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandalOneRedisHandle.class);

    private static boolean isStandalOneType() {
        return RedisConfig.RedisType.STANDALONE.equals(RedisFactory.getClientType());
    }

    public static void setThreadLocalPool() {
        if (isStandalOneType()) {
            Jedis jedis = RedisFactory.THREAD_LOCAL_JEDIS.get();
            if (null == jedis) {
                try {
                    jedis = RedisFactory.getClient().getResource();
                    if (null != jedis) {
                        RedisFactory.THREAD_LOCAL_JEDIS.set(jedis);
                        LOGGER.info("StandalOneRedisHandle setThreadLocalPool jedis hashCode:" + jedis.hashCode());
                    }
                } catch (Exception e) {
                    RedisFactory.THREAD_LOCAL_JEDIS.remove();
                    jedis.close();
                }
            }
        }
    }

    public static void closeThreadLocalPool() {
        if (isStandalOneType()) {
            Jedis jedis = RedisFactory.THREAD_LOCAL_JEDIS.get();
            if (null != jedis) {
                RedisFactory.THREAD_LOCAL_JEDIS.remove();
                jedis.close();
                LOGGER.info("StandalOneRedisHandle closeThreadLocalPool jedis hashCode:" + jedis.hashCode());
            }
        }
    }
}

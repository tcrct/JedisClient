package com.duang.jedisclient.builder;

import com.duang.jedisclient.common.RedisConfig;

/**
 * redis客户端builder
 *
 * @author Laotang
 * @Date  2020-09-05
 * @since 1.0
 */

public class ClientBuilder {

    /**
     * 构造redis cluster的builder
     *
     * @param appId
     * @return
     */
    public static RedisClusterBuilder redisCluster(final RedisConfig config) {
        return new RedisClusterBuilder(config);
    }

    /**
     * 构造redis sentinel的builder
     *
     * @param appId
     * @return
     */
    public static RedisSentinelBuilder redisSentinel(final RedisConfig config) {
        return new RedisSentinelBuilder(config);
    }

    /**
     * 构造redis standalone的builder
     * @param appId
     * @return
     */
    public static RedisStandaloneBuilder redisStandalone(final RedisConfig config) {
        return new RedisStandaloneBuilder(config);
    }
}

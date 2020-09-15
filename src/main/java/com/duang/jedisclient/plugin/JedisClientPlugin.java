package com.duang.jedisclient.plugin;

import com.duang.jedisclient.builder.ClientBuilder;
import com.duang.jedisclient.builder.RedisClusterBuilder;
import com.duang.jedisclient.builder.RedisSentinelBuilder;
import com.duang.jedisclient.builder.RedisStandaloneBuilder;
import com.duang.jedisclient.common.CacheException;
import com.duang.jedisclient.common.RedisConfig;
import com.duang.jedisclient.core.IJedisClient;
import com.duang.jedisclient.core.Redis;
import com.duang.jedisclient.core.RedisCluster;
import com.duang.jedisclient.utils.RedisUtil;
import com.duang.jedisclient.serializer.FastJsonSerializer;
import com.duang.jedisclient.serializer.ISerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisSentinelPool;

/**
 * RedisCluster Plugin
 * @author Laotang
 * @date 2020-09-05
 * @since 1.0
 */
public class JedisClientPlugin implements IPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClientPlugin.class);

    private RedisConfig redisConfig;
    private IJedisClient jedisClient;

    public JedisClientPlugin(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @Override
    public void start() throws Exception {
        RedisConfig.RedisType redisType = redisConfig.getRedisType();
        ISerializer serializer = redisConfig.getSerializer();
        String appId = redisConfig.getAppId();
        if (null == appId) {
            throw new NullPointerException("JedisClient AppId 不能为空");
        }
        if (null == redisType) {
            redisType = RedisConfig.RedisType.STANDALONE;
        }
        if (null == serializer) {
            serializer = new FastJsonSerializer();
        }
        if (RedisConfig.RedisType.CLUSTER.equals(redisType)) {
            RedisClusterBuilder clusterBuilder = ClientBuilder.redisCluster(redisConfig);
            jedisClient = new RedisCluster(clusterBuilder.build(), serializer);
        }
        else if (RedisConfig.RedisType.STANDALONE.equals(redisType)) {
            RedisStandaloneBuilder standaloneBuilder = ClientBuilder.redisStandalone(redisConfig);
            jedisClient = new Redis(standaloneBuilder.build(), serializer, redisType);
        }
        else if (RedisConfig.RedisType.SENTINEL.equals(redisType)) {
            RedisSentinelBuilder sentinelBuilder = ClientBuilder.redisSentinel(redisConfig);
            JedisSentinelPool sentinelPool = sentinelBuilder.build();
            jedisClient = new Redis(sentinelPool, serializer, redisType);
            HostAndPort currentHostMaster = sentinelPool.getCurrentHostMaster();
            if (null != currentHostMaster) {
                LOGGER.info("current master: {}", currentHostMaster.toString());
            }
        }

        if (null == jedisClient) {
            throw new CacheException("构建JedisClient时出错");
        }
        RedisFactory.setClient(appId, jedisClient);
        RedisUtil.log(LOGGER, "链接JedisClient成功: " + RedisUtil.toJsonString(redisConfig));
    }

    @Override
    public void stop() throws Exception {
        redisConfig = null;
        jedisClient = null;
    }
}

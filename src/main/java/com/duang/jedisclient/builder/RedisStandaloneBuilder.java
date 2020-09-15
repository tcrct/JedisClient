package com.duang.jedisclient.builder;

import cn.hutool.core.util.StrUtil;
import com.duang.jedisclient.common.RedisConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 构造redis单机的builder；
 *
 * @author: Laotang
 * @date 2020-09-05
 * @since 1.0
 */
public class RedisStandaloneBuilder {
    private Logger logger = LoggerFactory.getLogger(RedisStandaloneBuilder.class);

    private static final Lock LOCK = new ReentrantLock();
    private volatile JedisPool jedisPool;
    private GenericObjectPoolConfig poolConfig;
    private RedisConfig config;
    private HostAndPort node;
    private int timeout = Protocol.DEFAULT_TIMEOUT;

    /**
     * 构造函数package访问域，package外直接构造实例；
     *
     * @param appId
     */
    RedisStandaloneBuilder(final RedisConfig config) {
        this.config = config;
        poolConfig = config.getJedisPoolConfig();
        if (null == poolConfig) {
            poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 3);
            poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
            poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
            poolConfig.setJmxEnabled(true);
        }
        poolConfig.setJmxNamePrefix("jedis-standalone-pool");
        if (null != config.getNodeSet() && !config.getNodeSet().isEmpty()) {
            this.node = config.getNodeSet().iterator().next();
        }
    }

    public JedisPool build() {
        if (jedisPool == null && node != null) {
            while (true) {
                try {
                    LOCK.tryLock(100, TimeUnit.MILLISECONDS);
                    if (jedisPool == null) {
                        if (StrUtil.isBlank(config.getPassword())) {
                            jedisPool = new JedisPool(poolConfig, node.getHost(), node.getPort(), config.getConnectionTimeout());
                        } else {
                            jedisPool = new JedisPool(poolConfig, node.getHost(), node.getPort(), config.getConnectionTimeout(), config.getPassword());
                        }

                        return jedisPool;
                    }
                } catch (InterruptedException e) {
                    logger.error("error in build().", e);
                }
            }
        }
        return jedisPool;
    }
}

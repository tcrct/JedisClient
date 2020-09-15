package com.duang.jedisclient.builder;

import cn.hutool.core.util.StrUtil;
import com.duang.jedisclient.common.RedisConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis sentinel 客户端的builder
 * @author Laotang
 * @since 1.0
 */
public class RedisSentinelBuilder {
    private static Logger logger = LoggerFactory.getLogger(RedisSentinelBuilder.class);
    
    /**
     * jedis对象池配置
     */
    private GenericObjectPoolConfig poolConfig;
    /**
     * jedis sentinel连接池
     */
    private volatile JedisSentinelPool sentinelPool;
    /**
     * 构建锁
     */
    private static final Lock LOCK = new ReentrantLock();

    private RedisConfig config;

    /**
     * 构造函数package访问域，package外不能直接构造实例；
     *
     * @param appId
     */
    RedisSentinelBuilder(final RedisConfig config) {
        this.config = config;
        poolConfig = config.getJedisPoolConfig();
        if (null == poolConfig) {
            poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 3);
            poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
            poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
            poolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
            poolConfig.setJmxEnabled(true);
        }
        poolConfig.setJmxNamePrefix("jedis-sentinel-pool");
    }

    public JedisSentinelPool build() {
        if (sentinelPool == null) {
            Set<String> sentinelSet = new HashSet<String>();
            Set<HostAndPort> nodeSet = config.getNodeSet();
            if (null != nodeSet) {
                for (HostAndPort hostAndPort : nodeSet) {
                    sentinelSet.add(hostAndPort.getHost()+":"+hostAndPort.getPort());
                }
            }
            while (true) {
                try {
                    LOCK.tryLock(10, TimeUnit.MILLISECONDS);
                    if (StrUtil.isBlank(config.getPassword())) {
                        sentinelPool = new JedisSentinelPool(config.getMasterName(), sentinelSet, poolConfig, config.getConnectionTimeout(), config.getReadTimeout(), null, Protocol.DEFAULT_DATABASE);
                    } else {
                        sentinelPool = new JedisSentinelPool(config.getMasterName(), sentinelSet, poolConfig, config.getConnectionTimeout(), config.getReadTimeout(), config.getPassword(), Protocol.DEFAULT_DATABASE);
                    }
                    return sentinelPool;
                } catch (Throwable e) {//容错
                    logger.error("error in build, appId: {}", config.getAppId(), e);
                } finally {
                    LOCK.unlock();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));//活锁
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return sentinelPool;
    }
}

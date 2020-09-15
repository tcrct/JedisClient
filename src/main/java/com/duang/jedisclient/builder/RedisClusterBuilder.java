package com.duang.jedisclient.builder;

import cn.hutool.core.util.StrUtil;
import com.duang.jedisclient.common.RedisConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis cluster 客户端builder
 * @author Laotang
 * @since 1.0
 * @date 2020-09-05
 */
public class RedisClusterBuilder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * jedis对象池配置
     */
    private GenericObjectPoolConfig jedisPoolConfig;
    
    /**
     * jedis集群对象
     */
    private JedisCluster jedisCluster;

    /**
     * 构建锁
     */
    private final Lock lock = new ReentrantLock();
    
    /**
     * 集群密码
     */
    private RedisConfig config;

    /**
     * 构造函数package访问域，package外不能直接构造实例；
     *
     * @param config
     */
    RedisClusterBuilder(final RedisConfig config) {
        this.config = config;
        this.jedisPoolConfig = config.getJedisPoolConfig();
        if (null == jedisPoolConfig) {
            jedisPoolConfig = new GenericObjectPoolConfig();
            jedisPoolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 5);
            jedisPoolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
            jedisPoolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
            jedisPoolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
            jedisPoolConfig.setJmxEnabled(true);
        }
        jedisPoolConfig.setJmxNamePrefix("jedis-cluster-pool");
    }

    public JedisCluster build() {
        if (jedisCluster == null) {
            while (true) {
                try {
                    lock.tryLock(10, TimeUnit.SECONDS);
                    if (jedisCluster != null) {
                        return jedisCluster;
                    }
                    if (StrUtil.isBlank(config.getPassword())) {
                        jedisCluster = new JedisCluster(config.getNodeSet(), config.getConnectionTimeout(), config.getReadTimeout(), config.getMaxRedirections(), jedisPoolConfig);
                    } else {
                        jedisCluster = new JedisCluster(config.getNodeSet(), config.getConnectionTimeout(), config.getReadTimeout(), config.getMaxRedirections(), config.getPassword(), jedisPoolConfig);
                    }
                    return jedisCluster;
                } catch (Throwable e) {
                    logger.error(e.getMessage(), e);
                } finally {
                    lock.unlock();
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));//活锁
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } else {
            return jedisCluster;
        }
    }
}

package com.duang.jedisclient.plugin;

import com.duang.jedisclient.common.RedisConfig;
import com.duang.jedisclient.serializer.ISerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Protocol;

import java.util.HashSet;
import java.util.Set;

public class JedisClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClient.class);

    private RedisConfig redisConfig;

    public JedisClient(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    public void start() {
        try {
            JedisClientPlugin jedisClientPlugin = new JedisClientPlugin(redisConfig);
            jedisClientPlugin.start();
        } catch (Exception e) {
            LOGGER.info("启动Redis时出错: " + e.getMessage(), e);
        }
    }

    public static class  Builder {

        /**
         * 应用id
         */
        private String appId;
        /**
         * 安全码
         */
        private String secret;
        /**
         * 集群节点
         */
        private Set<HostAndPort> nodeSet = new HashSet<>();
        /**
         * 集群密码
         */
        private String password;
        /**
         * Redis类型
         */
        private RedisConfig.RedisType redisType;

        /**
         * 序列化方式
         */
        private ISerializer serializer;

        /**
         * jedis读写超时(单位:毫秒)
         * 默认2秒
         */
        private Integer readTimeout;
        /**
         * 节点定位重试次数:默认3次
         */
        private Integer maxRedirections = 3;

        /**
         * 设置配置
         */
        private GenericObjectPoolConfig jedisPoolConfig;


        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder node(HostAndPort hostAndPort) {
            this.nodeSet.add(hostAndPort);
            return this;
        }
        public Builder nodeSet(Set<HostAndPort> nodeSet) {
            this.nodeSet.clear();
            this.nodeSet.addAll(nodeSet);
            return this;
        }

        public Builder password(String password) {
            if (null == password || "".equals(password.trim()) || password.trim().length() == 0) {
                return this;
            }
            this.password = password;
            return this;
        }

        public Builder redisType(RedisConfig.RedisType redisType) {
            this.redisType = redisType;
            return this;
        }

        public Builder serializer(ISerializer serializer) {
            this.serializer = serializer;
            return this;
        }

        public Builder readTimeOut(Integer timeout) {
            this.readTimeout = timeout;
            return this;
        }

        public Builder maxRedirections(Integer maxRedirections) {
            this.maxRedirections = maxRedirections;
            return this;
        }

        public Builder poolConfig(GenericObjectPoolConfig poolConfig) {
            this.jedisPoolConfig = poolConfig;
            return this;
        }

        public JedisClient build() {
            RedisConfig redisConfig = new RedisConfig(appId,secret,nodeSet,serializer,redisType);
            if (null != password && password.trim().length() > 0) {
                redisConfig.setPassword(password);
            }
            if (null != readTimeout && readTimeout > 0) {
                redisConfig.setReadTimeout(readTimeout);
            }
            if (null != maxRedirections && maxRedirections > 0) {
                redisConfig.setMaxRedirections(maxRedirections);
            }
            if (null != jedisPoolConfig) {
                redisConfig.setJedisPoolConfig(jedisPoolConfig);
            }
            return new JedisClient(redisConfig);
        }
    }
}

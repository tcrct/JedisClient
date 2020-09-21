package com.duang.jedisclient.plugin;

import com.duang.jedisclient.common.RedisConfig;
import com.duang.jedisclient.serializer.ISerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;

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

        public JedisClient build() {
            return new JedisClient( new RedisConfig(appId,secret,nodeSet,password,serializer,redisType) );
        }
    }
}

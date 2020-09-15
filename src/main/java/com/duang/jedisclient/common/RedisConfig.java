package com.duang.jedisclient.common;

import com.duang.jedisclient.serializer.ISerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Protocol;

import java.util.Set;

/**
 * 配置
 * @author Laotang
 * @since 1.0
 */
public class RedisConfig {

    /**
     * 应用id
     */
    private String appId;
    /**
     * 安全码
     */
    private String secret;

    /**
     * jedis连接超时(单位:毫秒)
     * 默认2秒
     */
    private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;

    /**
     * jedis读写超时(单位:毫秒)
     * 默认2秒
     */
    private int readTimeout = Protocol.DEFAULT_TIMEOUT;

    /**
     * JedisPool.borrowObject最大等待时间
     */
    private long maxWaitMillis = 1000L;

    /**
     * 节点定位重试次数:默认3次
     */
    private int maxRedirections = 3;

    /**
     * 集群节点
     */
    private Set<HostAndPort> nodeSet;

    /**
     * 设置配置
     */
    private GenericObjectPoolConfig jedisPoolConfig;

    /**
     * 集群密码
     */
    private String password;

    /**
     * 哨兵集群时需要指定主节点名称
     */
    private String masterName;

    /**
     * Redis类型
     */
    private RedisType redisType;

    /**
     * 序列化方式
     */
    private ISerializer serializer;

    public enum RedisType {
        STANDALONE, SENTINEL, CLUSTER,
    }



    public RedisConfig() {
    }

    public RedisConfig(String appId, String secret, Set<HostAndPort> nodeSet, String password) {
        this.appId = appId;
        this.secret = secret;
        this.nodeSet = nodeSet;
        this.password = password;
    }

    public RedisConfig(String appId, String secret, Set<HostAndPort> nodeSet, String password, ISerializer serializer, RedisType redisType) {
        this.appId = appId;
        this.secret = secret;
        this.nodeSet = nodeSet;
        this.password = password;
        this.serializer = serializer;
        this.redisType = redisType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public Set<HostAndPort> getNodeSet() {
        return nodeSet;
    }

    public void setNodeSet(Set<HostAndPort> nodeSet) {
        this.nodeSet = nodeSet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GenericObjectPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(GenericObjectPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public RedisType getRedisType() {
        return redisType;
    }

    public void setRedisType(RedisType redisType) {
        this.redisType = redisType;
    }

    public ISerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(ISerializer serializer) {
        this.serializer = serializer;
    }
}

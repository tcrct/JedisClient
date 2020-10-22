package com.duang.jedisclient.core;

import com.duang.jedisclient.common.CacheKeyModel;
import com.duang.jedisclient.common.RedisConfig;
import com.duang.jedisclient.serializer.ISerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractRedis implements IJedisClient {

    protected final static String OK = "OK";
    protected JedisPool jedisPool;
    protected JedisSentinelPool jedisSentinelPool;
    protected JedisCluster jedisCluster;
    protected RedisConfig redisConfig;
    protected ISerializer serializer;

    @Override
    public Jedis getResource() {
        if (null != jedisPool && RedisConfig.RedisType.STANDALONE.equals(redisConfig.getRedisType())) {
            return jedisPool.getResource();
        }
        if (null != jedisSentinelPool && RedisConfig.RedisType.SENTINEL.equals(redisConfig.getRedisType())) {
            return jedisSentinelPool.getResource();
        }
        return null;
    }

    public JedisCluster getClusterResource() {
        return jedisCluster;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public AbstractRedis(JedisPool jedisPool, RedisConfig redisConfig){
        this.jedisPool = jedisPool;
        this.redisConfig = redisConfig;
        this.serializer = redisConfig.getSerializer();
    }

    public AbstractRedis(JedisSentinelPool jedisSentinelPool, RedisConfig redisConfig){
        this.jedisSentinelPool = jedisSentinelPool;
        this.redisConfig = redisConfig;
        this.serializer = redisConfig.getSerializer();
    }

    public AbstractRedis(JedisCluster jedisCluster,  RedisConfig redisConfig){
        this.jedisCluster = jedisCluster;
        this.redisConfig = redisConfig;
        this.serializer = redisConfig.getSerializer();
    }
    /**
     * 序列化key
     * @param key 要缓存的key值
     * @return
     */
    protected byte[] serializerKey(String key)  {
        return serializer.serializerKey(key);
    }
    protected byte[] serializerField(String key)  {
        return serializer.serializerField(key);
    }

    protected String deSerializeKey(byte[] key)  {
        return serializer.deSerializerKey(key);
    }

    protected byte[][] serializerKeyArray(String... keys) {
        byte[][] result = new byte[keys.length][];
        for (int i=0; i<result.length; i++) {
            result[i] = serializerKey(keys[i]);
        }
        return result;
    }

    protected byte[][] serializerValueArray(Object... keys) {
        byte[][] result = new byte[keys.length][];
        for (int i=0; i<result.length; i++) {
            result[i] = serializerValue(keys[i]);
        }
        return result;
    }


    /**
     * 序列化value
     * @param value 要缓存的value值
     * @return
     */
    protected byte[] serializerValue(Object value) {
//        return SafeEncoder.encode((String)value);
        return serializer.serializerValue(value);
    }

    /**
     * 反序列化value
     * @param bytes 要反序列化的字节数组
     * @return
     */
    protected <T> T deSerializeValue(byte[] bytes, Class<T> type) {
        return  (String.class.equals(type)) ? (T)new String(bytes) : serializer.deSerializerValue(bytes, type);
    }

    protected <T> List<T> toValueList(List<byte[]> data, Class<T> type) {
        if (null == data) {
            return null;
        }
        List<T> result = new ArrayList<T>();
        for (byte[] d : data) {
            result.add(deSerializeValue(d, type));
        }
        return result;
    }

    protected <T> Set<T> toValueSet(Set<byte[]> data, Class<T> type) {
        if (null == data) {
            return null;
        }
        Set<T> result = new HashSet<T>();
        for (byte[] d : data) {
            result.add(deSerializeValue(d, type));
        }
        return result;
    }

    protected String[] getCacheModelKeyArray(CacheKeyModel... cacheKeyModels) {
        if (null == cacheKeyModels) {
            return null;
        }
        String[] keys =new String[cacheKeyModels.length];
        for(int i=0; i< cacheKeyModels.length; i++) {
            keys[i] = cacheKeyModels[i].getKey();
        }
        return keys;
    }
}

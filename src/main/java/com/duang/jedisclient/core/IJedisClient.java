package com.duang.jedisclient.core;

import com.duang.jedisclient.common.CacheKeyModel;
import com.duang.jedisclient.common.KeyValueParam;
import redis.clients.jedis.JedisPubSub;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 接口
 */
public interface IJedisClient {

    <T> T get(final CacheKeyModel model, final Class<T> type);
    String type(final CacheKeyModel model);
    redis.clients.jedis.Jedis getResource();
    Set<String> keys(final String pattern);
    Boolean set(final CacheKeyModel model, final Object value);
    Boolean exists(final CacheKeyModel model);
    String rename(final CacheKeyModel oldModel, final CacheKeyModel newModel);
    <T> Set<T> zrangeByScore(final CacheKeyModel model, final Double min, final Double max, final Class<T> type);
    redis.clients.jedis.JedisPubSub subscribeThread(final JedisPubSub jedisPubSub, final String... channels);
    Boolean sismember(final CacheKeyModel model, final Object value);
    String subscribe(final JedisPubSub jedisPubSub, final String... channels);
    Double hincrByFloat(final CacheKeyModel model, final String field, final Double value);
    <T> T rpoplpush(final CacheKeyModel sourceModel, final CacheKeyModel destModel, final Class<T> type);
    <T> T srandmember(final CacheKeyModel model, final Class<T> type);
    <T> List<T> srandmember(final CacheKeyModel model, final Integer count, final Class<T> type);
    <T> Set<T> zrevrange(final CacheKeyModel model, final Long start, final Long end, final Class<T> type);
    redis.clients.jedis.JedisPubSub psubscribeThread(final JedisPubSub jedisPubSub, final String... patterns);
    String psubscribe(final JedisPubSub jedisPubSub, final String... patterns);
    Long llen(final CacheKeyModel model);
    Boolean mset(final CacheKeyModel model, final List<KeyValueParam> keysValues);
    Long decr(final CacheKeyModel model);
    Long hset(final CacheKeyModel model, final String field, final Object value);
    Long decrBy(final CacheKeyModel model, final Long longValue);
    Long incr(final CacheKeyModel model);
    <T> Map<String,T> hgetAll(final CacheKeyModel model, final Class<T> type);
    Long incrBy(final CacheKeyModel model, final Long longValue);
    Set<String> hkeys(final CacheKeyModel model);
    Long expire(final CacheKeyModel model);
    Long persist(final CacheKeyModel model);
    Long del(final CacheKeyModel model);
    <T> List<T> mget(final Class<T> type, final String... keys);
    Long ttl(final CacheKeyModel model);
    <T> T getSet(final CacheKeyModel model, final Class<T> type, final Object value);
    Long lpush(final CacheKeyModel model, final Object... values);
    Long lpush(final CacheKeyModel model, final Object value);
    Boolean hmset(final CacheKeyModel model, final Map<String, Object> values);
    Boolean hexists(final CacheKeyModel model, final String field);
    Long hlen(final CacheKeyModel model);
    String setex(final CacheKeyModel model, final Object value);
    <T> List<T> hvals(final CacheKeyModel model, final Class<T> type);
    Long hincrBy(final CacheKeyModel model, final String field, final Long value);
    <T> List<T> hmget(final CacheKeyModel model, final Class<T> type, final String... fields);
    <T> T hget(final CacheKeyModel model, final Class<T> type, final String field);
    Long hdel(final CacheKeyModel model, final String... fields);
    Integer lindex(final CacheKeyModel model, final Long index);
    Long sadd(final CacheKeyModel model, final Object... values);
    String ltrim(final CacheKeyModel model, final Integer start, final Integer end);
    Long lpop(final CacheKeyModel model);
    Long rpush(final CacheKeyModel model, final Object... value);
    String ping();
    <T> Set<T> smembers(final CacheKeyModel model, final Class<T> type);
    <T> List<T> blpop(final Integer timeout, final Class<T> type, final String... keys);
    <T> Set<T> zrange(final CacheKeyModel model, final Long start, final Long end, final Class<T> type);
    Long zrem(final CacheKeyModel model, final Object... members);
    <T> Set<T> sinter(final Class<T> type, final CacheKeyModel... cacheKeyModels);
    Double zscore(final CacheKeyModel model, final Object members);
    String flushDB();
    Long lrem(final CacheKeyModel model, final Long count, final Object value);
    Long zrank(final CacheKeyModel model, final Object member);
    Long zadd(final CacheKeyModel model, final Double score, final Object value);
    Long zadd(final CacheKeyModel model, final Map<Object, Double> scoreMembers);
    Long zrevrank(final CacheKeyModel model, final Object member);
    String flushAll();
    Long zcard(final CacheKeyModel model);
    <T> Set<T> sdiff(final Class<T> type, final CacheKeyModel... cacheKeyModels);
    String rpop(final CacheKeyModel model);
    <T> List<T> lrange(final CacheKeyModel model, final Class<T> type, final Integer start, final Integer end);
    <T> List<T> lrange(final CacheKeyModel model, final Class<T> type, final Long start, final Long end);
    <T> T spop(final CacheKeyModel model, final Class<T> type);
    <T> Set<T> sunion(final Class<T> type, final CacheKeyModel... cacheKeyModels);
    String lset(final CacheKeyModel model, final Long index, final Object value);
    <T> List<T> brpop(final Integer timeout, final Class<T> type, final String... keys);
    Long srem(final CacheKeyModel model, final String... members);
    Long scard(final CacheKeyModel model);
    Long zcount(final CacheKeyModel model, final Double min, final Double max);
    Double zincrby(final CacheKeyModel model, final Double score, final Object member);
    Long publish(final String channel, final String message);

}

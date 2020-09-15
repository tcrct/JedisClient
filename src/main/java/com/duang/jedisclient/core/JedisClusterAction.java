package com.duang.jedisclient.core;

import redis.clients.jedis.JedisCluster;

/**
 *  Redis缓存的执行方法接口
 *
 * @param <T>
 *
 * @author Laotang
 */
public interface JedisClusterAction<T> {

    T execute(JedisCluster jedisCluster) throws Exception;

}

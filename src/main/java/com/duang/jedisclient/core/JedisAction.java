package com.duang.jedisclient.core;

import redis.clients.jedis.Jedis;

/**
 *  Redis缓存的执行方法接口
 *
 * @param <T>
 *
 * @author Laotang
 */
public interface JedisAction<T> {

    T execute(Jedis jedis) throws Exception;

}

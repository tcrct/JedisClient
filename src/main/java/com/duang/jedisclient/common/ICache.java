package com.duang.jedisclient.common;

/**
 * 缓存接口
 */
public interface ICache<E> {


    E model(CacheKeyModel model);

}

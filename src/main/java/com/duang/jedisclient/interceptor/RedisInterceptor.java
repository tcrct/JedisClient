package com.duang.jedisclient.interceptor;

import com.duang.jedisclient.plugin.RedisFactory;
import redis.clients.jedis.Jedis;

public class RedisInterceptor implements Interceptor {

    @Override
    public void intercept() {
        Jedis jedis = RedisFactory.THREAD_LOCAL_JEDIS.get();
        if (jedis != null) {
            // 下一个调用链
//            inv.invoke();
            return ;
        }

        try {
            jedis = RedisFactory.getClient().getResource();
            if (null != jedis) {
                RedisFactory.THREAD_LOCAL_JEDIS.set(jedis);
            }
            // 下一个调用链
//            inv.invoke();
        }
        finally {
            RedisFactory.THREAD_LOCAL_JEDIS.remove();
            jedis.close();
        }
    }

}

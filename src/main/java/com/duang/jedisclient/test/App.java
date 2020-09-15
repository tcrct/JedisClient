package com.duang.jedisclient.test;

import cn.hutool.core.util.IdUtil;
import com.duang.jedisclient.common.CacheKeyModel;
import com.duang.jedisclient.common.KeyValueParam;
import com.duang.jedisclient.common.RedisConfig;
import com.duang.jedisclient.plugin.JedisClient;
import com.duang.jedisclient.plugin.JedisClientPlugin;
import com.duang.jedisclient.plugin.RedisFactory;
import com.duang.jedisclient.utils.RedisUtil;
import redis.clients.jedis.HostAndPort;

import java.util.*;
import java.util.List;

public class App {

    public static void main(String[] args) {

//        RedisClusterPlugin clusterPlugin = new RedisClusterPlugin();
//        try {
//            clusterPlugin.start();
//        } catch (Exception e){
//            e.printStackTrace();
//        }

        Set<HostAndPort> nodeSet = new HashSet<HostAndPort>();
        nodeSet.add(new HostAndPort("172.16.7.10", 6379));
        String appId = "a123";
        String secret= "123456";
        String password = "123456";
//        JedisClient.Builder

        RedisConfig redisConfig = new RedisConfig(appId, secret, nodeSet, password);
        JedisClientPlugin redisPlugin = new JedisClientPlugin(redisConfig);
        try {
            redisPlugin.start();
        } catch (Exception e){
            e.printStackTrace();
        }

        zadd();

//        hmset();
//        hset();
//        lpush();
//        incr();
//        incr();
//        decr();
//        incrby();
//        decrby();
//        mset();
    }

    private static void zadd() {
        RedisTestUser user = getUser();
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("zadd").build();
        Long result = RedisFactory.getClient().zadd(cacheKeyModel, 1D, user );
        if (result>0) {
            System.out.println(RedisFactory.getClient().zcard(cacheKeyModel));
            System.out.println(RedisFactory.getClient().zcount(cacheKeyModel,0D, 100D));
            Set<RedisTestUser> set = RedisFactory.getClient().zrange(cacheKeyModel, 0L, 10L, RedisTestUser.class);
            System.out.println(RedisUtil.toJsonString(set));

        }
    }

    private static void hmset() {
        String field = IdUtil.objectId();
        RedisTestUser user = getUser();
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("hmset").build();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", user);
        map.put("bbb", user);
        map.put("ccc", user);
        boolean isok = RedisFactory.getClient().hmset(cacheKeyModel, map);
        if (isok) {
            String[] strKey = {"aaa","bbb","ccc"};
            List<RedisTestUser> userList = RedisFactory.getClient().hmget(cacheKeyModel, RedisTestUser.class, strKey);
            System.out.println(RedisUtil.toJsonString(userList));

            Map<String, RedisTestUser> userMap = RedisFactory.getClient().hgetAll(cacheKeyModel, RedisTestUser.class);
            System.out.println(RedisUtil.toJsonString(userMap));
        }
    }

    private static void hset() {
        String field = IdUtil.objectId();

        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("hset").build();
//        RedisTestUser user = getUser();
//        Long count = RedisFactory.getClient().hset(cacheKeyModel, field, user);
//        System.out.println(count);
//        if (count>0){
//            RedisTestUser redisTestUser = RedisFactory.getClient().hget(cacheKeyModel, field, RedisTestUser.class);
//            System.out.println(RedisUtil.toJsonString(redisTestUser));
//        }
        String user = "{\"appId\":\"a123\",\"connectionTimeout\":2000,\"maxRedirections\":5,\"maxWaitMillis\":1000,\"nodeSet\":[{\"host\":\"172.16.7.10\",\"port\":6379}],\"password\":\"123456\",\"readTimeout\":2000,\"secret\":\"123456\"}";
        Long count = RedisFactory.getClient().hset(cacheKeyModel, field, user);
        System.out.println(count);
        if (count>0){
            String redisTestUser = RedisFactory.getClient().hget(cacheKeyModel, String.class, field);
            System.out.println(redisTestUser);
        }
    }
    private static void incrby() {
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("incr").build();
        Long count = RedisFactory.getClient().incrBy(cacheKeyModel, 10L);
        System.out.println(count);
    }
    private static void decrby() {
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("incr").build();
        Long count = RedisFactory.getClient().decrBy(cacheKeyModel, 5l);
        System.out.println(count);
    }
    private static void incr() {
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("incr").build();
        Long count = RedisFactory.getClient().incr(cacheKeyModel);
        System.out.println(count);
    }
    private static void decr() {
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("incr").build();
        Long count = RedisFactory.getClient().decr(cacheKeyModel);
        System.out.println(count);
    }

    private static void lpush() {
        RedisTestUser user = getUser();
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("lpush").build();
        Long count = RedisFactory.getClient().lpush(cacheKeyModel, user);
        System.out.println(count);
    }

    private static void mset() {
        // for 100次
        // fast ：60.85 KB
        // fst :3.12 KB
        // jdk : 5.53 KB

        List<KeyValueParam>keyValueParamList = new ArrayList<KeyValueParam>();
        List<RedisTestUser> userList = new ArrayList<RedisTestUser>();
//        for (int i=0; i<1; i++) {
            RedisTestUser user = getUser();
            userList.add(user);
//        }
//        map.put("123", RedisUtil.toJsonString(userList));
        keyValueParamList.add(new KeyValueParam("123", user));
        keyValueParamList.add(new KeyValueParam("456", user));
        CacheKeyModel cacheKeyModel = new CacheKeyModel.Builder(TestCacheKeyEnum.USER_ID).customKey("mset").build();
        Boolean isOk = RedisFactory.getClient().mset(cacheKeyModel, keyValueParamList);
        if (isOk) {
            List<RedisTestUser> redisUser = RedisFactory.getClient().mget(RedisTestUser.class, cacheKeyModel.getKey()+":123",cacheKeyModel.getKey()+":456");
            System.out.println("########: " + RedisUtil.toJsonString(redisUser));
        }
    }

    private static RedisTestUser getUser() {
        String id = IdUtil.objectId();
        RedisTestUser user = new RedisTestUser();
        user.setAddress("中国广东珠海");
        user.setBother(new Date());
        user.setEmail("tcrct@qq.com");
        user.setId(id);
        user.setName("laotang");
        user.setRemake("Redis命令十分丰富，如果您有兴趣的话也可以查看我们的网站结构图,它以节点图的形式展示了所有redis命令。");
        return user;
    }

}

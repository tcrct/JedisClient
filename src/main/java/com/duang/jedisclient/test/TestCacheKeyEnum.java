package com.duang.jedisclient.test;


import com.duang.jedisclient.common.ICacheKeyEnums;

public enum TestCacheKeyEnum implements ICacheKeyEnums {

    USER_ID("zat:mpay:userid:", ICacheKeyEnums.DEFAULT_TTL, "用户ID"),
    ORDER_MAPPING("order:mapping", ICacheKeyEnums.DEFAULT_TTL, "用户ID");


    private String keyPrefix;
    private int  ttl;
    private String keyDesc;
    private TestCacheKeyEnum(String keyPrefix, int ttl, String keyDesc) {
        this.keyPrefix = keyPrefix;
        this.ttl = ttl;
        this.keyDesc = keyDesc;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public int getKeyTTL() {
        return ttl;
    }

    public String getKeyDesc() {
        return keyDesc;
    }
}

package com.duang.jedisclient.common;

/**
 * 缓存key模型
 * 用来确定每一个缓存的key或key前缀，过期时间等
 *
 * @author Laotang
 * @since 1.0
 */
public class CacheKeyModel {

    /**
     * 自定义前缀
     */
    private String keyPrefix;
    /**
     * 自定义关键字，作区分
     */
    private String customKey;
    /**
     * 过期时间
     */
    private Integer ttl;
    /**
     * 关键字说明
     */
    private String keyDesc;
    /**
     * 缓存关键字枚举对象
     */
    private ICacheKeyEnums keyEnums;

    public static class Builder {

        private String customKey;
        private String keyPrefix;
        private int ttl;
        private String keyDesc;
        private ICacheKeyEnums keyEnums;

        public Builder() { }

        /**
         * 枚举对象设置相关值
         * @param enums
         */
        public Builder(ICacheKeyEnums enums) {
            this.keyEnums = enums;
            this.keyPrefix = enums.getKeyPrefix();
            this.ttl = enums.getKeyTTL();
            this.keyDesc = enums.getKeyDesc();
        }

        /**
         * 自定义前缀
         * @param keyPrefix key关键字的前缀
         * @return
         */
        public Builder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }

        /**
         * 自定义的key值，一般用于区分，例如ID值
         * @param customKey 自定义的key值
         * @return
         */
        public Builder customKey(Object customKey) {
            this.customKey = String.valueOf(customKey);
            return this;
        }

        public CacheKeyModel build() {
            return new CacheKeyModel(this);
        }
    }


    private CacheKeyModel(Builder builder) {
        keyPrefix = builder.keyPrefix;
        customKey = builder.customKey;
        ttl = builder.ttl;
        keyDesc = builder.keyDesc;
        keyEnums = builder.keyEnums;
    }

    /**
     * 将key前缀值与自定义的key值结合，组成最终key值
     * @return
     */
    public String getKey() {
        if (null != keyEnums) {
            if ((keyPrefix.endsWith(":") || keyPrefix.endsWith(".")) && null != customKey) {
                return keyPrefix + customKey;
            } else {
                return null != customKey ? keyPrefix + ":" + customKey : keyPrefix;
            }
        } else {
            return customKey;
        }
    }

    /**
     * 缓存过期时间
     * @return
     */
    public Integer getKeyTTL() {
        if(ttl <= 0 ) {
            ttl = ICacheKeyEnums.NEVER_TTL;
        }
        return ttl;
    }

    /**
     * 缓存key说明
     * @return
     */
    public String getKeyDesc() {
        return keyDesc;
    }

}

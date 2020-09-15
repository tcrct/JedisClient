package com.duang.jedisclient.common;

/**
 * key - value 键值对象
 * @param <T> value泛型对象
 *
 * @author Laotang
 * @since 1.0
 * @date 2020-09-08
 */
public class KeyValueParam<T> implements java.io.Serializable {

    private String key;
    private T value;

    public KeyValueParam() {
    }

    public KeyValueParam(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

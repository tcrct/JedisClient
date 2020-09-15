package com.duang.jedisclient.test;

import java.util.Date;

public class RedisTestUser implements java.io.Serializable {
    private String id;
    private String name;
    private String address;
    private String email;
    private Date bother;
    private String remake;

    public RedisTestUser() {
    }

    public RedisTestUser(String id, String name, String address, String email, Date bother, String remake) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.bother = bother;
        this.remake = remake;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBother() {
        return bother;
    }

    public void setBother(Date bother) {
        this.bother = bother;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }
}

package com.e.maidregistrationrcm.VOs;

public class VisitorVO {
    private Integer id;
    private String maidid;
    private String temperature;
    private String o2level;
    private String name;
    private String address;
    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaidid() {
        return maidid;
    }

    public void setMaidid(String maidid) {
        this.maidid = maidid;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getO2level() {
        return o2level;
    }

    public void setO2level(String o2level) {
        this.o2level = o2level;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    private Integer token;
}

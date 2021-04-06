package com.e.maidregistrationrcm.VOs;

public class EmployerVO {
    private Integer id;
    private Integer maidid;
    private String name;
    private String mobile;
    private String address;
    private String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaidid() {
        return maidid;
    }

    public void setMaidid(Integer maidid) {
        this.maidid = maidid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString(){
        return  name;
    }
}

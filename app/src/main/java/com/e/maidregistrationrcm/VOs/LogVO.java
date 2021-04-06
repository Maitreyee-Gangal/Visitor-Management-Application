package com.e.maidregistrationrcm.VOs;

import java.util.Date;

public class LogVO {
    private Integer maidId;
    private String name;
    private Date inTime;

    public Integer getMaidId() {
        return maidId;
    }

    public void setMaidId(Integer maidId) {
        this.maidId = maidId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }
}

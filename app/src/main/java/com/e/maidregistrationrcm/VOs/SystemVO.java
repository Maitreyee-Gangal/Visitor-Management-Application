package com.e.maidregistrationrcm.VOs;

import java.util.HashMap;

public class SystemVO {
    private HashMap cupLog;
    private int RAM;
    private int Storage;
    private String UPTime;
    private String StartTime;

    public HashMap getCupLog() {
        return cupLog;
    }

    public void setCupLog(HashMap cupLog) {
        this.cupLog = cupLog;
    }

    public int getRAM() {
        return RAM;
    }

    public void setRAM(int RAM) {
        this.RAM = RAM;
    }

    public int getStorage() {
        return Storage;
    }

    public void setStorage(int storage) {
        Storage = storage;
    }

    public String getUPTime() {
        return UPTime;
    }

    public void setUPTime(String UPTime) {
        this.UPTime = UPTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

}

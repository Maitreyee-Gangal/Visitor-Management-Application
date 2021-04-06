package com.e.maidregistrationrcm.VOs;

import java.util.ArrayList;

public class MaidVO {
    private Integer MaidId;
    private Integer token;
    private String name;
    private String mobile;
    private String address;
    private String adharNo;
    private String imageRef;
    private String Ocupation;
    private Integer access = 1;

    public String getOcupation() {
        return Ocupation;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }

    public void setOcupation(String ocupation) {
        Ocupation = ocupation;
    }

    //read only
    private String inTime;

    public int getInBL() {
        return InBL;
    }

    public void setInBL(int inBL) {
        InBL = inBL;
    }

    public String getBanfrom() {
        return Banfrom;
    }

    public void setBanfrom(String banfrom) {
        Banfrom = banfrom;
    }

    public String getBanTo() {
        return BanTo;
    }

    public void setBanTo(String banTo) {
        BanTo = banTo;
    }

    private String outTime;
    private int InBL;
    private String Banfrom;
    private String BanTo;


    private ArrayList<EmployerVO> employers;
    private byte[] bytes;

    public byte[] getImage() {
        return bytes;
    }

    public void setImage(byte[] bytes) {
        this.bytes = bytes;
    }

    public ArrayList<EmployerVO> getEmployers() {
        return employers;
    }

    public void setEmployers(ArrayList<EmployerVO> employerVOS) {
        this.employers = employerVOS;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public Integer getMaidId() {
        return MaidId;
    }

    public void setMaidId(Integer MaidId) {
        this.MaidId = MaidId;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
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

    public String getAdharNo() {
        return adharNo;
    }

    public void setAdharNo(String adharNo) {
        this.adharNo = adharNo;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    public String toString(){
        return token+" "+name;
    }
}

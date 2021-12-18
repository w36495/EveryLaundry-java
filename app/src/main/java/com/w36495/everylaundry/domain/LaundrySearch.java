package com.w36495.everylaundry.domain;

public class LaundrySearch {

    private String userID;
    private int laundryKey;
    private String laundryNM;
    private String laundryAddress;
    private String laundryTel;
    private String laundryRegistDate;

    public LaundrySearch(String userID, int laundryKey, String laundryNM, String laundryAddress, String laundryTel, String laundryRegistDate) {
        this.userID = userID;
        this.laundryKey = laundryKey;
        this.laundryNM = laundryNM;
        this.laundryAddress = laundryAddress;
        this.laundryTel = laundryTel;
        this.laundryRegistDate = laundryRegistDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getLaundryKey() {
        return laundryKey;
    }

    public void setLaundryKey(int laundryKey) {
        this.laundryKey = laundryKey;
    }

    public String getLaundryNM() {
        return laundryNM;
    }

    public void setLaundryNM(String laundryNM) {
        this.laundryNM = laundryNM;
    }

    public String getLaundryAddress() {
        return laundryAddress;
    }

    public void setLaundryAddress(String laundryAddress) {
        this.laundryAddress = laundryAddress;
    }

    public String getLaundryTel() {
        return laundryTel;
    }

    public void setLaundryTel(String laundryTel) {
        this.laundryTel = laundryTel;
    }

    public String getLaundryRegistDate() {
        return laundryRegistDate;
    }

    public void setLaundryRegistDate(String laundryRegistDate) {
        this.laundryRegistDate = laundryRegistDate;
    }

    @Override
    public String toString() {
        return "LaundrySearch{" +
                "userID='" + userID + '\'' +
                ", laundryKey=" + laundryKey +
                ", laundryNM='" + laundryNM + '\'' +
                ", laundryAddress='" + laundryAddress + '\'' +
                ", laundryTel='" + laundryTel + '\'' +
                ", laundryRegistDate='" + laundryRegistDate + '\'' +
                '}';
    }
}

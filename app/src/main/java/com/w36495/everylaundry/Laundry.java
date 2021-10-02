package com.w36495.everylaundry;

/**
 * Laundry Data Class
 */
public class Laundry {

    private int laundryKey;
    private String laundryName;
    private String laundryTel;
    private String laundryAddress;
    private String laundryZipCode;
    private Double laundryCoordsX;
    private Double laundryCoordsY;

    public Laundry(int laundryKey, String laundryName, String laundryTel, String laundryAddress, String laundryZipCode, Double laundryCoordsX, Double laundryCoordsY) {
        this.laundryKey = laundryKey;
        this.laundryName = laundryName;
        this.laundryTel = laundryTel;
        this.laundryAddress = laundryAddress;
        this.laundryZipCode = laundryZipCode;
        this.laundryCoordsX = laundryCoordsX;
        this.laundryCoordsY = laundryCoordsY;
    }

    public int getLaundryKey() {
        return laundryKey;
    }

    public void setLaundryKey(int laundryKey) {
        this.laundryKey = laundryKey;
    }

    public String getLaundryName() {
        return laundryName;
    }

    public void setLaundryName(String laundryName) {
        this.laundryName = laundryName;
    }

    public String getLaundryTel() {
        return laundryTel;
    }

    public void setLaundryTel(String laundryTel) {
        this.laundryTel = laundryTel;
    }

    public String getLaundryAddress() {
        return laundryAddress;
    }

    public void setLaundryAddress(String laundryAddress) {
        this.laundryAddress = laundryAddress;
    }

    public String getLaundryZipCode() {
        return laundryZipCode;
    }

    public void setLaundryZipCode(String laundryZipCode) {
        this.laundryZipCode = laundryZipCode;
    }

    public Double getLaundryCoordsX() {
        return laundryCoordsX;
    }

    public void setLaundryCoordsX(Double laundryCoordsX) {
        this.laundryCoordsX = laundryCoordsX;
    }

    public Double getLaundryCoordsY() {
        return laundryCoordsY;
    }

    public void setLaundryCoordsY(Double laundryCoordsY) {
        this.laundryCoordsY = laundryCoordsY;
    }

    @Override
    public String toString() {
        return "Laundry{" +
                "laundryKey=" + laundryKey +
                ", laundryName='" + laundryName + '\'' +
                ", laundryTel='" + laundryTel + '\'' +
                ", laundryAddress='" + laundryAddress + '\'' +
                ", laundryZipCode='" + laundryZipCode + '\'' +
                ", laundryCoordsX=" + laundryCoordsX +
                ", laundryCoordsY=" + laundryCoordsY +
                '}';
    }
}

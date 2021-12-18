package com.w36495.everylaundry.domain;


import com.google.gson.annotations.SerializedName;

/**
 * Laundry Data Class
 */
public class Laundry {

    @SerializedName("LAUNDRY_KEY")
    private int laundryKey;

    @SerializedName("LAUNDRY_NM")
    private String laundryName;

    @SerializedName("LAUNDRY_TEL")
    private String laundryTel;

    @SerializedName("LAUNDRY_ADDR")
    private String laundryAddress;

    @SerializedName("LAUNDRY_ZIP_CODE")
    private String laundryZipCode;

    @SerializedName("LAUNDRY_TYPE")
    private String laundryType;

    @SerializedName("COORDS_X")
    private Double laundryCoordsX;

    @SerializedName("COORDS_Y")
    private Double laundryCoordsY;

    public Laundry(int laundryKey, String laundryName, String laundryTel, String laundryAddress, String laundryZipCode, String laundryType, Double laundryCoordsX, Double laundryCoordsY) {
        this.laundryKey = laundryKey;
        this.laundryName = laundryName;
        this.laundryTel = laundryTel;
        this.laundryAddress = laundryAddress;
        this.laundryZipCode = laundryZipCode;
        this.laundryType = laundryType;
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

    public String getLaundryType() {
        return laundryType;
    }

    public void setLaundryType(String laundryType) {
        this.laundryType = laundryType;
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
                ", laundryType='" + laundryType + '\'' +
                ", laundryCoordsX=" + laundryCoordsX +
                ", laundryCoordsY=" + laundryCoordsY +
                '}';
    }
}

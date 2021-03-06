package com.w36495.everylaundry.data.domain;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("RV_KEY")
    private int reviewKey;

    @SerializedName("USER_ID")
    private String reviewUserID;

    @SerializedName("LAUNDRY_KEY")
    private int reviewLaundryKey;

    @SerializedName("RV_CONTENTS")
    private String reviewContents;

    @SerializedName("RED_DT")
    private String reviewRegistDate;

    public Review(int reviewKey, String reviewUserID, int reviewLaundryKey, String reviewContents, String reviewRegistDate) {
        this.reviewKey = reviewKey;
        this.reviewUserID = reviewUserID;
        this.reviewLaundryKey = reviewLaundryKey;
        this.reviewContents = reviewContents;
        this.reviewRegistDate = reviewRegistDate;
    }

    public int getReviewKey() {
        return reviewKey;
    }

    public void setReviewKey(int reviewKey) {
        this.reviewKey = reviewKey;
    }

    public String getReviewUserID() {
        return reviewUserID;
    }

    public void setReviewUserID(String reviewUserID) {
        this.reviewUserID = reviewUserID;
    }

    public int getReviewLaundryKey() {
        return reviewLaundryKey;
    }

    public void setReviewLaundryKey(int reviewLaundryKey) {
        this.reviewLaundryKey = reviewLaundryKey;
    }

    public String getReviewContents() {
        return reviewContents;
    }

    public void setReviewContents(String reviewContents) {
        this.reviewContents = reviewContents;
    }

    public String getReviewRegistDate() {
        return reviewRegistDate;
    }

    public void setReviewRegistDate(String reviewRegistDate) {
        this.reviewRegistDate = reviewRegistDate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewKey=" + reviewKey +
                ", reviewUserID='" + reviewUserID + '\'' +
                ", reviewLaundryKey=" + reviewLaundryKey +
                ", reviewContents='" + reviewContents + '\'' +
                ", reviewRegistDate='" + reviewRegistDate + '\'' +
                '}';
    }
}

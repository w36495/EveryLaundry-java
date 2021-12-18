package com.w36495.everylaundry.domain;

import com.google.gson.annotations.SerializedName;

/**
 * User Data Class
 */
public class User {

    @SerializedName("USER_ID")
    private String userId;

    @SerializedName("USER_PASSWD")
    private String userPassword;

    @SerializedName("USER_MOBILE")
    private String userMobile;

    @SerializedName("USER_EMAIL")
    private String userEmail;

    @SerializedName("USER_NICKNM")
    private String userNickname;

    @SerializedName("USER_IMG")
    private int userImg;

    public User(String userId, String userPassword, String userMobile, String userEmail, String userNickname, int userImg) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userImg = userImg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userImg) {
        this.userImg = userImg;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userNickname='" + userNickname + '\'' +
                ", userImg=" + userImg +
                '}';
    }
}

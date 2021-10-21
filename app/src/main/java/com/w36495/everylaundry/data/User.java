package com.w36495.everylaundry.data;

/**
 * User Data Class
 */
public class User {

    private String userId;
    private String userPassword;
    private String userMobile;
    private String userEmail;
    private String userNickname;
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

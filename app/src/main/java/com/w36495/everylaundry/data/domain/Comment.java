package com.w36495.everylaundry.data.domain;

import com.google.gson.annotations.SerializedName;

/**
 * 댓글 데이터 클래스
 */
public class Comment {

    @SerializedName("CM_KEY")
    private int commentKey;

    @SerializedName("USER_ID")
    private String userID;

    @SerializedName("POST_KEY")
    private int postKey;

    @SerializedName("CM_CONTENTS")
    private String commentContents;

    @SerializedName("REG_DT")
    private String commentRegistDate;

    @SerializedName("UPD_DT")
    private String commentUpdateDate;

    public Comment(int commentKey, String userID, int postKey, String commentContents, String commentRegistDate, String commentUpdateDate) {
        this.commentKey = commentKey;
        this.userID = userID;
        this.postKey = postKey;
        this.commentContents = commentContents;
        this.commentRegistDate = commentRegistDate;
        this.commentUpdateDate = commentUpdateDate;
    }

    public int getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(int commentKey) {
        this.commentKey = commentKey;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getPostKey() {
        return postKey;
    }

    public void setPostKey(int postKey) {
        this.postKey = postKey;
    }

    public String getCommentContents() {
        return commentContents;
    }

    public void setCommentContents(String commentContents) {
        this.commentContents = commentContents;
    }

    public String getCommentRegistDate() {
        return commentRegistDate;
    }

    public void setCommentRegistDate(String commentRegistDate) {
        this.commentRegistDate = commentRegistDate;
    }

    public String getCommentUpdateDate() {
        return commentUpdateDate;
    }

    public void setCommentUpdateDate(String commentUpdateDate) {
        this.commentUpdateDate = commentUpdateDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentKey=" + commentKey +
                ", userID='" + userID + '\'' +
                ", postKey=" + postKey +
                ", commentContents='" + commentContents + '\'' +
                ", commentRegistDate='" + commentRegistDate + '\'' +
                ", commentUpdateDate='" + commentUpdateDate + '\'' +
                '}';
    }
}

package com.w36495.everylaundry.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Post Data Class
 */
public class Post {

    @SerializedName("POST_KEY")
    private int postKey;

    @SerializedName("USER_ID")
    private String postWriter;

    @SerializedName("CATEGORY_KEY")
    private int postCategory;

    @SerializedName("POST_TITLE")
    private String postTitle;

    @SerializedName("POST_CONTENTS")
    private String postContents;

    @SerializedName("VIEW_CNT")
    private int postViewCnt;

    @SerializedName("RECOMMENT_CNT")
    private int postRecommentCnt;

    @SerializedName("POST_NOTICE")
    private char postNotice;

    @SerializedName("POST_CM_FLAG")
    private char postCommentFlag;

    @SerializedName("REG_DT")
    private String postRegistDate;

    @SerializedName("UPD_DT")
    private String postUpdateDate;

    public Post(int postKey, String postWriter, int postCategory, String postTitle, String postContents, int postViewCnt, int postRecommentCnt, char postNotice, char postCommentFlag, String postRegistDate, String postUpdateDate) {
        this.postKey = postKey;
        this.postWriter = postWriter;
        this.postCategory = postCategory;
        this.postTitle = postTitle;
        this.postContents = postContents;
        this.postViewCnt = postViewCnt;
        this.postRecommentCnt = postRecommentCnt;
        this.postNotice = postNotice;
        this.postCommentFlag = postCommentFlag;
        this.postRegistDate = postRegistDate;
        this.postUpdateDate = postUpdateDate;
    }

    public int getPostKey() {
        return postKey;
    }

    public void setPostKey(int postKey) {
        this.postKey = postKey;
    }

    public String getPostWriter() {
        return postWriter;
    }

    public void setPostWriter(String postWriter) {
        this.postWriter = postWriter;
    }

    public int getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(int postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContents() {
        return postContents;
    }

    public void setPostContents(String postContents) {
        this.postContents = postContents;
    }

    public int getPostViewCnt() {
        return postViewCnt;
    }

    public void setPostViewCnt(int postViewCnt) {
        this.postViewCnt = postViewCnt;
    }

    public int getPostRecommentCnt() {
        return postRecommentCnt;
    }

    public void setPostRecommentCnt(char postRecommentCnt) {
        this.postRecommentCnt = postRecommentCnt;
    }

    public char getPostNotice() {
        return postNotice;
    }

    public void setPostNotice(char postNotice) {
        this.postNotice = postNotice;
    }

    public char getPostCommentFlag() {
        return postCommentFlag;
    }

    public void setPostCommentFlag(char postCommentFlag) {
        this.postCommentFlag = postCommentFlag;
    }

    public String getPostRegistDate() {
        return postRegistDate;
    }

    public void setPostRegistDate(String postRegistDate) {
        this.postRegistDate = postRegistDate;
    }

    public String getPostUpdateDate() {
        return postUpdateDate;
    }

    public void setPostUpdateDate(String postUpdateDate) {
        this.postUpdateDate = postUpdateDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postKey=" + postKey +
                ", postWriter='" + postWriter + '\'' +
                ", postCategory=" + postCategory +
                ", postTitle='" + postTitle + '\'' +
                ", postContents='" + postContents + '\'' +
                ", postViewCnt=" + postViewCnt +
                ", postRecommentCnt=" + postRecommentCnt +
                ", postNotice=" + postNotice +
                ", postCommentFlag=" + postCommentFlag +
                ", postRegistDate=" + postRegistDate +
                ", postUpdateDate=" + postUpdateDate +
                '}';
    }
}

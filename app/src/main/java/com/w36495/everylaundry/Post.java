package com.w36495.everylaundry;

public class Post {

    private int postKey;
    private String postCategory;
    private String postTitle;
    private String postContents;
    private String postWriter;
    private String postDate;
    private String postViewCnt;
    private String postRecommentCnt;

    public Post(int postKey, String postCategory, String postTitle, String postContents, String postWriter, String postDate, String postViewCnt, String postRecommentCnt) {
        this.postKey = postKey;
        this.postCategory = postCategory;
        this.postTitle = postTitle;
        this.postContents = postContents;
        this.postWriter = postWriter;
        this.postDate = postDate;
        this.postViewCnt = postViewCnt;
        this.postRecommentCnt = postRecommentCnt;
    }

    public int getPostKey() {
        return postKey;
    }

    public void setPostKey(int postKey) {
        this.postKey = postKey;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
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

    public String getPostWriter() {
        return postWriter;
    }

    public void setPostWriter(String postWriter) {
        this.postWriter = postWriter;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostViewCnt() {
        return postViewCnt;
    }

    public void setPostViewCnt(String postViewCnt) {
        this.postViewCnt = postViewCnt;
    }

    public String getPostRecommentCnt() {
        return postRecommentCnt;
    }

    public void setPostRecommentCnt(String postRecommentCnt) {
        this.postRecommentCnt = postRecommentCnt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postKey=" + postKey +
                ", postCategory='" + postCategory + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", postContents='" + postContents + '\'' +
                ", postWriter='" + postWriter + '\'' +
                ", postDate='" + postDate + '\'' +
                ", postViewCnt='" + postViewCnt + '\'' +
                ", postRecommentCnt='" + postRecommentCnt + '\'' +
                '}';
    }
}

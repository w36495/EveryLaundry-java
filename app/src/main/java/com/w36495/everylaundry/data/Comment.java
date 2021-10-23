package com.w36495.everylaundry.data;

/**
 * 댓글 데이터 클래스
 */
public class Comment {

    private int commentKey;
    private String userID;
    private int postKey;
    private String commentContents;
    private String CommentRegistDate;

    public Comment(int commentKey, String userID, int postKey, String commentContents, String commentRegistDate) {
        this.commentKey = commentKey;
        this.userID = userID;
        this.postKey = postKey;
        this.commentContents = commentContents;
        this.CommentRegistDate = commentRegistDate;
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
        return CommentRegistDate;
    }

    public void setCommentRegistDate(String commentRegistDate) {
        this.CommentRegistDate = commentRegistDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentKey=" + commentKey +
                ", userID='" + userID + '\'' +
                ", postKey=" + postKey +
                ", commentContents='" + commentContents + '\'' +
                ", CommentRegistDate='" + CommentRegistDate + '\'' +
                '}';
    }
}

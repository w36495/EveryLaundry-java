package com.w36495.everylaundry.api;

import com.google.gson.JsonArray;
import com.w36495.everylaundry.domain.Comment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 댓글 관련 API
 */
public interface CommentAPI {

    @FormUrlEncoded
    @POST("selectComment.php")
    Call<List<Comment>> getCommentList(@Field("postKey") int postKey);

    @FormUrlEncoded
    @POST("selectCommentCount.php")
    Call<String> getCommentCount(@Field("postKey") int postKey);

    @FormUrlEncoded
    @POST("insertComment.php")
    Call<String> insertComment(
            @Field("commentKey") int commentKey
            , @Field("userID") String userID
            , @Field("postKey") int postKey
            , @Field("contents") String contents);

}

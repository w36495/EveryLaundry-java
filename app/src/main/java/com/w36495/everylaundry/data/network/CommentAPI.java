package com.w36495.everylaundry.data.network;

import com.w36495.everylaundry.data.domain.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 댓글 관련 API
 */
public interface CommentAPI {

    @GET("selectComment.php")
    Call<List<Comment>> getCommentList(@Query("postKey") int postKey);

    @GET("selectCommentCount.php")
    Call<String> getCommentCount(@Query("postKey") int postKey);

    @FormUrlEncoded
    @POST("insertComment.php")
    Call<String> insertComment(
            @Field("commentKey") int commentKey
            , @Field("userID") String userID
            , @Field("postKey") int postKey
            , @Field("contents") String contents);

}

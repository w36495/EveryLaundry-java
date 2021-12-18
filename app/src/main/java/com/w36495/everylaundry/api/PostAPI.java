package com.w36495.everylaundry.api;

import com.w36495.everylaundry.domain.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 게시물 관련 API
 */
public interface PostAPI {

    @GET("selectPostList.php")
    Call<List<Post>> getPostList();

    @FormUrlEncoded
    @POST("selectPost.php")
    Call<Post> getPost(@Field("postKey") int postKey);

    @FormUrlEncoded
    @POST("selectPostByUserId.php")
    Call<List<Post>> getPostByUserId(@Field("userID") String userID);

    @FormUrlEncoded
    @POST("insertPost.php")
    Call<String> insertPost(
            @Field("postKey") String postKey
            , @Field("userID") String userID
            , @Field("categoryKey") String categoryKey
            , @Field("postTitle") String postTitle
            , @Field("postContents") String postContents);

    @FormUrlEncoded
    @POST("updatePost.php")
    Call<String> updatePost(
            @Field("postKey") String postKey
            , @Field("categoryKey") String categoryKey
            , @Field("postTitle") String postTitle
            , @Field("postContents") String postContents);

    @FormUrlEncoded
    @POST("deletePost.php")
    Call<String> deletePost(@Field("postKey") int postKey, @Field("userID") String userID);

    @FormUrlEncoded
    @POST("selectPostRecommend.php")
    Call<String> getPostRecommend(@Field("userID") String userID, @Field("postKey") int postKey);

    @FormUrlEncoded
    @POST("updatePostRecommend.php")
    Call<String> updatePostRecommend(@Field("userID") String userID, @Field("postKey") int postKey);

    @FormUrlEncoded
    @POST("insertPostRecommend.php")
    Call<String> insertPostRecommend(@Field("userID") String userID, @Field("postKey") int postKey);

    @FormUrlEncoded
    @POST("updatePostViewCount.php")
    Call<String> updatePostViewCount(@Field("postKey") int postKey);



}

package com.w36495.everylaundry.data.network;

import com.w36495.everylaundry.data.domain.Post;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 게시물 관련 API
 */
public interface PostAPI {

    @GET("selectPostList.php")
    Call<List<Post>> getPostList();

    @GET("selectPost.php")
    Call<Post> getPost(@Query("postKey") int postKey);

    @GET("selectPostByUserId.php")
    Call<List<Post>> getPostByUserId(@Query("userID") String userID);

    @FormUrlEncoded
    @POST("insertPost.php")
    Call<String> insertPost(
            @Field("postKey") String postKey
            , @Field("userID") String userID
            , @Field("categoryKey") String categoryKey
            , @Field("postTitle") String postTitle
            , @Field("postContents") String postContents);

    @FormUrlEncoded
    @PUT("updatePost.php")
    Call<String> updatePost(
            @Field("postKey") String postKey
            , @Field("categoryKey") String categoryKey
            , @Field("postTitle") String postTitle
            , @Field("postContents") String postContents);

    @FormUrlEncoded
    @POST("deletePost.php")
    Call<String> deletePost(@Field("postKey") int postKey, @Field("userID") String userID);

    @GET("selectPostRecommend.php")
    Call<String> getPostRecommend(@Query("userID") String userID, @Query("postKey") int postKey);

    @FormUrlEncoded
    @POST("updatePostRecommend.php")
    Call<String> updatePostRecommend(@Field("userID") String userID, @Field("postKey") int postKey);

    @FormUrlEncoded
    @POST("insertPostRecommend.php")
    Call<String> insertPostRecommend(@Field("userID") String userID, @Field("postKey") int postKey);

    @FormUrlEncoded
    @PUT("updatePostViewCount.php")
    Call<String> updatePostViewCount(@Query("postKey") int postKey);


}

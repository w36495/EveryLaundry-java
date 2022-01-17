package com.w36495.everylaundry.data.network;

import com.w36495.everylaundry.data.domain.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 유저 관련 API
 */
public interface UserAPI {

    @GET("selectUser.php")
    Call<User> selectUser(@Query("userID") String userID);

    @FormUrlEncoded
    @POST("updateUser.php")
    Call<String> updateUser(
            @Field("userID") String userID
            , @Field("userPW") String userPW
            , @Field("userMobile") String userMobile
            , @Field("userNickNM") String userNickNm
            , @Field("userEmail") String userEmail);

    @FormUrlEncoded
    @POST("insertUser.php")
    Call<String> signup(
            @Field("userID") String userID
            , @Field("userPW") String userPW
            , @Field("userMobile") String userMobile
            , @Field("userNickNM") String userNickNm
            , @Field("userEmail") String userEmail);

}

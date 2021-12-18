package com.w36495.everylaundry.api;

import com.google.gson.JsonArray;
import com.w36495.everylaundry.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 유저 관련 API
 */
public interface UserAPI {

    @FormUrlEncoded
    @POST("selectUser.php")
    Call<User> selectUser(@Field("userID") String userID);

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

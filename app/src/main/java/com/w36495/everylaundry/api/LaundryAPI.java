package com.w36495.everylaundry.api;

import com.google.gson.JsonArray;
import com.w36495.everylaundry.domain.Laundry;
import com.w36495.everylaundry.domain.Review;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 세탁소 관련 API
 */
public interface LaundryAPI {

    @GET("selectLaundry.php")
    Call<List<Laundry>> getLaundryInfo();

    @FormUrlEncoded
    @POST("selectReview.php")
    Call<List<Review>> getLaundryReview(@Field("laundryKey") int laundryKey);

    @FormUrlEncoded
    @POST("insertLaundryLike.php")
    Call<String> insertLaundryLike(@Field("userID") String userID, @Field("laundryKey") int laundryKey);

    @FormUrlEncoded
    @POST("selectLaundryLike.php")
    Call<String> getLaundryLike(@Field("userID") String userID, @Field("laundryKey") int laundryKey);

    @FormUrlEncoded
    @POST("selectLaundryLikeList.php")
    Call<JsonArray> getLaundryLikeList(@Field("userID") String userID);

    @FormUrlEncoded
    @POST("selectLaundrySearch.php")
    Call<List<Laundry>> getLaundrySearch(@Field("userID") String userID);

    @FormUrlEncoded
    @POST("updateLaundryLike.php")
    Call<String> updateLaundryLike(@Field("userID") String userID, @Field("laundryKey") int laundryKey);



}

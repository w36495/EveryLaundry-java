package com.w36495.everylaundry.data.network;

import com.google.gson.JsonArray;
import com.w36495.everylaundry.data.domain.Laundry;
import com.w36495.everylaundry.data.domain.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 세탁소 관련 API
 */
public interface LaundryAPI {

    @GET("selectLaundry.php")
    Call<List<Laundry>> getLaundryInfo();

    @GET("selectReview.php")
    Call<List<Review>> getLaundryReview(@Query("laundryKey") int laundryKey);

    @FormUrlEncoded
    @POST("insertLaundryLike.php")
    Call<String> insertLaundryLike(@Field("userID") String userID, @Field("laundryKey") int laundryKey);

    @GET("selectLaundryLike.php")
    Call<String> getLaundryLike(@Query("userID") String userID, @Query("laundryKey") int laundryKey);

    @GET("selectLaundryLikeList.php")
    Call<List<Laundry>> getLaundryLikeList(@Query("userID") String userID);

    @GET("selectLaundrySearch.php")
    Call<List<Laundry>> getLaundrySearch(@Query("userID") String userID);

    @FormUrlEncoded
    @POST("updateLaundryLike.php")
    Call<String> updateLaundryLike(@Field("userID") String userID, @Field("laundryKey") int laundryKey);



}

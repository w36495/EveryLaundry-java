package com.w36495.everylaundry.api;

import com.w36495.everylaundry.domain.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;

/**
 * 카테고리 관련 API
 */
public interface CategoryAPI {

    @GET("selectCategory.php")
    Call<List<Category>> getCategoryAll();

}

package com.w36495.everylaundry.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.BuildConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.w36495.everylaundry.MainActivity;
import com.w36495.everylaundry.RetrofitBuilder;
import com.w36495.everylaundry.api.LaundryAPI;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.LaundryLikeAdapter;
import com.w36495.everylaundry.domain.LaundryLike;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * 즐겨찾는 세탁소 목록
 */
public class LikeFragment extends Fragment {

    private RecyclerView like_recyclerView;
    private LaundryLikeAdapter laundryLikeAdapter;

    private ArrayList<LaundryLike> laundryList;

    private String loginID = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_list, container, false);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit(view);

        return view;
    }

    private void setInit(View view) {
        loginID = MainActivity.getLoginUserID();
        like_recyclerView = view.findViewById(R.id.like_recyclerView);

        // 즐겨찾는 세탁소 목록
        showLaundryLikeList(view);
    }

    /**
     * 즐겨찾는 세탁소 목록 보여주기
     */
    private void showLaundryLikeList(View view) {

        Retrofit retrofit = RetrofitBuilder.getClient();
        LaundryAPI laundryAPI = retrofit.create(LaundryAPI.class);

        laundryAPI.getLaundryLikeList(loginID).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, retrofit2.Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    laundryList = new ArrayList<>();
                    
                    for (int index = 0; index < response.body().size(); index++) {
                        JsonObject like = (JsonObject) response.body().get(index);

                        LaundryLike laundryLike = new LaundryLike(
                                like.get("USER_ID").getAsString(),
                                like.get("LAUNDRY_KEY").getAsInt(),
                                like.get("LAUNDRY_NM").getAsString(),
                                like.get("LAUNDRY_ADDR").getAsString(),
                                like.get("LAUNDRY_TEL").getAsString()
                        );

                        laundryList.add(laundryLike);
                    }

                    laundryLikeAdapter = new LaundryLikeAdapter(view.getContext(), laundryList);

                    like_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    like_recyclerView.setAdapter(laundryLikeAdapter);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Timber.d("ERROR(getLaundryLikeList) : " + t);
            }
        });
    }
}

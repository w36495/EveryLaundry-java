package com.w36495.everylaundry.view.fragment;

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
import com.w36495.everylaundry.data.domain.Laundry;
import com.w36495.everylaundry.view.LoginActivity;
import com.w36495.everylaundry.data.network.RetrofitBuilder;
import com.w36495.everylaundry.data.network.LaundryAPI;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.view.adapter.LaundryLikeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * 즐겨찾는 세탁소 목록
 */
public class LikeFragment extends Fragment {

    private RecyclerView like_recyclerView;
    private LaundryLikeAdapter laundryLikeAdapter;

    private ArrayList<Laundry> laundryList;

    private String loginID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginID = LoginActivity.loginID;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit(view);
    }

    private void setInit(View view) {
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

        laundryAPI.getLaundryLikeList(loginID).enqueue(new Callback<List<Laundry>>() {
            @Override
            public void onResponse(Call<List<Laundry>> call, Response<List<Laundry>> response) {
                if (response.isSuccessful()) {
                    laundryList = (ArrayList<Laundry>) response.body();
                    laundryLikeAdapter = new LaundryLikeAdapter(view.getContext(), laundryList);

                    like_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    like_recyclerView.setAdapter(laundryLikeAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Laundry>> call, Throwable t) {
                Timber.d("ERROR(getLaundryLikeList) : " + t.getMessage());
            }
        });
    }
}

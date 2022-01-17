package com.w36495.everylaundry.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.BuildConfig;
import com.w36495.everylaundry.view.LaundryMapActivity;
import com.w36495.everylaundry.view.LoginActivity;
import com.w36495.everylaundry.data.network.RetrofitBuilder;
import com.w36495.everylaundry.data.network.LaundryAPI;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.view.adapter.LaundrySearchAdapter;
import com.w36495.everylaundry.data.domain.Laundry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Laundry Search Fragment
 */
public class SearchFragment extends Fragment {

    private RecyclerView search_recyclerView;
    private LaundrySearchAdapter laundrySearchAdapter;

    private EditText search_laundry;

    private ArrayList<Laundry> laundryList;

    private Retrofit retrofit;
    private LaundryAPI laundryAPI;

    private static String loginID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        loginID = LoginActivity.loginID;

        retrofit = RetrofitBuilder.getClient();
        laundryAPI = retrofit.create(LaundryAPI.class);


        setInit(view);
    }

    private void setInit(View view) {

        search_recyclerView = view.findViewById(R.id.search_recyclerView);
        search_laundry = view.findViewById(R.id.search_laundry);

        showLaundrySearchList(view);

        // 검색창 버튼 클릭했을 때
        search_laundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), LaundryMapActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 최근 검색한 세탁소 목록 보여주기
     */
    private void showLaundrySearchList(View view) {
        laundryAPI.getLaundrySearch(loginID).enqueue(new Callback<List<Laundry>>() {
            @Override
            public void onResponse(Call<List<Laundry>> call, Response<List<Laundry>> response) {
                if (response.isSuccessful()) {
                    laundryList = (ArrayList<Laundry>) response.body();
                    laundrySearchAdapter = new LaundrySearchAdapter(view.getContext(), laundryList);
                    search_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    search_recyclerView.setAdapter(laundrySearchAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Laundry>> call, Throwable t) {
                Timber.d("ERROR(getLaundrySearch) : " + t.getMessage());
            }
        });
    }
}

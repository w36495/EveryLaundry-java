package com.w36495.everylaundry.fragment;

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
import com.w36495.everylaundry.LaundryMapActivity;
import com.w36495.everylaundry.MainActivity;
import com.w36495.everylaundry.RetrofitBuilder;
import com.w36495.everylaundry.api.LaundryAPI;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.LaundrySearchAdapter;
import com.w36495.everylaundry.domain.Laundry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Laundry Search Fragment
 */
public class SearchFragment extends Fragment {

    private RecyclerView search_recyclerView;
    private LaundrySearchAdapter laundrySearchAdapter;

    private EditText search_laundry;

    private static ArrayList<Laundry> laundryList = new ArrayList<>();

    private String loginID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit(view);
        return view;
    }

    private void setInit(View view) {

        search_recyclerView = view.findViewById(R.id.search_recyclerView);
        search_laundry = view.findViewById(R.id.search_laundry);

        showLaundrySearchList(view);

        loginID = MainActivity.getLoginUserID();


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
    // todo: 처음 화면에서 바로 목록이 뜨지 않음 -> 수정할 것
    private void showLaundrySearchList(View view) {

        Retrofit retrofit = RetrofitBuilder.getClient();
        LaundryAPI laundryAPI = retrofit.create(LaundryAPI.class);

        laundryAPI.getLaundrySearch(loginID).enqueue(new Callback<List<Laundry>>() {
            @Override
            public void onResponse(Call<List<Laundry>> call, retrofit2.Response<List<Laundry>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("===== 최근 검색한 세탁소 =====");
//                    for (int index = 0; index < response.body().size(); index++) {
//                        Laundry laundrySearch = (JsonObject)response.body().get(index);
//
//                        LaundrySearch search = new LaundrySearch(
//                                laundrySearch.get("USER_ID").getAsString(),
//                                laundrySearch.get("LAUNDRY_KEY").getAsInt(),
//                                laundrySearch.get("LAUNDRY_NM").getAsString(),
//                                laundrySearch.get("LAUNDRY_ADDR").getAsString(),
//                                laundrySearch.get("LAUNDRY_TEL").getAsString(),
//                                laundrySearch.get("UPD_DT").getAsString()
//                        );
//                        laundryList.add(search);
//                    }
                    laundryList = (ArrayList<Laundry>) response.body();
                    laundrySearchAdapter = new LaundrySearchAdapter(view.getContext(), laundryList);
                    search_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    search_recyclerView.setAdapter(laundrySearchAdapter);
                }
            }
            @Override
            public void onFailure(Call<List<Laundry>> call, Throwable t) {
                Timber.d("ERROR(getLaundrySearch) : " + t);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Timber.d("onAttach() 호출");
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.d("onStart() 호출");
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume() 호출");
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause() 호출");
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.d("onStop() 호출");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Timber.d("onDestroyView() 호출");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy() 호출");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach() 호출");
    }
}

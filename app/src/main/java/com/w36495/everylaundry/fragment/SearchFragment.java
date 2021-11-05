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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.LaundryMapActivity;
import com.w36495.everylaundry.MainActivity;
import com.w36495.everylaundry.adapter.LaundryLikeAdapter;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.Laundry;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.LaundrySearchAdapter;
import com.w36495.everylaundry.data.LaundryLike;
import com.w36495.everylaundry.data.LaundrySearch;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Laundry Search Fragment
 */
public class SearchFragment extends Fragment {

    private RecyclerView search_recyclerView;
    private LaundrySearchAdapter laundrySearchAdapter;

    private EditText search_laundry;

    private ArrayList<LaundrySearch> laundryList;

    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(view.getContext());
        }
        setInit(view);
        return view;
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
     * 즐겨찾는 세탁소 목록 보여주기
     */
    private void showLaundrySearchList(View view) {

        String URL = DatabaseInfo.showLaundrySearchURL;


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("showLaundrySearchList() - onResponse : " + response);
                parseLaundrySearchList(view, response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.d("showLaundrySearchList() - onErrorResponse : " + error);
                        return;
                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void parseLaundrySearchList(View view, String response) {
        laundryList = new ArrayList<>();

        String loginUserID = MainActivity.getLoginUserID();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonSearch = (JsonArray)jsonObject.get("laundrySearch");

        Timber.d("parseLaundrySearchList의 jsonSearch SIZE : " + jsonSearch.size());

        for (int index=0; index<jsonSearch.size(); index++) {
            JsonObject laundrySearch = (JsonObject)jsonSearch.get(index);
            if (loginUserID.equals(laundrySearch.get("USER_ID").getAsString())) {

                LaundrySearch search = new LaundrySearch(
                        laundrySearch.get("USER_ID").getAsString(),
                        laundrySearch.get("LAUNDRY_KEY").getAsInt(),
                        laundrySearch.get("LAUNDRY_NM").getAsString(),
                        laundrySearch.get("LAUNDRY_ADDR").getAsString(),
                        laundrySearch.get("LAUNDRY_TEL").getAsString(),
                        laundrySearch.get("UPD_DT").getAsString()
                );
                System.out.println(search.toString());
                laundryList.add(search);
            }
        }

        laundrySearchAdapter = new LaundrySearchAdapter(view.getContext(), laundryList);
        search_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        search_recyclerView.setAdapter(laundrySearchAdapter);
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

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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.MainActivity;
import com.w36495.everylaundry.adapter.BoardCategoryAdapter;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.Laundry;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.LaundryLikeAdapter;
import com.w36495.everylaundry.data.LaundryLike;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * 즐겨찾는 세탁소 목록
 */
public class LikeFragment extends Fragment {

    private RecyclerView like_recyclerView;
    private LaundryLikeAdapter laundryLikeAdapter;

    private ArrayList<LaundryLike> laundryList;

    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like_list, container, false);

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

        like_recyclerView = view.findViewById(R.id.like_recyclerView);





        showLaundryLikeList(view);




    }

    /**
     * 즐겨찾는 세탁소 목록 보여주기
     */
    private void showLaundryLikeList(View view) {

        String URL = DatabaseInfo.showLaundryLikeURL;


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("showLaundryLikeList() - onResponse : " + response);
                parseLaundryLikeList(view, response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.d("showLaundryLikeList() - onErrorResponse : " + error);
                        return;
                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void parseLaundryLikeList(View view, String response) {
        laundryList = new ArrayList<>();

        String loginUserID = MainActivity.getLoginUserID();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonLike = (JsonArray)jsonObject.get("laundryLike");

        Timber.d("parseLaundryLikeList의 jsonLike SIZE : " + jsonLike.size());

        for (int index=0; index<jsonLike.size(); index++) {
            JsonObject laundryLike = (JsonObject)jsonLike.get(index);


            if (loginUserID.equals(laundryLike.get("USER_ID").getAsString())) {

                LaundryLike like = new LaundryLike(
                        laundryLike.get("USER_ID").getAsString(),
                        laundryLike.get("LAUNDRY_KEY").getAsInt(),
                        laundryLike.get("LAUNDRY_NM").getAsString(),
                        laundryLike.get("LAUNDRY_ADDR").getAsString(),
                        laundryLike.get("LAUNDRY_TEL").getAsString()
                );
                System.out.println(like.toString());
                laundryList.add(like);
            }
        }

        laundryLikeAdapter = new LaundryLikeAdapter(view.getContext(), laundryList);

        like_recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        like_recyclerView.setAdapter(laundryLikeAdapter);
    }
}

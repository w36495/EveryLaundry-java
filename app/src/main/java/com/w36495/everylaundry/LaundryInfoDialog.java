package com.w36495.everylaundry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.adapter.ReviewAdapter;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.Laundry;
import com.w36495.everylaundry.data.Review;

import java.util.ArrayList;

import timber.log.Timber;

public class LaundryInfoDialog extends BottomSheetDialog {

    private Context context;
    private Laundry laundry;

    private RecyclerView reviewReycyclerView;
    private ReviewAdapter reviewAdapter;

    private TextView map_title, map_address, map_tel;
    private ImageButton map_like_btn;

    private ArrayList<Review> resultReviewList;
    private RequestQueue requestQueue;

    private String loginUserID = null;

    public LaundryInfoDialog(@NonNull Context context, Laundry laundry) {
        super(context);
        this.context = context;
        this.laundry = laundry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_laundry_map_info);

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.d("onCreate() 호출");

        setInit();

    }

    private void setInit() {

        map_title = findViewById(R.id.map_title);
        map_address = findViewById(R.id.map_address);
        map_tel = findViewById(R.id.map_tel);
        map_like_btn = findViewById(R.id.map_like_btn);

        reviewReycyclerView = findViewById(R.id.review_recyclerView);

        Timber.d("title : " + laundry.getLaundryName());
        Timber.d("address : " + laundry.getLaundryAddress());
        Timber.d("tel : " + laundry.getLaundryTel());

        map_title.setText(laundry.getLaundryName());
        map_address.setText(laundry.getLaundryAddress());
        map_tel.setText(laundry.getLaundryTel());

        SharedPreferences sharedPreferences = context.getSharedPreferences("session", 0);
        loginUserID = sharedPreferences.getString("userID", "");


        String URL = DatabaseInfo.showLaundryReviewURL;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("onResponse() 응답 : " + response);
                parseResponse(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러나면 error로 들어옴
                        Timber.d("onErrorResponse() 로그인 오류 : " + error.getMessage());
                    }
                });
        request.setShouldCache(false);
        requestQueue.add(request);

        map_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateLaundryDetail updateLaundryDetail = new UpdateLaundryDetail();
                updateLaundryDetail.execute(DatabaseInfo.updateLaundryLikeURL, loginUserID, String.valueOf(laundry.getLaundryKey()));
                //todo : 사용자의 LIKE_FLAG에 맞춰 아이콘 변경
            }
        });

    }

    private void parseResponse(String response) {
        resultReviewList = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonReview = (JsonArray) jsonObject.get("reviews");
        Timber.d("jsonReview의 size() : " + jsonReview.size());

        JsonObject laundryReview = new JsonObject();

        for (int index=0; index<jsonReview.size(); index++) {
            laundryReview = (JsonObject) jsonReview.get(index);

            int reviewKey = laundryReview.get("RV_KEY").getAsInt();
            String reviewUserID = laundryReview.get("USER_ID").getAsString();
            int reviewLaundryKey = laundryReview.get("LAUNDRY_KEY").getAsInt();
            String reviewContents = laundryReview.get("RV_CONTENTS").getAsString();
            String reviewRegistDate = laundryReview.get("REG_DT").getAsString();

            if (reviewLaundryKey == laundry.getLaundryKey()) {
                Review review = new Review(reviewKey, reviewUserID, reviewLaundryKey, reviewContents, reviewRegistDate);

                resultReviewList.add(review);
            }


        }

        reviewAdapter = new ReviewAdapter(context, resultReviewList);
        reviewReycyclerView.setLayoutManager(new LinearLayoutManager(context));
        reviewReycyclerView.setAdapter(reviewAdapter);
    }



}

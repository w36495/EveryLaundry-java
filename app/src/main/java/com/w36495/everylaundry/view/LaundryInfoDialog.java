package com.w36495.everylaundry.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.w36495.everylaundry.BuildConfig;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.view.adapter.ReviewAdapter;
import com.w36495.everylaundry.data.network.LaundryAPI;
import com.w36495.everylaundry.data.domain.Laundry;
import com.w36495.everylaundry.data.domain.Review;
import com.w36495.everylaundry.data.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class LaundryInfoDialog extends BottomSheetDialog {

    private Context context;
    private Laundry laundry;

    private RecyclerView reviewReycyclerView;
    private ReviewAdapter reviewAdapter;

    private TextView map_title, map_address, map_tel, map_laundry_type;
    private ImageButton map_like_btn;

    private ArrayList<Review> resultReviewList;

    private String loginID = null;
    private boolean likeFlag = false;

    public LaundryInfoDialog(@NonNull Context context, Laundry laundry, boolean likeFlag) {
        super(context);
        this.context = context;
        this.laundry = laundry;
        this.likeFlag = likeFlag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_laundry_map_info);

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
        map_laundry_type = findViewById(R.id.map_laundry_type);

        // 하트(즐겨찾기) 표시
        if (likeFlag == true) {
            map_like_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else if (likeFlag == false) {
            map_like_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        reviewReycyclerView = findViewById(R.id.review_recyclerView);

        map_title.setText(laundry.getLaundryName());
        map_address.setText(laundry.getLaundryAddress());
        map_tel.setText(laundry.getLaundryTel());

        if (Integer.valueOf(laundry.getLaundryType()) == 0) {
            map_laundry_type.setText("코인");
        } else {
            map_laundry_type.setText("일반");
        }

        loginID = MainActivity.getLoginUserID();

        Retrofit retrofit = RetrofitBuilder.getClient();
        LaundryAPI laundryAPI = retrofit.create(LaundryAPI.class);

        // 세탁소 리뷰 얻기
        laundryAPI.getLaundryReview(laundry.getLaundryKey()).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, retrofit2.Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultReviewList = (ArrayList<Review>) response.body();
                }

                reviewAdapter = new ReviewAdapter(context, resultReviewList);
                reviewReycyclerView.setLayoutManager(new LinearLayoutManager(context));
                reviewReycyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Timber.d("ERROR(getLaundryReview) : " + t.getMessage());
            }
        });
        
        // 즐겨찾기 클릭 시 상태변경
        map_like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 하트(즐겨찾기) 표시
                // LIKE_FLAG = 'Y'인 상태 -> LIKE_FLAG = 'N'로 변경
                if (likeFlag == true) {
                    likeFlag = false;
                    map_like_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
                // LIKE_FLAG = 'N'인 상태 -> LIKE_FLAG = 'Y'로 변경
                else {
                    likeFlag = true;
                    map_like_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                }


                laundryAPI.updateLaundryLike(loginID, laundry.getLaundryKey()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Timber.d("ERROR(updateLaundryLike) : " + t.getMessage());
                    }
                });
            }
        });

    }

}

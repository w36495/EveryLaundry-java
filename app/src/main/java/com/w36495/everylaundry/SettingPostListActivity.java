package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.adapter.SettingPostListAdapter;
import com.w36495.everylaundry.api.PostAPI;
import com.w36495.everylaundry.domain.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import timber.log.Timber;

public class SettingPostListActivity extends AppCompatActivity {

    private RecyclerView setting_post_recyclerView;
    private SettingPostListAdapter settingPostListAdapter;
    private TextView setting_post_title, setting_post_app_name;

    private ArrayList<Post> settingPostList;

    private String loginID = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_post_list);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit();
    }

    private void setInit() {

        setting_post_title = findViewById(R.id.setting_post_title);
        setting_post_app_name = findViewById(R.id.setting_post_app_name);
        setting_post_recyclerView = findViewById(R.id.setting_post_recyclerView);

        loginID = MainActivity.getLoginUserID();

        setting_post_app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPostListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Retrofit retrofit = RetrofitBuilder.getClient();
        PostAPI postAPI = retrofit.create(PostAPI.class);

        postAPI.getPostByUserId(loginID).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    settingPostList = (ArrayList<Post>) response.body();

                    settingPostListAdapter = new SettingPostListAdapter(getApplicationContext(), settingPostList);
                    setting_post_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    setting_post_recyclerView.setAdapter(settingPostListAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Timber.d("ERROR(getPostByUserId) : " + t);
            }
        });
    }
}

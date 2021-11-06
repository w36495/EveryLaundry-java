package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.adapter.SettingPostListAdapter;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.Post;
import com.w36495.everylaundry.fragment.BoardFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class SettingPostListActivity extends AppCompatActivity {

    private RecyclerView setting_post_recyclerView;
    private SettingPostListAdapter settingPostListAdapter;
    private TextView setting_post_title, setting_post_app_name;

    private ArrayList<Post> settingPostList;

    private RequestQueue requestQueue;

    private String loginID = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_post_list);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

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

        String URL = DatabaseInfo.showSettingPostListURL;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("세팅 작성 게시물 목록 : " + response);
                parsePostList(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", loginID);
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);

    }

    private void parsePostList(String response) {
        settingPostList = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        JsonArray jsonPostList = (JsonArray) jsonObject.get("postArray");

        JsonObject jsonPost;

        for (int index = 0; index < jsonPostList.size(); index++) {
            jsonPost = (JsonObject) jsonPostList.get(index);

            Post post = new Post(
                    jsonPost.get("POST_KEY").getAsInt(),
                    jsonPost.get("USER_ID").getAsString(),
                    jsonPost.get("CATEGORY_KEY").getAsInt(),
                    jsonPost.get("POST_TITLE").getAsString(),
                    jsonPost.get("POST_CONTENTS").getAsString(),
                    jsonPost.get("VIEW_CNT").getAsInt(),
                    jsonPost.get("RECOMMENT_CNT").getAsInt(),
                    jsonPost.get("POST_NOTICE").getAsBoolean(),
                    jsonPost.get("POST_CM_FLAG").getAsBoolean(),
                    jsonPost.get("REG_DT").getAsString(),
                    jsonPost.get("UPD_DT").getAsString());

            settingPostList.add(post);
        }

        settingPostListAdapter = new SettingPostListAdapter(getApplicationContext(), settingPostList);
        setting_post_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setting_post_recyclerView.setAdapter(settingPostListAdapter);

    }
}

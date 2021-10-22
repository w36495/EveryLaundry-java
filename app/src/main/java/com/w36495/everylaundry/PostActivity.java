package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 게시물 조회
 */
public class PostActivity extends AppCompatActivity {
    private static String TAG = "로그";

    private TextView post_title, post_writer, post_regist_date, post_view_count, post_recommend_count, post_contents;
    private EditText post_comment;
    private Button post_comment_btn;
    private ImageButton post_add_btn, post_back_btn;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_post);
        Log.d(TAG, "PostActivity - onCreate() 호출");

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        setInit();



    }

    private void setInit() {

        post_title = findViewById(R.id.post_title);
        post_writer = findViewById(R.id.post_writer);
        post_regist_date = findViewById(R.id.post_regist_date);
        post_view_count = findViewById(R.id.post_view_count);
        post_recommend_count = findViewById(R.id.post_recommend_count);
        post_contents = findViewById(R.id.post_contents);
        post_comment = findViewById(R.id.post_comment);
        post_comment_btn = findViewById(R.id.post_comment_btn);
        post_back_btn = findViewById(R.id.post_back_btn);
        post_add_btn = findViewById(R.id.post_add_btn);


        Intent intent = getIntent();
        int postPosition = -1;
        if (getIntent().hasExtra("postPosition")) {
            postPosition = intent.getIntExtra("postPosition", -2);
        }

        Log.d(TAG, "PostActivity - postPosition : " + postPosition);

        getPostContents(postPosition);

        post_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PostActivity.this, PostAddActivity.class);
                startActivity(intent1);
            }
        });



    }

    private void getPostContents(int postPosition) {
        Log.d(TAG, "PostActivity - getPostContents() 호출");

        String URL = DatabaseInfo.showPostURL;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "PostActivity - parsePostContents() 성공 : " + response);
                parsePostContents(response, postPosition);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "PostActivity - parsePostContents() 에러 : " + error);
                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);

    }

    private void parsePostContents(String response, int postPosition) {
        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(response);
        JsonArray postArray = (JsonArray) object.get("board_post");

        JsonObject post = new JsonObject();
        for (int index=0; index<postArray.size(); index++) {
            post = (JsonObject) postArray.get(index);
            if ((postPosition+1) == post.get("POST_KEY").getAsInt()) {
                break;
            }
        }

        System.out.println("제목 : " + post.get("POST_TITLE"));
        System.out.println("내용 : " + post.get("POST_CONTENTS"));
        System.out.println("글쓴이 : " + post.get("USER_ID"));

        post_title.setText(post.get("POST_TITLE").getAsString());
        post_contents.setText(post.get("POST_CONTENTS").getAsString());
        post_writer.setText(post.get("USER_ID").getAsString());
        post_regist_date.setText(post.get("REG_DT").getAsString());
        post_view_count.setText(post.get("VIEW_CNT").getAsString());
        post_recommend_count.setText(post.get("RECOMMENT_CNT").getAsString());

    }

}

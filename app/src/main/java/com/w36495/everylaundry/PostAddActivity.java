package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.w36495.everylaundry.fragment.BoardFragment;

import timber.log.Timber;

/**
 * 게시물 작성/수정하는 클래스
 */
public class PostAddActivity extends AppCompatActivity {

    private ImageButton post_add_back_btn, post_add_save_btn;
    private EditText post_add_title, post_add_contents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_post_add);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.d("onCreate() 호출");

        setInit();

    }

    private void setInit() {

        post_add_back_btn = findViewById(R.id.post_add_back_btn);
        post_add_save_btn = findViewById(R.id.post_add_save_btn);
        post_add_title = findViewById(R.id.post_add_title);
        post_add_contents = findViewById(R.id.post_add_contents);

        // 확인(저장) 버튼을 클릭했을 때 -> DB에 데이터 전송
        post_add_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int postKey = 4;
                String userID = "toby";
                int categoryKey = 2;
                String postTitle = post_add_title.getText().toString();
                String postContents = post_add_contents.getText().toString();

                InsertPost insertPost = new InsertPost();
                insertPost.execute(DatabaseInfo.setPostURL, String.valueOf(postKey), userID, String.valueOf(categoryKey), postTitle, postContents);
                Timber.d("create Post!");

            }
        });

    }
}

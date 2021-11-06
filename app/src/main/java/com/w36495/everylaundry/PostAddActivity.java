package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.w36495.everylaundry.api.InsertPost;
import com.w36495.everylaundry.api.UpdatePost;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.fragment.BoardFragment;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * 게시물 작성/수정하는 클래스
 */
public class PostAddActivity extends AppCompatActivity {

    private ImageButton post_add_back_btn, post_add_save_btn;
    private EditText post_add_title, post_add_contents;
    private Spinner post_add_spinner;

    private int postKey = -1;
    private int categoryKey = -1;
    private boolean postFlag = true;    // true : 추가, false : 수정


    private String[] categoryList;

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
        post_add_spinner = findViewById(R.id.post_add_spinner);

        Intent intent = getIntent();
        if (intent.hasExtra("postKey")) {
            this.postKey = intent.getIntExtra("postKey",-1);
        }
        if (intent.hasExtra("updateFlag")) {
            postFlag = false;
            Timber.d("updateFloag : " + intent.getStringExtra("updateFlag"));
            postKey = intent.getIntExtra("updatePostKey", -1);
            categoryKey = intent.getIntExtra("updatePostCategoryKey", -1);
            post_add_title.setText(intent.getStringExtra("updatePostTitle"));
            post_add_contents.setText(intent.getStringExtra("updatePostContents"));
        }

        ArrayList<String> category = BoardFragment.categoryList;
        categoryList = category.toArray(new String[category.size()]);

        categoryList[0] = "카테고리를 선택해주세요.";

        Timber.d("선택된 카테고리 키 : " + categoryKey);

        Timber.d("PostFlag : " + postFlag);


        // 스피너(콤보박스) 어댑터
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        post_add_spinner.setAdapter(categoryAdapter);
        if (categoryKey != -1) {
            post_add_spinner.setSelection(categoryKey);
        }

        // 스피너(콤보박스) 카테고리 클릭했을 때
        post_add_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {
                    case 0:     // 카테고리를 선택해주세요.
                        categoryKey = 0;
                        break;
                    case 1:     // #수선
                        categoryKey = 1;
                        break;
                    case 2:
                        categoryKey = 2;
                        break;
                    case 3:
                        categoryKey = 3;
                        break;
                    case 4:
                        categoryKey = 4;
                        break;
                }

                Timber.d("선택된 카테고리 : " + categoryList[position] + " position : " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // todo : 뒤로가기 버튼 클릭했을 때 -> 글 내용으로 이동 (콜백사용)
        post_add_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 확인(저장) 버튼을 클릭했을 때 -> DB에 데이터 전송
        post_add_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginID = MainActivity.getLoginUserID();
                Timber.d("userID : " + loginID);
                String postTitle = post_add_title.getText().toString();
                String postContents = post_add_contents.getText().toString();

                if (categoryKey == 0) {
                    Toast.makeText(getApplicationContext(), "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();

                } else {

                    if (postFlag) {
                        // DB에 저장
                        InsertPost insertPost = new InsertPost();
                        insertPost.execute(DatabaseInfo.setPostURL, String.valueOf(++postKey), loginID, String.valueOf(categoryKey), postTitle, postContents);
                        Timber.d("1 added Success!");
                    } else {
                        UpdatePost updatePost = new UpdatePost();
                        updatePost.execute(DatabaseInfo.updatePostURL, String.valueOf(postKey), String.valueOf(categoryKey), postTitle, postContents);
                        Timber.d("1 updated Success!");
                    }


                    Intent  intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("PostAddActivity", "PostActivity");
                    startActivity(intent);
                }


            }
        });

    }
}

package com.w36495.everylaundry.view;

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

import com.w36495.everylaundry.BuildConfig;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.network.CategoryAPI;
import com.w36495.everylaundry.data.network.PostAPI;
import com.w36495.everylaundry.data.domain.Category;
import com.w36495.everylaundry.data.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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

    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    private Retrofit retrofit;
    private PostAPI postAPI;
    private CategoryAPI categoryAPI;

    private String loginID;

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
        loginID = LoginActivity.loginID;

        retrofit = RetrofitBuilder.getClient();
        postAPI = retrofit.create(PostAPI.class);
        categoryAPI = retrofit.create(CategoryAPI.class);

        post_add_back_btn = findViewById(R.id.post_add_back_btn);
        post_add_save_btn = findViewById(R.id.post_add_save_btn);
        post_add_title = findViewById(R.id.post_add_title);
        post_add_contents = findViewById(R.id.post_add_contents);
        post_add_spinner = findViewById(R.id.post_add_spinner);

        Intent intent = getIntent();
        if (intent.hasExtra("postKey")) {
            postKey = intent.getIntExtra("postKey",-1) + 1;
        }
        if (intent.hasExtra("updateFlag")) {
            postFlag = false;
            postKey = intent.getIntExtra("updatePostKey", -1);
            categoryKey = intent.getIntExtra("updatePostCategoryKey", -1);
            post_add_title.setText(intent.getStringExtra("updatePostTitle"));
            post_add_contents.setText(intent.getStringExtra("updatePostContents"));
        }


        // 카테고리 셋팅
        setCategoryList();

        // 스피너(콤보박스) 카테고리 클릭했을 때
        post_add_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:     // 전체
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 확인(저장) 버튼을 클릭했을 때 -> DB에 데이터 전송
        post_add_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postTitle = post_add_title.getText().toString();
                String postContents = post_add_contents.getText().toString();

                if (postFlag) {
                    // DB에 저장
                    postAPI.insertPost(String.valueOf(postKey), loginID, String.valueOf(categoryKey), postTitle, postContents).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(PostAddActivity.this, "게시물이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Timber.d(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Timber.d("ERROR(insertPost) : " + t.getMessage());
                        }
                    });
                } else {
                    postAPI.updatePost(String.valueOf(postKey), String.valueOf(categoryKey), postTitle, postContents).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(PostAddActivity.this, "게시물이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Timber.d(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Timber.d("ERROR(updatePost) : " + t.getMessage());
                        }
                    });
                }
                Intent  intent = new Intent(PostAddActivity.this, MainActivity.class);
                intent.putExtra("PostAddActivity", "PostActivity");
                startActivity(intent);
            }
        });

    }


    /**
     * 카테고리 셋팅
     */
    private void setCategoryList() {
        categoryAPI.getCategoryAll().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    for (int index = 0; index < response.body().size(); index++) {
                        categoryList.add(response.body().get(index).getCategoryTitle());
                    }

                    // 스피너(콤보박스) 어댑터
                    categoryAdapter = new ArrayAdapter<>(PostAddActivity.this, android.R.layout.simple_spinner_item, categoryList);
                    post_add_spinner.setAdapter(categoryAdapter);
                    if (categoryKey != -1) {
                        post_add_spinner.setSelection(categoryKey);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Timber.d("ERROR(getCategoryAll) : " + t.getMessage());
            }
        });

    }
}

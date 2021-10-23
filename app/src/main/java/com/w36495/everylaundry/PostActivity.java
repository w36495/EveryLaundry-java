package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.adapter.BoardCategoryAdapter;
import com.w36495.everylaundry.adapter.BoardCommentAdapter;
import com.w36495.everylaundry.data.Comment;
import com.w36495.everylaundry.data.DatabaseInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * 게시물 조회
 */
public class PostActivity extends AppCompatActivity {

    private RecyclerView commentRecyclerView;
    private BoardCommentAdapter commentAdapter;

    private ArrayList<Comment> commentList = new ArrayList<>();

    private TextView post_title, post_writer, post_regist_date, post_view_count, post_recommend_count, post_contents;
    private EditText post_comment;
    private Button post_comment_btn;
    private ImageButton post_update_btn, post_back_btn;

    private RequestQueue requestQueue;

    private int postKey = -1;
    private int categoryKey = -1;
    private int commentKey = 0;
    private String postWriter = null;
    private String loginUser = LoginActivity.userID;
    private String loginUserNickNM = LoginActivity.userNickNM;
    private boolean userFlag = true;    // 작성자 == 로그인한사람 : true, 작성자 != 로그인한사람 : false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_post);
        Timber.d("onCreate() 호출");

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
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
        post_update_btn = findViewById(R.id.post_update_btn);
        post_comment = findViewById(R.id.post_comment);
        post_comment_btn = findViewById(R.id.post_comment_btn);

        // 댓글 리싸이클러뷰
        commentRecyclerView = findViewById(R.id.post_comment_recyclerView);

        showPostComment();

        Intent intent = getIntent();

        if (getIntent().hasExtra("postKey")) {
            postKey = intent.getIntExtra("postKey", -2);
            categoryKey = intent.getIntExtra("categoryKey", -2);
            postWriter = intent.getStringExtra("postWriter");
        }

        Timber.d("선택된 게시물 postKey : " + postKey);

        getPostContents(postKey);

        // 작성자 != 로그인 한 사람
        if (!loginUser.equals(postWriter)) {
            userFlag = false;
            post_update_btn.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24);
        }

        // 게시물 수정 버튼
        post_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userFlag) {
                    String updatePostTitle = post_title.getText().toString();
                    String updatePostContents = post_contents.getText().toString();

                    Timber.d("postKey : " + postKey);
                    Timber.d("넘어갈 카테고리 키 : " + categoryKey);

                    Intent updateIntent = new Intent(PostActivity.this, PostAddActivity.class);
                    updateIntent.putExtra("updateFlag", "N");
                    updateIntent.putExtra("updatePostKey", postKey);
                    updateIntent.putExtra("updatePostCategoryKey", categoryKey);
                    updateIntent.putExtra("updatePostTitle", updatePostTitle);
                    updateIntent.putExtra("updatePostContents", updatePostContents);
                    startActivity(updateIntent);
                }

                else {
                    // todo: 좋아요(따봉)수 연결하기
                    Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /**
         * 댓글 작성 버튼
         */
        post_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = post_comment.getText().toString();


                Timber.d("댓글키 : " + commentKey);
                Timber.d("작성자 닉네임 : " + loginUserNickNM);
                Timber.d("댓글작성한 글 키 : " + postKey);
                Timber.d("댓글 : " + comment);

                InsertComment insertComment = new InsertComment();
                insertComment.execute(DatabaseInfo.setBoardCommentURL, String.valueOf(commentKey), loginUserNickNM, String.valueOf(postKey), comment);
                Timber.d("1 added comments!");

                post_comment.setText("");
            }
        });
    }

    /**
     * DB 연결하여 댓글 가져와서 보여주기
     */
    private void showPostComment() {
        String URL = DatabaseInfo.showBoardCommentURl;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parsePostComment( response, postKey);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    /**
     * DB에서 댓글 가져오기
     * @param response
     * @param postKey
     */
    private void parsePostComment( String response, int postKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonComment = (JsonArray)jsonObject.get("board_comments");

        for (int index=0; index<jsonComment.size(); index++) {
            JsonObject comments = (JsonObject)jsonComment.get(index);
            commentKey++;
            if (postKey == comments.get("POST_KEY").getAsInt()) {
                Comment comment = new Comment(comments.get("CM_KEY").getAsInt(),
                        comments.get("CM_WRITER").getAsString(),
                        comments.get("POST_KEY").getAsInt(),
                        comments.get("CM_CONTENTS").getAsString(),
                        comments.get("REG_DT").getAsString());
                commentList.add(comment);
            }
        }

        commentAdapter = new BoardCommentAdapter(this, commentList);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentRecyclerView.setAdapter(commentAdapter);
    }

    private void getPostContents(int postKey) {
        Timber.d("getPostContents() 호출");

        String URL = DatabaseInfo.showPostURL;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("parsePostContents() 성공 : " + response);
                parsePostContents(response, postKey);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.d("parsePostContents() 에러 : " + error);
                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);

    }

    private void parsePostContents(String response, int postKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject object = (JsonObject) jsonParser.parse(response);
        JsonArray postArray = (JsonArray) object.get("board_post");

        JsonObject post = new JsonObject();
        for (int index=0; index<postArray.size(); index++) {
            post = (JsonObject) postArray.get(index);
            if (postKey == post.get("POST_KEY").getAsInt()) {
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

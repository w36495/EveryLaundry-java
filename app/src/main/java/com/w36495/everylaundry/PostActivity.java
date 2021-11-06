package com.w36495.everylaundry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.adapter.BoardCommentAdapter;
import com.w36495.everylaundry.data.Comment;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.fragment.BoardFragment;
import com.w36495.everylaundry.util.DateUtil;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * 게시물 조회
 */
public class PostActivity extends AppCompatActivity {

    private RecyclerView commentRecyclerView;
    private BoardCommentAdapter commentAdapter;
    private BoardFragment boardFragment;

    private ArrayList<Comment> commentList;

    private TextView post_app_name, post_title, post_writer, post_regist_date, post_view_count, post_recommend_count, post_contents;
    private EditText post_comment;
    private Button post_comment_btn;
    private ImageButton post_update_btn, post_back_btn, post_delete_btn;
    private SwipeRefreshLayout commentSwipeLayout;

    private RequestQueue requestQueue;

    private int postKey = -1;
    private int categoryKey = -1;
    private int commentKey = 0;
    private String postWriter = null;
    private String loginID = null;
    private String loginNickNM = null;
    private boolean isRecommend = false;
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

        post_app_name = findViewById(R.id.post_app_name);
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
        post_delete_btn = findViewById(R.id.post_delete_btn);
        post_comment = findViewById(R.id.post_comment);
        post_comment_btn = findViewById(R.id.post_comment_btn);
        commentSwipeLayout = findViewById(R.id.comment_swipeLayout);

        // 댓글 리싸이클러뷰
        commentRecyclerView = findViewById(R.id.post_comment_recyclerView);

        showPostComment();

        Intent intent = getIntent();

        if (getIntent().hasExtra("postKey")) {
            postKey = intent.getIntExtra("postKey", -2);
            categoryKey = intent.getIntExtra("categoryKey", -2);
            postWriter = intent.getStringExtra("postWriter");
            isRecommend = intent.getBooleanExtra("postRecommend", false);
        }

        Timber.d("선택된 게시물 postKey : " + postKey);

        getPostContents(postKey);

        // 세션으로부터 현재 로그인한 사용자의 아이디와 닉네임 넘겨받기
        loginID = MainActivity.getLoginUserID();
        loginNickNM = MainActivity.getLoginUserNickNM();

        // 작성자 != 로그인 한 사람이면 수정버튼 대신 따봉(추천)버튼 보이기, 삭제 버튼 없애기
        if (!loginID.equals(postWriter)) {
            userFlag = false;
            Timber.d("넘어온 추천 : " + isRecommend);
            if (isRecommend == true) {
                post_update_btn.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24);
            } else {
                post_update_btn.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24);
            }
            post_delete_btn.setVisibility(View.INVISIBLE);
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
                    // 추천을 누른 상태라면 -> 추천 해제
                    if (isRecommend == true) {
                        Toast.makeText(getApplicationContext(), "추천을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                        isRecommend = false;
                        // 색이 비어있는 추천 이미지 변경
                        post_update_btn.setImageResource(R.drawable.ic_baseline_thumb_up_off_alt_24);
                    }
                    // 추천을 누르지 않은 상태라면 -> 추천
                    else {
                        Toast.makeText(getApplicationContext(), "추천하였습니다.", Toast.LENGTH_SHORT).show();
                        isRecommend = true;
                        // 색이 꽉 차있는 추천 이미지로 변경
                        post_update_btn.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24);
                    }
                    UpdatePostRecommend updatePostRecommend = new UpdatePostRecommend();
                    updatePostRecommend.execute(DatabaseInfo.updatePostRecommendURL, loginID, String.valueOf(postKey));
                }

            }
        });

        /**
         * 앱 이름 클릭 -> 메인 화면(검색)으로 이동
         */
        post_app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

        /**
         * 게시물 삭제 버튼
         */
        post_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletePostDialog();
            }
        });

        /**
         * 뒤로가기 버튼(글 조회 -> 글 목록)
         */
        post_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                intent1.putExtra("PostActivity", "PostActivity");
                startActivity(intent1);
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
                Timber.d("작성자 닉네임 : " + loginNickNM);
                Timber.d("댓글작성한 글 키 : " + postKey);
                Timber.d("댓글 : " + comment);

                InsertComment insertComment = new InsertComment();
                insertComment.execute(DatabaseInfo.setBoardCommentURL, String.valueOf(commentKey), loginNickNM, String.valueOf(postKey), comment);
                Timber.d("1 added comments!");

                post_comment.setText("");
            }
        });

        // todo: 댓글 스와이프(새로고침)
        commentSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentAdapter.notifyDataSetChanged();
                commentSwipeLayout.setRefreshing(false);
            }
        });

    }

    private void showDeletePostDialog() {
        boardFragment = new BoardFragment();

        AlertDialog.Builder deleteDialogBuilder = new AlertDialog.Builder(PostActivity.this)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .setTitle("게시물 삭제")
                .setMessage("게시물을 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Timber.d("게시물 삭제 버튼 클릭");
                        DeletePost deletePost = new DeletePost();
                        deletePost.execute(DatabaseInfo.deletePost, loginID, String.valueOf(postKey));
                        Toast.makeText(getApplicationContext(), "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("PostActivity", "PostActivity");
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Timber.d("게시물 취소 버튼 클릭");
                    }
                });

        AlertDialog deleteDialog = deleteDialogBuilder.create();
        deleteDialog.show();

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

        commentList = new ArrayList<>();
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

        commentAdapter = new BoardCommentAdapter(this, commentList, postWriter);
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
        JsonArray postArray = (JsonArray) object.get("posts");

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

        // 날짜
        String registDate = DateUtil.parseDate(post.get("REG_DT").getAsString());

        post_title.setText(post.get("POST_TITLE").getAsString());
        post_contents.setText(post.get("POST_CONTENTS").getAsString());
        post_writer.setText(post.get("USER_ID").getAsString());
        post_regist_date.setText(registDate);
        post_view_count.setText(post.get("VIEW_CNT").getAsString());
        post_recommend_count.setText(post.get("RECOMMENT_CNT").getAsString());

        this.postWriter = post.get("USER_ID").getAsString();

    }


}

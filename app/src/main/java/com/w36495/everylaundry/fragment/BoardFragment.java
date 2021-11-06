package com.w36495.everylaundry.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.BuildConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.BoardCategoryClickListener;
import com.w36495.everylaundry.api.InsertPostRecommend;
import com.w36495.everylaundry.MainActivity;
import com.w36495.everylaundry.api.UpdatePostViewCount;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.PostActivity;
import com.w36495.everylaundry.PostAddActivity;
import com.w36495.everylaundry.PostClickListener;
import com.w36495.everylaundry.data.Post;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.BoardCategoryAdapter;
import com.w36495.everylaundry.adapter.BoardPostAdapter;
import com.w36495.everylaundry.util.DateUtil;

import java.util.ArrayList;

import timber.log.Timber;

public class BoardFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private RecyclerView postRecyclerView;
    private BoardCategoryAdapter categoryAdapter;
    private BoardPostAdapter postAdapter;

    private SwipeRefreshLayout boardSwipeLayout;
    private FloatingActionButton board_add_fab;

    private RequestQueue requestQueue;

    private int lastPostKey = -1;   // 마지막 게시물 키
    public static ArrayList<String> categoryList;
    private ArrayList<Post> postList;

    private String loginID = null;

    private BoardCategoryClickListener boardCategoryClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(view.getContext());
        }

        setInit(view);
        return view;
    }

    private void setInit(View view) {

        categoryRecyclerView = view.findViewById(R.id.board_category_recyclerView);
        postRecyclerView = view.findViewById(R.id.board_post_recyclerView);
        board_add_fab = view.findViewById(R.id.board_add_fab);
        boardSwipeLayout = view.findViewById(R.id.board_swipeLayout);

        // 게시물 새로고침
        boardSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(BoardFragment.this).attach(BoardFragment.this).commit();
                boardSwipeLayout.setRefreshing(false);
            }
        });

        categoryList = new ArrayList<>();

        // 세션
        loginID = MainActivity.getLoginUserID();


        // 카테고리 //
        showBoardCategory(view);

        // 게시물
        showBoardPost(view);


        // 게시물 작성 버튼
        board_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PostAddActivity.class);
                intent.putExtra("postKey", lastPostKey);
                startActivity(intent);
            }
        });


    }

    /**
     * 카테고리
     */
    private void showBoardCategory(View view) {
        String URL = DatabaseInfo.showBoardCategoryURL;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("showBoardCategory() - onResponse : " + response);
                parseBoardCategory(response, view, categoryList);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.d("showBoardCategory() - onErrorResponse : " + error);
                        return;
                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    /**
     * DB로부터 가져온 카테고리데이터 가공 => 어댑터통해서 카테고리 띄우기
     */
    private void parseBoardCategory(String response, View view, ArrayList<String> categoryList) {

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonCategory = (JsonArray)jsonObject.get("board_category");

        for (int index=0; index<jsonCategory.size(); index++) {
            JsonObject category = (JsonObject)jsonCategory.get(index);

            // #카테고리
            StringBuilder builder = new StringBuilder();
            builder.append("#").append(category.get("CATEGORY_TITLE").getAsString());

            categoryList.add(builder.toString());
        }

        categoryAdapter = new BoardCategoryAdapter(view.getContext(), categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);


        categoryAdapter.setOnBoardCategoryClicked(new BoardCategoryClickListener() {
            @Override
            public void onBoardCategoryClicked(View view, int position) {
                Toast.makeText(getActivity(), "선택한 카테고리 : " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 글
     */

    private void showBoardPost(View view) {
        String URL = DatabaseInfo.showBoardPostURL;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("showBoardPost() - onResponse : " + response);
                parseBoardPost(response, view);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.d("showBoardPost() - onErrorResponse : " + error);
                        return;
                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void parseBoardPost(String response, View view) {

        postList = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        JsonArray jsonPost = (JsonArray) jsonObject.get("posts");

        for (int index=0; index<jsonPost.size(); index++) {
            JsonObject jsonOnePost = (JsonObject)jsonPost.get(index);

            boolean postNotice = false;
            boolean postCommentFlag = true;

            if (jsonOnePost.get("POST_NOTICE").getAsCharacter() == 'Y') {
                postNotice = true;
            }
            if (jsonOnePost.get("POST_CM_FLAG").getAsCharacter() == 'N') {
                postCommentFlag = false;
            }

            String registDate = DateUtil.parseDate(jsonOnePost.get("REG_DT").getAsString());
            String updateDate = DateUtil.parseDate(jsonOnePost.get("UPD_DT").getAsString());

            Post post = new Post(
                    jsonOnePost.get("POST_KEY").getAsInt(),
                    jsonOnePost.get("USER_ID").getAsString(),
                    jsonOnePost.get("CATEGORY_KEY").getAsInt(),
                    jsonOnePost.get("POST_TITLE").getAsString(),
                    jsonOnePost.get("POST_CONTENTS").getAsString(),
                    jsonOnePost.get("VIEW_CNT").getAsInt(),
                    jsonOnePost.get("RECOMMENT_CNT").getAsInt(),
                    postNotice,
                    postCommentFlag,
                    registDate,
                    updateDate
            );

            if (lastPostKey < jsonOnePost.get("POST_KEY").getAsInt()) {
                lastPostKey = jsonOnePost.get("POST_KEY").getAsInt();
            }

            postList.add(post);
        }

        Timber.d("마지막 게시물 키 : " + lastPostKey);

        postAdapter = new BoardPostAdapter(view.getContext(), postList, categoryList);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postRecyclerView.setAdapter(postAdapter);

        postRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Toast.makeText(view.getContext(), "터치", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // 게시물 클릭했을 때의 이벤트 리스너
        postAdapter.setOnPostClickListener(new PostClickListener() {
            @Override
            public void onClickPost(View view, int position) {
                Timber.d("postPosition : " + position);

                int choicePostKey = postList.get(position).getPostKey();
                int choicePostCategoryKey = postList.get(position).getPostCategory();
                String choicePostWriter = postList.get(position).getPostWriter();
                Timber.d("선택한 게시물의 키 : " + choicePostKey);
                Timber.d("선택한 게시물의 카테고리 : " + choicePostCategoryKey);
                Timber.d("선택한 게시물의 작성자 : " + choicePostWriter);

                // Recommend Setting
                InsertPostRecommend insertPostRecommend = new InsertPostRecommend();
                insertPostRecommend.execute(DatabaseInfo.insertPostRecommendURL, loginID, String.valueOf(choicePostKey));

                // 게시물 작성자 != 로그인 사용자 => 조회수 업데이트/따봉 표시
                if (loginID.equals(postList.get(position).getPostWriter()) == false) {
                    UpdatePostViewCount updatePostViewCount = new UpdatePostViewCount();
                    updatePostViewCount.execute(DatabaseInfo.updatePostViewCountURL, String.valueOf(choicePostKey));

                    String URL = DatabaseInfo.showPostRecommendURL;

                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Timber.d("추천 결과 : " + isPostRecommend(response, choicePostKey));
                            // 추천 누른 경우
                            boolean isRecommend = isPostRecommend(response, choicePostKey);
                            if (isRecommend == true) {
                                Intent intent = new Intent(view.getContext(), PostActivity.class);
                                intent.putExtra("choicePostKey", choicePostKey);
                                intent.putExtra("choiceCategoryKey", choicePostCategoryKey);
                                intent.putExtra("postWriter", choicePostWriter);
                                intent.putExtra("postRecommend", isRecommend);
                                startActivity(intent);
                            }
                            // 추천을 누르지 않은 경우
                            else {

                            }
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


                Intent intent = new Intent(view.getContext(), PostActivity.class);
                intent.putExtra("choicePostKey", choicePostKey);
                intent.putExtra("choicePostCategoryKey", choicePostCategoryKey);
                intent.putExtra("choicePostWriter", choicePostWriter);
                startActivity(intent);
            }
        });

    }

    private boolean isPostRecommend(String response, int choicePostKey) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonRecommend = (JsonArray)jsonObject.get("postRecommend");

        boolean recommendCheck = false;

        Timber.d("jsonRecommend 사이즈 : " + jsonRecommend.size());
        for (int index=0; index<jsonRecommend.size(); index++) {
            JsonObject recommend = (JsonObject) jsonRecommend.get(index);

            // 같은 게시물이고
            if (choicePostKey == recommend.get("POST_KEY").getAsInt()) {
                // 아이디가 다르면
                if (loginID.equals(recommend.get("USER_ID").getAsString())) {
                    // 추천버튼 누르지 않은 경우
                    if (recommend.get("RECOMMEND_FLAG").getAsString().equals("Y")) {
                        Timber.d("추천 버튼 : Y");
                        recommendCheck = true;
                    }
                    // 추천버튼 누른 경우
                    else {
                        Timber.d("추천 버튼 : N");
                        recommendCheck = false;
                    }
                }
            }
        }
        return recommendCheck;
    }
}

package com.w36495.everylaundry.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.PostActivity;
import com.w36495.everylaundry.PostAddActivity;
import com.w36495.everylaundry.PostClickListener;
import com.w36495.everylaundry.data.Post;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.BoardCategoryAdapter;
import com.w36495.everylaundry.adapter.BoardPostAdapter;

import java.util.ArrayList;

import timber.log.Timber;

public class BoardFragment extends Fragment {
    private RecyclerView categoryRecyclerView;
    private RecyclerView postRecyclerView;
    private BoardCategoryAdapter categoryAdapter;
    private BoardPostAdapter postAdapter;

    private FloatingActionButton board_add_fab;

    private RequestQueue requestQueue;

    private int postKey = 0;
    public static ArrayList<String> categoryList;
    private ArrayList<Post> postList;

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

        // 카테고리 //
        showBoardCategory(view);

        // 게시물
        showBoardPost(view);


        // 게시물 작성 버튼
        board_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), PostAddActivity.class);
                intent.putExtra("postKey", postKey);
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

        categoryList = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonCategory = (JsonArray)jsonObject.get("board_category");

        for (int index=0; index<jsonCategory.size(); index++) {
            JsonObject category = (JsonObject)jsonCategory.get(index);

            // # + 카테고리
            StringBuilder builder = new StringBuilder();
            builder.append("#").append(category.get("CATEGORY_TITLE").getAsString());

            categoryList.add(builder.toString());
        }

        categoryAdapter = new BoardCategoryAdapter(view.getContext(), categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);
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
                //parseBoardPost(response, view, postList);
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
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonPost = (JsonArray)jsonObject.get("posts");

        postKey = jsonPost.size();

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
                    jsonOnePost.get("REG_DT").getAsString(),
                    jsonOnePost.get("UPD_DT").getAsString()
            );

            postList.add(post);
        }

        for (Post post : postList) {
            System.out.println(post.toString());
        }

        postAdapter = new BoardPostAdapter(view.getContext(), postList);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postRecyclerView.setAdapter(postAdapter);

        // 게시물 클릭했을 때의 이벤트 리스너
        postAdapter.setOnPostClickListener(new PostClickListener() {
            @Override
            public void onClickPost(View view, int position) {
                Timber.d("postPosition : " + position);
                Timber.d("선택한 postKey : " + postList.get(position).getPostKey());
                int postKey = postList.get(position).getPostKey();
                int categoryKey = postList.get(position).getPostCategory();
                String postWriter = postList.get(position).getPostWriter();
                Intent intent = new Intent(view.getContext(), PostActivity.class);
                intent.putExtra("postKey", postKey);
                intent.putExtra("categoryKey", categoryKey);
                intent.putExtra("postWriter", postWriter);
                startActivity(intent);
            }
        });

        Timber.d("글 마지막 번호 : " + postKey);

    }

}

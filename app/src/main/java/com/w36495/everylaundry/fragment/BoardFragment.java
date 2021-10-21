package com.w36495.everylaundry.fragment;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.PostActivity;
import com.w36495.everylaundry.PostClickListener;
import com.w36495.everylaundry.data.Post;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.BoardCategoryAdapter;
import com.w36495.everylaundry.adapter.BoardPostAdapter;

import java.util.ArrayList;

import retrofit2.http.POST;
import timber.log.Timber;

public class BoardFragment extends Fragment {
    private static String TAG = "로그";

    private RecyclerView categoryRecyclerView;
    private RecyclerView postRecyclerView;
    private BoardCategoryAdapter categoryAdapter;
    private BoardPostAdapter postAdapter;

    private RequestQueue requestQueue;

    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayList<Post> postList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        Timber.plant(new Timber.DebugTree());

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(view.getContext());
        }

        setInit(view);
        return view;
    }

    private void setInit(View view) {

        categoryRecyclerView = view.findViewById(R.id.board_category_recyclerView);
        postRecyclerView = view.findViewById(R.id.board_post_recyclerView);

        // 카테고리 //
        showBoardCategory(view);

        // 게시물
        showBoardPost(view);


    }

    /**
     * 카테고리
     */
    private void showBoardCategory(View view) {
        String URL = "http://54.180.88.233/selectCategory.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "BoardFragment - showBoardCategory() - onResponse : " + response);
                parseBoardCategory(response, view, categoryList);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "BoardFragment - showBoardCategory() - onErrorResponse : " + error);
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

            // # + 카테고리
            StringBuilder builder = new StringBuilder();
            builder.append("#").append(category.get("CATEGORY_TITLE").getAsString());

            categoryList.add(builder.toString());
        }

        categoryAdapter = new BoardCategoryAdapter(view.getContext(), this.categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    /**
     * 글
     */

    private void showBoardPost(View view) {
        String URL = "http://54.180.88.233/selectPost.php";

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "BoardFragment - showBoardPost() - onResponse : " + response);
                parseBoardPost(response, view, postList);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "BoardFragment - showBoardPost() - onErrorResponse : " + error);
                        return;
                    }
                });

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void parseBoardPost(String response, View view, ArrayList<Post> postList) {

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonPost = (JsonArray)jsonObject.get("board_post");

        for (int index=0; index<jsonPost.size(); index++) {
            JsonObject category = (JsonObject)jsonPost.get(index);

            boolean postNotice = false;
            boolean postCommentFlag = true;

            if (category.get("POST_NOTICE").getAsCharacter() == 'Y') {
                postNotice = true;
            }
            if (category.get("POST_CM_FLAG").getAsCharacter() == 'N') {
                postCommentFlag = false;
            }

            Post post = new Post(
                    category.get("POST_KEY").getAsInt(),
                    category.get("USER_ID").getAsString(),
                    category.get("CATEGORY_KEY").getAsInt(),
                    category.get("POST_TITLE").getAsString(),
                    category.get("POST_CONTENTS").getAsString(),
                    category.get("VIEW_CNT").getAsInt(),
                    category.get("RECOMMENT_CNT").getAsInt(),
                    postNotice,
                    postCommentFlag,
                    category.get("REG_DT").getAsString(),
                    category.get("UPD_DT").getAsString()
            );

            postList.add(post);
        }

        for (Post post : postList) {
            System.out.println(post.toString());
        }

        postAdapter = new BoardPostAdapter(view.getContext(), this.postList);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postRecyclerView.setAdapter(postAdapter);

        postAdapter.setOnPostClickListener(new PostClickListener() {
            @Override
            public void onClickPost(View view, int position) {
                Log.d(TAG, "BoardFragment - postPosition : " + position);
                Intent intent = new Intent(view.getContext(), PostActivity.class);
                intent.putExtra("postPosition", position);
                startActivity(intent);
            }
        });

    }


}

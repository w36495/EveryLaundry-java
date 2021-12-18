package com.w36495.everylaundry.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.w36495.everylaundry.api.CategoryAPI;
import com.w36495.everylaundry.api.PostAPI;
import com.w36495.everylaundry.BoardCategoryClickListener;
import com.w36495.everylaundry.RetrofitBuilder;
import com.w36495.everylaundry.MainActivity;
import com.w36495.everylaundry.domain.Category;
import com.w36495.everylaundry.PostActivity;
import com.w36495.everylaundry.PostAddActivity;
import com.w36495.everylaundry.PostClickListener;
import com.w36495.everylaundry.domain.Post;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.adapter.BoardCategoryAdapter;
import com.w36495.everylaundry.adapter.BoardPostAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class BoardFragment extends Fragment implements View.OnClickListener {
    private RecyclerView categoryRecyclerView;
    private RecyclerView postRecyclerView;
    private BoardCategoryAdapter categoryAdapter;
    private BoardPostAdapter postAdapter;

    private SwipeRefreshLayout boardSwipeLayout;
    private FloatingActionButton board_add_fab;
    private Button board_category_btn1, board_category_btn2, board_category_btn3, board_category_btn4, board_category_btn5;

    private RequestQueue requestQueue;

    private int lastPostKey = -1;   // 마지막 게시물 키
    public static ArrayList<Category> categoryList;
    private int categoryKey = 0;
    private ArrayList<Post> postList;

    // Retrofit2
    private Retrofit retrofit;
    private PostAPI postAPI;
    private CategoryAPI categoryAPI;

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

        retrofit = RetrofitBuilder.getClient();
        postAPI = retrofit.create(PostAPI.class);
        categoryAPI = retrofit.create(CategoryAPI.class);

        setInit(view);
        return view;
    }

    private void setInit(View view) {

        postRecyclerView = view.findViewById(R.id.board_post_recyclerView);
        board_add_fab = view.findViewById(R.id.board_add_fab);
        boardSwipeLayout = view.findViewById(R.id.board_swipeLayout);

        board_category_btn1 = view.findViewById(R.id.board_category_btn1);
        board_category_btn2 = view.findViewById(R.id.board_category_btn2);
        board_category_btn3 = view.findViewById(R.id.board_category_btn3);
        board_category_btn4 = view.findViewById(R.id.board_category_btn4);
        board_category_btn5 = view.findViewById(R.id.board_category_btn5);
        
        // 카테고리 클릭 이벤트
        board_category_btn1.setOnClickListener(this);
        board_category_btn2.setOnClickListener(this);
        board_category_btn3.setOnClickListener(this);
        board_category_btn4.setOnClickListener(this);
        board_category_btn5.setOnClickListener(this);


        // 게시물 새로고침
        boardSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(BoardFragment.this).attach(BoardFragment.this).commit();
                boardSwipeLayout.setRefreshing(false);
            }
        });


        // 세션
        loginID = MainActivity.getLoginUserID();


        // 카테고리 설정
        setBoardCategory();

        // 게시물
        setBoardPost(view);


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
     * 카테고리 목록 표시
     */
    private void setBoardCategory() {
        
        categoryAPI.getCategoryAll().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, retrofit2.Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = (ArrayList<Category>) response.body();
                    board_category_btn1.setText(categoryList.get(0).getCategoryTitle());
                    board_category_btn2.setText(categoryList.get(1).getCategoryTitle());
                    board_category_btn3.setText(categoryList.get(2).getCategoryTitle());
                    board_category_btn4.setText(categoryList.get(3).getCategoryTitle());
                    board_category_btn5.setText(categoryList.get(4).getCategoryTitle());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    /**
     * 게시물 목록
     * 
     * @param view
     */
    private void setBoardPost(View view) {
        postAPI.getPostList().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, retrofit2.Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isEmpty()) {
                        // 게시물 마지막 키
                        lastPostKey = response.body().size();
                        postList = (ArrayList<Post>) response.body();
                        postAdapter = new BoardPostAdapter(view.getContext(), postList, categoryList);
                        postRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        postRecyclerView.setAdapter(postAdapter);

                        // 게시물 클릭했을 때 이벤트
                        postAdapter.setOnPostClickListener(new PostClickListener() {
                            @Override
                            public void onClickPost(View view, int position) {
                                // 선택한 게시물
                                Post selectedPost = postList.get(position);
                                Retrofit retrofit = RetrofitBuilder.getClient();
                                PostAPI postAPI = retrofit.create(PostAPI.class);

                                // Recommend Setting
                                postAPI.insertPostRecommend(loginID, selectedPost.getPostKey()).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) { }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Timber.d("ERROR(insertPostRecommend) : " + t);
                                    }
                                });

                                // 게시물 작성자 != 로그인 사용자 => 조회수 업데이트/따봉 표시
                                if (loginID.equals(selectedPost.getPostWriter()) == false) {
                                    // 조회수 조회 -> 증가
                                    postAPI.updatePostViewCount(selectedPost.getPostKey()).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Timber.d("ERROR(updatePostViewCount) : " + t);
                                        }
                                    });

                                    postAPI.getPostRecommend(loginID, selectedPost.getPostKey()).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                            boolean isRecommend = false;
                                            if (response.isSuccessful() && response.body() != null) {
                                                // 로그인 한 사용자 != 글을 작성한 사용자
                                                if (!selectedPost.getPostWriter().equals(loginID)) {
                                                    // 추천을 눌렀다면
                                                    if (response.body() == "Y") {
                                                        isRecommend = true;
                                                    }
                                                    // 추천을 누르지 않았다면
                                                    else if (response.body() == "N") {
                                                        isRecommend = false;
                                                    }
                                                }
                                            }

                                            Intent intent = new Intent(view.getContext(), PostActivity.class);
                                            intent.putExtra("choicePostKey", selectedPost.getPostKey());
                                            intent.putExtra("choiceCategoryKey", selectedPost.getPostCategory());
                                            intent.putExtra("postWriter", selectedPost.getPostWriter());
                                            intent.putExtra("postRecommend", isRecommend);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Timber.d("ERROR(getPostRecommend) : " + t);
                                        }
                                    });
                                }


                                // 게시물 작성자 == 로그인 사용자
                                Intent intent = new Intent(view.getContext(), PostActivity.class);
                                intent.putExtra("choicePostKey", selectedPost.getPostKey());
                                intent.putExtra("choicePostCategoryKey", selectedPost.getPostCategory());
                                intent.putExtra("choicePostWriter", selectedPost.getPostWriter());
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.board_category_btn1:
                Toast.makeText(view.getContext(), "전체 카테고리", Toast.LENGTH_SHORT).show();
                categoryKey = 0;
                break;
            case R.id.board_category_btn2:
                Toast.makeText(view.getContext(), "세탁 카테고리", Toast.LENGTH_SHORT).show();
                categoryKey = 1;
                break;
            case R.id.board_category_btn3:
                Toast.makeText(view.getContext(), "수선 카테고리", Toast.LENGTH_SHORT).show();
                categoryKey = 2;
                break;
            case R.id.board_category_btn4:
                Toast.makeText(view.getContext(), "꿀팁 카테고리", Toast.LENGTH_SHORT).show();
                categoryKey = 3;
                break;
            case R.id.board_category_btn5:
                Toast.makeText(view.getContext(), "추천 카테고리", Toast.LENGTH_SHORT).show();
                categoryKey = 4;
                break;
        }

    }
}

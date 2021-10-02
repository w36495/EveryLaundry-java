package com.w36495.everylaundry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import timber.log.Timber;

public class BoardFragment extends Fragment {

    private RecyclerView categoryRecyclerView;
    private RecyclerView postRecyclerView;
    private BoardCategoryAdapter categoryAdapter;
    private BoardPostAdapter postAdapter;


    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayList<Post> postList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        Timber.plant(new Timber.DebugTree());

        setInit(view);
        return view;
    }

    private void setInit(View view) {

        categoryRecyclerView = view.findViewById(R.id.board_category_recyclerView);
        postRecyclerView = view.findViewById(R.id.board_post_recyclerView);

        categoryList.add("#수선");
        categoryList.add("#세탁");
        categoryList.add("#꿀팁");
        categoryList.add("#추천");

        categoryAdapter = new BoardCategoryAdapter(view.getContext(), categoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        postList.add(new Post(000001, "#수선", "이거 수선돼요???", "어떻게 해야해요?", "toby", "09/29", "33", "999"));
        postList.add(new Post(000001, "#수선", "이거 수선돼요???", "어떻게 해야해요?", "toby", "09/29", "33", "999"));
        postList.add(new Post(000001, "#수선", "이거 수선돼요???", "어떻게 해야해요?", "toby", "09/29", "33", "999"));

        postAdapter = new BoardPostAdapter(view.getContext(), postList);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postRecyclerView.setAdapter(postAdapter);

    }
}

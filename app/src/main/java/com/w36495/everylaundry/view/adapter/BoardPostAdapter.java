package com.w36495.everylaundry.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.view.listener.PostClickListener;
import com.w36495.everylaundry.data.domain.Category;
import com.w36495.everylaundry.data.domain.Post;
import com.w36495.everylaundry.R;

import java.util.ArrayList;

public class BoardPostAdapter extends RecyclerView.Adapter<BoardPostHolder> {

    private Context context;
    private ArrayList<Post> postList;
    private ArrayList<Category> categoryList;
    private PostClickListener postClickListener;

    public BoardPostAdapter(Context context, ArrayList<Post> postList, ArrayList<Category> categoryList) {
        this.context = context;
        this.postList = postList;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public BoardPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_post_list_item, parent, false);
        return new BoardPostHolder(view, postClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardPostHolder holder, int position) {
        int postCategory = postList.get(position).getPostCategory();
        for (int index = 0; index < categoryList.size(); index++) {
            if (postCategory == index) {
                holder.postListCategory.setText(categoryList.get(index).getCategoryTitle());
                break;
            }
        }

        holder.postListTitle.setText(postList.get(position).getPostTitle());
        holder.postListWriter.setText(postList.get(position).getPostWriter());
        holder.postListRegistDate.setText(postList.get(position).getPostRegistDate());
        holder.postListViewCount.setText(String.valueOf(postList.get(position).getPostViewCnt()));
        holder.postListRecommendCount.setText(String.valueOf(postList.get(position).getPostRecommentCnt()));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setOnPostClickListener(PostClickListener postClickListener) {
        this.postClickListener = postClickListener;
    }
}

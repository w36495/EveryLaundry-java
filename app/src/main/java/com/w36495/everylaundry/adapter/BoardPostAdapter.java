package com.w36495.everylaundry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.PostClickListener;
import com.w36495.everylaundry.data.Post;
import com.w36495.everylaundry.R;

import java.util.ArrayList;

public class BoardPostAdapter extends RecyclerView.Adapter<BoardPostHolder> {

    private Context context;
    private ArrayList<Post> postList;
    private PostClickListener postClickListener;

    public BoardPostAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public BoardPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_post_list_item, parent, false);
        return new BoardPostHolder(view, postClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardPostHolder holder, int position) {

        holder.postListCategory.setText(String.valueOf(postList.get(position).getPostCategory()));
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

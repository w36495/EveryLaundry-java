package com.w36495.everylaundry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardPostAdapter extends RecyclerView.Adapter<BoardPostHolder> {

    private Context context;
    private ArrayList<Post> postList;

    public BoardPostAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public BoardPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_post_item, parent, false);
        return new BoardPostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardPostHolder holder, int position) {
        holder.postCategory.setText(postList.get(position).getPostCategory());
        holder.postTitle.setText(postList.get(position).getPostTitle());
        holder.postWriter.setText(postList.get(position).getPostWriter());
        holder.postDate.setText(postList.get(position).getPostDate());
        holder.postViewCount.setText(postList.get(position).getPostViewCnt());
        holder.postRecommendCount.setText(postList.get(position).getPostRecommentCnt());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}

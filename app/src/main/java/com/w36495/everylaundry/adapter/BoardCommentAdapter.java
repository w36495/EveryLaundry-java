package com.w36495.everylaundry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.Comment;

import java.util.ArrayList;

public class BoardCommentAdapter extends RecyclerView.Adapter<BoardCommentHolder> {

    private Context context;
    private ArrayList<Comment> commentList;

    public BoardCommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public BoardCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_board_post_comment_item, parent, false);
        return new BoardCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardCommentHolder holder, int position) {
        holder.comment_nickNM.setText(commentList.get(position).getUserID());
        holder.comment_contents.setText(commentList.get(position).getCommentContents());
        holder.comment_date.setText(commentList.get(position).getCommentRegistDate());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}

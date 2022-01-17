package com.w36495.everylaundry.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.domain.Comment;

import java.util.ArrayList;

public class BoardCommentAdapter extends RecyclerView.Adapter<BoardCommentHolder> {

    private Context context;
    private ArrayList<Comment> commentList;

    private String postWriter;

    public BoardCommentAdapter(Context context, ArrayList<Comment> commentList, String postWriter) {
        this.context = context;
        this.commentList = commentList;
        this.postWriter = postWriter;
    }

    @NonNull
    @Override
    public BoardCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_board_post_comment_item, parent, false);
        return new BoardCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardCommentHolder holder, int position) {
        // 댓글작성자와 글 작성자가 일치하지 않으면 공백
        if (!postWriter.equals(commentList.get(position).getUserID())) {
            holder.comment_flag.setText("");
        }
        // 일치하면 "작성자" 표시
        else {
            holder.comment_flag.setText("(작성자)");
        }
        holder.comment_nickNM.setText(commentList.get(position).getUserID());
        holder.comment_contents.setText(commentList.get(position).getCommentContents());
        holder.comment_date.setText(commentList.get(position).getCommentRegistDate());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}

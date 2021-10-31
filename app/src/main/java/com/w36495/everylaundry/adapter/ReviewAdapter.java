package com.w36495.everylaundry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {

    private Context context;
    private ArrayList<Review> reviewList;

    public ReviewAdapter(Context context, ArrayList<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_board_post_comment_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        holder.comment_nickNM.setText(reviewList.get(position).getReviewUserID());
        holder.comment_contents.setText(reviewList.get(position).getReviewContents());
        holder.comment_date.setText(reviewList.get(position).getReviewRegistDate());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

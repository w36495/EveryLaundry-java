package com.w36495.everylaundry;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class BoardPostHolder extends RecyclerView.ViewHolder {

    protected TextView postCategory;
    protected TextView postTitle;
    protected TextView postWriter;
    protected TextView postDate;
    protected TextView postViewCount;
    protected TextView postRecommendCount;

    public BoardPostHolder(@NonNull View itemView) {
        super(itemView);

        postCategory = itemView.findViewById(R.id.post_category);
        postTitle = itemView.findViewById(R.id.post_title);
        postWriter = itemView.findViewById(R.id.post_writer);
        postDate = itemView.findViewById(R.id.post_date);
        postViewCount = itemView.findViewById(R.id.post_view_count);
        postRecommendCount = itemView.findViewById(R.id.post_recommend_count);
    }
}

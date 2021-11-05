package com.w36495.everylaundry.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.PostActivity;
import com.w36495.everylaundry.PostClickListener;
import com.w36495.everylaundry.R;

import org.w3c.dom.Text;

import timber.log.Timber;

public class BoardPostHolder extends RecyclerView.ViewHolder {

    protected TextView postListCategory;
    protected TextView postListTitle;
    protected TextView postListWriter;
    protected TextView postListRegistDate;
    protected TextView postListViewCount;
    protected TextView postListRecommendCount;

    public BoardPostHolder(@NonNull View itemView, PostClickListener postClickListener) {
        super(itemView);

        postListCategory = itemView.findViewById(R.id.post_list_category);
        postListTitle = itemView.findViewById(R.id.post_list_title);
        postListWriter = itemView.findViewById(R.id.post_list_writer);
        postListRegistDate = itemView.findViewById(R.id.post_list_date);
        postListViewCount = itemView.findViewById(R.id.post_list_view_count);
        postListRecommendCount = itemView.findViewById(R.id.post_list_recommend_count);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAbsoluteAdapterPosition();
                postClickListener.onClickPost(view, position);
            }
        });
    }
}

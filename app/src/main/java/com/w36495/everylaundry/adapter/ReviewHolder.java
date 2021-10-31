package com.w36495.everylaundry.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;


public class ReviewHolder extends RecyclerView.ViewHolder {

    protected TextView comment_date;
    protected TextView comment_nickNM;
    protected TextView comment_contents;
    protected TextView comment_flag;

    public ReviewHolder(@NonNull View itemView) {
        super(itemView);

        comment_nickNM = itemView.findViewById(R.id.comment_nickNM);
        comment_contents = itemView.findViewById(R.id.comment_contents);
        comment_date = itemView.findViewById(R.id.comment_date);
        comment_flag = itemView.findViewById(R.id.comment_flag);

        comment_flag.setVisibility(View.INVISIBLE);
    }
}

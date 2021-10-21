package com.w36495.everylaundry.adapter;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;

public class BoardCategoryHolder extends RecyclerView.ViewHolder {

    protected Button board_category;

    public BoardCategoryHolder(@NonNull View itemView) {
        super(itemView);

        board_category = itemView.findViewById(R.id.board_category);
    }
}

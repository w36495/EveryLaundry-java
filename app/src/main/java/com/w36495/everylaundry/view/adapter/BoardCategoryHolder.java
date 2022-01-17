package com.w36495.everylaundry.view.adapter;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.view.listener.BoardCategoryClickListener;
import com.w36495.everylaundry.R;

public class BoardCategoryHolder extends RecyclerView.ViewHolder{

    protected Button board_category;

    public BoardCategoryHolder(@NonNull View itemView, BoardCategoryClickListener boardCategoryClickListener) {
        super(itemView);

        board_category = itemView.findViewById(R.id.board_category);

        itemView.setOnClickListener(view -> {
            int position = getAbsoluteAdapterPosition();
            boardCategoryClickListener.onBoardCategoryClicked(view, position);
        });
    }

}

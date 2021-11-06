package com.w36495.everylaundry.adapter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.BoardCategoryClickListener;
import com.w36495.everylaundry.R;

import timber.log.Timber;

public class BoardCategoryHolder extends RecyclerView.ViewHolder{

    protected Button board_category;
    protected BoardCategoryClickListener boardCategoryClickListener;

    public BoardCategoryHolder(@NonNull View itemView, BoardCategoryClickListener boardCategoryClickListener) {
        super(itemView);

        board_category = itemView.findViewById(R.id.board_category);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAbsoluteAdapterPosition();
                boardCategoryClickListener.onBoardCategoryClicked(view, position);
            }
        });
    }

}

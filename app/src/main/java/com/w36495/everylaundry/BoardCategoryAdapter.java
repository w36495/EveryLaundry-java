package com.w36495.everylaundry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardCategoryAdapter extends RecyclerView.Adapter<BoardCategoryHolder> {

    private Context context;
    private ArrayList<String> categoryList;

    public BoardCategoryAdapter(Context context, ArrayList<String> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public BoardCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_board_category_item, parent, false);
        return new BoardCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardCategoryHolder holder, int position) {
        holder.board_category.setText(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}

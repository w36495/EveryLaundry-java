package com.w36495.everylaundry.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.domain.Laundry;

import java.util.ArrayList;

/**
 * MainActivity -> LikeFragment -> RecyclerView - LaundryLikeAdapter
 */
public class LaundryLikeAdapter extends RecyclerView.Adapter<LaundryLikeHolder> {

    private Context context;
    private ArrayList<Laundry> laundryList;

    public LaundryLikeAdapter(Context context, ArrayList<Laundry> laundryList) {
        this.context = context;
        this.laundryList = laundryList;
    }

    @NonNull
    @Override
    public LaundryLikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_like_list_item, parent, false);
        return new LaundryLikeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaundryLikeHolder holder, int position) {
        holder.like_pin.setImageResource(R.drawable.ic_baseline_push_pin_24);
        holder.like_name.setText(laundryList.get(position).getLaundryName());
        holder.like_address.setText(laundryList.get(position).getLaundryAddress());
        holder.like_tel.setText(laundryList.get(position).getLaundryTel());
    }

    @Override
    public int getItemCount() {
        return laundryList.size();
    }

}

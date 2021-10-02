package com.w36495.everylaundry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * MainActivity -> SearchFragment -> RecyclerView - LaundrySearchAdapter
 */
public class LaundrySearchAdapter extends RecyclerView.Adapter<LaundrySearchHolder> {

    private Context context;
    private ArrayList<Laundry> laundryList;

    public LaundrySearchAdapter(Context context, ArrayList<Laundry> laundryList) {
        this.context = context;
        this.laundryList = laundryList;
    }

    @NonNull
    @Override
    public LaundrySearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_list_item, parent, false);
        return new LaundrySearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaundrySearchHolder holder, int position) {
        holder.search_name.setText(laundryList.get(position).getLaundryName());
        holder.search_tel.setText(laundryList.get(position).getLaundryTel());
        holder.search_address.setText(laundryList.get(position).getLaundryAddress());
        holder.search_zip_code.setText(laundryList.get(position).getLaundryZipCode());
    }

    @Override
    public int getItemCount() {
        return laundryList.size();
    }
}

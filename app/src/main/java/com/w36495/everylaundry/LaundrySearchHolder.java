package com.w36495.everylaundry;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LaundrySearchHolder extends RecyclerView.ViewHolder {

    protected TextView search_distance;
    protected TextView search_name;
    protected TextView search_zip_code;
    protected TextView search_address;
    protected TextView search_tel;

    public LaundrySearchHolder(@NonNull View itemView) {
        super(itemView);

        search_distance = itemView.findViewById(R.id.search_distance);
        search_name = itemView.findViewById(R.id.search_name);
        search_zip_code = itemView.findViewById(R.id.search_zip_code);
        search_address = itemView.findViewById(R.id.search_address);
        search_tel = itemView.findViewById(R.id.search_tel);
    }
}

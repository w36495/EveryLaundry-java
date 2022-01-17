package com.w36495.everylaundry.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LaundryLikeHolder extends RecyclerView.ViewHolder {

    protected ImageView like_pin;
    protected TextView like_name;
    protected TextView like_address;
    protected TextView like_tel;
    protected ImageView like_check;

    public LaundryLikeHolder(@NonNull View itemView) {
        super(itemView);

        like_pin = itemView.findViewById(R.id.like_pin);
        like_name = itemView.findViewById(R.id.like_name);
        like_address = itemView.findViewById(R.id.like_address);
        like_tel = itemView.findViewById(R.id.like_tel);
        like_check = itemView.findViewById(R.id.like_check);
    }
}

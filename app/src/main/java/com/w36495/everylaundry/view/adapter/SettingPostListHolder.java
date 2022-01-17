package com.w36495.everylaundry.view.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;

public class SettingPostListHolder extends RecyclerView.ViewHolder {

    protected TextView setting_post_item_title;
    protected TextView setting_post_item_category;
    protected TextView setting_post_item_date;

    public SettingPostListHolder(@NonNull View itemView) {
        super(itemView);

        setting_post_item_title = itemView.findViewById(R.id.setting_post_item_title);
        setting_post_item_category = itemView.findViewById(R.id.setting_post_item_category);
        setting_post_item_date = itemView.findViewById(R.id.setting_post_item_date);
    }
}

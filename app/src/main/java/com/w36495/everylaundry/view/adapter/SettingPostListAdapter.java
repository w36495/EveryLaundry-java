package com.w36495.everylaundry.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.domain.Post;

import java.util.ArrayList;

public class SettingPostListAdapter extends RecyclerView.Adapter<SettingPostListHolder> {

    private Context context;
    private ArrayList<Post> settingPostList;
    private ArrayList<String> categoryList;

    public SettingPostListAdapter(Context context, ArrayList<Post> settingPostList) {
        this.context = context;
        this.settingPostList = settingPostList;
//        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public SettingPostListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_setting_post_list_item, parent, false);

        return new SettingPostListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingPostListHolder holder, int position) {
//        String category = null;
//        for (int index = 0; index < categoryList.size(); index++) {
//            if (settingPostList.get(position).getPostCategory() == index) {
//                category = categoryList.get(index);
//            }
//        }

        holder.setting_post_item_title.setText(settingPostList.get(position).getPostTitle());
        holder.setting_post_item_category.setText(String.valueOf(settingPostList.get(position).getPostCategory()));
        holder.setting_post_item_date.setText(settingPostList.get(position).getPostRegistDate());
    }

    @Override
    public int getItemCount() {
        return settingPostList.size();
    }
}

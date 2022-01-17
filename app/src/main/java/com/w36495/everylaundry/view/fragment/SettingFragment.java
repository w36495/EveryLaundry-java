package com.w36495.everylaundry.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.w36495.everylaundry.view.LoginActivity;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.view.SettingPostListActivity;
import com.w36495.everylaundry.view.SettingUpdateActivity;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private TextView setting_userNickName;
    private Button setting_btn_update, setting_post_list, setting_review_list, setting_faq, setting_app_info;

    private String loginID;
    private String loginNickNM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginID = LoginActivity.loginID;
        loginNickNM = LoginActivity.loginNickNm;
        // 닉네임 설정
        setting_userNickName.setText(loginNickNM);

        setting_userNickName = view.findViewById(R.id.setting_userNickName);
        setting_btn_update = view.findViewById(R.id.setting_btn_update);
        setting_post_list = view.findViewById(R.id.setting_post_list);
        setting_review_list = view.findViewById(R.id.setting_review_list);
        setting_faq = view.findViewById(R.id.setting_faq);
        setting_app_info = view.findViewById(R.id.setting_app_info);

        setting_btn_update.setOnClickListener(this);
        setting_post_list.setOnClickListener(this);
        setting_review_list.setOnClickListener(this);
        setting_faq.setOnClickListener(this);
        setting_app_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.setting_btn_update:
                intent = new Intent(view.getContext(), SettingUpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_post_list:
                intent = new Intent(view.getContext(), SettingPostListActivity.class);
                startActivity(intent);
                break;
        }
    }
}

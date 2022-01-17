package com.w36495.everylaundry.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.w36495.everylaundry.BuildConfig;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.domain.User;
import com.w36495.everylaundry.data.network.UserAPI;
import com.w36495.everylaundry.data.network.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class SettingUpdateActivity extends AppCompatActivity {

    private EditText setting_user_id, setting_user_pw, setting_user_mobile, setting_user_nickNM, setting_user_email;
    private Button setting_user_save_btn;

    private String loginID;

    private Retrofit retrofit;
    private UserAPI userAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_update);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit();
    }

    private void setInit() {
        // 현재 로그인된 사용자
        loginID = LoginActivity.loginID;

        retrofit = RetrofitBuilder.getClient();
        userAPI = retrofit.create(UserAPI.class);

        setting_user_id = findViewById(R.id.setting_user_id);
        setting_user_pw = findViewById(R.id.setting_user_pw);
        setting_user_email = findViewById(R.id.setting_user_email);
        setting_user_mobile = findViewById(R.id.setting_user_mobile);
        setting_user_nickNM = findViewById(R.id.setting_user_nickNM);
        setting_user_save_btn = findViewById(R.id.setting_user_save_btn);

        setting_user_id.setEnabled(false);



        // 사용자 정보 가져오기
        setUserInfo(userAPI);

        setting_user_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPW = setting_user_pw.getText().toString();
                String userMobile = setting_user_mobile.getText().toString();
                String userEmail = setting_user_email.getText().toString();
                String userNickNM = setting_user_nickNM.getText().toString();

                userAPI.updateUser(loginID, userPW, userMobile, userNickNM, userEmail).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SettingUpdateActivity.this, MainActivity.class);
                            intent.putExtra("SettingUpdateActivity", "SettingUpdateActivity");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Timber.d("ERROR(updateUser) : " + t.getMessage());
                    }
                });

            }
        });
    }

    /**
     * 회원 정보 가져오기
     */
    private void setUserInfo(UserAPI userAPI) {

        userAPI.selectUser(loginID).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();

                    setting_user_id.setText(user.getUserId());
                    setting_user_nickNM.setText(user.getUserNickname());
                    setting_user_mobile.setText(user.getUserMobile());
                    setting_user_email.setText(user.getUserEmail());
                    setting_user_pw.setText(user.getUserPassword());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Timber.d("ERROR(selectUser) : " + t.getMessage());
            }
        });
    }
}

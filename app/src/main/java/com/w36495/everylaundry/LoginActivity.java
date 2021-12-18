package com.w36495.everylaundry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.w36495.everylaundry.domain.User;
import com.w36495.everylaundry.api.UserAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    private Button login_btn, login_btn_signup;
    private EditText  login_id, login_password;

    private SharedPreferences sharedPreferences;


    
    public static String loginID = null;
    public static String loginNickNm = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit();
    }

    private void setInit() {

        login_btn = findViewById(R.id.login_btn);
        login_btn_signup = findViewById(R.id.login_btn_singup);
        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);

        // 로그인
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = RetrofitBuilder.getClient();
                UserAPI userAPI = retrofit.create(UserAPI.class);

                String userId = login_id.getText().toString();
                String userPassword = login_password.getText().toString();

                userAPI.selectUser(userId).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();
                        
                            if (user.getUserPassword().equals(userPassword)) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                // 현재 로그인한 회원의 아이디와 닉네임
                                loginID = user.getUserId();
                                loginNickNm = user.getUserNickname();
                                Toast.makeText(LoginActivity.this, "로그인되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "아이디 및 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "아이디 및 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Timber.d("ERROR(selectUser) : " + t);
                    }
                });
                
            }
        });

        // 회원가입
        login_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                Timber.d("signup() 호출");
            }
        });
    }
}

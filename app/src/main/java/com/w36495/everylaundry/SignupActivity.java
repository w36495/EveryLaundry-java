package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.BuildConfig;
import com.w36495.everylaundry.api.UserAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class SignupActivity extends AppCompatActivity {

    private Button signup_btn;
    private EditText signup_id, signup_password, signup_nickname, signup_email, signup_mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        
        Timber.d("onCreate() 호출");

        setInit();
    }

    private void setInit() {

        signup_btn = findViewById(R.id.signup_btn);
        signup_id = findViewById(R.id.signup_id);
        signup_password = findViewById(R.id.signup_password);
        signup_nickname = findViewById(R.id.signup_nick_name);
        signup_email = findViewById(R.id.signup_email);
        signup_mobile = findViewById(R.id.signup_mobile);


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = signup_id.getText().toString();
                String userPW = signup_password.getText().toString();
                String userNickNM = signup_nickname.getText().toString();
                String userEmail = signup_email.getText().toString();
                String userMobile = signup_mobile.getText().toString();

                Retrofit retrofit = RetrofitBuilder.getClient();
                UserAPI userAPI = retrofit.create(UserAPI.class);

                userAPI.signup(userID, userPW, userMobile, userNickNM, userEmail).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Timber.d("ERROR(signup) : " + t);
                    }
                });
            }
        });
    }
}

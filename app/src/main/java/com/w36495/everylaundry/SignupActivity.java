package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private Button signup_btn;
    private EditText signup_id, signup_password, signup_nickname, signup_email, signup_mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Log.d("SignupActivity : ", "onCreate() 호출");

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

                InsertData insertUser = new InsertData();
                insertUser.execute("http://54.180.88.233/insert.php"
                        , userID, userPW, userNickNM, userEmail, userMobile);

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }



}

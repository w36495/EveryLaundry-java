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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginActivity extends AppCompatActivity {

    private Button login_btn, login_btn_signup;
    private EditText login_id, login_password;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        setInit();
    }

    private void setInit() {

        login_btn = findViewById(R.id.login_btn);
        login_btn_signup = findViewById(R.id.login_btn_singup);
        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = login_id.getText().toString();
                String userPassword = login_password.getText().toString();


                String URL = DatabaseInfo.loginURL;

                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("로그", "응답 : " + response);
                        parseResponse(response, userId, userPassword);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // 에러나면 error로 들어옴
                                Log.d("로그", "로그인 오류 : "+error.getMessage());
                            }
                        });

                request.setShouldCache(false);
                requestQueue.add(request);

            }
        });

        login_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                Log.d("LoginActivity : ", "signup 호출()");
            }
        });





    }

    public void parseResponse(String response, String userID, String userPW) {

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonUser = (JsonArray) jsonObject.get("users");

        // todo: jsonUser.get(index) 여기 index부분을 아이디와 일치하는 사람으로 찾아야할것같은데 고민해보자
        JsonObject userObject = (JsonObject) jsonUser.get(0);
        String DB_userID = userObject.get("USER_ID").getAsString();
        String DB_userPASSWD = userObject.get("USER_PASSWD").getAsString();


        if (DB_userID.equals(userID)) {
            if (DB_userPASSWD.equals(userPW)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "아이디를 확인하세요.", Toast.LENGTH_SHORT).show();
        }


    }

}

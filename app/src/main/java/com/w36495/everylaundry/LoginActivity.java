package com.w36495.everylaundry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.User;

import java.net.CookieManager;

import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    private Button login_btn, login_btn_signup;
    private EditText login_id, login_password;

    private RequestQueue requestQueue;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

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

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = login_id.getText().toString();
                String userPassword = login_password.getText().toString();

                String URL = DatabaseInfo.loginURL;

                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.d("onResponse() 응답 : " + response);
                        parseResponse(response, userId, userPassword);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // 에러나면 error로 들어옴
                                Timber.d("onErrorResponse() 로그인 오류 : " + error.getMessage());
                                error.printStackTrace();
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
                Timber.d("signup() 호출");
            }
        });
    }

    public void parseResponse(String response, String userID, String userPW) {

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(response);
        JsonArray jsonUser = (JsonArray) jsonObject.get("userInfo");
        Timber.d("jsonUser의 size() : " + jsonUser.size());
        Timber.d("parseResponse() userID : " + userID);

        JsonObject userInfo = new JsonObject();

        for (int index=0; index<jsonUser.size(); index++) {
            userInfo = (JsonObject) jsonUser.get(index);
            Timber.d("사용자 아이디 : " + userInfo.get("USER_ID").getAsString());
            // 아이디가 같다면
            if (userID.equals(userInfo.get("USER_ID").getAsString())) {
                Timber.d("DB아이디 : " + userInfo.get("USER_ID") + "\n로그인하는 아이디 : " + userID);
                break;
            }
        }

        String session = userInfo.get("SESSION").getAsString();
        String DB_userID = userInfo.get("USER_ID").getAsString();
        String DB_userPASSWD = userInfo.get("USER_PASSWD").getAsString();


        if (DB_userID.equals(userID)) {
            if (DB_userPASSWD.equals(userPW)) {
                sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("session", session);
                editor.putString("loginID", userInfo.get("USER_ID").getAsString());
                editor.putString("loginNickNM", userInfo.get("USER_NICKNM").getAsString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                editor.commit();
                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                System.out.println("세션저장 성공 : " + sharedPreferences.getAll());
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

package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.api.UpdateUser;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.User;

import java.io.DataInput;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class SettingUpdateActivity extends AppCompatActivity {

    private EditText setting_user_id, setting_user_pw, setting_user_mobile, setting_user_nickNM, setting_user_email;
    private Button setting_user_save_btn;

    private String loginID = null;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_update);

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit();
    }

    private void setInit() {
        // 현재 로그인된 사용자
        loginID = MainActivity.getLoginUserID();

        setting_user_id = findViewById(R.id.setting_user_id);
        setting_user_pw = findViewById(R.id.setting_user_pw);
        setting_user_email = findViewById(R.id.setting_user_email);
        setting_user_mobile = findViewById(R.id.setting_user_mobile);
        setting_user_nickNM = findViewById(R.id.setting_user_nickNM);
        setting_user_save_btn = findViewById(R.id.setting_user_save_btn);

        setting_user_id.setText(loginID);
        setting_user_id.setEnabled(false);

        // 사용자 정보 가져오기
        setUserInfo();

        setting_user_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPW = setting_user_pw.getText().toString();
                String userMobile = setting_user_mobile.getText().toString();
                String userEmail = setting_user_email.getText().toString();
                String userNickNM = setting_user_nickNM.getText().toString();

                UpdateUser updateUser = new UpdateUser();
                updateUser.execute(DatabaseInfo.updateUserURL, loginID, userPW, userMobile, userNickNM, userEmail);
                Toast.makeText(getApplicationContext(), "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SettingUpdateActivity.this, MainActivity.class);
                intent.putExtra("SettingUpdateActivity", "SettingUpdateActivity");
                startActivity(intent);
            }
        });
    }

    private void setUserInfo() {

        String URL = DatabaseInfo.selectUserURL;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseUserInfo(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", loginID);
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);

    }

    private void parseUserInfo(String response) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        JsonArray jsonUser = (JsonArray) jsonObject.get("user");
        JsonObject user = (JsonObject) jsonUser.get(0);

        setting_user_nickNM.setText(user.get("USER_NICKNM").getAsString());
        setting_user_mobile.setText(user.get("USER_MOBILE").getAsString());
        setting_user_email.setText(user.get("USER_EMAIL").getAsString());
        setting_user_pw.setText(user.get("USER_PASSWD").getAsString());
    }
}

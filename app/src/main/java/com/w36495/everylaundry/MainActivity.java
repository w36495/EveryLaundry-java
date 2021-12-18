package com.w36495.everylaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.w36495.everylaundry.fragment.BoardFragment;
import com.w36495.everylaundry.fragment.LikeFragment;
import com.w36495.everylaundry.fragment.SearchFragment;
import com.w36495.everylaundry.fragment.SettingFragment;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private Fragment searchFragment;
    private Fragment likeFragment;
    private Fragment boardFragment;
    private Fragment settingFragment;

    private BottomNavigationView bottomNav;

    private static String loginID = null;
    private static String loginNickNM = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());
        Timber.d("onCreate() 호출");

        setLoginUserID(LoginActivity.loginID);
        setLoginUserNickNM(LoginActivity.loginNickNm);

        setInit();
    }

    private void setInit() {

        searchFragment = new SearchFragment();
        likeFragment = new LikeFragment();
        boardFragment = new BoardFragment();
        settingFragment = new SettingFragment();

        bottomNav = findViewById(R.id.main_bottomNav);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, searchFragment).commit();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_list:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, searchFragment).commit();
                        return true;
                    case R.id.nav_like:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, likeFragment).commit();
                        return true;
                    case R.id.nav_board:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, boardFragment).commit();
                        return true;
                    case R.id.nav_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, settingFragment).commit();
                        return true;

                }
                return false;

            }
        });

        // 게시물 추가했을 때 화면이동시키기
        Intent intent = getIntent();
        if (intent.hasExtra("PostAddActivity") || intent.hasExtra("PostActivity")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, boardFragment).commit();
            Timber.d("BoardFragment() 호출");
        }
        if (intent.hasExtra("SettingUpdateActivity")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, settingFragment).commit();
            Timber.d("SettingFragment() 호출");
        }


    }

    /**
     * 현재 로그인 한 사용자의 아이디
     * @return
     */
    public static String getLoginUserID() {
        return loginID;
    }

    private void setLoginUserID(String loginID) {
        this.loginID = loginID;
    }

    /**
     * 현재 로그인 한 사용자의 닉네임
     * @return
     */
    public static String getLoginUserNickNM() {
        return loginNickNM;
    }

    private void setLoginUserNickNM(String loginNickNM) {
        this.loginNickNM = loginNickNM;
    }

    /**
     * 생명주기
     */
    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("onStart() 호출");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume() 호출");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("onPause() 호출");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("onStop() 호출");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy() 호출");
    }


}
package com.w36495.everylaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());
        Timber.d("onCreate() 호출");

        setInit();
    }

    private void setInit() {

        SharedPreferences sharedPreferences = getSharedPreferences("session", 0);
        String userID = sharedPreferences.getString("userID", "");
        String userSession = sharedPreferences.getString("session", "");


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
                        break;
                    case R.id.nav_like:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, likeFragment).commit();
                        break;
                    case R.id.nav_board:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, boardFragment).commit();
                        break;
                    case R.id.nav_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, settingFragment).commit();
                        break;

                }
                return false;

            }
        });


        // 게시물 추가했을 때 화면이동시키기
        Intent intent = getIntent();
        if (intent.hasExtra("PostAddActivity")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, boardFragment).commit();
            Timber.d("BoardFragment() 호출");
        }



    }

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
        SharedPreferences sharedPreferences = getSharedPreferences("session", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        System.out.println("===============종료될때 세션값보기");
        sharedPreferences.getAll();
        Timber.d("onDestroy() 호출");
    }


}
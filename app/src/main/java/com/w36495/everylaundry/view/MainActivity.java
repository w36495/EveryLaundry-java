package com.w36495.everylaundry.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.view.fragment.BoardFragment;
import com.w36495.everylaundry.view.fragment.LikeFragment;
import com.w36495.everylaundry.view.fragment.SearchFragment;
import com.w36495.everylaundry.view.fragment.SettingFragment;

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
            bottomNav.setSelectedItemId(R.id.nav_board);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, boardFragment).commit();
            Timber.d("BoardFragment() 호출");
        }
        if (intent.hasExtra("SettingUpdateActivity")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, settingFragment).commit();
            Timber.d("SettingFragment() 호출");
        }

    }

}
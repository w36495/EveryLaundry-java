package com.w36495.everylaundry;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import timber.log.Timber;

public class LaundryMenuDialog extends Dialog implements View.OnClickListener {
    private final int ALL_LAUNDRY = 0;
    private final int COIN_LAUNDRY = 1;
    private final int NORMAL_LAUNDRY = 2;

    private Context context;
    private TextView map_menu_all, map_menu_coin, map_menu_normal;
    private MapMenuClickListener menuClickListener;

    public LaundryMenuDialog(@NonNull Context context, MapMenuClickListener menuClickListener) {
        super(context);
        this.context = context;
        this.menuClickListener = menuClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_laundry_map_menu);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.d("onCreate() 호출");

        setInit();
    }

    private void setInit() {

        map_menu_all = findViewById(R.id.map_menu_all);
        map_menu_coin = findViewById(R.id.map_menu_coin);
        map_menu_normal = findViewById(R.id.map_menu_normal);

        map_menu_all.setOnClickListener(this);
        map_menu_coin.setOnClickListener(this);
        map_menu_normal.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.map_menu_all:
                menuClickListener.onClickedMenu(ALL_LAUNDRY);
                dismiss();
                break;
            case R.id.map_menu_coin:
                menuClickListener.onClickedMenu(COIN_LAUNDRY);
                dismiss();
                break;
            case R.id.map_menu_normal:
                menuClickListener.onClickedMenu(NORMAL_LAUNDRY);
                dismiss();
                break;
        }
    }


}

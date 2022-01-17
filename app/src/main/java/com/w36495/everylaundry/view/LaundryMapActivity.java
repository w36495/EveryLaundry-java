package com.w36495.everylaundry.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.BuildConfig;
import com.w36495.everylaundry.R;
import com.w36495.everylaundry.data.network.LaundryAPI;
import com.w36495.everylaundry.data.domain.Laundry;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.w36495.everylaundry.data.network.RetrofitBuilder;
import com.w36495.everylaundry.view.listener.MapMenuClickListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * 세탁소 맵 보여지는 액티비티
 */
public class LaundryMapActivity extends AppCompatActivity implements MapView.POIItemEventListener, View.OnClickListener {

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private FloatingActionButton fab_zoomIn, fab_zoomOut, fab_gps;
    private ImageButton map_view_back_btn, map_view_menu_btn;
    private TextView map_view_app_name;
    private MapMenuClickListener menuClickListener;

    private ArrayList<Laundry> laundryList;
    private String loginID;

    private Double networkLatitude = 0.0;
    private Double networkLongitude = 0.0;
    private int showLaundryListType = 0;    // 세탁소 구분(0:전체, 1:코인, 2:일반)

    private Retrofit retrofit = RetrofitBuilder.getClient();
    private LaundryAPI laundryAPI = retrofit.create(LaundryAPI.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_map);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit();
    }


    private void setInit() {
        loginID = LoginActivity.loginID;

        mapView = new MapView(this);

        map_view_app_name = findViewById(R.id.map_view_app_name);
        map_view_back_btn = findViewById(R.id.map_view_back_btn);
        map_view_menu_btn = findViewById(R.id.map_view_menu_btn);
        fab_zoomIn = findViewById(R.id.fab_zoomIn);
        fab_zoomOut = findViewById(R.id.fab_zoomOut);
        fab_gps = findViewById(R.id.fab_gps);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setPOIItemEventListener(this);

        // 처음 지도 켰을때 현재 위치로 지정
        getGps();
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(networkLatitude, networkLongitude), true);

        // 줌 레벨 설정
        mapView.setZoomLevel(1, true);

        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        // 줌 변경 버튼
        fab_zoomIn.setOnClickListener(this);
        fab_zoomOut.setOnClickListener(this);
        // GPS 버튼
        fab_gps.setOnClickListener(this);
        map_view_app_name.setOnClickListener(this);
        map_view_back_btn.setOnClickListener(this);

        // 세탁소 정보 호출
        getLaundryInfo();

        // 오른쪽 상단 메뉴를 통해 코인세탁소/일반세탁소 구분
        menuClickListener = new MapMenuClickListener() {
            @Override
            public void onClickedMenu(int laundryType) {
                // 전체
                if (laundryType == 0) {
                    Toast.makeText(LaundryMapActivity.this, "전체", Toast.LENGTH_SHORT).show();
                    showLaundryListType = 0;
                }
                // 코인세탁소
                else if (laundryType == 1) {
                    Toast.makeText(LaundryMapActivity.this, "코인세탁소", Toast.LENGTH_SHORT).show();
                    showLaundryListType = 1;
                }
                // 일반세탁소
                else {
                    Toast.makeText(LaundryMapActivity.this, "일반세탁소", Toast.LENGTH_SHORT).show();
                    showLaundryListType = 2;
                }
            }
        };

        map_view_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaundryMenuDialog menuDialog = new LaundryMenuDialog(LaundryMapActivity.this, menuClickListener);
                // 타이틀 제거
                menuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                menuDialog.show();
            }
        });


    }

    /**
     * 세탁소 정보
     */
    private void getLaundryInfo() {
        laundryAPI.getLaundryInfo().enqueue(new Callback<List<Laundry>>() {
            @Override
            public void onResponse(Call<List<Laundry>> call, retrofit2.Response<List<Laundry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    laundryList = (ArrayList<Laundry>) response.body();

                    setMapViewMarker(laundryList);
                }
            }

            @Override
            public void onFailure(Call<List<Laundry>> call, Throwable t) {
                Timber.d("ERROR(getLaundryInfo) : " + t.getMessage());
            }
        });

    }

    /**
     * 지도 확대/축소
     * 확대 = true, 축소 = false
     * @param isZoomIn
     */
    private void setZoomInOut(boolean isZoomIn) {
        int zoomLevel = mapView.getZoomLevel();
        if (isZoomIn == true) {
            mapView.setZoomLevel(--zoomLevel, true);
        } else {
            mapView.setZoomLevel(++zoomLevel, true);
        }
    }

    /**
     * DB에 저장되어 있는 세탁소 정보들 마커로 표시
     * @param DB_laundryList
     */
    private void setMapViewMarker(ArrayList<Laundry> DB_laundryList) {

        // 마커
        for (int index = 0; index < DB_laundryList.size(); index++) {
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(DB_laundryList.get(index).getLaundryCoordsX(), DB_laundryList.get(index).getLaundryCoordsY());
            marker.setItemName(String.valueOf(DB_laundryList.get(index).getLaundryName()));
            marker.setTag(index);
            marker.setMapPoint(mapPoint);

            // 코인세탁소 -> 노란색 마커
            if (Integer.valueOf(DB_laundryList.get(index).getLaundryType()) == 0) {
                marker.setMarkerType(MapPOIItem.MarkerType.YellowPin);
            }
            // 일반세탁소 -> 파란색 마커
            else if (Integer.valueOf(DB_laundryList.get(index).getLaundryType()) == 1){
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            }

            mapView.addPOIItem(marker);
        }

    }

    /**
     * 지도의 마커를 클릭했을 때
     * 1) 하단에 선택된 세탁소의 정보 표시
     * 2) 사용자가 선택한 세탁소 DB에 저장
     * @param mapView
     * @param mapPOIItem
     */
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        int laundryKey = mapPOIItem.getTag();
        Laundry laundry = laundryList.get(laundryKey);

        // DB의 LAUNDRY_DEATIL에 삽입하기
        laundryAPI.insertLaundryLike(loginID, laundryKey).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Timber.d("ERROR(insertLaundryLike) : " + t.getMessage());
            }
        });

        // 좋아요(즐겨찾기)를 했는지 안했는지 얻기
        laundryAPI.getLaundryLike(loginID, laundryKey).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                boolean likeFlag = false;
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().equals("Y")) {
                        likeFlag = true;
                    }

                    // 하단에 세탁소 정보 다이얼로그 띄우기
                    LaundryInfoDialog dialog = new LaundryInfoDialog(LaundryMapActivity.this, laundry, likeFlag);
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Timber.d("ERROR(getLaundryLike) : " + t.getMessage());
            }
        });
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }


    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    /**
     * GPS
     */
    private void getGps() {
        boolean isNetworkEnabled = false;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // 네트워크 프로바이더 사용가능여부
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // 네트워크 사용여부 체크 -> 사용거부 해놓았으면 휴대폰의 설정 앱 열기
        if (isNetworkEnabled == false) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // 퍼미션 체크
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "네트워크 사용이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            // latitude : 위도, longitude : 경도
            networkLatitude = location.getLatitude();
            networkLongitude = location.getLongitude();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.fab_zoomIn:
                setZoomInOut(true);
                break;
            case R.id.fab_zoomOut:
                setZoomInOut(false);
                break;
            case R.id.fab_gps:
                getGps();
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(networkLatitude, networkLongitude), true);
                break;
            case R.id.map_view_app_name:
            case R.id.map_view_back_btn:
                intent = new Intent(LaundryMapActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * HASH KEY 사용
     */
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Timber.d("Hash key : " + something);
            }
        } catch (Exception e) {
            Timber.d("로그 name not found" + e.toString());
        }
    }

}
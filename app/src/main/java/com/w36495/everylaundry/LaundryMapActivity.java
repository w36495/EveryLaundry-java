package com.w36495.everylaundry;

import android.Manifest;
import android.animation.TimeAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.w36495.everylaundry.api.InsertLaundryLike;
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.Laundry;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import timber.log.Timber;

/**
 * 세탁소 맵 보여지는 액티비티
 */
public class LaundryMapActivity extends AppCompatActivity implements MapView.POIItemEventListener, View.OnClickListener {

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private FloatingActionButton fab_zoomIn, fab_zoomOut, fab_gps;
    private ImageButton map_view_back_btn;
    private TextView map_view_app_name;

    private RequestQueue requestQueue;

    private ArrayList<Laundry> laundryList = new ArrayList<>();
    private int mapZoomLevel = 0;
    private String loginID = null;

    private Double networkLatitude = 0.0;
    private Double networkLongitude = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_map);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        setInit();
    }

    private void setInit() {

        loginID = MainActivity.getLoginUserID();

        mapView = new MapView(this);

        map_view_app_name = findViewById(R.id.map_view_app_name);
        map_view_back_btn = findViewById(R.id.map_view_back_btn);
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
        mapView.setZoomLevel(4, true);

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

        String URL = DatabaseInfo.showLaundryURL;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("onResponse() 응답 : " + response);
                parseResponse(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 에러나면 error로 들어옴
                        Timber.d("onErrorResponse() 세탁소 오류 오류 : " + error.getMessage());
                    }
                });
        request.setShouldCache(false);
        requestQueue.add(request);
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

    private void parseResponse(String response) {
        ArrayList<Laundry> resultLaundry = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        JsonArray jsonLaundry = (JsonArray) jsonObject.get("laundry");

        for (int index = 0; index < jsonLaundry.size(); index++) {
            JsonObject laundryInfo = (JsonObject) jsonLaundry.get(index);

            int LAUNDRY_KEY = laundryInfo.get("LAUNDRY_KEY").getAsInt();
            String LAUNDRY_NM = laundryInfo.get("LAUNDRY_NM").getAsString();
            String LAUNDRY_TEL = laundryInfo.get("LAUNDRY_TEL").getAsString();
            String LAUNDRY_ADDR = laundryInfo.get("LAUNDRY_ADDR").getAsString();
            String LAUNDRY_ZIP_CODE = laundryInfo.get("LAUNDRY_ZIP_CODE").getAsString();
            Double COORDS_X = laundryInfo.get("COORDS_X").getAsDouble();
            Double COORDS_Y = laundryInfo.get("COORDS_Y").getAsDouble();

            Laundry laundry = new Laundry(LAUNDRY_KEY, LAUNDRY_NM, LAUNDRY_TEL, LAUNDRY_ADDR, LAUNDRY_ZIP_CODE, COORDS_X, COORDS_Y);

            resultLaundry.add(laundry);
        }

        setMapViewMarker(resultLaundry);
    }

    /**
     * DB에 저장되어 있는 세탁소 정보들 마커로 표시
     * @param DB_laundryList
     */
    private void setMapViewMarker(ArrayList<Laundry> DB_laundryList) {
        this.laundryList = DB_laundryList;

        // 마커
        for (int index = 0; index < DB_laundryList.size(); index++) {
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(DB_laundryList.get(index).getLaundryCoordsX(), DB_laundryList.get(index).getLaundryCoordsY());
            marker.setItemName(String.valueOf(DB_laundryList.get(index).getLaundryName()));
            marker.setTag(index);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin);

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
        int mapKey = mapPOIItem.getTag();
        Laundry laundry = laundryList.get(mapKey);

        String laundryKey = String.valueOf(mapKey);

        // DB의 LAUNDRY_DEATIL에 삽입하기
        InsertLaundryLike insertLaundryLike = new InsertLaundryLike();
        insertLaundryLike.execute(DatabaseInfo.insertLaundryLikeURL, loginID, laundryKey);

        // 좋아요(즐겨찾기)를 했는지 안했는지 얻기
        String URL = DatabaseInfo.showLaundryLikeURL;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d("좋아요 뭐를 했니? : " + response);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
                JsonArray jsonLike = (JsonArray) jsonObject.get("laundryLikeArray");
                JsonObject like = (JsonObject) jsonLike.get(0);
                boolean likeFlag = false;

                if (like.get("LIKE_FLAG").getAsString().equals("Y")) {
                    likeFlag = true;
                }

                // 하단에 세탁소 정보 다이얼로그 띄우기
                LaundryInfoDialog dialog = new LaundryInfoDialog(LaundryMapActivity.this, laundry, likeFlag);
                dialog.show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.d(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", loginID);
                params.put("laundryKey", laundryKey);
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);

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

        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Timber.d("GPS 프로바이더 사용가능 ? : " + isGPSEnabled);
        Timber.d("네트워크 프로바이더 사용가능? : " + isNetworkEnabled);

        //TODO: GPS 안켜져있으면 위치 서비스로 이동 => 나중에 팝업창으로 안내문구 띄우기
        if (isGPSEnabled == false) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        // 퍼미션 체크
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            // latitude : 위도, longitude : 경도
            networkLatitude = location.getLatitude();
            networkLongitude = location.getLongitude();
            Timber.d("위도 : " + networkLatitude + " 경도 : " + networkLongitude);
        } else {
            Timber.d("location 없음");
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
}
package com.w36495.everylaundry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.w36495.everylaundry.data.DatabaseInfo;
import com.w36495.everylaundry.data.Laundry;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import timber.log.Timber;

public class LaundryMapActivity extends AppCompatActivity implements MapView.POIItemEventListener {

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private FloatingActionButton fab_zoomIn, fab_zoomOut;

    private RequestQueue requestQueue;

    private ArrayList<Laundry> laundryList = new ArrayList<>();
    private int mapZoomLevel = 0;

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

        mapView = new MapView(this);

        fab_zoomIn = findViewById(R.id.fab_zoomIn);
        fab_zoomOut = findViewById(R.id.fab_zoomOut);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setPOIItemEventListener(this);

        // 줌 레벨 설정
        mapView.setZoomLevel(4, true);

        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);


        // 줌 변경 버튼
        fab_zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int zoomLevel = mapView.getZoomLevel();
                mapView.setZoomLevel(--zoomLevel, true);
            }
        });

        fab_zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int zoomLevel = mapView.getZoomLevel();
                mapView.setZoomLevel(++zoomLevel, true);
            }
        });

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

    // 지도에 마커 설정
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

    // 지도 마커를 클릭했을 때의 이벤트 => 하단에 다이얼로그로 세탁소 정보 띄우기
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        int mapKey = mapPOIItem.getTag();

        Laundry laundry = laundryList.get(mapKey);

        LaundryInfoDialog dialog = new LaundryInfoDialog(LaundryMapActivity.this, laundry);
        dialog.show();

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


}
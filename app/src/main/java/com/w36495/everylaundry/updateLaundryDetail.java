package com.w36495.everylaundry;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import timber.log.Timber;

public class updateLaundryDetail extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        String userID = (String) strings[1];
        String laundryKey = (String) strings[2];

        String serverURL = (String) strings[0];
        // 홈페이지로 치면 주소창에 파라미터 넘어가듯
        String postParameters = "userID=" + userID + "&laundryKey=" + laundryKey;

        StringBuilder stringBuilder = new StringBuilder();

        try {
            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Timber.d("POST response code : " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                Timber.d("updateLaundryDetail : HTTP_OK");
                inputStream = httpURLConnection.getInputStream();
                Timber.d("updateLaundryDetail - getInputStream() : " + inputStream);
            } else {
                Timber.d("updateLaundryDetail : HTTP_FAIL");
                inputStream = httpURLConnection.getErrorStream();
                Timber.d("updateLaundryDetail - getErrorStream() : " + inputStream);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            Timber.d("updateLaundryDetail - stringBuilder : " + stringBuilder);

        } catch (IOException e) {
            Timber.d("updateLaundryDetail : Error " + e.getMessage());
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

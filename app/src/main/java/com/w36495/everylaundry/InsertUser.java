package com.w36495.everylaundry;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class InsertUser extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {

        String userID = (String) strings[1];
        String userPW = (String) strings[2];
        String userNickNM = (String) strings[3];
        String userEmail = (String) strings[4];
        String userMobile = (String) strings[5];

        String serverURL = (String) strings[0];
        // 홈페이지로 치면 주소창에 파라미터 넘어가듯
        String postParameters = "userID=" + userID + "&userPW=" + userPW + "&userNickNM=" + userNickNM
                + "&userEmail=" + userEmail + "&userMobile=" + userMobile;

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
            Log.d("로그", "POST response code : " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                Log.d("로그", "InsertData : HTTP_OK");
                inputStream = httpURLConnection.getInputStream();
                Log.d("로그", "InsertData - getInputStream() : " + inputStream);
            } else {
                Log.d("로그", "InsertData : HTTP_FAIL");
                inputStream = httpURLConnection.getErrorStream();
                Log.d("로그", "InsertData - getErrorStream() : " + inputStream);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();

            Log.d("로그", "InsertData - stringBuilder : " + stringBuilder);

        } catch (IOException e) {
            Log.d("로그", "InsertData : Error " + e.getMessage());
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}





package com.example.tuanle.weadroid;

import android.util.Log;

import com.google.android.gms.common.api.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class HttpHandler {

    //Read request
    public static String getRequest(String urlStr){
        //urlStr = address ie: localhost:8000

        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!=null){ //while line is not empty
                builder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    //Create method
    public static String postRain(String urlStr, Rain rain){
        String status = "";

        try {
            //Connect
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            //Prepare json object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", rain.latitude);
            jsonObject.put("longitude", rain.longitude);
            jsonObject.put("condition", rain.condition);
            jsonObject.put("time", rain.time);

            //Write data
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(jsonObject.toString());
            outputStream.flush();
            outputStream.close();
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    //Update method
    public static String editRain(String urlStr, Rain rain){
        String status = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", rain.latitude);
            jsonObject.put("longitude", rain.longitude);
            jsonObject.put("condition", rain.condition);
            jsonObject.put("time", rain.time);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();
            os.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    //Delete method
    public static String deleteRequest(String urlStr){
        String status = "";
        try{
            URL url = new URL(urlStr);
            Log.d(TAG, "DeleteClinicRequest: " + urlStr);
            HttpURLConnection conn = (HttpURLConnection)
                    url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type","application/json");
            status = conn.getResponseCode() + ": " + conn.getResponseMessage();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }
}
package com.example.tuanle.weadroid;

import android.util.Log;

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
    static String status;

    //Read request
    public static String getRequest(String urlStr){
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
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
    public static String postRequest(String urlStr, Condition condition){
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", condition.latitude);
            jsonObject.put("longitude", condition.longitude);
            jsonObject.put("condition", condition.condition);
            jsonObject.put("time", condition.time);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();
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
    public static String putRequest(String urlStr, Condition condition, String id){
        try {
            URL url = new URL(urlStr + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type","application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", condition.latitude);
            jsonObject.put("longitude", condition.longitude);
            jsonObject.put("condition", condition.condition);
            jsonObject.put("time", condition.time);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonObject.toString());
            os.flush();
            os.close();
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

    //Delete method
    public static String deleteRequest(String urlStr, String id){
        try{
            URL url = new URL(urlStr + "/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
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
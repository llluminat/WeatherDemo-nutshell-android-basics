package de.onelogic.android.weatherdemo;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Marco Ziegaus <github@marcoziegaus.de> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return. Cheers! Marco
 * ----------------------------------------------------------------------------
 */

/**
 * Created by marco on 10/02/16.
 */
public class WeatherFetchTask extends AsyncTask<Void, Void, JSONObject> {
    // Please provide your own APPID which you receive after signing up on openweathermap.com
    private String url = "http://api.openweathermap.org/data/2.5/weather?q=Passau&lang=en&mode=json&units=metric&APPID=********************************";

    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
            String jsonString = downloadUrl(url);
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream is = conn.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }
}

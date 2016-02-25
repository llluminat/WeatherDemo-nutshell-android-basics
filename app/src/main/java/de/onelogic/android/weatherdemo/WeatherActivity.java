package de.onelogic.android.weatherdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Marco Ziegaus <github@marcoziegaus.de> wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return. Cheers! Marco
 * ----------------------------------------------------------------------------
 */

/**
 * Before running this app, go to WeatherFetchTask.java and insert your own APPID (API code for the weather REST API)
 */

public class WeatherActivity extends AppCompatActivity {

    TextView textViewTemperature;
    TextView textViewCity;
    TextView textViewDescription;
    TextView textViewCloudiness;
    TextView textViewWind;
    TextView textViewHumidity;
    Button   buttonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        textViewCloudiness = (TextView) findViewById(R.id.textViewCloudiness);
        textViewWind = (TextView) findViewById(R.id.textViewWind);
        textViewHumidity = (TextView) findViewById(R.id.textViewHumidity);

        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherFetchTask task = new WeatherFetchTask() {
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {
                        updateView(jsonObject);
                    }
                };
                //                task.execute();
                task.doInBackground();
            }
        });
    }

    private void updateView(JSONObject jsonObject) {
        JSONObject main;
        try {
            main = jsonObject.getJSONObject("main");
            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject wind = jsonObject.getJSONObject("wind");
            JSONObject clouds = jsonObject.getJSONObject("clouds");

            // retrieve the weather information (temperature, wind speed, etc.) from the JSON objects
            String weatherDescription = weather.getString("description");
            double temperature = main.getDouble("temp");
            int humidity = main.getInt("humidity");
            double windSpeed = wind.getDouble("speed");
            double windDegree = wind.getDouble("deg");
            int cloudiness = clouds.getInt("all");

            // set the values to the GUI
            textViewTemperature.setText(String.valueOf(Math.round(temperature)) + "°C");
            textViewDescription.setText(weatherDescription);
            textViewWind.setText(String.valueOf(windSpeed + " km/h " + getWindDirection(windDegree)));
            textViewCloudiness.setText(String.valueOf(cloudiness) + " %");
            textViewHumidity.setText(String.valueOf(humidity) + " %");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the coarse wind direction (N, NE, E, SE, S, SW, W, NW) from the wind direction angle
     *
     * @param degree The wind direction in degrees (0 - 360)
     * @return A String representing the coarse wind direction.
     */
    private String getWindDirection(double degree) {
        // subtract 22.5 to have "nice borders" such as 45, 90, 135, etc. rather than 22.5, 67.5, 112.5, etc.
        degree = degree - 22.5;
        if (degree <= 0 || degree >= 315) {
            return "N";
        }
        else if (degree <= 45) {
            return "NE";
        }
        else if (degree <= 90) {
            return "E";
        }
        else if (degree <= 135) {
            return "SE";
        }
        else if (degree <= 180) {
            return "S";
        }
        else if (degree <= 225) {
            return "SW";
        }
        else if (degree <= 270) {
            return "W";
        }
        else if (degree <= 315) {
            return "NW";
        }
        else {
            throw new IllegalArgumentException("Degree must be between 0 and 360");
        }
    }

}

package com.example.tuanle.weadroid;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ReportLocationWeatherActivity extends AppCompatActivity {
    String weatherCondition;
    TextView latitudeText;
    TextView longitudeText;
    Condition condition;
    double latitude;
    double longitude;
    private RadioButton clearDay, clearNight, rain, snow, sleet, wind, fog, cloudy, partlyCloudyDay, partlyCloudyNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_location_weather);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        condition = new Condition();
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        latitudeText = findViewById(R.id.latitudeConditionC);
        longitudeText = findViewById(R.id.longitudeConditionC);
        latitudeText.setText(latitude + "");
        longitudeText.setText(longitude + "");
        clearDay = findViewById(R.id.clearDay);
        clearNight = findViewById(R.id.clearNight);
        rain = findViewById(R.id.rain);
        snow = findViewById(R.id.snow);
        sleet = findViewById(R.id.sleet);
        wind = findViewById(R.id.wind);
        fog = findViewById(R.id.fog);
        cloudy = findViewById(R.id.cloudy);
        partlyCloudyDay = findViewById(R.id.partlyCloudyDay);
        partlyCloudyNight = findViewById(R.id.partlyCloudyNight);
        weatherCondition = "";

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(LightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    private final SensorEventListener LightSensorListener = new SensorEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                int grayShade = (int) event.values[0];
                ViewGroup viewGroup = findViewById(R.id.reportView);
                if (grayShade < MainActivity.MAX_LIGHT/13) {
                    clearDay.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    clearNight.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    rain.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    snow.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    sleet.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    wind.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    fog.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    cloudy.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    partlyCloudyDay.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    partlyCloudyNight.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
                    viewGroup.setBackgroundColor(Color.BLACK);
                    MainActivity.changeText(viewGroup, Color.WHITE);
                } else {
                    clearDay.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    clearNight.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    rain.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    snow.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    sleet.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    wind.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    fog.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    cloudy.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    partlyCloudyDay.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    partlyCloudyNight.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                    viewGroup.setBackgroundColor(Color.WHITE);
                    MainActivity.changeText(viewGroup, Color.BLACK);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.clearDay:
                if (checked) {
                    weatherCondition = "clear-day";
                }
                break;
            case R.id.clearNight:
                if (checked) {
                    weatherCondition = "clear-night";
                }
                break;
            case R.id.rain:
                if (checked) {
                    weatherCondition = "rain";
                }
                break;
            case R.id.snow:
                if (checked) {
                    weatherCondition = "snow";
                }
                break;
            case R.id.sleet:
                if (checked) {
                    weatherCondition = "sleet";
                }
                break;
            case R.id.wind:
                if (checked) {
                    weatherCondition = "wind";
                }
                break;
            case R.id.fog:
                if (checked) {
                    weatherCondition = "fog";
                }
                break;
            case R.id.cloudy:
                if (checked) {
                    weatherCondition = "cloudy";
                }
                break;
            case R.id.partlyCloudyDay:
                if (checked) {
                    weatherCondition = "partly-cloudy-day";
                }
                break;
            case R.id.partlyCloudyNight:
                if (checked) {
                    weatherCondition = "partly-cloudy-night";
                }
                break;
        }
    }

    public void onReportClicked(View view) {
        if (weatherCondition.equals("")) {
            Toast.makeText(this, "Please pick one condition", Toast.LENGTH_SHORT).show();
        } else {
            condition.latitude = latitude;
            condition.longitude = longitude;
            condition.condition = weatherCondition;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
            condition.time = df.format(new Date());
            new PostCondition().execute();
        }
    }

    public void onCancelClicked(View view) {
        finish();
    }

    private class PostCondition extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler.postRequest(MapsActivity.WEATHER_REPORT_API, condition);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }
}

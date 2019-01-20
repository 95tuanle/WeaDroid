package com.example.tuanle.weadroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
        weatherCondition = "";
    }

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

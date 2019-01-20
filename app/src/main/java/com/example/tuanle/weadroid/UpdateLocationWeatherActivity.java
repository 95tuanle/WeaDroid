package com.example.tuanle.weadroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class UpdateLocationWeatherActivity extends AppCompatActivity {
    Condition condition;
    String weatherCondition;
    TextView latitudeText;
    TextView longitudeText;
    TextView idText;
    RadioButton clearDay, clearNight, rain, snow, sleet, wind, fog, cloudy, partlyCloudyDay, partlyCloudyNight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location_weather);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        condition = new Condition();
        idText = findViewById(R.id.idConditionU);
        latitudeText = findViewById(R.id.latitudeConditionU);
        longitudeText = findViewById(R.id.longitudeConditionU);
        clearDay = findViewById(R.id.clearDayU);
        clearNight = findViewById(R.id.clearNightU);
        rain = findViewById(R.id.rainU);
        snow = findViewById(R.id.snowU);
        sleet = findViewById(R.id.sleetU);
        wind = findViewById(R.id.windU);
        fog = findViewById(R.id.fogU);
        cloudy = findViewById(R.id.cloudyU);
        partlyCloudyDay = findViewById(R.id.partlyCloudyDayU);
        partlyCloudyNight = findViewById(R.id.partlyCloudyNightU);
        weatherCondition = "";
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("JSONString"));
            String conditionToBeUpdated = jsonObject.getString("condition");
            if (conditionToBeUpdated.equals("clear-day")) {
                clearDay.toggle();
                weatherCondition = "clear-day";
            } else if (conditionToBeUpdated.equals("clear-night")) {
                clearNight.toggle();
                weatherCondition = "clear-night";
            } else if (conditionToBeUpdated.equals("rain")) {
                rain.toggle();
                weatherCondition = "rain";
            } else if (conditionToBeUpdated.equals("snow")) {
                snow.toggle();
                weatherCondition = "snow";
            } else if (conditionToBeUpdated.equals("sleet")) {
                sleet.toggle();
                weatherCondition = "sleet";
            } else if (conditionToBeUpdated.equals("wind")) {
                wind.toggle();
                weatherCondition = "wind";
            } else if (conditionToBeUpdated.equals("fog")) {
                fog.toggle();
                weatherCondition = "fog";
            } else if (conditionToBeUpdated.equals("cloudy")) {
                cloudy.toggle();
                weatherCondition = "cloudy";
            } else if (conditionToBeUpdated.equals("partly-cloudy-day")) {
                partlyCloudyDay.toggle();
                weatherCondition = "partly-cloudy-day";
            } else if (conditionToBeUpdated.equals("partly-cloudy-night")) {
                partlyCloudyNight.toggle();
                weatherCondition = "partly-cloudy-night";
            }
            idText.setText(jsonObject.getString("_id"));
            latitudeText.setText(jsonObject.getString("latitude"));
            longitudeText.setText(jsonObject.getString("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.clearDayU:
                if (checked) {
                    weatherCondition = "clear-day";
                }
                break;
            case R.id.clearNightU:
                if (checked) {
                    weatherCondition = "clear-night";
                }
                break;
            case R.id.rainU:
                if (checked) {
                    weatherCondition = "rain";
                }
                break;
            case R.id.snowU:
                if (checked) {
                    weatherCondition = "snow";
                }
                break;
            case R.id.sleetU:
                if (checked) {
                    weatherCondition = "sleet";
                }
                break;
            case R.id.windU:
                if (checked) {
                    weatherCondition = "wind";
                }
                break;
            case R.id.fogU:
                if (checked) {
                    weatherCondition = "fog";
                }
                break;
            case R.id.cloudyU:
                if (checked) {
                    weatherCondition = "cloudy";
                }
                break;
            case R.id.partlyCloudyDayU:
                if (checked) {
                    weatherCondition = "partly-cloudy-day";
                }
                break;
            case R.id.partlyCloudyNightU:
                if (checked) {
                    weatherCondition = "partly-cloudy-night";
                }
                break;
        }
    }

    private class PutCondition extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler.putRequest(MapsActivity.WEATHER_REPORT_API, condition, (String) idText.getText());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }

    private class DeleteCondition extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler.deleteRequest(MapsActivity.WEATHER_REPORT_API, (String) idText.getText());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }

    public void onUpdateClicked(View view) {
        if (weatherCondition.equals("")) {
            Toast.makeText(this, "Please pick one condition", Toast.LENGTH_SHORT).show();
        } else {
            condition.latitude = Double.parseDouble(latitudeText.getText().toString());;
            condition.longitude = Double.parseDouble(longitudeText.getText().toString());
            condition.condition = weatherCondition;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
            condition.time = df.format(new Date());
            new PutCondition().execute();
        }
    }

    public void onCancelClicked(View view) {
        finish();
    }

    public void onDeleteClicked(View view) {
        new DeleteCondition().execute();
    }
}

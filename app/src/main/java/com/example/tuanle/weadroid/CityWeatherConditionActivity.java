package com.example.tuanle.weadroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.models.DataPoint;
import android.zetterstrom.com.forecast.models.Forecast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityWeatherConditionActivity extends AppCompatActivity {
    TextView txtTemp;
    TextView txtToday, txtDay1, txtDay2, txtDay3, txtDay4, txtDay5, txtDay6, txtDay7;
    TextView iconToday, iconDay1, iconDay2, iconDay3, iconDay4, iconDay5, iconDay6, iconDay7;
    TextView tempToday, tempDay1, tempDay2, tempDay3, tempDay4, tempDay5, tempDay6, tempDay7;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather_condition);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        City city = (City) getIntent().getSerializableExtra("city");
        setTitle(city.getName());
        txtTemp = findViewById(R.id.txtTemp);
        txtToday = findViewById(R.id.txtToday);
        txtDay1 = findViewById(R.id.txtDay1);
        txtDay2 = findViewById(R.id.txtDay2);
        txtDay3 = findViewById(R.id.txtDay3);
        txtDay4 = findViewById(R.id.txtDay4);
        txtDay5 = findViewById(R.id.txtDay5);
        txtDay6 = findViewById(R.id.txtDay6);
        txtDay7 = findViewById(R.id.txtDay7);

        //icon column
        iconToday = findViewById(R.id.iconToday);
        iconDay1 = findViewById(R.id.iconDay1);
        iconDay2 = findViewById(R.id.iconDay2);
        iconDay3 = findViewById(R.id.iconDay3);
        iconDay4 = findViewById(R.id.iconDay4);
        iconDay5 = findViewById(R.id.iconDay5);
        iconDay6 = findViewById(R.id.iconDay6);
        iconDay7 = findViewById(R.id.iconDay7);

        //TEMPERATURE COLUMN

        tempToday = findViewById(R.id.tempToday);
        tempDay1 = findViewById(R.id.tempDay1);
        tempDay2 = findViewById(R.id.tempDay2);
        tempDay3 = findViewById(R.id.tempDay3);
        tempDay4 = findViewById(R.id.tempDay4);
        tempDay5 = findViewById(R.id.tempDay5);
        tempDay6 = findViewById(R.id.tempDay6);
        tempDay7 = findViewById(R.id.tempDay7);

        ForecastClient.getInstance().getForecast(city.getLatitude(),city.getLongitude(), new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {
                if (response.isSuccessful()) {
                    Forecast forecast = response.body();
                    txtTemp.setText(forecast.getCurrently().getApparentTemperature().intValue() + "°F");
                    ArrayList<DataPoint> dataPoints = forecast.getDaily().getDataPoints();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    String today = sdf.format(dataPoints.get(0).getTime());
                    String day1 = sdf.format(dataPoints.get(1).getTime());
                    String day2 = sdf.format(dataPoints.get(2).getTime());
                    String day3 = sdf.format(dataPoints.get(3).getTime());
                    String day4 = sdf.format(dataPoints.get(4).getTime());
                    String day5 = sdf.format(dataPoints.get(5).getTime());
                    String day6 = sdf.format(dataPoints.get(6).getTime());
                    String day7 = sdf.format(dataPoints.get(7).getTime());

                    txtToday.setText(today);
                    txtDay1.setText(day1);
                    txtDay2.setText(day2);
                    txtDay3.setText(day3);
                    txtDay4.setText(day4);
                    txtDay5.setText(day5);
                    txtDay6.setText(day6);
                    txtDay7.setText(day7);

                    iconToday.setText(returnEmoji(dataPoints.get(0).getIcon().getText()));
                    iconDay1.setText(returnEmoji(dataPoints.get(1).getIcon().getText()));
                    iconDay2.setText(returnEmoji(dataPoints.get(2).getIcon().getText()));
                    iconDay3.setText(returnEmoji(dataPoints.get(3).getIcon().getText()));
                    iconDay4.setText(returnEmoji(dataPoints.get(4).getIcon().getText()));
                    iconDay5.setText(returnEmoji(dataPoints.get(5).getIcon().getText()));
                    iconDay6.setText(returnEmoji(dataPoints.get(6).getIcon().getText()));
                    iconDay7.setText(returnEmoji(dataPoints.get(7).getIcon().getText()));

                    tempToday.setText(dataPoints.get(0).getTemperatureHigh().toString() + "   " + dataPoints.get(0).getTemperatureLow().toString());

                }
            }
            @Override
            public void onFailure(Call<Forecast> forecastCall, Throwable t) {

            }
        });
    }

    private String returnEmoji(String toString) {
        String emo ="";
        if (toString.equals("clear-day")) {
            emo = "☀️";
        }else if (toString.equals("clear-night")){
            emo = "🌙";
        }else if (toString.equals("rain")){
            emo = "☔️";
        }else if (toString.equals("snow")){
            emo = "❄️";
        }else if (toString.equals("sleet")){
            emo = "🌨";
        }else if (toString.equals("wind")){
            emo = "🌬";
        }else if (toString.equals("fog")){
            emo = "🌫";
        }else if (toString.equals("cloudy")){
            emo = "☁️";
        }else if (toString.equals("partly-cloudy-day")){
            emo = "🌤";
        }else if (toString.equals("partly-cloudy-night")){
            emo = "🌥";
        }else {
            emo = "error";
        }
        return emo;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}

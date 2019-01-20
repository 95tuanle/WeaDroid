package com.example.tuanle.weadroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.models.DataPoint;
import android.zetterstrom.com.forecast.models.Forecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityWeatherConditionActivity extends AppCompatActivity {
    TextView txtTemp, txtSummary, txtSunriseTime, txtSunsetTime, txtChanceOfRain, txtHumidity, txtWind, txtFeelsLike;
    TextView txtToday, txtDay1, txtDay2, txtDay3, txtDay4, txtDay5, txtDay6, txtDay7;
    TextView iconToday, iconDay1, iconDay2, iconDay3, iconDay4, iconDay5, iconDay6, iconDay7;
    TextView tempToday, tempDay1, tempDay2, tempDay3, tempDay4, tempDay5, tempDay6, tempDay7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather_condition);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        final City city = (City) getIntent().getSerializableExtra("city");
        setTitle(city.getName());
        txtTemp = findViewById(R.id.txtTemp);
        txtSummary = findViewById(R.id.txtSumary);
        txtSunriseTime = findViewById(R.id.txtSunriseTime);
        txtSunsetTime = findViewById(R.id.txtSunsetTime);
        txtChanceOfRain = findViewById(R.id.txtChanceOfRain);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWind = findViewById(R.id.txtWind);
        txtFeelsLike = findViewById(R.id.txtFeelsLike);
        //Date column
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
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        MainActivity.musicService.playSong(getSongPosition(forecast.getCurrently().getIcon().getText()));

                    }else {
                        ActivityCompat.requestPermissions(CityWeatherConditionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);

                    }

                    //looping for horiental Scrolling
                    int[] hourId = new int[] {R.id.txtHour0, R.id.txtHour1,R.id.txtHour2, R.id.txtHour3,R.id.txtHour4,
                            R.id.txtHour5,R.id.txtHour6, R.id.txtHour7,R.id.txtHour8, R.id.txtHour9,
                            R.id.txtHour10,R.id.txtHour11, R.id.txtHour12,R.id.txtHour13, R.id.txtHour14,
                            R.id.txtHour15,R.id.txtHour16, R.id.txtHour17,R.id.txtHour18, R.id.txtHour19,
                            R.id.txtHour20,R.id.txtHour21, R.id.txtHour22,R.id.txtHour23, R.id.txtHour24};
                    for (int i = 0; i<hourId.length; i++){
                        TextView y = findViewById(hourId[i]);
                        SimpleDateFormat sdfTime = new SimpleDateFormat("H");
                        String time = sdfTime.format(forecast.getHourly().getDataPoints().get(i).getTime());
                        if (Integer.parseInt(time) <=12){
                            time += "AM";
                        }else {
                            time += "PM";
                        }
                        y.setText(time+"\n"+returnEmoji(forecast.getHourly().getDataPoints().get(i).getIcon().getText()));
                    }
                    if (MainActivity.DISPLAYING_CELSIUS) {
                        txtTemp.setText(MainActivity.toCelsius(forecast.getCurrently().getTemperature()) + "°C");
                        txtFeelsLike.setText("Feels like\n"+MainActivity.toCelsius(forecast.getCurrently().getApparentTemperature()) + "°C");
                    } else {
                        txtTemp.setText(forecast.getCurrently().getTemperature().intValue() + "°F");
                        txtFeelsLike.setText("Feels like\n"+forecast.getCurrently().getApparentTemperature().intValue() + "°F");

                    }
                    ArrayList<DataPoint> hourlyDataPoints = forecast.getHourly().getDataPoints();

                    ArrayList<DataPoint> dataPoints = forecast.getDaily().getDataPoints();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("H:mm");
                    String sunriseTime = sdfTime.format(dataPoints.get(0).getSunriseTime());
                    String sunsetTime = sdfTime.format(dataPoints.get(0).getSunsetTime());


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

                    if (MainActivity.DISPLAYING_CELSIUS) {
                        tempToday.setText(MainActivity.toCelsius(dataPoints.get(0).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(0).getTemperatureLow()));
                        tempDay1.setText(MainActivity.toCelsius(dataPoints.get(1).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(1).getTemperatureLow()));
                        tempDay2.setText(MainActivity.toCelsius(dataPoints.get(2).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(2).getTemperatureLow()));
                        tempDay3.setText(MainActivity.toCelsius(dataPoints.get(3).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(3).getTemperatureLow()));
                        tempDay4.setText(MainActivity.toCelsius(dataPoints.get(4).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(4).getTemperatureLow()));
                        tempDay5.setText(MainActivity.toCelsius(dataPoints.get(5).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(5).getTemperatureLow()));
                        tempDay6.setText(MainActivity.toCelsius(dataPoints.get(6).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(6).getTemperatureLow()));
                        tempDay7.setText(MainActivity.toCelsius(dataPoints.get(7).getTemperatureHigh()) + "   " + MainActivity.toCelsius(dataPoints.get(7).getTemperatureLow()));
                    } else {
                        tempToday.setText(dataPoints.get(0).getTemperatureHigh().intValue() + "   " + dataPoints.get(0).getTemperatureLow().intValue());
                        tempDay1.setText(dataPoints.get(1).getTemperatureHigh().intValue() + "   " + dataPoints.get(1).getTemperatureLow().intValue());
                        tempDay2.setText(dataPoints.get(2).getTemperatureHigh().intValue() + "   " + dataPoints.get(2).getTemperatureLow().intValue());
                        tempDay3.setText(dataPoints.get(3).getTemperatureHigh().intValue() + "   " + dataPoints.get(3).getTemperatureLow().intValue());
                        tempDay4.setText(dataPoints.get(4).getTemperatureHigh().intValue() + "   " + dataPoints.get(4).getTemperatureLow().intValue());
                        tempDay5.setText(dataPoints.get(5).getTemperatureHigh().intValue() + "   " + dataPoints.get(5).getTemperatureLow().intValue());
                        tempDay6.setText(dataPoints.get(6).getTemperatureHigh().intValue() + "   " + dataPoints.get(6).getTemperatureLow().intValue());
                        tempDay7.setText(dataPoints.get(7).getTemperatureHigh().intValue() + "   " + dataPoints.get(7).getTemperatureLow().intValue());
                    }
                    txtSummary.setText(forecast.getDaily().getSummary());
                    //bottom textviews
                    txtSunriseTime.setText("Sunrise\n"+sunriseTime);
                    txtSunsetTime.setText("Sunset\n"+sunsetTime);
                    txtChanceOfRain.setText("Chance of rain\n"+ Integer.valueOf((int) (forecast.getCurrently().getPrecipProbability()*100))+"%");
                    txtHumidity.setText("Humidity\n"+ Integer.valueOf((int) (forecast.getCurrently().getHumidity()*100))+"%");
                    txtWind.setText("Wind\n"+ forecast.getCurrently().getWindSpeed()+" m/s");

                }
            }
            @Override
            public void onFailure(Call<Forecast> forecastCall, Throwable t) {

            }
        });
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(LightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    private int getSongPosition(String iconString) {
        int position;
        if (iconString.equals("clear-day")) {
            position = 0;
        }else if (iconString.equals("clear-night")){
            position = 1;
        }else if (iconString.equals("rain")){
            position = 2;
        }else if (iconString.equals("snow")){
            position = 3;
        }else if (iconString.equals("sleet")){
            position = 4;
        }else if (iconString.equals("wind")){
            position = 5;
        }else if (iconString.equals("fog")){
            position = 6;
        }else if (iconString.equals("cloudy")){
            position = 7;
        }else if (iconString.equals("partly-cloudy-day")){
            position = 8;
        }else if (iconString.equals("partly-cloudy-night")){
            position = 9;
        }else {
            position = -1;
        }
        return position;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.musicService.stopSong();
    }

    private final SensorEventListener LightSensorListener = new SensorEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                int grayShade = (int) event.values[0];
                ViewGroup viewGroup = findViewById(R.id.detailView);
                if (grayShade < MainActivity.MAX_LIGHT/13) {

                    viewGroup.setBackgroundColor(Color.BLACK);
                    MainActivity.changeText(viewGroup, Color.WHITE);
                } else {

                    viewGroup.setBackgroundColor(Color.WHITE);
                    MainActivity.changeText(viewGroup, Color.BLACK);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public static String returnEmoji(String iconString) {
        String emoji;
        if (iconString.equals("clear-day")) {
            emoji = "☀️";
        }else if (iconString.equals("clear-night")){
            emoji = "🌙";
        }else if (iconString.equals("rain")){
            emoji = "☔️";
        }else if (iconString.equals("snow")){
            emoji = "❄️";
        }else if (iconString.equals("sleet")){
            emoji = "🌨";
        }else if (iconString.equals("wind")){
            emoji = "🌬";
        }else if (iconString.equals("fog")){
            emoji = "🌫";
        }else if (iconString.equals("cloudy")){
            emoji = "☁️";
        }else if (iconString.equals("partly-cloudy-day")){
            emoji = "🌤";
        }else if (iconString.equals("partly-cloudy-night")){
            emoji = "🌥";
        }else {
            emoji = "error";
        }
        return emoji;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void changeDisplayTemperature(View view) {
    }
}
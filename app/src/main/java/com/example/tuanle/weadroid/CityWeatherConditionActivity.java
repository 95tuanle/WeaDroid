package com.example.tuanle.weadroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
    TextView txtTemp, txtSumary, txtSunriseTime, txtSunsetTime, txtChanceOfRain, txtHumidity, txtWind, txtFeelsLike;
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
        txtSumary = findViewById(R.id.txtSumary);
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
                    txtSumary.setText(forecast.getDaily().getSummary());
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
    }

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

package com.example.tuanle.weadroid;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;
import android.zetterstrom.com.forecast.models.Forecast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String DARK_SKY_API_KEY = "5528c7901c45cba63baa891e648c897e";
    public static final String WEATHER_REPORT_API = "https://whereisrain.herokuapp.com/rains";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    FloatingActionButton floatingActionButton;
    CityRepository cityRepository;
    ListView citiesList;
    public boolean DISPLAYING_CELCIUS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ForecastConfiguration configuration = new ForecastConfiguration.Builder(DARK_SKY_API_KEY).setCacheDirectory(getCacheDir()).build();
        ForecastClient.create(configuration);
        cityRepository = new CityRepository(getApplicationContext());
        citiesList = findViewById(R.id.citiesList);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setImageBitmap(textAsBitmap("°C", 40, Color.WHITE));

    }

    public void addCity(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    public void reloadData() {
        cityRepository.getAllCities().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(@Nullable final List<City> cities) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                cities.add(0, new City("Current Location", location.getLatitude(), location.getLongitude()));
                            } else {
                                Toast.makeText(MainActivity.this, "Fetching location, please wait and try again to see your current location weather", Toast.LENGTH_SHORT).show();
                            }
                            updateCitiesList(cities);
                        }
                    });
                } else {
                    requestLocationPermission();
                }

            }
        });
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

    private void updateCitiesList(final List<City> cities) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, cities) {
            @Override
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                final TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                final City city = cities.get(position);
                ForecastClient.getInstance().getForecast(city.getLatitude(),city.getLongitude(), new Callback<Forecast>() {
                    @Override
                    public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {
                        if (response.isSuccessful()) {
                            Forecast forecast = response.body();
                            if (DISPLAYING_CELCIUS) {
                                text1.setText(toCelsius(forecast.getCurrently().getTemperature()) + "°C");
                            } else {
                                text1.setText(forecast.getCurrently().getTemperature().intValue() + "°F");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Forecast> forecastCall, Throwable t) {

                    }
                });
                text2.setText(city.getName()+"");
                return view;
            }
        };
        citiesList.setAdapter(arrayAdapter);
        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CityWeatherConditionActivity.class);
                intent.putExtra("city", cities.get(position));
                startActivity(intent);
            }
        });

        citiesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cityRepository.deleteCity(cities.get(position));
                return false;
            }
        });
    }

    private int toCelsius(Double temperature) {
        return (int) (((temperature - 32)*5)/9);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (cityRepository.findCityByName(place.getName().toString()) == null){
                    cityRepository.addCity(new City(place.getName().toString(), place.getLatLng().latitude, place.getLatLng().longitude));
                }else {
                    Toast.makeText(this, "City is already existed!", Toast.LENGTH_SHORT).show();
                }
                Log.i("Success", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



    public void toMap(View view) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }



    public void changeDisplayTemperature(View view) {
        if (DISPLAYING_CELCIUS) {
            DISPLAYING_CELCIUS = false;
            floatingActionButton.setImageBitmap(textAsBitmap("°C", 40, Color.WHITE));
        } else {
            DISPLAYING_CELCIUS = true;
            floatingActionButton.setImageBitmap(textAsBitmap("°F", 40, Color.WHITE));
        }
        reloadData();
    }

    //method to convert your text to image (StackOverFlow)
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

}

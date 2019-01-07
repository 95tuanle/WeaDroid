package com.example.tuanle.weadroid;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String DARK_SKY_API_KEY = "5528c7901c45cba63baa891e648c897e";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    CityRepository cityRepository;
    ListView citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ForecastConfiguration configuration = new ForecastConfiguration.Builder(DARK_SKY_API_KEY).setCacheDirectory(getCacheDir()).build();
        ForecastClient.create(configuration);
        cityRepository = new CityRepository(getApplicationContext());
        citiesList = findViewById(R.id.citiesList);
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
                                System.out.println("CONCAC");
                                cities.add(0, new City("Current Location", location.getLatitude(), location.getLongitude()));
                                updateCitiesList(cities);
                            } else {
                                Toast.makeText(MainActivity.this, "Fetching location, please wait and try again", Toast.LENGTH_SHORT).show();
                            }
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
                            text1.setText(forecast.getCurrently().getApparentTemperature().intValue() + "°F");
                            System.out.println(forecast.getCurrently().getApparentTemperature());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                // TODO: Filter duplicate cities 🙃
                cityRepository.addCity(new City(place.getName().toString(), place.getLatLng().latitude, place.getLatLng().longitude));
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
}

package com.example.tuanle.weadroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final String WEATHER_REPORT_API = "https://whereisrain.herokuapp.com/rains";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 15.0));
                    } else {
                        Toast.makeText(MapsActivity.this, "Fetching location, please wait and try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            requestLocationPermission();
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(MapsActivity.this, ReportLocationWeatherActivity.class);
                intent.putExtra("latitude", latLng.latitude);
                intent.putExtra("longitude", latLng.longitude);
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                        }
                    }
                });
            } else {
                requestLocationPermission();
            }
        }

        new GetConditions().execute();
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

    public void getCurrentLocationClicked(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mMap.clear();
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 15.0));
                    } else {
                        Toast.makeText(MapsActivity.this, "Fetching location, please wait and try again", Toast.LENGTH_SHORT).show();
                    }
                    new GetConditions().execute();

                }
            });
        } else {
            requestLocationPermission();
        }
    }

    public void reportCurrentLocationWeather(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Intent intent = new Intent(MapsActivity.this, ReportLocationWeatherActivity.class);
                        intent.putExtra("latitude", location.getLatitude());
                        intent.putExtra("longitude", location.getLongitude());
                        startActivity(intent);
                    } else {
                        Toast.makeText(MapsActivity.this, "Fetching location, please wait and try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            requestLocationPermission();
        }


    }

    public void finish(View view) {
        finish();
    }


    private class GetConditions extends AsyncTask<Void, Void, Void> {
        String jsonString;
        @Override
        protected Void doInBackground(Void... voids) {
            jsonString = HttpHandler.getRequest(WEATHER_REPORT_API);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String weatherCondition = jsonObject.getString("condition");
                    int drawable = 0;
                    if (weatherCondition.equals("clear-day")) {
                        drawable = R.drawable.clear_day;
                    } else if (weatherCondition.equals("clear-night")) {
                        drawable = R.drawable.clear_night;
                    } else if (weatherCondition.equals("rain")) {
                        drawable = R.drawable.rain;
                    } else if (weatherCondition.equals("snow")) {
                        drawable = R.drawable.snow;
                    } else if (weatherCondition.equals("sleet")) {
                        drawable = R.drawable.sleet;
                    } else if (weatherCondition.equals("wind")) {
                        drawable = R.drawable.wind;
                    } else if (weatherCondition.equals("fog")) {
                        drawable = R.drawable.fog;
                    } else if (weatherCondition.equals("cloudy")) {
                        drawable = R.drawable.cloudy;
                    } else if (weatherCondition.equals("partly-cloudy-day")) {
                        drawable = R.drawable.partly_cloudy_day;
                    } else if (weatherCondition.equals("partly-cloudy-night")) {
                        drawable = R.drawable.partly_cloudy_night;
                    }
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(drawable);
                    Bitmap rawBitmap = bitmapDrawable.getBitmap();
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(rawBitmap, 100, 100, false);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(jsonObject.getDouble("latitude")
                            , jsonObject.getDouble("longitude"))).icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));
                    marker.setTag(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

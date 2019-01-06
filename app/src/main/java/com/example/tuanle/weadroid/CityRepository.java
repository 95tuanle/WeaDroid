package com.example.tuanle.weadroid;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.List;

public class CityRepository {
    private CityDatabase cityDatabase;

    CityRepository(Context context) {
        cityDatabase = Room.databaseBuilder(context, CityDatabase.class, "cities").allowMainThreadQueries().build();
    }

    void addCity(City city) {
        cityDatabase.cityDao().addCity(city);
    }

    LiveData<List<City>> getAllCities() {
        return cityDatabase.cityDao().getAllCities();
    }

    City findCityByName(String name) {
        return  cityDatabase.cityDao().findCityByName(name);
    }

    void updateCity(City city) {
        cityDatabase.cityDao().updateCity(city);
    }

    void deleteCity(City city) {
        cityDatabase.cityDao().deleteCity(city);
    }
}

package com.example.tuanle.weadroid;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CityDao {
    @Insert
    void addCity(City city);

    @Query("SELECT * FROM city")
    LiveData<List<City>> getAllCities();

    @Query("SELECT * FROM city WHERE name LIKE :name")
    City findCityByName(String name);

    @Update
    void updateCity(City city);

    @Delete
    void deleteCity(City city);
}

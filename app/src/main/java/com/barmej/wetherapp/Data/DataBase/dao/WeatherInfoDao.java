package com.barmej.wetherapp.Data.DataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.barmej.wetherapp.Data.Entity.WeatherInfo;

@Dao
public interface WeatherInfoDao {

    @Query("Select * FROM weather_info LIMIT 1" )
    LiveData<WeatherInfo> getWeatherInfo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addWeatherInfo (WeatherInfo weatherInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeatherInfo(WeatherInfo weatherInfo);

    @Delete
    void deleteWeatherInfo(WeatherInfo weatherInfo);
}

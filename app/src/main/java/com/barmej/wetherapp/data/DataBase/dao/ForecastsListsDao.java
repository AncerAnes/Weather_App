package com.barmej.wetherapp.data.DataBase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.barmej.wetherapp.data.entity.ForecastLists;

@Dao
public interface ForecastsListsDao {
    @Query("Select * FROM forecasts LIMIT 1" )
    LiveData<ForecastLists> getForecastsList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addForecastsList (ForecastLists forecastLists);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateForecastsList(ForecastLists forecastLists);

    @Delete
    void deleteForecastsList(ForecastLists forecastLists);
}

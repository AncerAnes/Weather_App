package com.barmej.wetherapp.data.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.barmej.wetherapp.data.DataBase.dao.ForecastsListsDao;
import com.barmej.wetherapp.data.DataBase.dao.WeatherInfoDao;
import com.barmej.wetherapp.data.entity.ForecastLists;
import com.barmej.wetherapp.data.entity.WeatherInfo;
import com.barmej.wetherapp.converters.DaysForecastsConverter;
import com.barmej.wetherapp.converters.HoursForecastsConverter;
import com.barmej.wetherapp.converters.WeatherListConverter;
@Database(entities = {WeatherInfo.class, ForecastLists.class},version = 2,exportSchema = false)
@TypeConverters({WeatherListConverter.class, HoursForecastsConverter.class, DaysForecastsConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    private static final Object LOCK= new Object();
    public static AppDataBase sInstance;

    private static final String DATABASE_NAME ="weather_dp";

    public static AppDataBase getInstance(Context context){
        if ( sInstance ==null) {
            synchronized (LOCK){
                if  (sInstance==null){
                    sInstance= Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            AppDataBase.DATABASE_NAME
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return sInstance;
    }
    public abstract WeatherInfoDao weatherInfoDao();
    public abstract ForecastsListsDao  ForecastsListsDao();
}

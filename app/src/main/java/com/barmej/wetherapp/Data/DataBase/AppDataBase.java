package com.barmej.wetherapp.Data.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.barmej.wetherapp.Data.DataBase.dao.ForecastsListsDao;
import com.barmej.wetherapp.Data.DataBase.dao.WeatherInfoDao;
import com.barmej.wetherapp.Data.Entity.ForecastLists;
import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.barmej.wetherapp.Data.network.NetworkUtils;
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
                    ).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                }
            }
        }
        return sInstance;
    }
    public abstract WeatherInfoDao weatherInfoDao();
    public abstract ForecastsListsDao  ForecastsListsDao();
}

package com.barmej.wetherapp.data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barmej.wetherapp.data.DataBase.AppDataBase;
import com.barmej.wetherapp.data.entity.ForecastLists;
import com.barmej.wetherapp.data.entity.WeatherInfo;
import com.barmej.wetherapp.data.entity.weatherForecasts;
import com.barmej.wetherapp.data.network.NetworkUtils;
import com.barmej.wetherapp.utils.AppExecutor;
import com.barmej.wetherapp.utils.OpenWeatherDataParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataRepository {
    private static WeatherDataRepository sInstance;
    private static final Object LOCK=new Object();
    private static Context context;
    private NetworkUtils networkUtils;
    private MutableLiveData <WeatherInfo> weatherInfoMutableLiveData;
    private MutableLiveData <ForecastLists> forecastListsMutableLiveData;
    private AppExecutor appExecutor;
    private Call<WeatherInfo> mWeatherInfoCall;
    private Call<weatherForecasts> mForecastsCall;

    private static final String TAG =WeatherDataRepository.class.getSimpleName();
    private AppDataBase appDataBase;

    public static WeatherDataRepository getInstance(Context context){
        if (sInstance==null) {
            synchronized (LOCK){
                if  (sInstance==null) sInstance= new WeatherDataRepository(context.getApplicationContext());
            }
        }
        return sInstance;
    }

    public WeatherDataRepository(Context context) {
        networkUtils=NetworkUtils.getInstance(context);
        appDataBase=AppDataBase.getInstance(context);
        appExecutor=AppExecutor.getInstance();
    }

private void updateWeatherInfo(WeatherInfo weatherInfo){
        appExecutor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDataBase.weatherInfoDao().deleteAllWeatherInfo();
                appDataBase.weatherInfoDao().addWeatherInfo(weatherInfo);
            }
        });
  }

  private void  updateForecastsList(ForecastLists forecastLists){
       appExecutor.getDiskIO().execute(new Runnable() {
           @Override
           public void run() {
               appDataBase.ForecastsListsDao().addForecastsList(forecastLists);
           }
       });
  }
    public LiveData <WeatherInfo> getWeatherInfo(){
        final LiveData <WeatherInfo> weatherInfoLiveData =appDataBase.weatherInfoDao().getWeatherInfo();
        mWeatherInfoCall=networkUtils.getApiInterface().getWeatherInfo(networkUtils.getQueryMap());
        mWeatherInfoCall.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if(response.code()==200){
                    WeatherInfo weatherInfo=response.body();
                    if (weatherInfo !=null){
                    updateWeatherInfo(weatherInfo);
                }
                }
            }
            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
        return weatherInfoLiveData;
    }
    public LiveData <ForecastLists> getForecastsInfo(){
        final LiveData <ForecastLists> forecastListsLiveData =appDataBase.ForecastsListsDao().getForecastsList();
        mForecastsCall=networkUtils.getApiInterface().getForecast(networkUtils.getQueryMap());
        mForecastsCall.enqueue(new Callback<weatherForecasts>() {
            @Override
            public void onResponse(Call<weatherForecasts> call, Response<weatherForecasts> response) {
                if(response.code()==200){
                    weatherForecasts weatherForecasts=response.body();
                    if(weatherForecasts!= null){
                    ForecastLists forecastLists = OpenWeatherDataParser.getForecastsDataFromWeatherForecasts(weatherForecasts);
                    updateForecastsList(forecastLists);
                }
                }
            }
            @Override
            public void onFailure(Call<weatherForecasts> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
        return forecastListsLiveData;
    }
    public void cancelRequests(){
        mForecastsCall.cancel();
        mWeatherInfoCall.cancel();
    }
}
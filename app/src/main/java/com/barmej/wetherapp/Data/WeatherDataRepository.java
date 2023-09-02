package com.barmej.wetherapp.Data;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barmej.wetherapp.Data.DataBase.AppDataBase;
import com.barmej.wetherapp.Data.Entity.ForecastLists;
import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.barmej.wetherapp.Data.Entity.weatherForecasts;
import com.barmej.wetherapp.Data.network.NetworkUtils;
import com.barmej.wetherapp.Utils.OpenWeatherDataParser;

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
        weatherInfoMutableLiveData=new MutableLiveData<>();
        forecastListsMutableLiveData=new MutableLiveData<>();
        appDataBase=AppDataBase.getInstance(context);
    }

private void updateWeatherInfo(WeatherInfo weatherInfo){
  appDataBase.weatherInfoDao().addWeatherInfo(weatherInfo);
  }

  private void  updateForecastsList(ForecastLists forecastLists){
        appDataBase.ForecastsListsDao().addForecastsList(forecastLists);
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

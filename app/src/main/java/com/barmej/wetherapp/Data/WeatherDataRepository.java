package com.barmej.wetherapp.Data;

import android.content.Context;
import android.media.MediaSync;
import android.view.View;
import android.widget.Toast;

import com.barmej.wetherapp.Data.Entity.ForecastLists;
import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.barmej.wetherapp.Data.Entity.weatherForecasts;
import com.barmej.wetherapp.Data.network.NetworkUtils;
import com.barmej.wetherapp.Ui.Activities.MainActivity;
import com.barmej.wetherapp.Utils.OpenWeatherDataParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataRepository {
    private static WeatherDataRepository sInstance;
    private static final Object LOCK=new Object();
    private static Context context;
    private NetworkUtils networkUtils;
    private Call<WeatherInfo> mWeatherInfoCall;
    private Call<weatherForecasts> mForecastsCall;
    private static final String TAG =WeatherDataRepository.class.getSimpleName();

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
    }

    public void getWeatherInfo(final OnDataDeliveryListener<WeatherInfo> onDataDeliveryListener ){
        mWeatherInfoCall=networkUtils.getApiInterface().getWeatherInfo(networkUtils.getQueryMap());
        mWeatherInfoCall.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                if(response.code()==200){
                    WeatherInfo weatherInfo=response.body();
                    if (weatherInfo !=null){
                    onDataDeliveryListener.OnDataDelivery(weatherInfo);
                }
                }
            }
            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
                onDataDeliveryListener.OnErrorOccurred(t);
            }
        });
    }
    public void getForecastsInfo(final OnDataDeliveryListener<ForecastLists> onDataDeliveryListener){
        mForecastsCall=networkUtils.getApiInterface().getForecast(networkUtils.getQueryMap());
        mForecastsCall.enqueue(new Callback<weatherForecasts>() {
            @Override
            public void onResponse(Call<weatherForecasts> call, Response<weatherForecasts> response) {
                if(response.code()==200){
                    weatherForecasts weatherForecasts=response.body();
                    if(weatherForecasts!= null){
                    ForecastLists forecastLists = OpenWeatherDataParser.getForecastsDataFromWeatherForecasts(weatherForecasts);
                    onDataDeliveryListener.OnDataDelivery(forecastLists);

                }
                }
            }
            @Override
            public void onFailure(Call<weatherForecasts> call, Throwable t) {
                onDataDeliveryListener.OnErrorOccurred(t);
            }
        });
    }
    public void cancelRequests(){
        mForecastsCall.cancel();
        mWeatherInfoCall.cancel();
    }
}

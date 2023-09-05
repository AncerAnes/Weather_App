package com.barmej.wetherapp.data.network;

import com.barmej.wetherapp.data.entity.WeatherInfo;
import com.barmej.wetherapp.data.entity.weatherForecasts;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface OpenWeatherApiInterface {
    String WEATHER_ENDPOINT="weather";
    String FORECAST_ENDPOINT="forecast";

    @GET(WEATHER_ENDPOINT)
    Call<WeatherInfo> getWeatherInfo(@QueryMap Map <String,String> queryParams);

    @GET(FORECAST_ENDPOINT)
    Call<weatherForecasts> getForecast(@QueryMap Map <String,String> queryParams);
}

package com.barmej.wetherapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barmej.wetherapp.Data.Entity.ForecastLists;
import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.barmej.wetherapp.Data.WeatherDataRepository;

public class MainViewModel extends AndroidViewModel {
    private WeatherDataRepository weatherDataRepository;
    private LiveData<WeatherInfo> mWeatherInfoLiveData;
    private LiveData <ForecastLists> mForecastListsLiveData;
    public MainViewModel(@NonNull Application application) {
        super(application);
        weatherDataRepository=WeatherDataRepository.getInstance(application);
        mWeatherInfoLiveData= weatherDataRepository.getWeatherInfo();
        mForecastListsLiveData=weatherDataRepository.getForecastsInfo();
    }

    public LiveData<WeatherInfo> getWeatherInfoLiveData() {
        return mWeatherInfoLiveData;
    }


    public LiveData<ForecastLists> getForecastListsLiveData() {
        return mForecastListsLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        weatherDataRepository.cancelRequests();
    }
}

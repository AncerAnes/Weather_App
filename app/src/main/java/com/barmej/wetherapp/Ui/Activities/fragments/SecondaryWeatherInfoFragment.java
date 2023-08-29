package com.barmej.wetherapp.Ui.Activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.WeatherUtils;

public class SecondaryWeatherInfoFragment extends Fragment {
    private TextView humidityTextView;
    private TextView pressureTextView;
    private TextView windTextView;
    WeatherInfo mWeatherInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_secondary_weather_info,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        View mainView = getView();
        humidityTextView =mainView.findViewById(R.id.humidity);
        pressureTextView=mainView.findViewById(R.id.pressure);
        windTextView =mainView.findViewById(R.id.wind_measurement);
        super.onActivityCreated(savedInstanceState);
        showWeatherInfo();
    }
    public void updateWeatherInfo(WeatherInfo weatherInfo){
        mWeatherInfo=weatherInfo;
        showWeatherInfo();
    }

    private void showWeatherInfo() {
        if(mWeatherInfo == null){
            return;
        }
        float humidity = (float) mWeatherInfo.getMain().getHumidity();
        String humidityString=getString(R.string.format_humidity,humidity);
        humidityTextView.setText(humidityString);

        double windSpeed=mWeatherInfo.getWind().getSpeed();
        double windDirection=mWeatherInfo.getWind().getDeg();
        String windString= WeatherUtils.getFormattedWind(getContext(), windSpeed,windDirection);
        windTextView.setText(windString);

        double pressure = mWeatherInfo.getMain().getPressure();
        String pressureString=getString(R.string.format_pressure,pressure);
        pressureTextView.setText(pressureString);


    }
}


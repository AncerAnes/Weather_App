package com.barmej.wetherapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.barmej.wetherapp.Data.WeatherInfo;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.CustomDateUtils;
import com.barmej.wetherapp.Utils.WeatherUtils;

public class PrimaryWeatherInfoFragment extends Fragment {
    private ImageView mIconView;
    private TextView mCityNameTextView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mTempTextView;
    private TextView mHighLowTempView;
    private WeatherInfo mWeatherInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_primary_weather_info,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        View mainView = getView();
        if (mainView==null){
            return;
        }
        mIconView= mainView. findViewById(R.id.weather_icon);
        mCityNameTextView=mainView.findViewById(R.id.city);
        mDateView=mainView.findViewById(R.id.date);
        mDescriptionView=mainView.findViewById(R.id.weather_description);
        mTempTextView=mainView.findViewById(R.id.temperature);
        mHighLowTempView=mainView.findViewById(R.id.high_low_temperature);
        super.onActivityCreated(savedInstanceState);
        showWeatherInfo();
    }

    public void updateWeatherInfo(WeatherInfo weatherInfo){
     mWeatherInfo=weatherInfo;
        showWeatherInfo();
    }


    @SuppressLint("StringFormatMatches")
    private void showWeatherInfo() {
        if(mWeatherInfo == null){
            return;
        }
        int weatherImageId = WeatherUtils.getWeatherIcon(mWeatherInfo.getWeather().get(0).getIcon());
        mIconView.setImageResource(weatherImageId);
        String cityName = mWeatherInfo.getName();
        mCityNameTextView.setText(cityName);
        String dateString = CustomDateUtils.getFriendlyDateString(getContext(),mWeatherInfo.getDt(),false);
        mDateView.setText(dateString);
        String description = mWeatherInfo.getWeather().get(0).getDescription();
        mDescriptionView.setText(description);
        String temperatureString = getString(R.string.format_temperature,mWeatherInfo.getMain().getTemp());
        mTempTextView.setText(temperatureString);
        mHighLowTempView.setText(getString(R.string.high_low_temperature, mWeatherInfo.getMain().getTempMax(), mWeatherInfo.getMain().getTempMin() ));

    }
}

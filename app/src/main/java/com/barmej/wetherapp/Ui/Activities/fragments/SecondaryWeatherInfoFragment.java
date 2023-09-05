package com.barmej.wetherapp.Ui.Activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.barmej.wetherapp.data.entity.WeatherInfo;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.databinding.FragmentPrimaryWeatherInfoBinding;
import com.barmej.wetherapp.databinding.FragmentSecondaryWeatherInfoBinding;
import com.barmej.wetherapp.utils.WeatherUtils;
import com.barmej.wetherapp.ViewModel.MainViewModel;

public class SecondaryWeatherInfoFragment extends Fragment {
    private FragmentSecondaryWeatherInfoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_secondary_weather_info,container,false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setWeatherInfo(mainViewModel.getWeatherInfoLiveData());
    }
    }
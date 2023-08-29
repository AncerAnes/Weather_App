package com.barmej.wetherapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.barmej.wetherapp.Data.Forecast;
import com.barmej.wetherapp.Adapters.DaysForecastAdapter;
import com.barmej.wetherapp.Adapters.HoursForecastAdapter;
import com.barmej.wetherapp.Data.ForecastLists;
import com.barmej.wetherapp.Data.Weather;
import com.barmej.wetherapp.Data.WeatherInfo;
import com.barmej.wetherapp.Data.weatherForecasts;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.CustomDateUtils;
import com.barmej.wetherapp.Utils.OpenWeatherDataParser;
import com.barmej.wetherapp.Utils.sharedPreferenceHelper;
import com.barmej.wetherapp.fragments.PrimaryWeatherInfoFragment;
import com.barmej.wetherapp.fragments.SecondaryWeatherInfoFragment;
import com.barmej.wetherapp.network.NetworkUtils;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private HoursForecastAdapter mHoursForecastAdapter;
    private DaysForecastAdapter mDaysForecastsAdapter;

    private RecyclerView mHoursForecastsRecyclerView;
    private RecyclerView mDaysForecastRecyclerView;
    private FragmentManager mFragmentManager;
    private FrameLayout mHeaderLayout;
    private HeaderFragmentAdapter headerFragmentAdapter;
    private ViewPager viewPager;
    private NetworkUtils mNetworkUtils ;
    private static final String TAG =MainActivity.class.getSimpleName();
    private static final int Requests_Settings =0;
    private Call <WeatherInfo> mWeatherInfoCall;
    private Call<weatherForecasts> mForecastsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for scrolling between fragments
        mFragmentManager = getSupportFragmentManager();
        mHeaderLayout= (FrameLayout) findViewById(R.id.header);
       viewPager =(ViewPager) findViewById(R.id.pager);
       headerFragmentAdapter = new HeaderFragmentAdapter(mFragmentManager);
       viewPager.setAdapter(headerFragmentAdapter);
       TabLayout tabLayout =findViewById(R.id.indicator);
       tabLayout.setupWithViewPager(viewPager);
       //Recycler view and adapter for Hours Forecasts
        mHoursForecastAdapter = new HoursForecastAdapter(this);
        mHoursForecastsRecyclerView = findViewById(R.id.rv_hours_forecast);
        mHoursForecastsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mHoursForecastsRecyclerView.setAdapter(mHoursForecastAdapter);
        //Recycler view and adapter for days Forecasts
        mDaysForecastsAdapter = new DaysForecastAdapter(this);
        mDaysForecastRecyclerView = findViewById(R.id.rv_days_forecast);
        mDaysForecastRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDaysForecastRecyclerView.setAdapter(mDaysForecastsAdapter);

        mNetworkUtils = NetworkUtils.getInstance(this);

        requestForecastsInfo();
        requestWeatherInfo();

        mHeaderLayout.setVisibility(View.INVISIBLE);
        mDaysForecastRecyclerView.setVisibility(View.INVISIBLE);
        mHoursForecastsRecyclerView.setVisibility(View.INVISIBLE);

    }
    //request and response of data in background thread
    private void requestWeatherInfo(){
        mWeatherInfoCall=mNetworkUtils.getApiInterface().getWeatherInfo(mNetworkUtils.getQueryMap());
        mWeatherInfoCall.enqueue(new Callback<WeatherInfo>() {
            @Override
            public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
              if(response.code()==200){
                  WeatherInfo weatherInfo=response.body();
                  headerFragmentAdapter.updateData(weatherInfo);
                  mHeaderLayout.setVisibility(View.VISIBLE);
              }
            }
            @Override
            public void onFailure(Call<WeatherInfo> call, Throwable t) {
              Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestForecastsInfo() {
     mForecastsCall=mNetworkUtils.getApiInterface().getForecast(mNetworkUtils.getQueryMap());
     mForecastsCall.enqueue(new Callback<weatherForecasts>() {
         @Override
         public void onResponse(Call<weatherForecasts> call, Response<weatherForecasts> response) {
             if(response.code()==200){
                 weatherForecasts weatherForecasts=response.body();
              ForecastLists forecastLists =OpenWeatherDataParser.getForecastsDataFromWeatherForecasts(weatherForecasts);
              mHoursForecastAdapter.updateData(forecastLists.getHoursForecasts());
              mDaysForecastsAdapter.updateData(forecastLists.getDaysForecasts());
              mHoursForecastsRecyclerView.setVisibility(View.VISIBLE);
              mDaysForecastRecyclerView.setVisibility(View.VISIBLE);
             }
         }
         @Override
         public void onFailure(Call<weatherForecasts> call, Throwable t) {
             Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
         }
     });
    }

    //for scrolling between fragments
    class HeaderFragmentAdapter extends FragmentPagerAdapter{
        List <Fragment> fragments;
        public HeaderFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            fragments = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                    case 0:
                    return new PrimaryWeatherInfoFragment();
                    case 1:
                    return new SecondaryWeatherInfoFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container,position);
            fragments.add(position,fragment);
            return fragment;
        }
        void updateData(WeatherInfo weatherInfo){
            ((PrimaryWeatherInfoFragment) fragments.get(0)).updateWeatherInfo(weatherInfo);
            ((SecondaryWeatherInfoFragment) fragments.get(1)).updateWeatherInfo(weatherInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.action_settings){
            startActivityForResult(new Intent(this,SettingsActivity.class),Requests_Settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Requests_Settings && resultCode == RESULT_OK){
            requestForecastsInfo();
            requestWeatherInfo();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mForecastsCall.cancel();
        mWeatherInfoCall.cancel();

    }
}


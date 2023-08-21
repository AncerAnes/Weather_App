package com.barmej.wetherapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.barmej.wetherapp.Data.Forecast;
import com.barmej.wetherapp.Adapters.DaysForecastAdapter;
import com.barmej.wetherapp.Adapters.HoursForecastAdapter;
import com.barmej.wetherapp.Data.ForecastLists;
import com.barmej.wetherapp.Data.Weather;
import com.barmej.wetherapp.Data.WeatherInfo;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.OpenWeatherDataParser;
import com.barmej.wetherapp.fragments.PrimaryWeatherInfoFragment;
import com.barmej.wetherapp.fragments.SecondaryWeatherInfoFragment;
import com.barmej.wetherapp.network.NetworkUtils;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private HoursForecastAdapter mHoursForecastAdapter;
    private DaysForecastAdapter mDaysForecastsAdapter;

    private RecyclerView mHoursForecastsRecyclerView;
    private RecyclerView mDaysForecastRecyclerView;
    private FragmentManager mFragmentManager;
    private HeaderFragmentAdapter headerFragmentAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for scrolling between fragments
        mFragmentManager = getSupportFragmentManager();
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

        requestForecastsInfo();
        getRequestInfo();
    }
    //request and response of data in background thread
    private void getRequestInfo(){
      new weatherDataGetTask().execute();
    }
    class weatherDataGetTask extends AsyncTask<Void, Integer, WeatherInfo> {

        @Override
        protected WeatherInfo doInBackground(Void... voids) {
            URL weatherUrl = NetworkUtils.getWeatherUrl(MainActivity.this);
            WeatherInfo weatherInfo;
            try {
                String weatherJsonResponse=NetworkUtils.getResponseFromHttpUrl(weatherUrl);
                weatherInfo= OpenWeatherDataParser.getWeatherInfoObjectFromJson(weatherJsonResponse);
                return weatherInfo;
            }catch (IOException |JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherInfo weatherInfo) {
           if(weatherInfo != null){
               headerFragmentAdapter.updateData(weatherInfo);
           }
        }
    }
    private void requestForecastsInfo() {
        new ForecastsDataPullTask().execute();
    }
    class ForecastsDataPullTask extends AsyncTask<Void, Void, ForecastLists>{

        @Override
        protected ForecastLists doInBackground(Void... voids) {
            URL forecastsRequestUrl=NetworkUtils.getForecastUrl(MainActivity.this);
            ForecastLists forecastLists=null;

            try {
               String forecastsJsonResponse=NetworkUtils.getResponseFromHttpUrl(forecastsRequestUrl);
               forecastLists=OpenWeatherDataParser.getForecastsDataFromJson(forecastsJsonResponse);
               return forecastLists;
            }

            catch (IOException | JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ForecastLists forecastLists) {
            super.onPostExecute(forecastLists);
            if (forecastLists != null
                    && forecastLists.getHoursForecasts() != null
                    && forecastLists.getDaysForecasts() != null) {
                mHoursForecastAdapter.updateData(forecastLists.getHoursForecasts());
                mDaysForecastsAdapter.updateData(forecastLists.getDaysForecasts());
            }
        }
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
}
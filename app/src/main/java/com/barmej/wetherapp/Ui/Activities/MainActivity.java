package com.barmej.wetherapp.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.barmej.wetherapp.Data.OnDataDeliveryListener;
import com.barmej.wetherapp.Data.WeatherDataRepository;
import com.barmej.wetherapp.Ui.Activities.Adapters.DaysForecastAdapter;
import com.barmej.wetherapp.Ui.Activities.Adapters.HoursForecastAdapter;
import com.barmej.wetherapp.Data.Entity.ForecastLists;
import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.barmej.wetherapp.Data.Entity.weatherForecasts;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.OpenWeatherDataParser;
import com.barmej.wetherapp.Ui.Activities.fragments.PrimaryWeatherInfoFragment;
import com.barmej.wetherapp.Ui.Activities.fragments.SecondaryWeatherInfoFragment;
import com.barmej.wetherapp.Data.network.NetworkUtils;
import com.barmej.wetherapp.ViewModel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

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

        requestForecastsInfo();
        requestWeatherInfo();

        mHeaderLayout.setVisibility(View.INVISIBLE);
        mDaysForecastRecyclerView.setVisibility(View.INVISIBLE);
        mHoursForecastsRecyclerView.setVisibility(View.INVISIBLE);

    }
    //request and response of data in background thread
    private void requestWeatherInfo(){
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
         mainViewModel.getWeatherInfoLiveData().observe(this, new Observer<WeatherInfo>() {
            @Override
            public void onChanged(WeatherInfo weatherInfo) {
                mHeaderLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestForecastsInfo() {
        MainViewModel mainViewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getForecastListsLiveData().observe(this, new Observer<ForecastLists>() {
            @Override
            public void onChanged(ForecastLists forecastLists) {
                //LiveData is the broker between the server and MainActivity,it used in place of The callBack of OnDataDeliveryListener because this methode used when the activity alone request data,if there is a dataBase it does not notify with changed
                mHoursForecastAdapter.updateData(forecastLists.getHoursForecasts());
                mDaysForecastsAdapter.updateData(forecastLists.getDaysForecasts());
                mHoursForecastsRecyclerView.setVisibility(View.VISIBLE);
                mDaysForecastRecyclerView.setVisibility(View.VISIBLE);
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
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
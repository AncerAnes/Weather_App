package com.barmej.wetherapp.Ui.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.wetherapp.data.entity.Forecast;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.databinding.ItemHourForecastBinding;
import com.barmej.wetherapp.utils.CustomDateUtils;
import com.barmej.wetherapp.utils.WeatherUtils;

import java.util.List;

public class HoursForecastAdapter extends RecyclerView.Adapter<HoursForecastAdapter.ForecastAdapterViewHolder> {
    //context Used to access the the UI and app resources
    public final Context mContext;
   public List <Forecast> mForecasts;
    public HoursForecastAdapter(@NonNull Context context) {
        mContext = context;
    }
    @NonNull
    @Override
    public HoursForecastAdapter.ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHourForecastBinding binding= DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.item_hour_forecast,parent,false);
        return new ForecastAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HoursForecastAdapter.ForecastAdapterViewHolder holder, int position) {
      Object obj= mForecasts.get(position);
       holder.Bind(obj);
    }

    @Override
    public int getItemCount() {
        if (mForecasts == null) {
            return 0;
        } else {
            return mForecasts.size();
        }
    }
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{
        ItemHourForecastBinding binding;
        public ForecastAdapterViewHolder( ItemHourForecastBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        void Bind(Object object){
          binding.setVariable(BR.forecast,object);
          binding.executePendingBindings();
        }
    }
    public void updateData(List<Forecast> forecasts) {
        this.mForecasts = forecasts;
        notifyDataSetChanged();
    }
    }


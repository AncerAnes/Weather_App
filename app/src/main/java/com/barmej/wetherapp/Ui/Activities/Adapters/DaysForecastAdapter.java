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
import com.barmej.wetherapp.databinding.ItemDayForecastBinding;
import com.barmej.wetherapp.utils.CustomDateUtils;
import com.barmej.wetherapp.utils.WeatherUtils;

import java.util.List;

public class DaysForecastAdapter extends RecyclerView.Adapter<DaysForecastAdapter.ForecastAdapterViewHolder>  {
    public final Context mContext;
    public List <List<Forecast>> mForecasts;
    public DaysForecastAdapter(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public DaysForecastAdapter.ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDayForecastBinding binding= DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.item_day_forecast,parent,false);
        return new ForecastAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysForecastAdapter.ForecastAdapterViewHolder holder, int position) {
        Object obj= mForecasts.get(position).get(0);
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
        ItemDayForecastBinding binding;
        public ForecastAdapterViewHolder(ItemDayForecastBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        void Bind(Object object){
            binding.setVariable(BR.forecast,object);
        }
    }
    public void updateData(List<List<Forecast>> forecasts) {
        this.mForecasts = forecasts;
        notifyDataSetChanged();
    }
}

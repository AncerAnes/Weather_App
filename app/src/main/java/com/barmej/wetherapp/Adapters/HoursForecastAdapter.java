package com.barmej.wetherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.wetherapp.Data.Forecast;
import com.barmej.wetherapp.Data.WeatherInfo;
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.CustomDateUtils;
import com.barmej.wetherapp.Utils.WeatherUtils;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hour_forecast,parent,false);
        ForecastAdapterViewHolder forecastAdapterViewHolder = new ForecastAdapterViewHolder(view);
        return forecastAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HoursForecastAdapter.ForecastAdapterViewHolder holder, int position) {
       Forecast forecast= mForecasts.get(position);
        int weatherImageId = WeatherUtils.getWeatherIcon(forecast.getWeather().get(0).getIcon());
        holder.iconImageView.setImageResource(weatherImageId);
        String hourClockString = CustomDateUtils.getHourOfDayUTCTime(forecast.getDt());
        holder.timeTextView.setText(hourClockString);
        double highTemperature = forecast.getMain().getTempMax();
        String highTemperatureString = mContext.getString(R.string.format_temperature, highTemperature);
        holder.temperatureTextView.setText(highTemperatureString);
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
        final ImageView iconImageView;
        final TextView timeTextView;
        final TextView temperatureTextView;

        public ForecastAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
           iconImageView=itemView.findViewById(R.id.weather_icon);
           timeTextView=itemView.findViewById(R.id.time);
           temperatureTextView=itemView.findViewById(R.id.temperature);
        }
    }
    public void updateData(List<Forecast> forecasts) {
        this.mForecasts = forecasts;
        notifyDataSetChanged();
    }
    }


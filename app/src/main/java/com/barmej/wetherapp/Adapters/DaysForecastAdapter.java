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
import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.CustomDateUtils;
import com.barmej.wetherapp.Utils.WeatherUtils;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_day_forecast, parent, false);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysForecastAdapter.ForecastAdapterViewHolder holder, int position) {
        Forecast forecast= mForecasts.get(position).get(0);
        int weatherImageId = WeatherUtils.getWeatherIcon(forecast.getWeather().get(0).getIcon());
        holder.iconImageView.setImageResource(weatherImageId);
        String dateString = CustomDateUtils.getFriendlyDateString(mContext,forecast.getDt(),false);
        holder.dateTextView.setText(dateString);
        String description = forecast.getWeather().get(0).getDescription();
        holder.descriptionTextView.setText(description);
        double highTemperature = forecast.getMain().getTempMax();
        String highTemperatureString = mContext.getString(R.string.format_temperature, highTemperature);
        holder.highTempTextView.setText(highTemperatureString);
        double lowTemperature = forecast.getMain().getTempMin();
        String lowTemperatureString = mContext.getString(R.string.format_temperature, lowTemperature);
        holder.lowTempTextView.setText(lowTemperatureString);
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
        final TextView dateTextView;
        final TextView descriptionTextView;
        final  TextView highTempTextView;
        final TextView lowTempTextView;

        public ForecastAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView =itemView.findViewById(R.id.weather_icon);
            dateTextView =itemView.findViewById(R.id.date);
            descriptionTextView =itemView.findViewById(R.id.weather_description);
            highTempTextView =itemView.findViewById(R.id.high_temperature);
            lowTempTextView =itemView.findViewById(R.id.low_temperature);
        }
    }
    public void updateData(List<List<Forecast>> forecasts) {
        this.mForecasts = forecasts;
        notifyDataSetChanged();
    }
}

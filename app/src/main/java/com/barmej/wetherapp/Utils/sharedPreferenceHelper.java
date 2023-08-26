package com.barmej.wetherapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.barmej.wetherapp.R;

public class sharedPreferenceHelper {
    public static String getPreferredWeatherLocation(Context context){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        String locationKey= context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);
        return sp.getString(locationKey,defaultLocation);
    }
    public static String getMeasurementSystem(Context context){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(context);
        String unitKey= context.getString(R.string.pref_units_key);
        String defaultUnit= context.getString(R.string.pref_unit_metric);
        return sp.getString(unitKey,defaultUnit);
    }
}

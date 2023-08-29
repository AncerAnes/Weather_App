package com.barmej.wetherapp.network;

import android.app.GameState;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.barmej.wetherapp.R;
import com.barmej.wetherapp.Utils.sharedPreferenceHelper;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import kotlin.jvm.Synchronized;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    private static String Tag=NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private static final String WEATHER_ENDPOINT = "weather";
    private static final String FORECAST_ENDPOINT = "forecast";

    private static final String QUERY_PARAM = "q";
    private static final String FORMAT_PARAM = "mode";
    private static final String UNITS_PARAM = "units";
    private static final String LANG_PARAM = "lang";
    private static final String APP_ID_PARAM = "appid";

    private static final String FORMAT = "json";

    private static final String METRIC = "metric";
    private static final String IMPERIAL = "imperial";
    private static NetworkUtils sInstance;
    private static final Object LOCK= new Object();
    private static Context mContext;
    private OpenWeatherApiInterface openWeatherApiInterface;

    public static   NetworkUtils getInstance(Context context){
      if (sInstance==null) {
          synchronized (LOCK){
               if  (sInstance==null) sInstance= new NetworkUtils(context);
          }
      }
     return sInstance;
    }
    private NetworkUtils(Context context){
        mContext=context.getApplicationContext();
        HttpLoggingInterceptor interceptor =new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        openWeatherApiInterface=retrofit.create(OpenWeatherApiInterface.class);
    }
    public OpenWeatherApiInterface getApiInterface(){
        return openWeatherApiInterface;
    }

    public  HashMap <String,String> getQueryMap(){
        HashMap <String,String> map =new HashMap<>();
        map.put(QUERY_PARAM, sharedPreferenceHelper.getPreferredWeatherLocation(mContext));
        map.put(FORMAT_PARAM,FORMAT);
        map.put(UNITS_PARAM,sharedPreferenceHelper.getMeasurementSystem(mContext));
        map.put(LANG_PARAM, Locale.getDefault().getLanguage());
        map.put(APP_ID_PARAM,mContext.getString(R.string.api_key));
       return map;
    }
}
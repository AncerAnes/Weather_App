package com.barmej.wetherapp.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.barmej.wetherapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

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
    private RequestQueue mRequestQueue;
    public static NetworkUtils getInstance(Context context){
      if (sInstance==null) {
          synchronized (LOCK){
               if  (sInstance==null) sInstance= new NetworkUtils(context);
          }
      }
     return sInstance;
    }
    private NetworkUtils(Context context){
      mContext=context.getApplicationContext();
      mRequestQueue=getRequestQueue();
    }
    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue= Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
    public <T>void addRequestQueue(Request<T>request){
        getRequestQueue().add(request);
    }
    public void cancelRequests(String tag){
      getRequestQueue().cancelAll(tag);
    }

    public static URL getWeatherUrl(Context context){
        return buildUrl(context,WEATHER_ENDPOINT);
    }

    public static URL getForecastUrl(Context context){
        return buildUrl(context,FORECAST_ENDPOINT);
    }

    private static URL buildUrl(Context context,String endPoint){
        Uri.Builder uriBuilder=Uri.parse(BASE_URL+endPoint).buildUpon();
        Uri uri=uriBuilder.appendQueryParameter(QUERY_PARAM,context.getString(R.string.pref_location_default))
                .appendQueryParameter(FORMAT_PARAM,FORMAT)
                .appendQueryParameter(UNITS_PARAM,METRIC)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .appendQueryParameter(APP_ID_PARAM,context.getString(R.string.api_key))
                .build();
        try {
            URL url =new URL(uri.toString());
            Log.d(Tag,"url:"+url);
            return url;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
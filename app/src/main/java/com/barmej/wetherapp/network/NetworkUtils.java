package com.barmej.wetherapp.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

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

    public static String getResponseFromHttpUrl (URL url) throws IOException {
        // this class It's about http client enabling us to make http connections and send and resev data form internet.
        HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
        //to get data from server
        httpURLConnection.setRequestMethod("GET");
        //to do the connection and sending the request
        httpURLConnection.connect();
        try {
        //to read the response
            InputStream inputStream=httpURLConnection.getInputStream();
        //to read data from input stream and set it in String
            Scanner scanner=new Scanner(inputStream);
            //allow you to put a regular  expression string,it stops when the expression ending with A.
            scanner.useDelimiter("\\A");
            //Checks if there is more input data to read from the scanner
            boolean hasInput= scanner.hasNext();
            String response=null;
            if (hasInput){
                response =scanner.next();
            }
            scanner.close();
            Log.d(Tag,"response:"+response);
            return response;
        }finally {
            //to make end to connection if there are problems
            httpURLConnection.disconnect();
        }
    }
}

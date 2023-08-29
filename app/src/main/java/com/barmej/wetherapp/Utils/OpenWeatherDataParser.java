package com.barmej.wetherapp.Utils;

import com.barmej.wetherapp.Data.Entity.Forecast;
import com.barmej.wetherapp.Data.Entity.ForecastLists;
import com.barmej.wetherapp.Data.Entity.Main;
import com.barmej.wetherapp.Data.Entity.Weather;
import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.barmej.wetherapp.Data.Entity.Wind;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.barmej.wetherapp.Data.Entity.weatherForecasts;
public class OpenWeatherDataParser {
    private static final String OWM_MESSAGE_CODE = "cod";
    private static final String OWM_CITY = "city";
    private static final String OWM_CITY_NAME = "name";
    private static final String OWM_LIST = "list";
    private static final String OWM_DATE = "dt";
    private static final String OWM_DATE_TEXT = "dt_txt";
    private static final String OWM_WIND = "wind";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";
    private static final String OWM_MAIN = "main";
    private static final String OWM_TEMPERATURE = "temp";
    private static final String OWM_MAX = "temp_max";
    private static final String OWM_MIN = "temp_min";
    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_DESCRIPTION = "description";
    private static final String OWM_WEATHER_ICON = "icon";

    public static WeatherInfo getWeatherInfoObjectFromJson(JSONObject jsonObject) throws JSONException {
        // Weather description is in a child array called "weather", which is 1 element long.
        JSONObject weatherJsonObject=jsonObject.getJSONArray(OWM_WEATHER).getJSONObject(0);
        // Temperatures are sent by OpenWeatherMap in a child object called Main
        JSONObject mainJsonObject= jsonObject.getJSONObject(OWM_MAIN);
        // Wind speed and direction are wrapped in a Wind object
        JSONObject windJsonObject=jsonObject.getJSONObject(OWM_WIND);
        // Get data from Json and assign it to Java object
        WeatherInfo weatherInfo=new WeatherInfo();

        weatherInfo.setDt(jsonObject.getLong(OWM_DATE));

        Main main =new Main();
        main.setTemp(mainJsonObject.getDouble(OWM_TEMPERATURE));
        main.setTempMax(mainJsonObject.getDouble(OWM_MAX));
        main.setTempMin(mainJsonObject.getDouble(OWM_MIN));
        main.setPressure(mainJsonObject.getDouble(OWM_PRESSURE));
        main.setHumidity(mainJsonObject.getDouble(OWM_HUMIDITY));
        weatherInfo.setMain(main);

        Wind wind=new Wind();
        wind.setSpeed(windJsonObject.getDouble(OWM_WINDSPEED));
        wind.setDeg(windJsonObject.getDouble(OWM_WIND_DIRECTION));
        weatherInfo.setWind(wind);

        List<Weather> weatherList = new ArrayList<>();
        Weather weather = new Weather();
        weather.setDescription(weatherJsonObject.getString(OWM_WEATHER_DESCRIPTION));
        weather.setIcon(weatherJsonObject.getString(OWM_WEATHER_ICON));

        weatherList.add(weather);
        weatherInfo.setWeather(weatherList);
        weatherInfo.setName(jsonObject.has(OWM_CITY_NAME) ? jsonObject.getString(OWM_CITY_NAME) : "");

        return weatherInfo;
    }

    public static ForecastLists getForecastsDataFromWeatherForecasts(weatherForecasts weatherForecasts)  {
        List<Forecast> hoursForecasts=new ArrayList<>();
        LinkedHashMap<String, List<Forecast>> daysForecasts = new LinkedHashMap<>();

        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDay = df.format(new Date());
        int hoursForecastsCount = 0;

        for ( int i=0;i<weatherForecasts.getList().size();i++){
            Forecast forecast =weatherForecasts.getList().get(i);
            if (hoursForecastsCount++ < 8) {
                hoursForecasts.add(forecast);
            }
            String date = forecast.getDtTxt().split(" ")[0];
            if (!date.equals(currentDay)) {
                if (daysForecasts.containsKey(date)) {
                    List<Forecast> forecasts = daysForecasts.get(date);
                    assert forecasts != null;
                    forecasts.add(forecast);
                } else {
                    List<Forecast> forecasts = new ArrayList<>();
                    forecasts.add(forecast);
                    daysForecasts.put(date, forecasts);
                }
            }
        }
        ForecastLists forecastsData = new ForecastLists();
        forecastsData.setHoursForecasts(hoursForecasts);
        List<List<Forecast>> listOfDaysForecasts = new ArrayList<>();
        for (Map.Entry entry : daysForecasts.entrySet()) {
            listOfDaysForecasts.add((List<Forecast>) entry.getValue());
        }
        forecastsData.setDaysForecasts(listOfDaysForecasts);

        return forecastsData;
        }
    }


package com.barmej.wetherapp.converters;
import androidx.room.TypeConverter;
import com.barmej.wetherapp.Data.Entity.Weather;
import com.barmej.wetherapp.Data.Entity.WeatherInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class WeatherListConverter {
    @TypeConverter
    public static List<Weather> fromString(String value){
    Type listType=new  TypeToken <List<Weather>>(){}.getType();
    return new Gson().fromJson(value,listType);
   }

   @TypeConverter
   public static String fromWeatherList(List<Weather> list){
        return new Gson().toJson(list);
   }
}
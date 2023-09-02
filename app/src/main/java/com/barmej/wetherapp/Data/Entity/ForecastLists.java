package com.barmej.wetherapp.Data.Entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity(tableName = "forecasts")
public class ForecastLists {
    private List <Forecast> hoursForecasts = null;
    private List<List<Forecast>> daysForecasts = null;
    @PrimaryKey
    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        public List <Forecast> getHoursForecasts() {
            return hoursForecasts;
        }

        public void setHoursForecasts(List<Forecast> hoursForecasts) {
            this.hoursForecasts = hoursForecasts;
        }

        public List<List<Forecast>> getDaysForecasts() {
            return daysForecasts;
        }

        public void setDaysForecasts(List<List<Forecast>> daysForecasts) {
            this.daysForecasts = daysForecasts;
        }
}

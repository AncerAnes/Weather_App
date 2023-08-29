package com.barmej.wetherapp.Data;

import com.google.gson.annotations.SerializedName;

import kotlin.jvm.Synchronized;

public class Main {
    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    private double temp;
@SerializedName("temp_min")
    private double tempMin;
@SerializedName("temp_max")
    private double tempMax;
    private double pressure;
    private double humidity;
}

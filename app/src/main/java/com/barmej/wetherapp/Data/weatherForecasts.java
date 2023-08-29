package com.barmej.wetherapp.Data;

import java.util.List;

public class weatherForecasts {
    private List<Forecast> list;

    public List<Forecast> getList(){
        return list;
    }

    public void setList(List<Forecast> list) {
        this.list = list;
    }
}

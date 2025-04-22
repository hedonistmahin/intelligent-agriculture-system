package com.example.agri;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather> weather;
    private String name;
    private Sys sys;
    private Wind wind;
    private Rain rain;

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getName() {
        return name;
    }

    public Sys getSys() {
        return sys;
    }

    public Wind getWind() {
        return wind;
    }

    public Rain getRain() {
        return rain;
    }
}

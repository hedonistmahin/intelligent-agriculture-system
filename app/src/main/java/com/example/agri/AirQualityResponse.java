package com.example.agri;

public class AirQualityResponse {
    public ListData[] list;

    public static class ListData {
        public Main main;
        public Components components;
    }

    public static class Main {
        public int aqi;
    }

    public static class Components {
        public float pm2_5;
        public float pm10;
    }
}
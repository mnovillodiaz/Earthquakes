package com.example.android.quakereport.Data;


public class ApiUtils {

    public static final String BASE_URL = "https://earthquake.usgs.gov/";

    public static EarthquakeService getEarthquakeService() {
        return RetrofitClient.getClient(BASE_URL).create(EarthquakeService.class);
    }

}

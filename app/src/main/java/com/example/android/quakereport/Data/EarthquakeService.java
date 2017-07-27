package com.example.android.quakereport.Data;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EarthquakeService {

        @GET("/fdsnws/event/1/query?format=geojson&limit=10")
        Call<EarthquakeDataModel> getEarthquakes(
                @Query("minmag") String minMagnitude,
                @Query("orderby") String orderBy);
    }

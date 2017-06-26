package com.example.android.quakereport;

/**
 * Created by mdiaz on 25/06/17.
 */

public class Earthquake {

    // double for Earthquakes magnitude
    private String magnitude;
    private String place;
    private String date;

    /**
     Constructor to create new data
        @param magnitude
        @param place
        @param date
     **/

    public Earthquake(String magnitude, String place, String date){
        this.magnitude = magnitude;
        this.place= place;
        this.date = date;
    }

    // Getters

    public String getMagnitude() { return magnitude;}
    public String getPlace() {return place;}
    public String getDate() {return date;}

}

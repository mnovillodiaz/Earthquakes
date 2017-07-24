package com.example.android.quakereport;

/**
 * Created by mdiaz on 25/06/17.
 */

public class UiModel {

    private String magnitude;
    private int magnitudeColor;
    private String locationOffset;
    private String primaryLocation;
    private String date;
    private String time;

    /**
     Constructor to create new data
        @param magnitude
        @param magnitudeColor
        @param locationOffset
        @param primaryLocation
        @param date
        @param time
     **/

    public UiModel(String magnitude, int magnitudeColor, String locationOffset,
                   String primaryLocation, String date, String time) {
        this.magnitude = magnitude;
        this.magnitudeColor = magnitudeColor;
        this.locationOffset = locationOffset;
        this.primaryLocation = primaryLocation;
        this.date = date;
        this.time = time;
    }

    // Getters

    public String getMagnitude() {return magnitude;}
    public int getMagnitudeColor() {return magnitudeColor;}
    public String getLocationOffset() {return locationOffset;}
    public String getPrimaryLocation() {return  primaryLocation;};
    public String getDate() {return date;}
    public String getTime() {return time;}

    @Override
    public String toString() {
        return "UiModel{" +
                "magnitude='" + magnitude + '\'' +
                ", magnitudeColor='" + magnitudeColor + '\'' +
                ", locationOffset='" + locationOffset + '\'' +
                ", primaryLocation='" + primaryLocation + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                '}';
    }
}

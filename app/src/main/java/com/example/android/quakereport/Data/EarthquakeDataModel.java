package com.example.android.quakereport.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class EarthquakeDataModel {

    @SerializedName("features")
    @Expose
    private List<Feature> features = null;


    public List<Feature> getFeatures() {
        return features;
    }


    public static class Feature {
        @SerializedName("properties")
        @Expose
        private Properties properties;

        public Properties getProperties() {
            return properties;
        }

    }

    public static class Properties {

        @SerializedName("mag")
        @Expose
        private Double mag;
        @SerializedName("place")
        @Expose
        private String place;
        @SerializedName("time")
        @Expose
        private long time;

        public Double getMag() {
            return mag;
        }

        public String getPlace() {
            return place;
        }

        public long getTime() {
            return time;
        }
    }
}

package com.example.android.quakereport;

import java.util.List;

/**
 * Created by mdiaz on 24.07.17.
 */

public class DataModel {

    public class Response {
        List<Feature> features;
    }

    public class Feature {
        Properties properties;
    }

    public class Properties {
        double mag;
        String place;
        long time;
    }
}

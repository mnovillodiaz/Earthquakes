package com.example.android.quakereport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mdiaz on 15.07.17.
 */

public class EarthquakeTransformer {

    //To separate the place into 2 Strings
    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeTransformer() {}

    public ArrayList<Earthquake> transform(final HttpUtils.Response response) {

        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

            for (int i = 0; i < response.features.size(); i++){
            double mag = response.features.get(i).properties.mag;
            String place = response.features.get(i).properties.place;
            long time = response.features.get(i).properties.time;

            String magnitude = getMagnitudeFormatted(mag);
            int magnitudeColor = getMagnitudeColor(mag);
            String locationOffset = getLocationOffset(place);
            String primaryLocation = getPrimaryLocation(place);
            String dateFormatted = getDateFormatted(time);
            String timeFormatted = getTimeFormatted(time);

            earthquakes.add(new Earthquake(magnitude, magnitudeColor, locationOffset,
                    primaryLocation, dateFormatted, timeFormatted));
        }

        return earthquakes;
    }

    private String getMagnitudeFormatted(double mag){
    //Giving format to magnitude
    DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
    String magnitude = magnitudeFormat.format(mag);
    return magnitude;
    }

    private int getMagnitudeColor(double mag) {
        //Checking the color for the magnitude
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(mag);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return magnitudeColorResourceId;
    }

    private String getDateFormatted(long time) {
        //Giving format to date
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String formattedDate = dateFormat.format(time);
        return formattedDate;
    }

    private String getTimeFormatted(long time) {
        //Giving format to time to show
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String formattedTime = timeFormat.format(time);
        return formattedTime;
    }

    private String getPrimaryLocation(String place){
        String primaryLocation;
        if(place.contains(LOCATION_SEPARATOR)) {
            String[] parts = place.split(LOCATION_SEPARATOR);
            primaryLocation = parts[1];
        } else {
            primaryLocation = place;
        }
        return primaryLocation;
    }

    private String getLocationOffset(String place){
        String locationOffset;
        if(place.contains(LOCATION_SEPARATOR)) {
            String[] parts = place.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
        } else {
            locationOffset = "Near to";
        }
        return locationOffset;
    }
}

package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by mdiaz on 05.07.17.
 */

public final class HttpUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = HttpUtils.class.getSimpleName();


    public static Response fetchEarthquakeData(String requestUrl) {

        //Create URL object
        URL url = createUrl(requestUrl);


        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream ", e);
        }

        //Extract relevant fields from the JSON response and create an response object
        Response response = extractFeatureFromJson(jsonResponse);
        return response;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ",e);
        }
        return url;
    }

    /**
     * makes a HTTP request to a URL to get a String with the response
     * */

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        } return jsonResponse;
    }

    /**
     * Convert the InputStream into a String
     * */

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static Response extractFeatureFromJson(String earthquakeJSON) {
        //if the JSON string is empty or null, the return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        Response response;
        try {

            Gson gson = new Gson();
            response = gson.fromJson(earthquakeJSON, Response.class);

        } catch (JsonSyntaxException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("HttpUtils", "Problem parsing the earthquake JSON results", e);
            throw new JsonSyntaxException(e);
        }
        return response;
    }


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
package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mdiaz on 24.07.17.
 */

public class OkHttp {

    public static DataModel.Response fetchData(String requestUrl) {
        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        builder.url(requestUrl);

        Request request = builder.build();

        try {
            Response serverResponse = client.newCall(request).execute();
            String jsonResponse = serverResponse.body().string();
            DataModel.Response response = extractFeatureFromJson(jsonResponse);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DataModel.Response extractFeatureFromJson(String earthquakeJSON) {
        //if the JSON string is empty or null, the return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        DataModel.Response response;
        try {

            Gson gson = new Gson();
            response = gson.fromJson(earthquakeJSON, DataModel.Response.class);

        } catch (JsonSyntaxException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("HttpUtils", "Problem parsing the earthquake JSON results", e);
            throw new JsonSyntaxException(e);
        }
        return response;
    }
}



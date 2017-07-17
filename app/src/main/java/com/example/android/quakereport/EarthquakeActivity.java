/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public EarthquakeTransformer earthquakeTransformer = new EarthquakeTransformer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_list);


        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);

    }

    private void updateUi(ArrayList<Earthquake> result) {
        // Lookup the RecyclerView in activity layout
        RecyclerView rvQuake = (RecyclerView) findViewById(R.id.earthquake_list);

        // Create adapter passing in the sample user data
        RvEarthquakeAdapter adapter = new RvEarthquakeAdapter(result);

        // Attach the adapter to the RecyclerView to populate items
        rvQuake.setAdapter(adapter);
        // Set layout manager to position the items
        rvQuake.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvQuake.addItemDecoration(itemDecoration);
    }


    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Earthquake>> {
        @Override
        protected ArrayList<Earthquake> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            HttpUtils.Response response = HttpUtils.fetchEarthquakeData(urls[0]);
            ArrayList<Earthquake> result = earthquakeTransformer.transform(response);
            return result;
        }

       @Override
        protected void onPostExecute(ArrayList<Earthquake> result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
           updateUi(result);
        }
    }
}

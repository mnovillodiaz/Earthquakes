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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.empty_view) TextView emptyView;


    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    public EarthquakeTransformer earthquakeTransformer = new EarthquakeTransformer();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_list);
        ButterKnife.bind(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            EarthquakeAsyncTask task = new EarthquakeAsyncTask();
            task.execute(uriBuilder.toString());
        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.no_intenet);
        }

    }

    private void updateUi(ArrayList<EarthquakeUiModel> result) {
        // Lookup the RecyclerView in activity layout

        RecyclerView rvQuake = (RecyclerView) findViewById(R.id.earthquake_list);
        progressBar.setVisibility(View.GONE);

        if (result.isEmpty()) {
            rvQuake.setVisibility(View.GONE);
            emptyView.setText(R.string.no_results);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            rvQuake.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

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


    private class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<EarthquakeUiModel>> {
        @Override
        protected ArrayList<EarthquakeUiModel> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            EarthquakeDataModel.Response response = HttpUtils.fetchEarthquakeData(urls[0]);
            ArrayList<EarthquakeUiModel> result = earthquakeTransformer.transform(response);
            return result;
        }

       @Override
        protected void onPostExecute(ArrayList<EarthquakeUiModel> result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
           updateUi(result);
        }
    }
}

package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.android.quakereport.Data.ApiUtils;
import com.example.android.quakereport.Data.EarthquakeDataModel;
import com.example.android.quakereport.Data.EarthquakeService;
import com.example.android.quakereport.Data.EarthquakeTransformer;
import com.example.android.quakereport.UI.DividerItemDecoration;
import com.example.android.quakereport.UI.EarthquakeUiModel;
import com.example.android.quakereport.UI.RvEarthquakeAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarthquakeActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.empty_view)
    TextView emptyView;


    public EarthquakeTransformer earthquakeTransformer = new EarthquakeTransformer();

    private EarthquakeService service;

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

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            loadEarthquakes();
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
        } else {
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

    public void loadEarthquakes() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        service = ApiUtils.getEarthquakeService();
        service.getEarthquakes(minMagnitude, orderBy)
                .enqueue(new Callback<EarthquakeDataModel>() {
                    @Override
                    public void onResponse(Call<EarthquakeDataModel> call,
                                           Response<EarthquakeDataModel> response) {

                        if (response.isSuccessful()) {
                            ArrayList<EarthquakeUiModel> result = earthquakeTransformer
                                    .transform(response.body());
                            updateUi(result);
                        } else {
                            int statusCode = response.code();
                            String message = getString(R.string.error_code) + statusCode;
                            progressBar.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyView.setText(message);
                        }
                    }

                    @Override
                    public void onFailure(Call<EarthquakeDataModel> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyView.setText(R.string.problem);
                    }
                });
    }
}

package com.example.android.quakereport;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RvEarthquakeAdapter extends RecyclerView.Adapter<RvEarthquakeAdapter.ViewHolder> {
    private ArrayList<Earthquake> earthquake;

    public RvEarthquakeAdapter(ArrayList<Earthquake> earthquake) {
        this.earthquake = earthquake;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.magnitude_text) TextView magnitudeText;
        @BindView(R.id.location_offset_text) TextView locationOffsetText;
        @BindView(R.id.primary_location_text) TextView primaryLocationText;
        @BindView(R.id.date_text) TextView dateText;
        @BindView(R.id.time_text) TextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public RvEarthquakeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View earthquakeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(earthquakeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RvEarthquakeAdapter.ViewHolder viewHolder, int position) {
        final Earthquake quake = earthquake.get(position);

        String magnitude = quake.getMagnitude();
        String locationOffset = quake.getLocationOffset();
        String primaryLocation = quake.getPrimaryLocation();
        String formattedDate = quake.getDate();
        String formattedTime = quake.getTime();
        int magnitudeColorResourceId = quake.getMagnitudeColor();

        viewHolder.magnitudeText.setText(magnitude);
        viewHolder.locationOffsetText.setText(locationOffset);
        viewHolder.primaryLocationText.setText(primaryLocation);
        viewHolder.dateText.setText(formattedDate);
        viewHolder.timeText.setText(formattedTime);

        GradientDrawable magnitudeCircle = (GradientDrawable)
                viewHolder.magnitudeText.getBackground();
        magnitudeCircle.setColor(ResourcesCompat.
                getColor(viewHolder.magnitudeText.getContext().getResources(),magnitudeColorResourceId,null));
    }

    @Override
    public int getItemCount() {return earthquake.size();}

}


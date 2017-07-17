package com.example.android.quakereport;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



public class RvEarthquakeAdapter extends RecyclerView.Adapter<RvEarthquakeAdapter.ViewHolder> {
    private ArrayList<Earthquake> earthquake;

    public RvEarthquakeAdapter(ArrayList<Earthquake> earthquake) {
        this.earthquake = earthquake;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView magnitudeText;
        public TextView locationOffsetText;
        public TextView primaryLocationText;
        public TextView dateText;
        public TextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            magnitudeText = (TextView) itemView.findViewById(R.id.magnitude_text);
            locationOffsetText = (TextView) itemView.findViewById(R.id.location_offset_text);
            primaryLocationText = (TextView) itemView.findViewById(R.id.primary_location_text);
            dateText = (TextView) itemView.findViewById(R.id.date_text);
            timeText = (TextView) itemView.findViewById(R.id.time_text);
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

        GradientDrawable magnitudeCircle = (GradientDrawable) viewHolder.magnitudeText.getBackground();
        magnitudeCircle.setColor(magnitudeColorResourceId);
    }

    @Override
    public int getItemCount() {return earthquake.size();}

}


package com.example.android.quakereport;

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
        public TextView placeText;
        public TextView dateText;

        public ViewHolder(View itemView) {
            super(itemView);
            magnitudeText = (TextView) itemView.findViewById(R.id.magnitude_text);
            placeText = (TextView) itemView.findViewById(R.id.place_text);
            dateText = (TextView) itemView.findViewById(R.id.date_text);
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

        viewHolder.magnitudeText.setText(quake.getMagnitude());
        viewHolder.placeText.setText(quake.getPlace());
        viewHolder.dateText.setText(quake.getDate());

    }

    @Override
    public int getItemCount() {return earthquake.size();}
}


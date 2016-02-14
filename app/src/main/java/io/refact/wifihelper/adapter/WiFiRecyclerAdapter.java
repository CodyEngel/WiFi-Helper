package io.refact.wifihelper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.LineDataProvider;

import java.util.ArrayList;

import io.refact.wifihelper.R;
import io.refact.wifihelper.model.WiFi;

/**
 * Created by cody on 11/27/15.
 */
public class WiFiRecyclerAdapter extends RecyclerView.Adapter<WiFiRecyclerAdapter.ViewHolder> {

    private ArrayList<WiFi> mDataset;

    private int mPrimaryColor;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int accentColor,
                primaryColor;

        public LineChart lcSignalStrength;

        public TextView tvMacAddress,
                    tvSSID;

        public ViewHolder(View view) {
            super(view);
            tvMacAddress = (TextView) view.findViewById(R.id.tvMacAddress);
            tvSSID = (TextView) view.findViewById(R.id.tvSSID);
            lcSignalStrength = (LineChart) view.findViewById(R.id.lcSignalStrength);

            if(Build.VERSION.SDK_INT >= 23) {
                primaryColor = ContextCompat.getColor(view.getContext(), R.color.primary);
                accentColor = ContextCompat.getColor(view.getContext(), R.color.accent);
            } else {
                primaryColor = view.getContext().getResources().getColor(R.color.primary);
                accentColor = view.getContext().getResources().getColor(R.color.accent);
            }

            setLineChartConfigs();
        }

        private void setLineChartConfigs() {
            lcSignalStrength.setTouchEnabled(false);
            lcSignalStrength.setDragEnabled(false);
            lcSignalStrength.setScaleEnabled(false);
            lcSignalStrength.setPinchZoom(false);
            lcSignalStrength.setDrawGridBackground(false);
            lcSignalStrength.getAxisLeft().setAxisMinValue(-100f);
            lcSignalStrength.getAxisLeft().setEnabled(false);
            lcSignalStrength.getAxisRight().setEnabled(false);
            lcSignalStrength.setDescription("");
            lcSignalStrength.getXAxis().setEnabled(false);
            lcSignalStrength.getLegend().setEnabled(false);
            lcSignalStrength.setViewPortOffsets(4f, 4f, 4f, 4f);
        }
    }

    public WiFiRecyclerAdapter(ArrayList<WiFi> dataset) {
        mDataset = dataset;
    }

    public void update(ArrayList<WiFi> dataset) {
        mDataset.clear();
        mDataset.addAll(dataset);
        notifyDataSetChanged();
    }

    @Override
    public WiFiRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wifi, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        WiFi wifi = mDataset.get(position);
        viewHolder.tvSSID.setText(wifi.getSSID().length() > 0 ? wifi.getSSID() : "NO SSID FOUND FOR NETWORK");
        viewHolder.tvMacAddress.setText(wifi.getMacAddress());

        if(wifi.getFrequency() >= 2000 && wifi.getFrequency() <= 2999) {
            viewHolder.lcSignalStrength.setBackgroundColor(viewHolder.accentColor);
        } else {
            viewHolder.lcSignalStrength.setBackgroundColor(viewHolder.primaryColor);
        }

        int length = wifi.getScanLevels().size();
        ArrayList<String> xVals = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> vals = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            vals.add(new Entry(wifi.getScanLevels().get(i), i));
        }

        LineDataSet set = new LineDataSet(vals, "Signal Strength");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setDrawFilled(true);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setLineWidth(2.5f);
        set.setColor(Color.WHITE);
        set.setFillAlpha(0);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new FillFormatter() {
            @Override
            public float getFillLinePosition(LineDataSet dataSet, LineDataProvider dataProvider) {
                return -100;
            }
        });

        LineData data = new LineData(xVals, set);

        viewHolder.lcSignalStrength.setData(data);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

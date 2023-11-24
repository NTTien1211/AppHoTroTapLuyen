package com.example.app_hotrotapluyen.gym.User_screen;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.app_hotrotapluyen.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class User_Over_Fragment extends Fragment {
    BarChart barChartUserRent;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_user__over, container, false);
         barChartUserRent = view.findViewById(R.id.chartListUserRent);

        progressBar = view.findViewById(R.id.progressBarL);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 100));  // Assuming the first entry is for day 0
        entries.add(new BarEntry(1, 500));  // Assuming the second entry is for day 1
        entries.add(new BarEntry(2, 300));  // Assuming the third entry is for day 2

        BarDataSet barDataSet = new BarDataSet( entries,"Calo");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barData.setValueFormatter(new DefaultValueFormatter(0));
        YAxis yAxis = barChartUserRent.getAxisLeft();
        yAxis.setAxisMinimum(1f);
        yAxis.setAxisMaximum(1000f);

        barChartUserRent.setFitBars(true);
        barChartUserRent.setData(barData);
        barChartUserRent.getDescription().setText("Day");
        barChartUserRent.animateY(2000);
        barChartUserRent.invalidate();

        return  view;
    }
}
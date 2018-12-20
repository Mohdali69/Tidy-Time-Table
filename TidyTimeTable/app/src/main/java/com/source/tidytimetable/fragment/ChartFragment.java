package com.source.tidytimetable.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import com.source.tidytimetable.*;

import static android.content.ContentValues.TAG;

public class ChartFragment extends Fragment {

    private float[] yData = {25.3f, 10.6f, 66.76f, 44.32f, 46.01f, 16.89f, 23.9f};
    private String[] xData = {"Mitch", "Jessica" , "Mohammad" , "Kelsey", "Sam", "Robert", "Ashley"};
    PieChart pieChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting to create chart");
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.fragment_addtask, container, false);
        return inflater.inflate(R.layout.fragment_chart, container, false);

    }

}

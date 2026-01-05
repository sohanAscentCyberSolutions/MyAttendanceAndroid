package com.gmda.attendance.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gmda.attendance.R;

import java.time.Month;
import java.util.ArrayList;


public class StudentsMonthHistoryFragment extends Fragment {

    private static final int MAX_X_VALUE = 12;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String STACK_1_LABEL = "Present";
    private static final String STACK_2_LABEL = "Leave";
    private static final String STACK_3_LABEL = "Absent";
    private static final String SET_LABEL = "        Monthly History";
    private static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
    private BarChart chart;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students_month_history, container, false);

        chart = view.findViewById(R.id.fragment_stacked_barchart_chart);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);

        return view;
    }

    private void configureChartAppearance() {
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);

        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        //xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return MONTHS[(int) value];
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawAxisLine(false);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < MAX_X_VALUE; i++) {
            float value1 = Math.max(MIN_Y_VALUE, (float) Math.random() * (MAX_Y_VALUE + 1));
            float value2 = Math.max(MIN_Y_VALUE, (float) Math.random() * (MAX_Y_VALUE + 1));
            float value3 = Math.max(MIN_Y_VALUE, (float) Math.random() * (MAX_Y_VALUE + 1));
            values.add(new BarEntry(i, new float[]{value1, value2, value3}));
        }

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        set1.setColors(new int[] {ColorTemplate.MATERIAL_COLORS[0], ColorTemplate.MATERIAL_COLORS[1], ColorTemplate.MATERIAL_COLORS[2]});
        set1.setStackLabels(new String[] {STACK_1_LABEL, STACK_2_LABEL, STACK_3_LABEL});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }
}
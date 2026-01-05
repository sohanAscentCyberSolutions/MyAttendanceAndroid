package com.gmda.attendance.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gmda.attendance.R;
import com.robinhood.ticker.TickerView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class UserReportsActivity extends AppCompatActivity {

    String name,id,designation,totalPresent,totalLate,totalAbsent,totalLateAbsent;
    PieChart pieChart;

    TextView tv_name,tv_id,tv_designation;
    TickerView tv_user_total_late,presentTicker,absentTicker,lateAbsentTicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports);
        pieChart=findViewById(R.id.piechart);

        presentTicker=findViewById(R.id.presentTicker);
        absentTicker=findViewById(R.id.absentTicker);
        //presentTicker=findViewById(R.id.presentTicker);
        tv_user_total_late = findViewById(R.id.tv_user_total_lateTicker);
        lateAbsentTicker = findViewById(R.id.lateAbsentTicker);
        tv_name = findViewById(R.id.tv_emp_name);
        tv_id = findViewById(R.id.tv_emp_id);
        tv_designation = findViewById(R.id.tv_designation);


        name = getIntent().getStringExtra("emp_name");
        id = getIntent().getStringExtra("emp_id");
        designation = getIntent().getStringExtra("designation");
        designation = getIntent().getStringExtra("designation");
        totalPresent = getIntent().getStringExtra("totalPresent");
        totalAbsent = getIntent().getStringExtra("totalAbsent");
        totalLate = getIntent().getStringExtra("totalLate");
        totalLateAbsent = getIntent().getStringExtra("totalLateAbsent");

        tv_name.setText(String.valueOf(name));
        tv_id.setText(String.valueOf(id));
        tv_designation.setText(String.valueOf(designation));

        tv_user_total_late.setText(String.valueOf(totalLate));
        presentTicker.setText(String.valueOf(totalPresent));
        absentTicker.setText(String.valueOf(totalAbsent));
        lateAbsentTicker.setText(String.valueOf(totalLateAbsent));



        pieChart.addPieSlice(new PieModel("Present", Integer.parseInt(totalPresent), getResources().getColor(R.color.green_pie)));
        pieChart.addPieSlice(new PieModel("Absent", Integer.parseInt(totalAbsent), getResources().getColor(R.color.red_pie)));
        pieChart.addPieSlice(new PieModel("Total", Integer.parseInt(totalLate), getResources().getColor(R.color.yellow)));
      //  pieChart.addPieSlice(new PieModel("Total", String.valueOf()totalLateAbsent, getResources().getColor(R.color.late)));
        //tvPi_chart.addPieSlice(new PieModel("Absent", userSingleLate, getResources().getColor(R.color.late)));
        pieChart.startAnimation();


    }
}

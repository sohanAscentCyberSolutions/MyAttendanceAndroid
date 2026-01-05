package com.gmda.attendance.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gmda.attendance.R;

import java.util.Calendar;

public class ApplyLeaveActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    TextView tv_fromdate, tv_todate;
    String selected_fromdate = "", selected_todate = "";
    Button btn_submit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        tv_fromdate = findViewById(R.id.selectfromdate);
        tv_todate = findViewById(R.id.selectedtodate);
        btn_submit = findViewById(R.id.btn_submit);

        tv_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ApplyLeaveActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String finaldate = String.valueOf(dayOfMonth);
                        if (String.valueOf(dayOfMonth).length() == 1) {
                            finaldate = "0" + dayOfMonth;
                        }
                        String date = year + "-" + (month + 1) + "-" + finaldate;
                        tv_fromdate.setText(date);
                        selected_fromdate = date;
                    }
                }, year, month, day);
                //don't take advance date
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //select_Shift(manual_date);
                    }
                });
                datePickerDialog.show();
            }
        });

        tv_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ApplyLeaveActivity
                        .this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String finaldate = String.valueOf(dayOfMonth);
                        if (String.valueOf(dayOfMonth).length() == 1) {
                            finaldate = "0" + dayOfMonth;
                        }
                        String date = year + "-" + (month + 1) + "-" + finaldate;
                        tv_todate.setText(date);
                        selected_todate = date;
                    }
                }, year, month, day);
//                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        //select_Shift(manual_date);
//                    }
//                });
                datePickerDialog.show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
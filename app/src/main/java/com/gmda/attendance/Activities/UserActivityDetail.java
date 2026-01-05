package com.gmda.attendance.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmda.attendance.R;

public class UserActivityDetail extends AppCompatActivity {

    String name,id,designation,department,empRole,division,workLocation;
    ImageView imgBack;
    TextView tv_emp_id,tv_emp_name,tv_role,tv_division,tv_designation,tv_department,tv_work_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);


        imgBack=findViewById(R.id.imgBack);
        tv_emp_id=findViewById(R.id.tv_emp_id);
        tv_emp_name=findViewById(R.id.tv_emp_name);
        tv_role=findViewById(R.id.tv_role);
        tv_division=findViewById(R.id.tv_division);
        tv_designation=findViewById(R.id.tv_designation);
        tv_department=findViewById(R.id.tv_department);
        tv_work_location=findViewById(R.id.tv_work_location);

        name = getIntent().getStringExtra("emp_name");
        id = getIntent().getStringExtra("emp_id");
        department = getIntent().getStringExtra("department");
        designation = getIntent().getStringExtra("designation");
        workLocation = getIntent().getStringExtra("work_location");
        division = getIntent().getStringExtra("division");
        empRole = getIntent().getStringExtra("emp_role");
        //punchouttime = getIntent().getStringExtra("punch_out");


        tv_emp_name.setText(name);
        tv_emp_id.setText(id);
        tv_department.setText(department);
        tv_designation.setText(designation);
        tv_work_location.setText(workLocation);
        tv_division.setText(division);
        tv_role.setText(empRole);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivityDetail.this,Attendance_Report_Activity.class));
            }
        });


    }
}

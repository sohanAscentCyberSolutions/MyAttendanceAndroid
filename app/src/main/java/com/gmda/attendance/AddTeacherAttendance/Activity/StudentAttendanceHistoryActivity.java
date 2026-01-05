package com.gmda.attendance.AddTeacherAttendance.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.gmda.attendance.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentAttendanceHistoryActivity extends AppCompatActivity {
    TextView className;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_history);

        className = findViewById(R.id.className);
        String text = "--: Class = \"";
        String text2 ="\" :--";
        String name_of_class = text + getIntent().getStringExtra("classSelected") + text2;

        Spannable spannable = new SpannableString(name_of_class);
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), text.length(), (text+getIntent().getStringExtra("classSelected")).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        className.setText(spannable, TextView.BufferType.SPANNABLE);

        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        BottomNavigationView bottomNavigation = findViewById(R.id.activity_main_bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigation, navController);

    }
}
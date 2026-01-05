package com.gmda.attendance.AddTeacherAttendance.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.AddTeacherAttendance.Adapter.ClassAdapter;
import com.gmda.attendance.AddTeacherAttendance.Adapter.StudentAttendanceAdapter;
import com.gmda.attendance.AddTeacherAttendance.Interface.ClassInterface;
import com.gmda.attendance.AddTeacherAttendance.Model.StudentAttendanceModel;
import com.gmda.attendance.AddTeacherAttendance.Model.StudentClass;
import com.gmda.attendance.R;

import java.util.ArrayList;
import java.util.Collections;

public class StudentAttendanceActivity extends AppCompatActivity implements ClassInterface {
    Spinner assignedClassSpinner;
    String selected_value_sending;
    private ArrayList<String> assigned_class_array_list;
    RecyclerView studentListRecyclerView;
    RecyclerView assignedClassRecyclerView;
    TextView suggestion;
    Toolbar toolbar;

    ArrayList<StudentAttendanceModel> studentAttendanceModelArrayList;
    StudentAttendanceAdapter listAdapter;
    ArrayList<StudentClass> studentClassArrayList;
    ClassAdapter classAdapter;

    SearchView searchView;
    Dialog dialog_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);


//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        suggestion = findViewById(R.id.suggestion);
        searchView = findViewById(R.id.searchView);
        studentListRecyclerView = findViewById(R.id.studentListRecyclerView);
        assignedClassRecyclerView = findViewById(R.id.assignedClassRecyclerView);

        studentAttendanceModelArrayList = new ArrayList<>();
        studentClassArrayList = new ArrayList<>();

        dialog_loading = new Dialog(this);
        dialog_loading.setContentView(R.layout.loading_dailog);
        dialog_loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });


/*

        assignedClassSpinner = findViewById(R.id.assignedClassSpinner);
        assigned_class_array_list = new ArrayList<>();
        assigned_class_array_list.add("Select");

        assigned_class_array_list.add("7th A");
        assigned_class_array_list.add("7th B");
        assigned_class_array_list.add("8th A");
        assigned_class_array_list.add("8th B");

        ArrayAdapter<String> workAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, assigned_class_array_list);
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assignedClassSpinner.setAdapter(workAdapter);

        assignedClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_value_sending = assigned_class_array_list.get(position);
                if (!selected_value_sending.equalsIgnoreCase("Select")) {
                    getStudentsListOfSelectedClass(selected_value_sending);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        studentClassArrayList.add(new StudentClass("8th", "B"));
        studentClassArrayList.add(new StudentClass("7th", "B"));
        studentClassArrayList.add(new StudentClass("7th", "A"));
        studentClassArrayList.add(new StudentClass("9th", "A"));
        studentClassArrayList.add(new StudentClass("8th", "A"));
        studentClassArrayList.add(new StudentClass("9th", "B"));

        Log.d("TAG", "onCreate: " + studentClassArrayList);

        Collections.sort(studentClassArrayList, (o1, o2) -> {

            String x1 = o1.getStudent_class();
            String x2 = o2.getStudent_class();
            int sComp = x1.compareTo(x2);

            if (sComp != 0) {
                return sComp;
            }
            String x3 = o1.getSection();
            String x4 = o2.getSection();
            return x3.compareTo(x4);

        });
        classAdapter = new ClassAdapter(this, studentClassArrayList, this::getStudentsListOfSelectedClass);
        classAdapter.setHasStableIds(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        assignedClassRecyclerView.setLayoutManager(manager);
        assignedClassRecyclerView.setHasFixedSize(true);

        assignedClassRecyclerView.setAdapter(classAdapter);
        classAdapter.notifyDataSetChanged();


    }

    private void filter(String text) {

        ArrayList<StudentAttendanceModel> filteredList = new ArrayList<>();
        for (StudentAttendanceModel item : studentAttendanceModelArrayList) {
            if (item.getStudent_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            listAdapter.filterList(filteredList);
        }
    }

    private void getStudentsListOfSelectedClass(String selected_value_sending) {
        showProgressbar();
        suggestion.setVisibility(View.GONE);
        searchView.setVisibility(View.VISIBLE);
        studentListRecyclerView.setVisibility(View.VISIBLE);


        studentAttendanceModelArrayList.clear();
        if (selected_value_sending.equalsIgnoreCase("7thA") || selected_value_sending.equalsIgnoreCase("8thA")) {
            studentAttendanceModelArrayList.add(new StudentAttendanceModel(1, "Amar "));
            studentAttendanceModelArrayList.add(new StudentAttendanceModel(2, "Amit Malhan"));
            studentAttendanceModelArrayList.add(new StudentAttendanceModel(3, "Ankit Malhan"));

            for (int i = 0; i <= 20; i++) {
                studentAttendanceModelArrayList.add(new StudentAttendanceModel(i + 4, "Jyoti Choudhary"));
            }

        } else if (selected_value_sending.equalsIgnoreCase("7thB") || selected_value_sending.equalsIgnoreCase("8thB")) {

            studentAttendanceModelArrayList.add(new StudentAttendanceModel(1, "Vinay Choudhary"));
            studentAttendanceModelArrayList.add(new StudentAttendanceModel(2, "Vinit Choudhary"));
            studentAttendanceModelArrayList.add(new StudentAttendanceModel(3, "Yogesh Sheoran"));

            for (int i = 0; i <= 20; i++) {
                studentAttendanceModelArrayList.add(new StudentAttendanceModel(i + 4, "Pooja Sheoran Thakran"));
            }


        } else {
            suggestion.setText("No Students");
            suggestion.setTextSize(20);
            suggestion.setTextColor(getResources().getColor(R.color.bgColor));
            suggestion.setVisibility(View.VISIBLE);
        }
        Collections.sort(studentAttendanceModelArrayList, (lhs, rhs) -> lhs.getStudent_roll_no() - (rhs.getStudent_roll_no()));
        listAdapter = new StudentAttendanceAdapter(this, studentAttendanceModelArrayList);
        listAdapter.setHasStableIds(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        studentListRecyclerView.setLayoutManager(manager);
        studentListRecyclerView.setHasFixedSize(true);

        studentListRecyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        hideProgressbar();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.history_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.actionHistory);
        searchItem.setOnMenuItemClickListener(v -> {

            startActivity(new Intent(this, StudentAttendanceHistoryActivity.class));
            return false;
        });

        return true;
    }

    @Override
    public void onClickClassName(String c_name) {
        getStudentsListOfSelectedClass(c_name);
    }


    public void showProgressbar() {
        dialog_loading.show();
        dialog_loading.setCancelable(true);
    }


    public void hideProgressbar() {
        dialog_loading.dismiss();
    }

}
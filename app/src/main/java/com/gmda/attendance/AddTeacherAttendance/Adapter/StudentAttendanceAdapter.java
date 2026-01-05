package com.gmda.attendance.AddTeacherAttendance.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.AddTeacherAttendance.Model.StudentAttendanceModel;
import com.gmda.attendance.R;

import java.util.ArrayList;


public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.StudentAttendanceHolder> {
    Context context;
    ArrayList<StudentAttendanceModel> StudentAttendanceModelArrayList;


    public StudentAttendanceAdapter(Context context, ArrayList<StudentAttendanceModel> StudentAttendanceModelArrayList) {
        this.context = context;
        this.StudentAttendanceModelArrayList = StudentAttendanceModelArrayList;
    }
    public void filterList(ArrayList<StudentAttendanceModel> filterList) {
        StudentAttendanceModelArrayList = filterList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StudentAttendanceAdapter.StudentAttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_attendance_card_design, parent, false);
        return new StudentAttendanceHolder(view);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAttendanceHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.studentRollNo.setText(String.valueOf(StudentAttendanceModelArrayList.get(position).getStudent_roll_no()));
        holder.studentName.setText(String.valueOf(StudentAttendanceModelArrayList.get(position).getStudent_name()));


        holder.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radioPresent:
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.green_pie));
                    break;
                case R.id.radioAbsent:
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red_pie));
                    break;

                case R.id.radioLeave:
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));
                    break;

            }
        });
    }

    @Override
    public int getItemCount() {
        return StudentAttendanceModelArrayList.size();
    }

    public static class StudentAttendanceHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentRollNo;
        RadioGroup radioGroup;
        CardView cardView;

        public StudentAttendanceHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            studentRollNo = itemView.findViewById(R.id.studentRollNo);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}

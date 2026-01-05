package com.gmda.attendance.AddTeacherAttendance.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.AddTeacherAttendance.Interface.ClassInterface;
import com.gmda.attendance.AddTeacherAttendance.Model.StudentClass;
import com.gmda.attendance.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassHolder> {

    Context context;
    ArrayList<StudentClass> StudentClassArrayList;
    ClassInterface classInterface;
    private int lastPosition = -1;

    public ClassAdapter(Context context, ArrayList<StudentClass> StudentClassArrayList, ClassInterface classInterface) {
        this.context = context;
        this.StudentClassArrayList = StudentClassArrayList;
        this.classInterface = classInterface;
    }

    @NonNull
    @Override
    public ClassAdapter.ClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.assigned_class_card_design, parent, false);
        return new ClassAdapter.ClassHolder(view);

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
    public void onBindViewHolder(@NonNull ClassAdapter.ClassHolder holder, @SuppressLint("RecyclerView") int position) {
        StudentClass studentClass = StudentClassArrayList.get(position);
        holder.studentClass.setText(String.valueOf(StudentClassArrayList.get(position).getStudent_class()));
        holder.section.setText(String.valueOf(StudentClassArrayList.get(position).getSection()));
        holder.card_view_class.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        holder.card_view_class.setOnClickListener(v -> {
            classInterface.onClickClassName(studentClass.getStudent_class() + studentClass.getSection());
            lastPosition = position;
            notifyDataSetChanged();

        });

        if (lastPosition == position) {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.bgColor));
        }

    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.enter);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return StudentClassArrayList.size();
    }

    public static class ClassHolder extends RecyclerView.ViewHolder {
        TextView studentClass, section;
        CardView card_view_class;
        LinearLayout linearLayout;
        View view;

        public ClassHolder(@NonNull View itemView) {
            super(itemView);
            studentClass = itemView.findViewById(R.id.studentClass);
            section = itemView.findViewById(R.id.section);
            card_view_class = itemView.findViewById(R.id.card_view_class);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            view = itemView.findViewById(R.id.view);
        }
    }
}

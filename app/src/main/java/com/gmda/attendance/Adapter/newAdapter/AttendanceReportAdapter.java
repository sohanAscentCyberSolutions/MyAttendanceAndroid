package com.gmda.attendance.Adapter.newAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.R;
import com.gmda.attendance.newmodels.AttendanceReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceReportAdapter extends RecyclerView.Adapter<AttendanceReportAdapter.AttendanceViewHolder> {

    private static final String TAG = "MyTag";
    List<Address> addressList;
    private Context context;
    private ArrayList<AttendanceReport> arrayList;

    public AttendanceReportAdapter(Context context, ArrayList<AttendanceReport> arrayList) {
        this.context = context;
        this.arrayList = arrayList;


    }

    @NonNull
    @Override
    public AttendanceReportAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false);

        return new AttendanceViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceReportAdapter.AttendanceViewHolder holder, int position) {
        AttendanceReport attendanceReport = arrayList.get(position);

        if(!Objects.equals("null",attendanceReport.getPunchin_image())){
            byte[] decodedString = Base64.decode(attendanceReport.getPunchin_image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.punch_in_image.setImageBitmap(decodedByte);
        }

        if(!Objects.equals("null",attendanceReport.getPunchout_image())){
            byte[] decodedString = Base64.decode(attendanceReport.getPunchout_image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.punch_out_image.setImageBitmap(decodedByte);
        }

        String status_showing;
        if (attendanceReport.getA_status().equals("A")) {
            status_showing = "Absent";
            holder.dec_text_view.setText("    A");
            holder.dec_text_view.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (attendanceReport.getA_status().equals("P")) {
            holder.dec_text_view.setText("    P");
            status_showing = "Present";
            holder.dec_text_view.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            status_showing = "Late";
            holder.dec_text_view.setText("    L");
            holder.dec_text_view.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        }


        holder.attendance_status.setText("◈ Status : " + status_showing);
        holder.working_hours_card.setText("W. Hours : " + attendanceReport.getWorking_hr());

        switch (attendanceReport.getA_status()) {
            case "A":
                holder.punch_in_time_card.setText("In Time ------");
                holder.punch_out_time_card_view.setText("Out Time ------");
                holder.punch_in_location.setText("In Location ------");
                holder.punch_out_location.setText("Out Location ------");
                break;
            case "LATE":
                holder.punch_in_location.setText(" In 📍" + attendanceReport.getPunch_in_address());
                String s1 = attendanceReport.getPunch_in().substring(attendanceReport.getPunch_in().indexOf("T") + 1, attendanceReport.getPunch_in().indexOf("."));
                holder.punch_in_time_card.setText("In Time : " + s1);

                if (Objects.equals(attendanceReport.getPunch_out_address(), "null")) {
                    holder.punch_out_time_card_view.setText("Out Time -----");
                    holder.punch_out_location.setText(" Out  📍 ------");
                } else {
                    holder.punch_out_location.setText(" Out 📍 - " + attendanceReport.getPunch_out_address());
                    String s2 = attendanceReport.getPunch_out().substring(attendanceReport.getPunch_out().indexOf("T") + 1, attendanceReport.getPunch_out().indexOf("."));
                    holder.punch_out_time_card_view.setText("Out Time : " + s2);
                }

                break;
            case "P":
                if (Objects.equals(attendanceReport.getPunch_in_address(), "null")) {
                    holder.punch_in_location.setText(" In  📍 ------");
                    holder.punch_in_time_card.setText("In Time : -----");
                } else {
                    String s2 = attendanceReport.getPunch_in().substring(attendanceReport.getPunch_in().indexOf("T") + 1, attendanceReport.getPunch_in().indexOf("."));
                    holder.punch_in_time_card.setText("In Time : " + s2);
                    holder.punch_in_location.setText(" In 📍 - " + attendanceReport.getPunch_in_address());
                }

                if (Objects.equals(attendanceReport.getPunch_out_address(), "null")) {
                    holder.punch_out_time_card_view.setText("Out Time : -----");
                    holder.punch_out_location.setText(" Out 📍 ------ ");
                } else {
                    String s2 = attendanceReport.getPunch_out().substring(attendanceReport.getPunch_out().indexOf("T") + 1, attendanceReport.getPunch_out().indexOf("."));
                    holder.punch_out_time_card_view.setText("Out Time : " + s2);
                    holder.punch_out_location.setText(" Out 📍 - " + attendanceReport.getPunch_out_address());
                }
                break;
        }

        String data_show_variable = attendanceReport.getAttendance_date().split("T")[0];
        String[] date_show = data_show_variable.split("-");

        holder.date_view.setText("D " + date_show[2] + "\nM " + date_show[1] + "\nY " + date_show[0]);
        switch (attendanceReport.getA_status()) {
            case "A":
                holder.date_view.setBackgroundResource(R.color.red);
                break;
            case "P":
                holder.date_view.setBackgroundResource(R.color.green);
                break;
            case "LATE":
                holder.date_view.setBackgroundResource(R.color.yellow);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView date_view, punch_in_time_card, punch_out_time_card_view, punch_in_location;
        TextView attendance_status, working_hours_card, punch_out_location, dec_text_view;
        CircleImageView punch_out_image, punch_in_image;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date_view = itemView.findViewById(R.id.date_view);
            punch_in_time_card = itemView.findViewById(R.id.punch_in_time_card);
            punch_out_time_card_view = itemView.findViewById(R.id.punch_out_time_card_view);
            punch_in_location = itemView.findViewById(R.id.punch_in_location);
            attendance_status = itemView.findViewById(R.id.attendance_status);
            working_hours_card = itemView.findViewById(R.id.working_hours_card);
            punch_out_location = itemView.findViewById(R.id.punch_out_location);
            dec_text_view = itemView.findViewById(R.id.dec_text_view);
            punch_out_image = itemView.findViewById(R.id.punch_out_image);
            punch_in_image = itemView.findViewById(R.id.punch_in_image);


        }
    }
}

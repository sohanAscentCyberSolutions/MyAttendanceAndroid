package com.gmda.attendance.Adapter.newAdapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.R;
import com.gmda.attendance.newmodels.AttendanceReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdminAttendanceReportAdapter extends RecyclerView.Adapter<AdminAttendanceReportAdapter.AttendanceAdminViewHolder> {

    List<Address> addressList;
    private Context context;
    private ArrayList<AttendanceReport> arrayList;

    private Geocoder geocoder;
    private String mode;

    public AdminAttendanceReportAdapter(Context context, ArrayList<AttendanceReport> arrayList, String mode) {
        this.context = context;
        this.arrayList = arrayList;
        geocoder = new Geocoder(context, Locale.getDefault());
        this.mode = mode;

    }

    @NonNull
    @Override
    public AdminAttendanceReportAdapter.AttendanceAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (mode.equals("full")){
            view = LayoutInflater.from(context).inflate(R.layout.user_admin_layout_large, parent, false);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.user_admin_layout_small, parent, false);
        }
        return new AttendanceAdminViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AdminAttendanceReportAdapter.AttendanceAdminViewHolder holder, int position) {
        AttendanceReport attendanceReport = arrayList.get(position);


        String status_showing;
        if (attendanceReport.getA_status().equals("A")) {
            status_showing = "Absent";
            holder.dec_text_view.setText("    A");
        } else if (attendanceReport.getA_status().equals("P")) {
            holder.dec_text_view.setText("    P");
            status_showing = "Present";
        } else {
            status_showing = "Late";
            holder.dec_text_view.setText("    L");
        }


        holder.employee_id.setText(attendanceReport.getEmp_id());
        holder.employee_name.setText(attendanceReport.getEmp_name());
        holder.employee_designation.setText(attendanceReport.getDesignation() + " (" + attendanceReport.getDepartment() + ")");
        holder.attendance_status.setText("◈ Status : " + status_showing);
        holder.working_hours_card.setText("W. Hours : " + attendanceReport.getWorking_hr());

        if (attendanceReport.getA_status().equals("A")) {
            holder.punch_in_time_card.setText("In Time ------");
            holder.punch_out_time_card_view.setText("Out Time ------");
            holder.punch_in_location.setText("In Location ------");
            holder.punch_out_location.setText("Out Location ------");
        } else if (attendanceReport.getA_status().equals("LATE")) {
            try {
                List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(attendanceReport.getPunchin_lat()), Double.parseDouble(attendanceReport.getPunchin_long()), 1);
                String address = (addressList.get(0).getAddressLine(0)) + (addressList.get(0).getLocality());
                holder.punch_in_location.setText(" In 📍 - " + address);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s1 = attendanceReport.getPunch_in().substring(attendanceReport.getPunch_in().indexOf("T") + 1, attendanceReport.getPunch_in().indexOf("."));
            holder.punch_in_time_card.setText("In Time : " + s1);
            if (Objects.equals(attendanceReport.getPunch_out(), "null")) {
                holder.punch_out_time_card_view.setText("Out Time -----");
                holder.punch_out_location.setText(" Out  📍 ------");
            } else {
                try {
                    List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(attendanceReport.getPunchout_lat()), Double.parseDouble(attendanceReport.getPunchout_long()), 1);
                    String address = (addressList.get(0).getAddressLine(0)) + (addressList.get(0).getLocality());
                    holder.punch_out_location.setText(" Out 📍 - " + address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s2 = attendanceReport.getPunch_out().substring(attendanceReport.getPunch_out().indexOf("T") + 1, attendanceReport.getPunch_out().indexOf("."));
                holder.punch_out_time_card_view.setText("Out Time : " + s2);
            }

        } else if (attendanceReport.getA_status().equals("P")) {
            if (Objects.equals(attendanceReport.getPunch_in(), "null")) {
                holder.punch_in_location.setText(" In  📍 ------");
                holder.punch_in_time_card.setText("In Time : -----");
            } else {
                String s1 = attendanceReport.getPunch_in().substring(attendanceReport.getPunch_in().indexOf("T") + 1, attendanceReport.getPunch_in().indexOf("."));
                holder.punch_in_time_card.setText("In Time : " + s1);

                try {
                    List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(attendanceReport.getPunchin_lat()), Double.parseDouble(attendanceReport.getPunchin_long()), 1);
                    String address = (addressList.get(0).getAddressLine(0)) + (addressList.get(0).getLocality());
                    holder.punch_in_location.setText(" In 📍 - " + address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (Objects.equals(attendanceReport.getPunch_out(), "null")) {
                holder.punch_out_time_card_view.setText("Out Time : -----");
                holder.punch_out_location.setText(" Out 📍 ------ ");
            } else {
                String s2 = attendanceReport.getPunch_out().substring(attendanceReport.getPunch_out().indexOf("T") + 1, attendanceReport.getPunch_out().indexOf("."));
                holder.punch_out_time_card_view.setText("Out Time : " + s2);

                try {
                    List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(attendanceReport.getPunchout_lat()), Double.parseDouble(attendanceReport.getPunchout_long()), 1);
                    String address = (addressList.get(0).getAddressLine(0)) + (addressList.get(0).getLocality());
                    holder.punch_out_location.setText(" Out 📍 - " + address);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }


        String[] date_show = attendanceReport.getAttendance_date().split("-");
        holder.date_view.setText(new StringBuilder().append("D ").append(date_show[2]).append("\nM ").append(date_show[1]).append("\nY ").append(date_show[0]).toString());
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

    public class AttendanceAdminViewHolder extends RecyclerView.ViewHolder {
        TextView date_view, punch_in_time_card, punch_out_time_card_view, punch_in_location;
        TextView attendance_status, working_hours_card, punch_out_location, dec_text_view;
        private TextView employee_id, employee_name, employee_designation;

        public AttendanceAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            date_view = itemView.findViewById(R.id.date_view);
            punch_in_time_card = itemView.findViewById(R.id.punch_in_time_card);
            punch_out_time_card_view = itemView.findViewById(R.id.punch_out_time_card_view);
            punch_in_location = itemView.findViewById(R.id.punch_in_location);
            attendance_status = itemView.findViewById(R.id.attendance_status);
            working_hours_card = itemView.findViewById(R.id.working_hours_card);
            punch_out_location = itemView.findViewById(R.id.punch_out_location);
            dec_text_view = itemView.findViewById(R.id.dec_text_view);
            employee_id = itemView.findViewById(R.id.employee_id);
            employee_name = itemView.findViewById(R.id.employee_name);
            employee_designation = itemView.findViewById(R.id.employee_designation);


        }
    }
}

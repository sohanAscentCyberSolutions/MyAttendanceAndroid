package com.gmda.attendance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.Activities.AttendanceViewIdActivity;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.Models.UserMonthModel;
import com.gmda.attendance.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class UserReportAttendanceAdapter extends RecyclerView.Adapter<UserReportAttendanceAdapter.ViewHolder> {
    DateFormat outputFormat,outputFormat1,inputFormat1,inputFormat,outputFormatAtt,inputFormatAtt;
    public Date date,date1,dateAtt;
    String inputText,inputText1,inputTextAtt;
    private Context context;
    private List<UserMonthModel.ResponseDataItem> list;

    public UserReportAttendanceAdapter(Context context, List<UserMonthModel.ResponseDataItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.user_layout, parent, false );
        return new ViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferencesHandler s1=new SharedPreferencesHandler(context);

        final UserMonthModel.ResponseDataItem readAttendance = list.get(position);

        //  holder.tv_emp_id.setText(readAttendance.getEmpId());
        outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        outputFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        outputFormatAtt = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        inputFormatAtt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        inputText =String.valueOf(readAttendance.getPunchIn());
        inputText1 =String.valueOf(readAttendance.getPunchOut());
        inputTextAtt =String.valueOf(readAttendance.getAttendanceDate());
        date = null;

        if (readAttendance.getPunchIn()!=null && readAttendance.getPunchOut()!=null){
            try {
                date = inputFormat.parse(inputText);
                //date1 = inputFormat1.parse(inputText1);
                holder.tv_punchInTime.setText(String.valueOf(outputFormat.format(date)));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            date1=null;
            try {

                date1 = inputFormat1.parse(inputText1);
                holder.tv_punchOutTime.setText(String.valueOf(outputFormat1.format(date1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (readAttendance.getPunchIn()!=null && readAttendance.getPunchOut()==null){
            try {
                date = inputFormat.parse(inputText);
                //date1 = inputFormat1.parse(inputText1);
                holder.tv_punchInTime.setText(String.valueOf(outputFormat.format(date)));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            date1=null;
            // holder.tv_punchOutTime.setText(String.valueOf("Not Punch Out"));
        }
        dateAtt = null;

        try {
            dateAtt = inputFormatAtt.parse(inputTextAtt);
            holder.tvAttendanceDate.setText(String.valueOf(outputFormatAtt.format(dateAtt)));
            //date1 = inputFormat1.parse(inputText1);
            // holder.tv_punchInTime.setText(String.valueOf(outputFormat.format(date)));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (readAttendance.getAStatus()!=null){
            if (readAttendance.getAStatus().equalsIgnoreCase("A")){
                holder.img.setBackgroundResource(R.drawable.cgc);
                // holder.tv_punchOutTime.setText("Absent");

            }
            else if (readAttendance.getAStatus().equalsIgnoreCase("P")){
                holder.img.setBackgroundResource(R.drawable.p);

            }

            else if (readAttendance.getAStatus().equalsIgnoreCase("LATE")){
                holder.img.setBackgroundResource(R.drawable.l);

            }
            else if (readAttendance.getAStatus()==null){
                holder.img.setBackgroundResource(R.drawable.grdswaq);
            }
        }

     holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
            public void onClick(View v) {
          Intent intent= new Intent(context, AttendanceViewIdActivity.class);
          intent.putExtra("emp_name",readAttendance.getEmpName());
          intent.putExtra("emp_id",readAttendance.getEmpId());
          intent.putExtra("designation",readAttendance.getDesignation());
          intent.putExtra("department",readAttendance.getDepartment());
          intent.putExtra("division",readAttendance.getDivision());
          intent.putExtra("attendance_date",readAttendance.getAttendanceDate());
          //intent.putExtra("punch_out",readAttendance.getPunchOut());
          intent.putExtra("punch_in",readAttendance.getPunchIn());
           intent.putExtra("punch_out",readAttendance.getPunchOut());
          intent.putExtra("punchout_lat",readAttendance.getPunchoutLat());
         // intent.putExtra("punchin_long",readAttendance.getPunchoutLong());
          //  intent.putExtra("punchin_lat",readAttendance.getPunchinLat());
          // intent.putExtra("punchin_long",readAttendance.getPunchinLong());
          intent.putExtra("working_hr",readAttendance.getWorkingHr());
          intent.putExtra("a_status",readAttendance.getAStatus());

          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(intent);
            }
        });
       /* SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss a MM/dd");
        SimpleDateFormat hh_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date dat1 =hh_mm_ss.parse(list.get(position).getPunchIn());
            String out = dateFormat2.format(dat1);
            holder.tv_punchInTime.setText(out);
            holder.tv_punchOutTime.setText(out);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView tv_punchInTime,tv_punchOutTime,tvStatus,tvName,tv_status,tvAttendanceDate;

        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          // tv_emp_id=itemView.findViewById(R.id.tvpunch_in);

            tv_punchInTime=itemView.findViewById(R.id.tvpunch_in);
            tv_punchOutTime=itemView.findViewById(R.id.tv_punchout);
            img=itemView.findViewById(R.id.imgProfile);
            // tvName=itemView.findViewById(R.id.tvProfile);
            //  tv_status=itemView.findViewById(R.id.tv_status);

            tvAttendanceDate=itemView.findViewById(R.id.tvAttendanceDate);
        }
    }
}

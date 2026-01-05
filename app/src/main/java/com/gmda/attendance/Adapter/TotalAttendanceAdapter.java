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
import com.gmda.attendance.Models.TotalAttendanceToday;
import com.gmda.attendance.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TotalAttendanceAdapter extends RecyclerView.Adapter<TotalAttendanceAdapter.ViewHolder> {

    private DateFormat outputFormat,outputFormatAtt,inputFormatAtt;
    private DateFormat outputFormat1;
    public Date date,date1,dateAtt;
    String inputText,inputText1,inputTextAtt;
    private Context context;
   private List<TotalAttendanceToday.ResponseDataItem> list;


    public TotalAttendanceAdapter(Context context, List<TotalAttendanceToday.ResponseDataItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_layout, parent, false );
        return new ViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferencesHandler s1=new SharedPreferencesHandler(context);

        final TotalAttendanceToday.ResponseDataItem readAttendance = list.get(position);
        outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", DateFormatSymbols.getInstance());
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",DateFormatSymbols.getInstance());
        outputFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a",DateFormatSymbols.getInstance());
        DateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS",DateFormatSymbols.getInstance());
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
            //holder.tv_punchOutTime.setText(String.valueOf("Not Punch Out"));
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
      /*  if (readAttendance.getPunchIn()!=null && readAttendance.getPunchOut()!=null){
            holder.tv_punchInTime.setText(String.valueOf(outputFormat.format(date)));
            holder.tv_punchOutTime.setText(String.valueOf(outputFormat1.format(date1)));
        }
        else if (readAttendance.getPunchIn()!=null && readAttendance.getPunchOut()==null){
            holder.tv_punchInTime.setText(String.valueOf(outputFormat.format(date)));
            holder.tv_punchOutTime.setText(String.valueOf("Not Punch Out"));
        }*/
      /*  else if (readAttendance.getPunchIn()==null && readAttendance.getPunchOut()==null){
            holder.tv_punchInTime.setText(String.valueOf("Not punch in"));
            holder.tv_punchOutTime.setText(String.valueOf("Not Punch Out"));
        }*/
        //String punch_time = readAttendance.getPunchIn().split("T").toString();
        //Log.d("TAG", "onBindViewHolder: "+punch_time);
        //holder.tv_punchInTime.setText(String.valueOf(readAttendance.getPunchIn()));
        holder.tvName.setText(String.valueOf(String.valueOf(readAttendance.getEmpName())));
        //holder.tvAttendanceDate.setText(String.valueOf(String.valueOf(readAttendance.getAttendanceDate())));
        //holder.tv_punchOutTime.setText(String.valueOf(readAttendance.getPunchOut()));
       // holder.tvWorkingHour.setText(String.valueOf(readAttendance.getWorkingHr()));
        holder.tv_status.setText(String.valueOf(readAttendance.getAStatus()));

        if (readAttendance.getAStatus()!=null){
            if (readAttendance.getAStatus().equalsIgnoreCase("A")){
                holder.img.setBackgroundResource(R.drawable.sdcae);
                //holder.tv_punchOutTime.setText("Absent");
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
        intent.putExtra("punch_out",String.valueOf(readAttendance.getPunchOut()));
        intent.putExtra("punch_in",String.valueOf(readAttendance.getPunchOut()));
          //intent.putExtra("punch_out",String.valueOf(readAttendance.getPunchOut()));
          intent.putExtra("punchout_lat",String.valueOf(readAttendance.getPunchoutLat()));
          intent.putExtra("punchin_long",String.valueOf(readAttendance.getPunchoutLong()));
         // intent.putExtra("punch_in",String.valueOf(readAttendance.getPunchIn()));
         // intent.putExtra("punchin_lat",readAttendance.getPunchinLat());
       //   intent.putExtra("punchin_long",readAttendance.getPunchinLong());
          intent.putExtra("working_hr",String.valueOf(readAttendance.getWorkingHr()));
          intent.putExtra("a_status",readAttendance.getAStatus());

        /*  if (readAttendance.getPunchIn()!=null && readAttendance.getPunchOut()!=null){
              intent.putExtra("punch_in",String.valueOf(outputFormat.format(date)));
              intent.putExtra("punch_out",String.valueOf(outputFormat1.format(date1)));
              // holder.tv_punchInTime.setText(String.valueOf(outputFormat.format(date)));
              //holder.tv_punchOutTime.setText(String.valueOf(outputFormat1.format(date1)));
          }
          else if (readAttendance.getPunchIn()!=null && readAttendance.getPunchOut()==null){
            *//*  holder.tv_punchInTime.setText(String.valueOf(outputFormat.format(date)));
              holder.tv_punchOutTime.setText(String.valueOf("Not Punch Out"));
*//*
              intent.putExtra("punch_in",String.valueOf(outputFormat.format(date)));
              intent.putExtra("punch_out","0");
          }*/
        /*  else if (readAttendance.getPunchIn()==null && readAttendance.getPunchOut()==null){
              intent.putExtra("punch_in","0");
              intent.putExtra("punch_out","0");
          }*/
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
           img=itemView.findViewById(R.id.img_absent);
            tvName=itemView.findViewById(R.id.tvProfile);
            tv_status=itemView.findViewById(R.id.tv_status);
            tvAttendanceDate=itemView.findViewById(R.id.tvAttendanceDate);


        }
    }
}

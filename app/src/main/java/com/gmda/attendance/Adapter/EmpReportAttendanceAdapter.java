package com.gmda.attendance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.Models.UserModelAttendance;
import com.gmda.attendance.R;
import com.robinhood.ticker.TickerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class EmpReportAttendanceAdapter extends RecyclerView.Adapter<EmpReportAttendanceAdapter.ViewHolder> {

    DateFormat outputFormat,outputFormat1,inputFormat1,inputFormat,outputFormatAtt,inputFormatAtt;
    public Date date,date1,dateAtt;
    String inputText,inputText1,inputTextAtt;
    private Context context;
    private List<UserModelAttendance.ResponseDataItem> list;



    public EmpReportAttendanceAdapter(Context context, List<UserModelAttendance.ResponseDataItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_items, parent, false );
        return new ViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferencesHandler s1=new SharedPreferencesHandler(context);

        final UserModelAttendance.ResponseDataItem readAttendance = list.get(position);


       /* if (readAttendance.getEmpId()!=null){

            holder.img.setBackgroundResource(R.drawable.e);

        }*/
        holder.tvName.setText(String.valueOf(String.valueOf(readAttendance.getEmpName())));
        holder.tvEmpId.setText(String.valueOf(String.valueOf(readAttendance.getEmpId())));
        holder.tv_designation.setText(String.valueOf(String.valueOf(readAttendance.getDesignation())));
        holder.tv_Division.setText(String.valueOf(String.valueOf(readAttendance.getDivision())));
        holder.tv_total_present.setText(String.valueOf(String.valueOf(readAttendance.getTotalPresent())));
        holder.tv_total_absent.setText(String.valueOf(String.valueOf(readAttendance.getTotalAbsent())));
        holder.tickerLate.setText(String.valueOf(String.valueOf(readAttendance.getTotalLate())));


      /*  holder.tv_total_present.setText(String.valueOf(String.valueOf(readAttendance.gett)));
        holder.tv_total_absent.setText(String.valueOf(String.valueOf(readAttendance.getDivision())));
        holder.tickerLate.setText(String.valueOf(String.valueOf(readAttendance.getDivision())));*/


    /* holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
            public void onClick(View v) {

               *//* Intent intent= new Intent(context, UserReportsActivity.class);
                intent.putExtra("emp_name",readAttendance.getEmpName());
                intent.putExtra("emp_id",readAttendance.getEmpId());
                intent.putExtra("designation",readAttendance.getDesignation());
                intent.putExtra("totalPresent",readAttendance.getTotalPresent());
                intent.putExtra("totalAbsent",readAttendance.getTotalAbsent());
                intent.putExtra("totalLate",readAttendance.getTotalLate());
                intent.putExtra("totalLateAbsent",readAttendance.getTotalLateAbsent());*//*
               *//* intent.putExtra("work_location",readAttendance.getWorkLocation());
                intent.putExtra("emp_role",readAttendance.getEmpRole());*//*
               *//* intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*//*
            }
        });*/
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


        TextView tv_designation,tv_Division,tvStatus,tvName,tvEmpId;
        TickerView tv_total_present,tv_total_absent,tickerLate;

        ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          // tv_emp_id=itemView.findViewById(R.id.tvpunch_in);


          // img=itemView.findViewById(R.id.imgProfile);
            tvName=itemView.findViewById(R.id.nameTextView);
            tv_designation=itemView.findViewById(R.id.designationTextView);
            tv_Division=itemView.findViewById(R.id.divisionTextView);
            tvEmpId=itemView.findViewById(R.id.empTextView);
            tv_total_present=itemView.findViewById(R.id.tv_total_present);
            tv_total_absent=itemView.findViewById(R.id.tv_total_absent);
            tickerLate=itemView.findViewById(R.id.sdf);


        }
    }
}

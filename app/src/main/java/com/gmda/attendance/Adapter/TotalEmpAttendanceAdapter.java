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

import com.gmda.attendance.Activities.UserActivityDetail;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.Models.ReadTotalData;
import com.gmda.attendance.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class TotalEmpAttendanceAdapter extends RecyclerView.Adapter<TotalEmpAttendanceAdapter.ViewHolder> {
    DateFormat outputFormat,outputFormat1,inputFormat1,inputFormat,outputFormatAtt,inputFormatAtt;
    public Date date,date1,dateAtt;
    String inputText,inputText1,inputTextAtt;
    private Context context;
   private List<ReadTotalData.ResponseDataItem> list;

    public TotalEmpAttendanceAdapter(Context context, List<ReadTotalData.ResponseDataItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_total, parent, false );
        return new ViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedPreferencesHandler s1=new SharedPreferencesHandler(context);

        final ReadTotalData.ResponseDataItem readAttendance = list.get(position);


        if (readAttendance.getEmpId()!=null){

            holder.img.setBackgroundResource(R.drawable.e);

        }
        holder.tvName.setText(String.valueOf(String.valueOf(readAttendance.getEmpName())));
        holder.tvEmpId.setText(String.valueOf(String.valueOf(readAttendance.getEmpId())));
        holder.tv_Designation.setText(String.valueOf(String.valueOf(readAttendance.getDesignation())));
        holder.tv_WorkLocation.setText(String.valueOf(String.valueOf(readAttendance.getWorkLocation())));
     holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, UserActivityDetail.class);
                intent.putExtra("emp_name",readAttendance.getEmpName());
                intent.putExtra("emp_id",readAttendance.getEmpId());
                intent.putExtra("designation",readAttendance.getDesignation());
                intent.putExtra("department",readAttendance.getDepartment());
                intent.putExtra("division",readAttendance.getDivision());
                intent.putExtra("work_location",readAttendance.getWorkLocation());
                intent.putExtra("emp_role",readAttendance.getEmpRole());
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


        TextView tv_WorkLocation,tv_Designation,tvStatus,tvName,tvEmpId;

        ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

          // tv_emp_id=itemView.findViewById(R.id.tvpunch_in);


           img=itemView.findViewById(R.id.img_absent);
            tvName=itemView.findViewById(R.id.tvName);
            tv_WorkLocation=itemView.findViewById(R.id.tv_WorkLocation);
            tv_Designation=itemView.findViewById(R.id.tvDesignation);
            tvEmpId=itemView.findViewById(R.id.tvEmpId);


        }
    }
}

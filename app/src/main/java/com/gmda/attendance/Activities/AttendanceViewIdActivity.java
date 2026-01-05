package com.gmda.attendance.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gmda.attendance.R;
import com.robinhood.ticker.TickerView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceViewIdActivity extends AppCompatActivity {
    CircleImageView profileUser;
    TextView tvEmployeeName,tvEmployeeId,tvDepartment,tvDivision,tvDesignation,tvAttendanceDate,tvPunchin_time,tvLocations,
            tvpunchOut,tvPunchoutLat,tvpunchOutLong,tv_punchinlat,tvpunchinLong;
    TickerView workinghour,status,remark;
    PieChart pieChart;
    String name,id,designation,department,punchouttime,punchintime,punchinlat,punchinlong,workinhr,statuses,remarks,division,punchoutlat,punchoutlang,
           attendendate ;
    DateFormat outputFormat,outputFormat1,inputFormat1,inputFormat,outputFormatAtt,inputFormatAtt;
    public Date date,date1,dateAtt;
    String inputText,inputText1,inputTextAtt;

    androidx.appcompat.app.AlertDialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_view_id);
       // readAttemdenceByIdVM = ViewModelProviders.of(this).get(ReadAttemdenceByIdVM.class);

       // profileUser=findViewById(R.id.profile_image);
        tvEmployeeName=findViewById(R.id.t_name);
        tvEmployeeId=findViewById(R.id.t_id);
        tvDepartment=findViewById(R.id.t_department);
        tvDivision=findViewById(R.id.t_division);
        tvDesignation=findViewById(R.id.t_designation);
        tvAttendanceDate=findViewById(R.id.tvAtt);
        tvPunchin_time=findViewById(R.id.t_punch_in_time);
     // tv_punchinlat=findViewById(R.id.tv_punchInLat);
        tvpunchinLong=findViewById(R.id.tv_punchInLong);
        tvpunchOut=findViewById(R.id.t_punchOut);
        tvPunchoutLat=findViewById(R.id.tPunchOut_lat);
     //   tvpunchOutLong=findViewById(R.id.t_punchOut_long);
        workinghour=findViewById(R.id.t_working_day);
        status=findViewById(R.id.t_status);
        remark=findViewById(R.id.t_remark);
        pieChart=findViewById(R.id.piechart1);

        outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
        outputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
        outputFormatAtt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        inputFormatAtt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

        inputText =getIntent().getStringExtra("punch_in");  //String.valueOf(punchintime);
        inputText1 = getIntent().getStringExtra("punch_out");
        inputTextAtt = getIntent().getStringExtra("punch_out");
        date = null;

        try {
            date = inputFormat.parse(String.valueOf(inputText));
            //date1 = inputFormat1.parse(inputText1);
            tvPunchin_time.setText(String.valueOf(outputFormat.format(date)));
            //holder.tv_punchOutTime.setText(String.valueOf(outputFormat1.format(date1)));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        date1=null;
        try {

            date1 = inputFormat1.parse(String.valueOf(inputText1));
            tvpunchOut.setText(String.valueOf(outputFormat1.format(date1)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateAtt=null;
        try {

            dateAtt = inputFormatAtt.parse(String.valueOf(inputTextAtt));
            tvAttendanceDate.setText(String.valueOf(outputFormat1.format(dateAtt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        name = getIntent().getStringExtra("emp_name");
        id = getIntent().getStringExtra("emp_id");
        department = getIntent().getStringExtra("department");
        designation = getIntent().getStringExtra("designation");
        statuses = getIntent().getStringExtra("a_status");
        //punchouttime = getIntent().getStringExtra("punch_out");

        punchinlat = getIntent().getStringExtra("punchin_lat");
        punchinlong = getIntent().getStringExtra("punchin_long");
        workinhr = getIntent().getStringExtra("working_hr");
        punchoutlat = getIntent().getStringExtra("punchout_lat");
        punchoutlang = getIntent().getStringExtra("punchin_long");
        //punchoutlang = getIntent().getStringExtra("punchin_long");
        division = getIntent().getStringExtra("division");
        attendendate = getIntent().getStringExtra("attendance_date");
        Log.d("TAG", "onCreate: "+name);

        tvEmployeeName.setText(name);
        tvEmployeeId.setText(id);
        tvDepartment.setText(department);
         tvDivision.setText(division);
        tvDesignation.setText(designation);
//       tvAttendanceDate.setText(attendendate);

//    tv_punchinlat.setText(punchinlat);
    tvpunchinLong.setText(punchinlong);
    tvPunchoutLat.setText(punchoutlat);
    tvpunchinLong.setText(punchinlong);


      //tvPunchoutLat.setText(punchoutlat);
       //tvpunchOutLong.setText(punchoutlang);
        workinghour.setText(workinhr);
        status.setText(statuses);
        //remark.setText(String.valueOf(responseDataItem.getRemarks()));*/

     /*  pieChart.addPieSlice(new PieModel("Confirm",working,getResources().getColor(R.color.yellow)));
        pieChart.addPieSlice(new PieModel("Active",leave,getResources().getColor(R.color.blue_pie)));
        pieChart.addPieSlice(new PieModel("Recovered",present,getResources().getColor(R.color.green_pie)));
        pieChart.addPieSlice(new PieModel("Death",absent,getResources().getColor(R.color.red_pie)));
        pieChart.startAnimation();*/
       /* if (punchintime!=null && punchouttime!=null){*/

           // tvpunchOut.setText(punchouttime);

       // }
       /* else if (punchintime!=null && punchouttime==null){
            tvPunchin_time.setText(punchintime);
            tvpunchOut.setText("0");

        }*/



//        tvPunchin_time.setText(String.valueOf(outputFormat.format(date)));
       // tvpunchOut.setText(String.valueOf(outputFormat1.format(date1)));
    }
}

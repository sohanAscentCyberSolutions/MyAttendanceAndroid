package com.gmda.attendance.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gmda.attendance.Adapter.DurationAttendanceAdapter;
import com.gmda.attendance.Adapter.EmpReportAttendanceAdapter;
import com.gmda.attendance.Adapter.TodayAttendanceAdapter;
import com.gmda.attendance.Adapter.TotalAttendanceAdapter;
import com.gmda.attendance.Adapter.TotalEmpAttendanceAdapter;
import com.gmda.attendance.Adapter.UserAttendanceAdapter;
import com.gmda.attendance.Adapter.UserDurationAttendanceAdapter;
import com.gmda.attendance.Adapter.UserReportAttendanceAdapter;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.Models.DepartmentModel;
import com.gmda.attendance.Models.DivTodayAttendence;
import com.gmda.attendance.Models.DivisionModel;
import com.gmda.attendance.Models.DurationRecord;
import com.gmda.attendance.Models.ReadTotalData;
import com.gmda.attendance.Models.TotalAttendanceToday;
import com.gmda.attendance.Models.UserDateWiseData;
import com.gmda.attendance.Models.UserMonthModel;
import com.gmda.attendance.Models.WorkLocation;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.ViewModel.ReadAttemdenceVM;
import com.gmda.attendance.ViewModel.ReadAttendanceDateVM;
import com.gmda.attendance.common_module.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.robinhood.ticker.TickerView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.StateSet.TAG;


public class Attendance_ReportAdmin_Activity extends AppCompatActivity {

    public LinearLayout linear_list_of_employee, ll;
    Spinner sp_Department, sp_division, sp_month, sp_year;
    EditText etMonth, etYear;
    int totalEmp = 0;
    TextView tv_dateShow;
    TSnackbar snackbar;
    int a = 11, b = 2021;
    TotalEmpAttendanceAdapter totalEmpAttendanceAdapter;
    String modes;
    LinearLayout linearLate, linearTotalEmp;
    TextView tvd, tvDi;
    DivTodayAttendence divTodayAttendence;
    UserDurationAttendanceAdapter userDurationAttendanceAdapter;
    ImageView logOut;
    Button changePassword;
    PieChart tvPi_chart;
    private ShimmerFrameLayout mShimmerViewContainer;
    DivisionModel divisionModel;
    DepartmentModel departmentModel;
    WorkLocation workLocation;

    NavigationView navigationView;
    /*DrawerLayout drawer;
    ActionBarDrawerToggle toggle;*/
    ArrayList<String> division_name;
    ArrayList<String> month_name;
    ArrayList<String> year_name;
    TextView tvUser;
    ArrayList<String> department_name;
    ArrayList<String> workLocation_name;
    String div, depart, work;
    String selectedCalenderEndDate = "";
    String selctedCalenderStartDate = "";
    String selctedCalenderMonth = "";
    String selctedCalenderYear = "";
    String selectCalenderDate = "";
    String singleDate = "";
    int t;
    String[] department = {"GMDA"};
    String[] month = {"Select Month", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String[] monthName = {"Select Month", "January", "february", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
   // Integer [] monthNumbers = {1,2,3,4,5,6,7,8,9,10,11,12};
    String[] year = {"Select Year", "2021", "2022", "2023", "2024", "2025"};
    int iteratorSingleDate;
    String currentSpinnerDivisionValue = "";
    String currentSpinnerMonthValue = "";
    String currentSpinnerYearValue = "";
    String currentSpinnerDepartmentValue = "";
    TextView tv_date;
    RecyclerView recyclerViewTotal, recyclerViewReport;

    LinearLayout linearLayout11, linear_layout;
    int data = 0;
    EditText etStart, etEnd;
    ImageView searchData;
    private List<UserMonthModel.ResponseDataItem> listReportP;
    private List<UserMonthModel.ResponseDataItem> listReportUserA;
    private List<UserMonthModel.ResponseDataItem> listReportUserL;

    private List<UserMonthModel.ResponseDataItem> listReport;
    private List<UserMonthModel.ResponseDataItem> listReportUser;
    private List<ReadTotalData.ResponseDataItem> listEmpTotal;
    private List<TotalAttendanceToday.ResponseDataItem> list_total;
    private List<TotalAttendanceToday.ResponseDataItem> list_totalA;
    private List<TotalAttendanceToday.ResponseDataItem> list_totalP;
    private List<TotalAttendanceToday.ResponseDataItem> list_totalL;
    private List<DivTodayAttendence.ResponseDataItem> list_today;
    private List<DivTodayAttendence.ResponseDataItem> list_today_P;
    private List<DivTodayAttendence.ResponseDataItem> list_today_A;
    private List<DivTodayAttendence.ResponseDataItem> list_today_L;
    private List<DurationRecord.ResponseDataItem> list_duration;
    private List<DurationRecord.ResponseDataItem> list_duration_P;
    private List<DurationRecord.ResponseDataItem> list_duration_A;
    private List<DurationRecord.ResponseDataItem> list_duration_L;
    private List<UserDateWiseData.ResponseDataItem> list_userDateA;
    private List<UserDateWiseData.ResponseDataItem> list_userDateP;
    private List<UserDateWiseData.ResponseDataItem> list_userDateL;
    private TodayAttendanceAdapter todayAttendanceAdapter;
    private DurationAttendanceAdapter durationAttendanceAdapter;
    private TotalAttendanceAdapter totalAttendanceAdapter;
    private UserReportAttendanceAdapter userReportAttendanceAdapter;
    private UserAttendanceAdapter userAttendanceAdapter;
    private EmpReportAttendanceAdapter empReportAttendanceAdapter;

    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView selectDate;
    Button clearData;
    LinearLayout layout_data, layout1;
    Calendar myCalendar1, myCalendar, myCalendar2, calenderMonth, calenderYear;
    boolean DataSelectedDate;
    String monthSelect, yearSelect, startDate, endDate;
    CalendarView calendar;
    String DateWise;
    TickerView tvTotalEmp, tvTotalAbsent, tvTotalPresent, lateTicker;
    ReadAttemdenceVM readAttemdenceVM;
    ReadAttendanceDateVM readAttendanceDateVM;
    String myData, monthData, yearData;
    String dataMonth;
    Button submitAttendance;
    int currentSpinnerSelectionDivision = 0;
    int currentSpinnerSelectionDepartment = 0;
    String currentMonthSelection = String.valueOf(0);
    String currentYearSelection = String.valueOf(0);
    LinearLayout layoutNameAttendance;
    TextView late, tvemptotal;
    //ProgressBar progressBar;
    RecyclerView recUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendanceadmin__report_);

        // calendar = findViewById(R.id.calendar);
        recyclerView = findViewById(R.id.recyclerview);
        late = findViewById(R.id.late);
        recyclerViewReport = findViewById(R.id.recAdmin);
        /* lateTicker=findViewById(R.id.lateTicker);*/

        //progressBar=findViewById(R.id.progressBar);

        /* list_a = new ArrayList<ReadAttendance.ResponseDataItem>();*/
        //tv_date = findViewById(R.id.tv_date);
        //declare
        layoutNameAttendance = findViewById(R.id.linear_layoutName_Attendance);
        lateTicker = findViewById(R.id.sdf);
        ll = findViewById(R.id.ll);
        logOut = findViewById(R.id.imgLogout);
        tvd = findViewById(R.id.tvd);
        tvDi = findViewById(R.id.tvDi);
        sp_month = findViewById(R.id.sp_month);
        sp_year = findViewById(R.id.sp_year);
        lateTicker.setVisibility(View.VISIBLE);
        recyclerViewTotal = findViewById(R.id.recyclerviewTotal);
        list_today = new ArrayList<DivTodayAttendence.ResponseDataItem>();
        list_today_P = new ArrayList<DivTodayAttendence.ResponseDataItem>();
        list_today_A = new ArrayList<DivTodayAttendence.ResponseDataItem>();
        list_today_L = new ArrayList<DivTodayAttendence.ResponseDataItem>();
        list_duration = new ArrayList<DurationRecord.ResponseDataItem>();
        list_duration_P = new ArrayList<DurationRecord.ResponseDataItem>();
        list_duration_A = new ArrayList<DurationRecord.ResponseDataItem>();
        list_duration_L = new ArrayList<DurationRecord.ResponseDataItem>();
        list_total = new ArrayList<TotalAttendanceToday.ResponseDataItem>();
        list_totalA = new ArrayList<TotalAttendanceToday.ResponseDataItem>();
        list_totalP = new ArrayList<TotalAttendanceToday.ResponseDataItem>();
        //list_user = new ArrayList<ReadAttendanceById.ResponseDataItem>();
        list_totalL = new ArrayList<TotalAttendanceToday.ResponseDataItem>();
        // list_userA = new ArrayList<ReadAttendanceById.ResponseDataItem>();
        // list_userL = new ArrayList<ReadAttendanceById.ResponseDataItem>();
        // list_userP= new ArrayList<ReadAttendanceById.ResponseDataItem>();
        list_userDateA = new ArrayList<UserDateWiseData.ResponseDataItem>();
        list_userDateP = new ArrayList<UserDateWiseData.ResponseDataItem>();
        list_userDateL = new ArrayList<UserDateWiseData.ResponseDataItem>();
        listEmpTotal = new ArrayList<ReadTotalData.ResponseDataItem>();
        listReport = new ArrayList<UserMonthModel.ResponseDataItem>();
        listReportUser = new ArrayList<UserMonthModel.ResponseDataItem>();
        // tvLate,tvsTotalEmp
        listReportUserA = new ArrayList<UserMonthModel.ResponseDataItem>();
        listReportP = new ArrayList<UserMonthModel.ResponseDataItem>();
        listReportUserL = new ArrayList<UserMonthModel.ResponseDataItem>();
        recUser=findViewById(R.id.recUser);
        linearLate = findViewById(R.id.linearTotalLate);
        linearTotalEmp = findViewById(R.id.linearTotalEmp);
        sp_Department = findViewById(R.id.sp_department);
        sp_division = findViewById(R.id.sp_division);
        // sp_Department.setOnItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);
        tvTotalAbsent = findViewById(R.id.tv_total_absent);
        tvTotalPresent = findViewById(R.id.tv_total_present);
        tvTotalEmp = findViewById(R.id.tv_total_emp);
        tvPi_chart = findViewById(R.id.tvPi_chart);
        layout_data = findViewById(R.id.layout_data);
        etStart = findViewById(R.id.et_start);
        etEnd = findViewById(R.id.et_end);
        linear_layout = findViewById(R.id.layout1s);

        clearData = findViewById(R.id.clearData);
        sp_month = findViewById(R.id.sp_month);




        readAttemdenceVM = ViewModelProviders.of(this).get(ReadAttemdenceVM.class);
        readAttendanceDateVM = ViewModelProviders.of(this).get(ReadAttendanceDateVM.class);
        navigationView = findViewById(R.id.nav_view);
        // tv_emp_timer = findViewById(R.id.tv_emp_timer);

        toolbar = findViewById(R.id.toolbar);
        //timerTextView = findViewById(R.id.tv_timer);
        //drawer = findViewById(R.id.drawer_layout);
        SharedPreferencesHandler s1 = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
        //Creating the ArrayAdapter instance having the country list
        //ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, division);
        //aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        // sp_division.setAdapter(av);//
       // Utils.showProgress(Attendance_Report_Activity.this);
        //ArrayAdapter <Integer> am = new ArrayAdapter <Integer>(Attendance_Report_Activity.this, android.R.layout.simple_spinner_item, monthNumbers);
        ArrayAdapter am = new ArrayAdapter(this, android.R.layout.simple_spinner_item, monthName);
        am.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_month.setAdapter(am);

        ArrayAdapter as = new ArrayAdapter(this, android.R.layout.simple_spinner_item, year);
        as.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_year.setAdapter(as);

        ArrayAdapter av = new ArrayAdapter(this, android.R.layout.simple_spinner_item, department);
        av.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner

        sp_Department.setAdapter(av);
        submitAttendance = findViewById(R.id.btn_submit_attendance);
        tvemptotal = findViewById(R.id.tvemptotal);
        final TextView tvText = findViewById(R.id.tvText);
        tvText.setVisibility(View.VISIBLE);
        linearTotalEmp.setVisibility(View.GONE);
        list_userDateP.clear();
        list_userDateA.clear();
        list_userDateL.clear();
        tvd.setVisibility(View.GONE);
        tvDi.setVisibility(View.GONE);
        sp_division.setVisibility(View.GONE);
        sp_Department.setVisibility(View.GONE);

        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listReportP.clear();
                listReportUserL.clear();
                listReportUserA.clear();
                if (!currentMonthSelection.equals("")) {
                   if (sp_month.getSelectedItemPosition()!=0){
                       sp_month.setSelection(0);
                   }
                   // currentMonthSelection = month[position].toString();
                   // sp_month.setAdapter(null);
                }
                if (!currentYearSelection.equals("")) {
                    // currentMonthSelection = month[position].toString();
                    sp_year.setSelection(0);
                }
                if (!currentSpinnerDivisionValue.equals("")) {
                    // currentMonthSelection = month[position].toString();
                    sp_division.setSelection(0);
                }
                recyclerViewReport.setVisibility(View.GONE);
                etStart.setText("");
                etEnd.setText("");
                selctedCalenderStartDate = "";
                selectedCalenderEndDate = "";
                lateTicker.setText("0");
                recyclerViewTotal.setVisibility(View.GONE);
                list_duration_A.clear();
                list_duration_P.clear();
                list_duration_L.clear();
                list_totalL.clear();

                listEmpTotal.clear();
                list_totalP.clear();
                list_totalA.clear();
                // tv_total_late.setText("0");
                layout_data.setVisibility(View.GONE);
                list_userDateP.clear();
                list_userDateA.clear();
                list_userDateL.clear();
                recyclerView.setVisibility(View.GONE);
                list_today.clear();
                list_today_A.clear();
                list_today_P.clear();
                list_today_L.clear();
                list_duration.clear();
                etStart.setText("");
                etEnd.setText("");
                list_duration_A.clear();
                list_duration_P.clear();
                list_duration_L.clear();
                tvTotalEmp.setText("0");
                lateTicker.setText("0");
                tvTotalAbsent.setText("0");
                tvTotalPresent.setText("0");
                //list_duration_Aa.clear();
                //  list_duration_Pa.clear();
                list_today_A.clear();
                list_today_P.clear();
                list_today_L.clear();
                list_today.clear();
                linear_layout.setVisibility(View.VISIBLE);
                layoutNameAttendance.setVisibility(View.VISIBLE);
                tvPi_chart.clearChart();

            }
        });

        sp_Department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSpinnerSelectionDepartment = position;
                Log.d("CurrentSpinner", "Spinner item is : " + currentSpinnerSelectionDepartment);
                myData = String.valueOf(sp_Department.getAdapter().getItem(currentSpinnerSelectionDepartment));
                Log.d("CurrentSpinner", "Spinner value is : " + myData);
                currentSpinnerDepartmentValue = myData;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + position);
                if (position != 0) {
                    currentMonthSelection = month[position].toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position != 0) {
                    currentYearSelection = year[position];
                }

                // currentSpinnerYearValue=String.valueOf(currentYearSelection);

         /*  Log.d("CurrentSpinner", "Spinner item is : " + currentSpinnerSelectionDepartment);
                  yearData = String.valueOf(sp_year.getAdapter().getItem(currentYearSelection));
                Log.d( "CurrentSpinner", "Spinner value is : " + myData);
                currentSpinnerYearValue = yearData;
            }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            /*    div=  sp_division.getSelectedItem().toString();
                // division_name=sp_division.getSelectedItem().toString();
                // speciesname=species.getSelectedItem().toString();
*/

                currentSpinnerSelectionDivision = position;
                Log.d("CurrentSpinner", "Spinner item is : " + currentSpinnerSelectionDivision);
                String selected = String.valueOf(sp_division.getAdapter().getItem(currentSpinnerSelectionDivision));
                Log.d("CurrentSpinner", "Spinner value is : " + selected);
                currentSpinnerDivisionValue = selected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listReportP.clear();
                listReportUserL.clear();
                listReportUserA.clear();
                list_userDateL.clear();
                list_userDateA.clear();
                list_userDateP.clear();
                lateTicker.setText("0");
                listEmpTotal.clear();
                list_totalP.clear();
                list_totalA.clear();
                list_totalL.clear();
                list_duration_A.clear();
                list_duration_P.clear();
                list_duration_L.clear();
                //  validation();
                // list_today.clear();
                // late.setVisibility(View.GONE);
                // lateTicker.setVisibility(View.GONE);
                layout_data.setVisibility(View.GONE);
                lateTicker.setVisibility(View.VISIBLE);
                list_userDateP.clear();
                list_userDateL.clear();
                list_userDateA.clear();
                list_duration.clear();
                sp_division.getItemAtPosition(0);
                recyclerView.setVisibility(View.GONE);
                list_duration_A.clear();
                list_duration_P.clear();
                list_duration_L.clear();
                tvTotalEmp.setText("0");
                tvTotalAbsent.setText("0");
                tvTotalPresent.setText("0");

                //list_duration_Aa.clear();
                //  list_duration_Pa.clear();
                list_today_A.clear();
                list_today_P.clear();
                list_today_L.clear();


                linear_layout.setVisibility(View.VISIBLE);
                layoutNameAttendance.setVisibility(View.VISIBLE);
                tvPi_chart.clearChart();

               /* if (sp_Department.getSelectedItemPosition()==0 ){

                    showSnackbarAlert(ll, getString(R.string.err_msg_dept));
                    sp_Department.requestFocus();

                }*/
                if (sp_division.getSelectedItemPosition() == 0) {
                    showSnackbarAlert(ll, getString(R.string.err_msg_div));
                    sp_division.requestFocus();
                }
                SharedPreferencesHandler sq = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
               /* if (sq.DirectReadPreference(Constdata.emp_role, Attendance_ReportAdmin_Activity.this).equalsIgnoreCase("Admin")) {

                    // totalEmployeeApiCAll();
                 if (*//*currentSpinnerSelectionDepartment != 0 &&*//* *//*currentSpinnerSelectionDepartment!=0 && currentMonthSelection.equals("")&& currentYearSelection.equals("") &&*//* currentSpinnerSelectionDivision != 0 && selctedCalenderStartDate.equals("") && selectedCalenderEndDate.equals("")) {

                       // Utils.dismissProgress();
                      //  progressBar.setVisibility(View.VISIBLE);
                        todayAttendanceApiCall();
                        totalEmployeeApiCAll();
                        Log.d("today", "Spinner is selected and start date & end date null");
                    }
                    else if (*//*currentSpinnerSelectionDepartment != 0 &&*//**//*currentMonthSelection.equals("")&& currentYearSelection.equals("") && *//*currentSpinnerSelectionDivision != 0 && selctedCalenderStartDate != "" && selectedCalenderEndDate != "") {
                        //progressBar = new ProgressBar(Attendance_Report_Activity.this, null, android.R.attr.progressBarStyleSmall);
                       // progressBar.setVisibility(View.VISIBLE);
                        //Utils.dismissProgress();
                        startDateEndDateApiCall();
                        totalEmployeeApiCAll();
                        Log.d("button", "Spinner is selected and start date & end date selected");
                    } if (*//*currentMonthSelection.equals("")&& currentYearSelection.equals("") &&*//* currentSpinnerSelectionDivision != 0 && selctedCalenderStartDate != "" && selectedCalenderEndDate == "") {
                        //progressBar = new ProgressBar(Attendance_Report_Activity.this, null, android.R.attr.progressBarStyleSmall);
                        //progressBar.setVisibility(View.VISIBLE);
                        //Utils.dismissProgress();
                        DateWiseDateApiCall();
                        totalEmployeeApiCAll();

                    }
                    else if (currentMonthSelection!=""&& currentYearSelection!="" && currentSpinnerSelectionDivision == 0 && selctedCalenderStartDate == "" && selectedCalenderEndDate == "") {
                       // progressBar = new ProgressBar(Attendance_Report_Activity.this, null, android.R.attr.progressBarStyleSmall);
                       // progressBar.setVisibility(View.VISIBLE);
                        snackbar.dismiss();
                        Utils.dismissProgress();
                        monthWiseApiCall();
                    }
*/
                if (sq.DirectReadPreference(Constdata.emp_id, Attendance_ReportAdmin_Activity.this)!=null) {
                    if (selctedCalenderStartDate != "" && selectedCalenderEndDate != "") {
                        //progressBar.setVisibility(View.VISIBLE);
                        userAttendanceApiDuration();
                        Log.d("button", "Spinner is selected and start date & end date selected");
                    } else if (selctedCalenderStartDate != "" && selectedCalenderEndDate == "") {
                       // progressBar.setVisibility(View.VISIBLE);

                        userDateWiseApiCall();

                    }
                   else if (currentMonthSelection!=""&& currentYearSelection!="" && selctedCalenderStartDate == "" && selectedCalenderEndDate == "") {
                        // progressBar = new ProgressBar(Attendance_Report_Activity.this, null, android.R.attr.progressBarStyleSmall);
                        // progressBar.setVisibility(View.VISIBLE);
                        //snackbar.dismiss();

                        usermonthApiCall();
                    }
                }

                /*if (*//*currentSpinnerSelectionDepartment!= 0 &&  *//*currentSpinnerSelectionDivision != 0 && singleDate != "" && selctedCalenderStartDate.equals("") && selectedCalenderEndDate.equals("")) {
                    singleDateAttendanceApiCall();
                    Log.d("setData", "Spinner is selected and seingleDate and start date & end date empty");
                }*/
            }
        });
        myCalendar = Calendar.getInstance();
        myCalendar1 = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
      /*  calenderMonth = Calendar.getInstance();
        calenderYear = Calendar.getInstance();
           *//* etStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar2.set(Calendar.YEAR, year);
                            myCalendar2.set(Calendar.MONTH, monthOfYear);
                            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateDateWise();
                        }


                    };
                    new DatePickerDialog(Attendance_Report_Activity.this, dates, myCalendar2
                            .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                            myCalendar2.get(Calendar.DAY_OF_MONTH)).show();

                }
            });*//*

        final DatePickerDialog.OnDateSetListener month = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                //myCalendar.set(Calendar.YEAR, year);
                calenderMonth.set(Calendar.MONTH, monthOfYear);
                //myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateRecord();


            }
        };

        final DatePickerDialog.OnDateSetListener year = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calenderYear.set(Calendar.YEAR, year);
                //myCalendar.set(Calendar.MONTH, monthOfYear);
                //myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateRecord();

            }
        };*/
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateRecord();

            }
        };
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateRecord1();

                // call Api

            }
        };
        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Attendance_ReportAdmin_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Attendance_ReportAdmin_Activity.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
    }

   private void usermonthApiCall() {

        SharedPreferencesHandler sb = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
        Call<UserMonthModel> callK = RetrofitClient.getInstance().getApiInterface().getAttendanceMonthUser("Bearer "+sb.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        currentMonthSelection,
                        currentYearSelection,
                        sb.DirectReadPreference(Constdata.emp_id,Attendance_ReportAdmin_Activity.this));

        callK.enqueue(new Callback<UserMonthModel>() {
            @Override
            public void onResponse(Call<UserMonthModel> call, Response<UserMonthModel> response) {

                if (response.body() != null) {

                    if (response.body().getResponseType().equalsIgnoreCase("success")) {

                        int monthPresent=0;
                        int monthAbsent=0;
                        int monthLate=0;
                        int monthCount;
                        for (monthCount=0;monthCount<response.body().getResponseData().size();monthCount++){

                            if (response.body().getResponseData().get(monthCount).getAttendanceDate()!=null){


                                if (response.body().getResponseData().get(monthCount).getAStatus().equalsIgnoreCase("P")){
                                    listReportP.add(response.body().getResponseData().get(monthCount));
                                    monthPresent++;
                                }
                                else if (response.body().getResponseData().get(monthCount).getAStatus().equalsIgnoreCase("A")){
                                    listReportUserA.add(response.body().getResponseData().get(monthCount));
                                    monthAbsent++;
                                }
                                else if (response.body().getResponseData().get(monthCount).getAStatus().equalsIgnoreCase("LATE")){
                                    listReportUserL.add(response.body().getResponseData().get(monthCount));
                                    monthLate++;
                                }
                            }
                        }

                        tvTotalPresent.setText(String.valueOf(monthPresent));
                        tvTotalAbsent.setText(String.valueOf(monthAbsent));
                        lateTicker.setText(String.valueOf(monthLate));
                        recyclerView.setVisibility(View.GONE);
                        layout_data.setVisibility(View.VISIBLE);


                        tvPi_chart.addPieSlice(new PieModel("Present", monthPresent, getResources().getColor(R.color.green_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Absent", monthAbsent, getResources().getColor(R.color.red_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Total", monthLate, getResources().getColor(R.color.yellow)));
                        //tvPi_chart.addPieSlice(new PieModel("Absent", userSingleLate, getResources().getColor(R.color.late)));
                        tvPi_chart.startAnimation();


                        tvTotalPresent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userReportAttendanceAdapter = new UserReportAttendanceAdapter(getApplicationContext(), listReportP);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userReportAttendanceAdapter);
                                userReportAttendanceAdapter.notifyDataSetChanged();
                            }
                        });
                        tvTotalAbsent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userReportAttendanceAdapter = new UserReportAttendanceAdapter(getApplicationContext(), listReportUserA);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userReportAttendanceAdapter);
                                userReportAttendanceAdapter.notifyDataSetChanged();
                            }
                        });
                        lateTicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userReportAttendanceAdapter = new UserReportAttendanceAdapter(getApplicationContext(), listReportUserL);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userReportAttendanceAdapter);
                                userReportAttendanceAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                    else if (response.body().getResponseType().equalsIgnoreCase("warning")){
                        Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserMonthModel> call, Throwable t) {

            }
        });

    }

  /*  private void monthWiseApiCall() {

        SharedPreferencesHandler sb = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
        Call<UserMonthModel> callK = RetrofitClient.getInstance().getApiInterface()
                .getAttendanceMonthAdmin("Bearer " + sb.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        currentMonthSelection,
                        currentYearSelection);
        callK.enqueue(new Callback<UserMonthModel>() {
            @Override
            public void onResponse(Call<UserMonthModel> call, Response<UserMonthModel> response) {

                if (response.body() != null) {


                    if (response.body().getResponseType().equalsIgnoreCase("success")) {

                        layoutNameAttendance.setVisibility(View.GONE);
                        recyclerViewReport.setVisibility(View.VISIBLE);
                        linear_layout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        recyclerViewTotal.setVisibility(View.GONE);
                        layout_data.setVisibility(View.GONE);
                        for (int i = 0; i < response.body().getResponseData().size(); i++) {
                            listReport.add(response.body().getResponseData().get(i));
                        }

                        userReportAttendanceAdapter = new UserReportAttendanceAdapter(getApplicationContext(), listReport);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                        recyclerViewReport.setLayoutManager(layoutManager);
                        recyclerViewReport.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewReport.setAdapter(userReportAttendanceAdapter);
                        userReportAttendanceAdapter.notifyDataSetChanged();
                    }

                    if (response.body().getResponseType().equalsIgnoreCase("warning")) {

                        Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<UserMonthModel> call, Throwable t) {

            }
        });

    }*/


    private void totalEmployeeApiCAll() {

        totalEmp = 0;
        SharedPreferencesHandler sc = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
        Call<ReadTotalData> call = RetrofitClient.getInstance().getApiInterface().
                getTotalEmp("Bearer " + sc.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        "GMDA",
                        currentSpinnerDivisionValue);


        call.enqueue(new Callback<ReadTotalData>() {
            @Override
            public void onResponse(Call<ReadTotalData> call, Response<ReadTotalData> response) {

                if (response.body().getResponseType().equalsIgnoreCase("success")) {
                    int count;
                    for (count = 0; count < response.body().getResponseData().size(); count++) {

                        if (response.body().getResponseData().get(count).getEmpId() != null) {
                            totalEmp++;
                            listEmpTotal.add(response.body().getResponseData().get(count));
                        }
                    }

                    tvTotalEmp.setText(String.valueOf(totalEmp));
                    tvPi_chart.addPieSlice(new PieModel("Total", totalEmp, getResources().getColor(R.color.blue_pie)));
                    tvTotalEmp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recyclerView.setVisibility(View.GONE);
                            recyclerViewTotal.setVisibility(View.VISIBLE);
                            layout_data.setVisibility(View.GONE);
                            totalEmpAttendanceAdapter = new TotalEmpAttendanceAdapter(getApplicationContext(), listEmpTotal);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                            recyclerViewTotal.setLayoutManager(layoutManager);
                            recyclerViewTotal.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewTotal.setAdapter(totalEmpAttendanceAdapter);
                            totalEmpAttendanceAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ReadTotalData> call, Throwable t) {

            }
        });
    }

    private void userDateWiseApiCall() {


        SharedPreferencesHandler sc = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
        Call<UserDateWiseData> call = RetrofitClient.getInstance().getApiInterface()
                .getUserAttendanceSingleDate("Bearer " + sc.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        selctedCalenderStartDate
                );
        call.enqueue(new Callback<UserDateWiseData>() {
            @Override
            public void onResponse(Call<UserDateWiseData> call, Response<UserDateWiseData> response) {

                if (response.body() != null) {
                    if (response.body().getResponseType().equalsIgnoreCase("success")) {
                        int userSingleDateAbsent = 0;
                        int userSinglePresent = 0;
                        int userSingleLate = 0;
                        int userCount;
                        int durationWiseTotalData = 0;

                        // HashSet<String> unique_total_emp2 = new HashSet<>();

                        for (userCount = 0; userCount < response.body().getResponseData().size(); userCount++) {
                            //  list_duration.clear();
                            /* list_userDateP.add(response.body().getResponseData().get(userCount));*/
                            //durationWiseTotalData++;


                            //   unique_total_emp2.add(response.body().getResponseData().get(durationWiseCount).getEmpId());
                            if (response.body().getResponseData().get(userCount).getAStatus().equals("P")) {
                                //  list_duration_P.clear();
                                list_userDateP.add(response.body().getResponseData().get(userCount));
                                userSinglePresent++;
                            } else if (response.body().getResponseData().get(userCount).getAStatus().equalsIgnoreCase("A")) {
                                //   list_duration_A.clear();
                                list_userDateA.add(response.body().getResponseData().get(userCount));
                                userSingleDateAbsent++;
                            } else if (response.body().getResponseData().get(userCount).getAStatus().equalsIgnoreCase("LATE")) {
                                list_userDateL.add(response.body().getResponseData().get(userCount));
                                userSingleLate++;

                            }
                        }
                        //tvTotalEmp.setText(String.valueOf(unique_total_emp2.size()));
                        //tvTotalEmp.setText(String.valueOf(durationWiseTotalData));
                        tvTotalPresent.setText(String.valueOf(userSinglePresent));
                        tvTotalAbsent.setText(String.valueOf(userSingleDateAbsent));
                        lateTicker.setText(String.valueOf(userSingleLate));

                        recyclerView.setVisibility(View.GONE);
                        layout_data.setVisibility(View.VISIBLE);


                        tvPi_chart.addPieSlice(new PieModel("Present", userSinglePresent, getResources().getColor(R.color.green_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Absent", userSingleDateAbsent, getResources().getColor(R.color.red_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Total", userSingleLate, getResources().getColor(R.color.yellow)));
                        //tvPi_chart.addPieSlice(new PieModel("Absent", userSingleLate, getResources().getColor(R.color.late)));
                        tvPi_chart.startAnimation();


                        tvTotalPresent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userDurationAttendanceAdapter = new UserDurationAttendanceAdapter(getApplicationContext(), list_userDateP);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userDurationAttendanceAdapter);
                                userDurationAttendanceAdapter.notifyDataSetChanged();
                            }
                        });
                        tvTotalAbsent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userDurationAttendanceAdapter = new UserDurationAttendanceAdapter(getApplicationContext(), list_userDateA);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userDurationAttendanceAdapter);
                                userDurationAttendanceAdapter.notifyDataSetChanged();
                            }
                        });
                        lateTicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userDurationAttendanceAdapter = new UserDurationAttendanceAdapter(getApplicationContext(), list_userDateL);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userDurationAttendanceAdapter);
                                userDurationAttendanceAdapter.notifyDataSetChanged();
                            }
                        });

                    } else if (response.body().getResponseType().equalsIgnoreCase("warning")) {
                        Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<UserDateWiseData> call, Throwable t) {

            }
        });

    }

    private void userAttendanceApiDuration() {
        SharedPreferencesHandler sc = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
        Call<UserDateWiseData> call = RetrofitClient.getInstance().getApiInterface()
                .getUserAttendanceDuration("Bearer " + sc.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        selctedCalenderStartDate,
                        selectedCalenderEndDate
                );
        call.enqueue(new Callback<UserDateWiseData>() {
            @Override
            public void onResponse(Call<UserDateWiseData> call, Response<UserDateWiseData> response) {


                if (response.body() != null) {


                    if (response.body().getResponseType().equalsIgnoreCase("success")) {
                        int userSingleDateAbsent = 0;
                        int userSinglePresent = 0;
                        int userSingleLate = 0;
                        int userCount;
                        int durationWiseTotalData = 0;

                        // HashSet<String> unique_total_emp2 = new HashSet<>();

                        for (userCount = 0; userCount < response.body().getResponseData().size(); userCount++) {
                            //  list_duration.clear();
                            /* list_userDateP.add(response.body().getResponseData().get(userCount));*/
                            durationWiseTotalData++;


                            //   unique_total_emp2.add(response.body().getResponseData().get(durationWiseCount).getEmpId());

                            if (response.body().getResponseData().get(userCount).getAStatus().equalsIgnoreCase("P")) {
                                //  list_duration_P.clear();
                                list_userDateP.add(response.body().getResponseData().get(userCount));
                                userSinglePresent++;


                            } else if (response.body().getResponseData().get(userCount).getAStatus().equalsIgnoreCase("A")) {
                                //   list_duration_A.clear();
                                list_userDateA.add(response.body().getResponseData().get(userCount));
                                userSingleDateAbsent++;
                            } else if (response.body().getResponseData().get(userCount).getAStatus().equalsIgnoreCase("LATE")) {
                                list_userDateL.add(response.body().getResponseData().get(userCount));
                                userSingleLate++;

                            }
                        }


                        //tvTotalEmp.setText(String.valueOf(unique_total_emp2.size()));
                        //tvTotalEmp.setText(String.valueOf(durationWiseTotalData));
                        tvTotalPresent.setText(String.valueOf(userSinglePresent));
                        tvTotalAbsent.setText(String.valueOf(userSingleDateAbsent));
                        lateTicker.setText(String.valueOf(userSingleLate));

                        recyclerView.setVisibility(View.GONE);
                        layout_data.setVisibility(View.VISIBLE);

                        tvPi_chart.addPieSlice(new PieModel("Total", userSingleLate, getResources().getColor(R.color.yellow)));
                        tvPi_chart.addPieSlice(new PieModel("Present", userSinglePresent, getResources().getColor(R.color.green_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Absent", userSingleDateAbsent, getResources().getColor(R.color.red_pie)));
                        tvPi_chart.startAnimation();

                        tvTotalPresent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userDurationAttendanceAdapter = new UserDurationAttendanceAdapter(getApplicationContext(), list_userDateP);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userDurationAttendanceAdapter);
                                userDurationAttendanceAdapter.notifyDataSetChanged();
                            }
                        });
                        tvTotalAbsent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userDurationAttendanceAdapter = new UserDurationAttendanceAdapter(getApplicationContext(), list_userDateA);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userDurationAttendanceAdapter);
                                userDurationAttendanceAdapter.notifyDataSetChanged();
                            }
                        });
                        lateTicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                userDurationAttendanceAdapter = new UserDurationAttendanceAdapter(getApplicationContext(), list_userDateL);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(userDurationAttendanceAdapter);
                                userDurationAttendanceAdapter.notifyDataSetChanged();
                            }
                        });

                    } else if (response.body().getResponseType().equalsIgnoreCase("warning")) {
                        Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<UserDateWiseData> call, Throwable t) {

            }
        });
    }

    private void DateWiseDateApiCall() {
        SharedPreferencesHandler s9 = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);

        // without use of MVVm or Normal Api Call
        Call<DurationRecord> call = RetrofitClient.getInstance().getApiInterface()
                .getDateWiseData("Bearer " + s9.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        selctedCalenderStartDate,
                        "single",
                        "GMDA",
                        currentSpinnerDivisionValue);

        //App.getpreff().ReadPreferences(Constdata.token)

        call.enqueue(new Callback<DurationRecord>() {
            @Override
            public void onResponse(Call<DurationRecord> call, Response<DurationRecord> response) {
                if (response.isSuccessful()) {
                    // startEndAttendance(response.body().getResponseData());
//
                    if (response.body() != null) {
                        if (response.body().getResponseType().equalsIgnoreCase("success")) {
                            int durationWiseAbsent = 0;
                            int durationWiseL = 0;
                            int durationWisePresent = 0;
                            int durationWiseCount;
                            // int durationWiseTotalData = 0;

                            // HashSet<String> unique_total_emp2 = new HashSet<>();


                            for (durationWiseCount = 0; durationWiseCount < response.body().getResponseData().size(); durationWiseCount++) {
                                //  list_duration.clear();
                                // list_duration.add(response.body().getResponseData().get(durationWiseCount));
                                // durationWiseTotalData++;
                               /* if (list_duration.contains(response.body().getResponseData().get(durationWiseTotalData))) {

                                } else  {
                                    list_duration.add(response.body().getResponseData().get(durationWiseCount));
                                    durationWiseTotalData++;
                                }*/
                                //   unique_total_emp2.add(response.body().getResponseData().get(durationWiseCount).getEmpId());

                                if (response.body().getResponseData().get(durationWiseCount).getAStatus().equalsIgnoreCase("P")) {
                                    //  list_duration_P.clear();
                                    durationWisePresent++;
                                    list_duration_P.add(response.body().getResponseData().get(durationWiseCount));

                                } else if (response.body().getResponseData().get(durationWiseCount).getAStatus().equalsIgnoreCase("A")) {
                                    //   list_duration_A.clear();
                                    list_duration_A.add(response.body().getResponseData().get(durationWiseCount));
                                    durationWiseAbsent++;
                                }

                                if (response.body().getResponseData().get(durationWiseCount).getAStatus().equalsIgnoreCase("LATE")) {
                                    //   list_duration_A.clear();
                                    durationWiseL++;
                                    list_duration_L.add(response.body().getResponseData().get(durationWiseCount));

                                }
                            }
                            tvemptotal.setVisibility(View.VISIBLE);
                            tvTotalEmp.setVisibility(View.VISIBLE);
                            //tvTotalEmp.setText(String.valueOf(unique_total_emp2.size()));
                            //  tvTotalEmp.setText(String.valueOf(durationWiseTotalData));
                            tvTotalPresent.setText(String.valueOf(durationWisePresent));
                            tvTotalAbsent.setText(String.valueOf(durationWiseAbsent));
                            lateTicker.setText(String.valueOf(durationWiseL));

                            recyclerView.setVisibility(View.GONE);
                            layout_data.setVisibility(View.VISIBLE);

                            tvPi_chart.addPieSlice(new PieModel("Total", totalEmp, getResources().getColor(R.color.blue_pie)));
                            tvPi_chart.addPieSlice(new PieModel("Present", durationWisePresent, getResources().getColor(R.color.green_pie)));
                            tvPi_chart.addPieSlice(new PieModel("Absent", durationWiseAbsent, getResources().getColor(R.color.red_pie)));
                            tvPi_chart.addPieSlice(new PieModel("Absent", durationWiseL, getResources().getColor(R.color.yellow)));
                            tvPi_chart.startAnimation();
                          /*  tvTotalEmp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_Report_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });*/

                            tvTotalPresent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerViewTotal.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration_P);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });

                            tvTotalAbsent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerViewTotal.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration_A);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });
                            lateTicker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerViewTotal.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration_L);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });

                        } else if (response.body().getResponseType().equalsIgnoreCase("warning")) {
                            Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_SHORT).show();

                        }
                 /*       mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);*/

                    } else if (response.body() == null) {
                        Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Attendance_ReportAdmin_Activity.this, LoginActivity.class));
                        finish();
                        Log.d(TAG, "onResponse: " + response.message());

                    }
                }
            }

            @Override
            public void onFailure(Call<DurationRecord> call, Throwable t) {
                Utils.dismissProgress();
//                    Toast.makeText(getApplicationContext(), "Image upload Failed", Toast.LENGTH_LONG).show();
                Log.i("ResponseBody", t.getMessage() + "");
            }
        });

    }

    private void todayAttendanceApiCall() {
        SharedPreferencesHandler sh = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);
        Call<DivTodayAttendence> callE = RetrofitClient.getInstance().getApiInterface()
                .getTodayAttendanceRecord("Bearer " + sh.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        "GMDA",
                        currentSpinnerDivisionValue);

        callE.enqueue(new Callback<DivTodayAttendence>() {
            @Override
            public void onResponse(Call<DivTodayAttendence> call, Response<DivTodayAttendence> response) {
                if (response.body() != null) {
                    if (response.body().getResponseType().equalsIgnoreCase("success")) {
                        int todayAbsent = 0;
                        int todayPresent = 0;
                        int todayLate = 0;
                        int todayCount;
                        //int totalData = 0;

                        //  HashSet<String> unique_total_emp1 = new HashSet<>();
                        for (todayCount = 0; todayCount < response.body().getResponseData().size(); todayCount++) {

                            /*totalData++;
                            //list_today.clear();
                            list_today.add(response.body().getResponseData().get(todayCount));*/
                           /* if (list_today.contains(response.body().getResponseData().get(todayCount))) {

                            } else  {
                                list_today.add(response.body().getResponseData().get(todayCount));
                                totalData++;
                            }*/


                            //unique_total_emp1.add(response.body().getResponseData().get(todayCount).getEmpId());
                            //  list_gdt.add(responseData.get(iteratorDuration));
                            if (response.body().getResponseData().get(todayCount).getAStatus().equalsIgnoreCase("P")) {

                                todayPresent++;
                                // list_today_P.clear();
                                list_today_P.add(response.body().getResponseData().get(todayCount));
                                //list_today.clear();

                            } else if (response.body().getResponseData().get(todayCount).getAStatus().equalsIgnoreCase("A")) {

                                todayAbsent++;
                                // list_today_A.clear();
                                list_today_A.add(response.body().getResponseData().get(todayCount));
                                //list_today.clear();

                            } else if (response.body().getResponseData().get(todayCount).getAStatus().equalsIgnoreCase("LATE")) {

                                todayLate++;
                                // list_today_A.clear();
                                list_today_L.add(response.body().getResponseData().get(todayCount));
                                //list_today.clear();
                            }

                        }
                        //totalEmployeeApiCAll();
                        tvemptotal.setVisibility(View.VISIBLE);
                        tvTotalEmp.setVisibility(View.VISIBLE);
                        lateTicker.setVisibility(View.VISIBLE);
                        late.setVisibility(View.VISIBLE);
                        //tvTotalEmp.setText(String.valueOf(unique_total_emp1.size()));
                        // tvTotalEmp.setText(String.valueOf(totalData));
                        tvTotalPresent.setText(String.valueOf(todayPresent));
                        tvTotalAbsent.setText(String.valueOf(todayAbsent));
                        lateTicker.setText(String.valueOf(todayLate));
                        recyclerView.setVisibility(View.GONE);
                        layout_data.setVisibility(View.VISIBLE);

                        tvPi_chart.addPieSlice(new PieModel("Total", totalEmp, getResources().getColor(R.color.blue_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Present", todayPresent, getResources().getColor(R.color.green_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Absent", todayAbsent, getResources().getColor(R.color.red_pie)));
                        tvPi_chart.addPieSlice(new PieModel("Absent", todayLate, getResources().getColor(R.color.yellow)));
                        tvPi_chart.startAnimation();
                     /* tvTotalEmp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                               todayAttendanceAdapter = new TodayAttendanceAdapter(getApplicationContext(), list_today);
                               RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_Report_Activity.this);
                               recyclerView.setLayoutManager(layoutManager);
                               recyclerView.setItemAnimator(new DefaultItemAnimator());
                               recyclerView.setAdapter(todayAttendanceAdapter);
                               todayAttendanceAdapter.notifyDataSetChanged();
                               //todayAttendanceAdapter.notifyItemChanged(currentSpinnerSelectionDivision);
                           }
                       }
                        );*/
                        tvTotalPresent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerViewTotal.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                todayAttendanceAdapter = new TodayAttendanceAdapter(getApplicationContext(), list_today_P);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(todayAttendanceAdapter);
                                todayAttendanceAdapter.notifyDataSetChanged();
                                //todayAttendanceAdapter.notifyItemChanged(currentSpinnerSelectionDivision);
                            }
                        });
                        tvTotalAbsent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerViewTotal.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                layout_data.setVisibility(View.GONE);
                                todayAttendanceAdapter = new TodayAttendanceAdapter(getApplicationContext(), list_today_A);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(todayAttendanceAdapter);
                                todayAttendanceAdapter.notifyDataSetChanged();
                                //todayAttendanceAdapter.notifyItemChanged(currentSpinnerSelectionDivision);
                            }
                        });
                        lateTicker.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              recyclerViewTotal.setVisibility(View.GONE);
                                                              recyclerView.setVisibility(View.VISIBLE);
                                                              layout_data.setVisibility(View.GONE);
                                                              todayAttendanceAdapter = new TodayAttendanceAdapter(getApplicationContext(), list_today_L);
                                                              RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                                              recyclerView.setLayoutManager(layoutManager);
                                                              recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                              recyclerView.setAdapter(todayAttendanceAdapter);
                                                              todayAttendanceAdapter.notifyDataSetChanged();
                                                              //todayAttendanceAdapter.notifyItemChanged(currentSpinnerSelectionDivision);
                                                          }
                                                      }
                        );

                    } else {
                        Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_SHORT).show();

                    }
                   /* mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);*/
                    // todayAttendanceset(response.body().getResponseData());

                } else if (response.body() == null) {

                    startActivity(new Intent(Attendance_ReportAdmin_Activity.this, LoginActivity.class));
                    Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_LONG).show();
                    finish();
                }

                // Log.d("My Sucess","saa"+response.isSuccessful());
                //

               /* } else if (response.body() == null) {
                    Toast.makeText(Attendance_Report_Activity.this, "Token is expired....Please login again...!! ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Attendance_Report_Activity.this, LoginActivity.class));
                    finish();

                }*/


            }

            @Override
            public void onFailure(Call<DivTodayAttendence> call, Throwable t) {
                Utils.dismissProgress();
//                    Toast.makeText(getApplicationContext(), "Image upload Failed", Toast.LENGTH_LONG).show();
                Log.i("ResponseBody", t.getMessage());
            }
        });
    }

    private void startDateEndDateApiCall() {
        SharedPreferencesHandler s9 = new SharedPreferencesHandler(Attendance_ReportAdmin_Activity.this);

        // without use of MVVm or Normal Api Call
        Call<DurationRecord> call = RetrofitClient.getInstance().getApiInterface()
                .getDurationAttendanceRecord("Bearer " + s9.DirectReadPreference(Constdata.token, Attendance_ReportAdmin_Activity.this),
                        selctedCalenderStartDate,
                        selectedCalenderEndDate,
                        "GMDA",
                        currentSpinnerDivisionValue);

        //App.getpreff().ReadPreferences(Constdata.token)

        call.enqueue(new Callback<DurationRecord>() {
            @Override
            public void onResponse(Call<DurationRecord> call, Response<DurationRecord> response) {
                if (response.isSuccessful()) {
                    // startEndAttendance(response.body().getResponseData());
//


                    if (response.body() != null) {
                        if (response.body().getResponseType().equalsIgnoreCase("success")) {
                            int durationWiseAbsent = 0;
                            int durationWiseLate = 0;
                            int durationWisePresent = 0;
                            int durationWiseCount;
                            // int durationWiseTotalData = 0;


                            // HashSet<String> unique_total_emp2 = new HashSet<>();


                            for (durationWiseCount = 0; durationWiseCount < response.body().getResponseData().size(); durationWiseCount++) {

                               /* if (response.body().getResponseData().get(durationWiseCount).getEmpId()!=null){
                                    durationWiseTotalData++;
                                    unique_total_emp2.add(response.body().getResponseData().get(durationWiseCount).getEmpId());
                                }*/


                                //  list_duration.clear();

                              /*  if (list_duration.contains(response.body().getResponseData().get(durationWiseCount))) {

                                } else  if (!list_duration.contains(response.body().getResponseData().get(durationWiseCount))){

                                    list_duration.add(response.body().getResponseData().get(durationWiseCount));
                                    durationWiseTotalData++;

                                }*/


                                //list_duration.add(response.body().getResponseData().get(durationWiseCount));


                                if (response.body().getResponseData().get(durationWiseCount).getAStatus().equalsIgnoreCase("P")) {
                                    //  list_duration_P.clear();
                                    durationWisePresent++;
                                    list_duration_P.add(response.body().getResponseData().get(durationWiseCount));

                                } else if (response.body().getResponseData().get(durationWiseCount).getAStatus().equalsIgnoreCase("A")) {
                                    //   list_duration_A.clear();
                                    list_duration_A.add(response.body().getResponseData().get(durationWiseCount));
                                    durationWiseAbsent++;
                                } else if (response.body().getResponseData().get(durationWiseCount).getAStatus().equalsIgnoreCase("LATE")) {
                                    //   list_duration_A.clear();
                                    list_duration_L.add(response.body().getResponseData().get(durationWiseCount));
                                    durationWiseLate++;
                                }
                            }
                            tvemptotal.setVisibility(View.VISIBLE);
                            tvTotalEmp.setVisibility(View.VISIBLE);
                            // tvTotalEmp.setText(String.valueOf(unique_total_emp2.size()));
                            //tvTotalEmp.setText(String.valueOf(durationWiseTotalData));
                            tvTotalPresent.setText(String.valueOf(durationWisePresent));
                            tvTotalAbsent.setText(String.valueOf(durationWiseAbsent));
                            lateTicker.setText(String.valueOf(durationWiseLate));

                            recyclerView.setVisibility(View.GONE);
                            layout_data.setVisibility(View.VISIBLE);

                            tvPi_chart.addPieSlice(new PieModel("Total", totalEmp, getResources().getColor(R.color.blue_pie)));
                            tvPi_chart.addPieSlice(new PieModel("Present", durationWisePresent, getResources().getColor(R.color.green_pie)));
                            tvPi_chart.addPieSlice(new PieModel("Absent", durationWiseAbsent, getResources().getColor(R.color.red_pie)));
                            tvPi_chart.addPieSlice(new PieModel("Absent", durationWiseLate, getResources().getColor(R.color.yellow)));
                            tvPi_chart.startAnimation();


                            /*tvTotalPresent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration_P);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_Report_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });*/
                            tvTotalPresent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerViewTotal.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration_P);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });
                            tvTotalAbsent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerViewTotal.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration_A);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });
                            lateTicker.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    recyclerViewTotal.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layout_data.setVisibility(View.GONE);
                                    durationAttendanceAdapter = new DurationAttendanceAdapter(getApplicationContext(), list_duration_L);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Attendance_ReportAdmin_Activity.this);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(durationAttendanceAdapter);
                                    durationAttendanceAdapter.notifyDataSetChanged();
                                }
                            });

                        } else {
                            Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_SHORT).show();
                            // mShimmerViewContainer.stopShimmerAnimation();
                            // mShimmerViewContainer.setVisibility(View.GONE);
                        }

                    } else if (response.body() == null) {
                        Toast.makeText(Attendance_ReportAdmin_Activity.this, response.body().getResponseMsg().getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Attendance_ReportAdmin_Activity.this, LoginActivity.class));
                        finish();
                        Log.d(TAG, "onResponse: " + response.message());

                    }
                }
            }

            @Override
            public void onFailure(Call<DurationRecord> call, Throwable t) {
                Utils.dismissProgress();
//                    Toast.makeText(getApplicationContext(), "Image upload Failed", Toast.LENGTH_LONG).show();
                Log.i("ResponseBody", t.getMessage() + "");
            }
        });

    }

    private void updateDateWise() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdfs = new SimpleDateFormat(myFormat, Locale.US);
        DateWise = sdfs.format(myCalendar2.getTime());
        singleDate = DateWise;
        tv_date.setText(DateWise);
        Log.d("Data", "fetch" + DateWise);
    }

    private void updateRecord() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate = sdf.format(myCalendar.getTime());
        selctedCalenderStartDate = startDate;
        Log.d("startDate", "Date is : " + selctedCalenderStartDate);
        etStart.setText(startDate);
        Log.d("Data", "fetch" + startDate);

    }

    private void updateMonth() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        monthSelect = sdf.format(calenderMonth.getTime());
        selctedCalenderMonth = monthSelect;
        //Log.d("startDate", "Date is : " + selctedCalenderStartDate);
        etMonth.setText(monthSelect);
        //  Log.d("Data", "fetch" + startDate);

    }

    private void updateYear() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        yearSelect = sdf.format(calenderMonth.getTime());
        selctedCalenderYear = yearSelect;
        //Log.d("startDate", "Date is : " + selctedCalenderStartDate);
        etYear.setText(yearSelect);
        //  Log.d("Data", "fetch" + startDate);

    }

    private void updateRecord1() {
        String myFormat1 = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        //SharedPreferencesHandler s2 = new SharedPreferencesHandler(Attendance_Report_Activity.this);
        endDate = sdf1.format(myCalendar1.getTime());
        selectedCalenderEndDate = endDate;
        Log.d("startDate", "Date is : " + selectedCalenderEndDate);
        etEnd.setText(endDate);
    }

    public void validation() {
        // It check a password is empty or not .
        if (sp_Department.getSelectedItemPosition() == 0) {
            showSnackbarAlert(ll, getString(R.string.err_msg_dept));

            //requestFocus(password);
            sp_Department.requestFocus();
            return;
        } else if (sp_division.getSelectedItemPosition() == 0) {
            showSnackbarAlert(ll, getString(R.string.err_msg_div));
            //requestFocus(password);

            sp_division.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(etStart.getText().toString())) {
            showSnackbarAlert(ll, getString(R.string.err_msg_sdate));
            etStart.setError(getString(R.string.err_msg_phone));
            etStart.requestFocus();
            return;
        } else if (TextUtils.isEmpty(etEnd.getText().toString())) {
            showSnackbarAlert(ll, getString(R.string.err_msg_edate));
            etEnd.setError(getString(R.string.err_msg_phone));
            etEnd.requestFocus();
            return;
        }
    }

    public  void showSnackbarAlert(View view, String message) {
      snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

  /*  @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:

                logOutUser();
                break;
        }
        return false;

    }
    private void logOutUser() {
        final SharedPreferencesHandler s = new SharedPreferencesHandler(Attendance_Report_Activity.this);

        Call<LogOut> call = RetrofitClient.getInstance().getApiInterface()
                .userLogout("Bearer " + s.DirectReadPreference(Constdata.token, Attendance_Report_Activity.this));

        call.enqueue(new Callback<LogOut>() {
            @Override
            public void onResponse(Call<LogOut> call, Response<LogOut> response) {

                if (response.body()!=null) {

                    SharedPreferencesHandler s1 = new SharedPreferencesHandler(Attendance_Report_Activity.this);
                    s1.DirectClearPreferences(Attendance_Report_Activity.this);
                    startActivity(new Intent(Attendance_Report_Activity.this, LoginActivity.class));
                    finish();
                    Toast.makeText(Attendance_Report_Activity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                }
                else if (response.body()==null){
                    Toast.makeText(Attendance_Report_Activity.this, "Token is expired....Please login again...!! ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Attendance_Report_Activity.this,LoginActivity.class));
                    finish();
                }

                //    userLogout(response.body().getResponseData());
                //response.body().
            }

            @Override
            public void onFailure(Call<LogOut> call, Throwable t) {

                t.getMessage();
            }
        });
    }
*/


}








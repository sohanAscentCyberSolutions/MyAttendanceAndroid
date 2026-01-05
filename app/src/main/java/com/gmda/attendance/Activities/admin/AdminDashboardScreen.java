package com.gmda.attendance.Activities.admin;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.Adapter.newAdapter.AdminAttendanceReportAdapter;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.newmodels.AttendanceReport;
import com.google.android.material.navigation.NavigationView;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboardScreen extends AppCompatActivity {
    private NavigationView navigation_view_admin;
    private Toolbar admin_toolbar_layout;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout admin_drawer_layout;
    private LinearLayout admin_self_mode_layout, admin_mode_linear_layout;
    private SwitchCompat admin_switch_btn;
    private TextView mode_text_view;
    private CardView team_report_card_view_click;
    private RecyclerView admin_recycler_view_dashboard;
    private ArrayList<AttendanceReport> attendanceReportList;
    private AdminAttendanceReportAdapter attendanceReportAdapter;
    private TickerView team_present_tc, team_absent_ticker, team_late_ticker, total_staff;
    private TextView see_all_tv;
    private TickerView late_ticker_admin, absentTicker_admin, presentTicker_admin;

    private CircleImageView imageView;
    private TextView profile_name, profile_designation, profile_location;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_screen);
        initViews();
    }

    private void initViews() {
        attendanceReportList = new ArrayList<>();
        attendanceReportAdapter = new AdminAttendanceReportAdapter(AdminDashboardScreen.this, attendanceReportList, "small");
        navigation_view_admin = findViewById(R.id.navigation_view_admin);
        admin_toolbar_layout = findViewById(R.id.admin_toolbar_layout);
        admin_drawer_layout = findViewById(R.id.admin_drawer_layout);
        admin_self_mode_layout = findViewById(R.id.admin_self_mode_layout);
        admin_switch_btn = findViewById(R.id.admin_switch_btn);
        admin_self_mode_layout = findViewById(R.id.admin_self_mode_layout);
        admin_mode_linear_layout = findViewById(R.id.admin_mode_linear_layout);
        mode_text_view = findViewById(R.id.mode_text_view);
        team_report_card_view_click = findViewById(R.id.team_report_card_view_click);
        team_present_tc = findViewById(R.id.team_present_tc);
        team_absent_ticker = findViewById(R.id.team_absent_ticker);
        team_late_ticker = findViewById(R.id.team_late_ticker);
        total_staff = findViewById(R.id.total_staff);
        see_all_tv = findViewById(R.id.see_all_tv);
        admin_recycler_view_dashboard = findViewById(R.id.admin_recycler_view_dashboard);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AdminDashboardScreen.this, RecyclerView.HORIZONTAL, false);
        admin_recycler_view_dashboard.setLayoutManager(mLayoutManager);
        admin_recycler_view_dashboard.setItemAnimator(new DefaultItemAnimator());
        admin_recycler_view_dashboard.setAdapter(attendanceReportAdapter);

        team_report_card_view_click.setOnClickListener(view -> {
            startActivity(new Intent(AdminDashboardScreen.this, AdminReportScreen.class));
        });

        see_all_tv.setOnClickListener(view -> {
            startActivity(new Intent(AdminDashboardScreen.this, AdminReportScreen.class));
        });


        toggle = new ActionBarDrawerToggle(
                this, admin_drawer_layout, admin_toolbar_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        admin_toolbar_layout.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        admin_toolbar_layout.getNavigationIcon();
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toggle.setDrawerIndicatorEnabled(true);
        admin_drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        admin_switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("TAG", "onCheckedChanged: " + b);
                if (b) {
                    getTodayAttendanceDivisionWise();
                    mode_text_view.setText("Admin Mode");
                    admin_self_mode_layout.setVisibility(View.GONE);
                    admin_mode_linear_layout.setVisibility(View.VISIBLE);
                } else {
                    mode_text_view.setText("User Mode");
                    admin_self_mode_layout.setVisibility(View.VISIBLE);
                    admin_mode_linear_layout.setVisibility(View.GONE);
                }

            }
        });


    }

    private void getTodayAttendanceDivisionWise() {
        attendanceReportList.clear();
        SharedPreferencesHandler sc = new SharedPreferencesHandler(AdminDashboardScreen.this);
        Call<ResponseBody> calls = RetrofitClient.getInstance().getApiInterface()
                .get_today_attendance_of_division_wise("Bearer " + sc.DirectReadPreference(Constdata.token, AdminDashboardScreen.this),
                        sc.DirectReadPreference(Constdata.department, AdminDashboardScreen.this),
                        sc.DirectReadPreference(Constdata.division, AdminDashboardScreen.this));

        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");

                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Data fetch successfully...!")) {
                                JSONArray result_data = jsonObject.getJSONArray("responseData");
                                int present_counter = 0;
                                int absent_counter = 0;
                                int late_counter = 0;
                                int total_staff_count = 0;
                                if (result_data.length() > 0) {

                                    for (int i = 0; i < result_data.length(); i++) {
                                        JSONObject jsonObject1 = result_data.getJSONObject(i);
                                        attendanceReportList.add(new AttendanceReport(
                                                jsonObject1.getString("emp_name"),
                                                jsonObject1.getString("emp_id"),
                                                jsonObject1.getString("department"),
                                                jsonObject1.getString("division"),
                                                jsonObject1.getString("designation"),
                                                jsonObject1.getString("attendance_date"),
                                                jsonObject1.getString("punch_in"),
                                                jsonObject1.getString("punchin_lat"),
                                                jsonObject1.getString("punchin_long"),
                                                jsonObject1.getString("punch_out"),
                                                jsonObject1.getString("punchout_lat"),
                                                jsonObject1.getString("punchout_long"),
                                                jsonObject1.getString("working_hr"),
                                                jsonObject1.getString("a_status"),
                                                jsonObject1.getString("remarks"),
                                                jsonObject1.getString("punchin_address"),
                                                jsonObject1.getString("punchout_address"),
                                                jsonObject1.getString("punchin_image"),
                                                jsonObject1.getString("punchout_image")

                                        ));
                                        total_staff_count++;
                                        if (jsonObject1.getString("a_status").equals("A")) {
                                            absent_counter++;
                                        } else if (jsonObject1.getString("a_status").equals("LATE")) {
                                            late_counter++;
                                        } else if (jsonObject1.getString("a_status").equals("P")) {
                                            present_counter++;
                                        }
                                    }
                                    attendanceReportAdapter.notifyDataSetChanged();
                                    update_user_today_dashboard(present_counter, absent_counter, late_counter,total_staff_count);

                                }


                            } else {

                                showMessage(api_message);
                            }

                        } else {
                            showMessage(api_message);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {

                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());

                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());

                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else {
                    onFailed("An unexpected error has occured.",
                            "Error Failure: " + t.getMessage());


                }
            }
        });
    }

    private void onFailed(String s, String s1) {
        Toast.makeText(this, "" + s1, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String api_message) {
        Toast.makeText(this, "" + api_message, Toast.LENGTH_SHORT).show();

    }

    private void update_user_today_dashboard(int present, int absent, int late,int total_staff_count) {
        Log.d(TAG, "update_user_monthly_dashboard: " + present);
        Log.d(TAG, "update_user_monthly_dashboard: " + absent);
        Log.d(TAG, "update_user_monthly_dashboard: " + late);
        team_present_tc.setText(String.valueOf(present));
        team_absent_ticker.setText(String.valueOf(absent));
        team_late_ticker.setText(String.valueOf(late));
        total_staff.setText(String.valueOf(total_staff_count));

    }


}
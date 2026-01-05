package com.gmda.attendance.Activities.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.Adapter.newAdapter.AdminAttendanceReportAdapter;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.newmodels.AttendanceReport;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReportScreen extends AppCompatActivity {
    TickerView late_team_ticker, absent_team_ticker, present_team_ticker;
    Spinner any_month_att_btn;
    private TextView today_att_btn, any_date_att_btn, this_month_att_btn;
    private AutoCompleteTextView employee_id_et;
    private ImageView search_image_view;
    private ArrayList<AttendanceReport> attendanceReportList;
    private ProgressBar progress_bar_list_of_user;

    private RecyclerView admin_report_attendance_recyclerview;
    private AdminAttendanceReportAdapter adminAttendanceReportAdapter;
    private LinearLayout loading_data_layout_admin;

    private ArrayList<String> employee_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_screen);
        initViews();
    }

    private void initViews() {
        employee_id = new ArrayList<>();

        attendanceReportList = new ArrayList<>();
        adminAttendanceReportAdapter = new AdminAttendanceReportAdapter(AdminReportScreen.this, attendanceReportList, "full");
        progress_bar_list_of_user = findViewById(R.id.progress_bar_list_of_user);
        late_team_ticker = findViewById(R.id.late_team_ticker);
        absent_team_ticker = findViewById(R.id.absent_team_ticker);
        present_team_ticker = findViewById(R.id.present_team_ticker);
        today_att_btn = findViewById(R.id.today_att_btn);
        any_date_att_btn = findViewById(R.id.any_date_att_btn);
        this_month_att_btn = findViewById(R.id.this_month_att_btn);
        any_month_att_btn = findViewById(R.id.any_month_att_btn);
        employee_id_et = findViewById(R.id.employee_id_et);
        search_image_view = findViewById(R.id.search_image_view);
        loading_data_layout_admin = findViewById(R.id.loading_data_layout_admin);
        admin_report_attendance_recyclerview = findViewById(R.id.admin_report_attendance_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AdminReportScreen.this);
        admin_report_attendance_recyclerview.setLayoutManager(mLayoutManager);
        admin_report_attendance_recyclerview.setItemAnimator(new DefaultItemAnimator());
        admin_report_attendance_recyclerview.setAdapter(adminAttendanceReportAdapter);
        getTodayAttendanceDivisionWise();
        fetch_all_listed_user();
        search_image_view.setOnClickListener(view -> {

            if (employee_id_et.getText().toString().isEmpty()) {
                showMessage("Enter Employee Id");
            } else {
                if (employee_id_et.getText().toString().contains("-")) {
                    String[] splitResult = employee_id_et.getText().toString().split("-");
                    fetch_complete_record_of_employee(splitResult[1]);
                } else {
                    showMessage("Check Your Input Value");
                }

            }
        });


        today_att_btn.setOnClickListener(v -> {
            today_att_btn.setTextColor(getResources().getColor(R.color.white));
            today_att_btn.setBackgroundResource(R.drawable.btn_background);
            any_date_att_btn.setTextColor(getResources().getColor(R.color.purple_700));
            any_date_att_btn.setBackgroundResource(R.drawable.btn_light_background);
            this_month_att_btn.setBackgroundResource(R.drawable.btn_light_background);
            this_month_att_btn.setTextColor(getResources().getColor(R.color.purple_700));
            getTodayAttendanceDivisionWise();
        });

        any_date_att_btn.setOnClickListener(v -> {
            any_date_att_btn.setTextColor(getResources().getColor(R.color.white));
            any_date_att_btn.setBackgroundResource(R.drawable.btn_background);
            today_att_btn.setTextColor(getResources().getColor(R.color.purple_700));
            today_att_btn.setBackgroundResource(R.drawable.btn_light_background);
            this_month_att_btn.setBackgroundResource(R.drawable.btn_light_background);
            this_month_att_btn.setTextColor(getResources().getColor(R.color.purple_700));
        });

        this_month_att_btn.setOnClickListener(v -> {
            this_month_att_btn.setTextColor(getResources().getColor(R.color.white));
            this_month_att_btn.setBackgroundResource(R.drawable.btn_background);
            any_date_att_btn.setTextColor(getResources().getColor(R.color.purple_700));
            any_date_att_btn.setBackgroundResource(R.drawable.btn_light_background);
            today_att_btn.setBackgroundResource(R.drawable.btn_light_background);
            today_att_btn.setTextColor(getResources().getColor(R.color.purple_700));
        });


    }

    private void fetch_all_listed_user() {
        employee_id.clear();
        progress_bar_list_of_user.setVisibility(View.VISIBLE);
        SharedPreferencesHandler sc = new SharedPreferencesHandler(AdminReportScreen.this);
        Call<ResponseBody> calls = RetrofitClient.getInstance().getApiInterface()
                .fetch_all_user_by_api("Bearer " + sc.DirectReadPreference(Constdata.token, AdminReportScreen.this),
                        sc.DirectReadPreference(Constdata.department, AdminReportScreen.this),
                        sc.DirectReadPreference(Constdata.division, AdminReportScreen.this));
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        progress_bar_list_of_user.setVisibility(View.GONE);
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Data fetch successfully.")) {
                                JSONArray result_data = jsonObject.getJSONArray("responseData");

                                if (result_data.length() > 0) {

                                    for (int i = 0; i < result_data.length(); i++) {
                                        JSONObject jsonObject1 = result_data.getJSONObject(i);
                                        employee_id.add(jsonObject1.getString("emp_name") + " -" + jsonObject1.getString("emp_id"));
                                    }
                                    set_auto_complete_text_view();
                                }


                            } else {
                                progress_bar_list_of_user.setVisibility(View.GONE);
                                showMessage(api_message);
                            }

                        } else {
                            progress_bar_list_of_user.setVisibility(View.GONE);
                            showMessage(api_message);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progress_bar_list_of_user.setVisibility(View.GONE);
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    progress_bar_list_of_user.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    progress_bar_list_of_user.setVisibility(View.GONE);
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                progress_bar_list_of_user.setVisibility(View.GONE);
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

    private void set_auto_complete_text_view() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.text_design, R.id.tvCustom, employee_id);

        employee_id_et.setThreshold(1);
        employee_id_et.setAdapter(adapter);

    }

    private void fetch_complete_record_of_employee(String employee_id) {
        loading_data_layout_admin.setVisibility(View.VISIBLE);
        attendanceReportList.clear();
        SharedPreferencesHandler sc = new SharedPreferencesHandler(AdminReportScreen.this);
        Call<ResponseBody> calls = RetrofitClient.getInstance().getApiInterface()
                .get_all_attendance_by_id("Bearer " + sc.DirectReadPreference(Constdata.token, AdminReportScreen.this),
                        employee_id);
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");

                        loading_data_layout_admin.setVisibility(View.GONE);
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Data fetch successfully...!")) {
                                JSONArray result_data = jsonObject.getJSONArray("responseData");
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

                                    }
                                    admin_report_attendance_recyclerview.setVisibility(View.VISIBLE);
                                    adminAttendanceReportAdapter.notifyDataSetChanged();

                                }


                            } else {
                                loading_data_layout_admin.setVisibility(View.GONE);
                                showMessage(api_message);
                            }

                        } else {
                            showMessage(api_message);
                            loading_data_layout_admin.setVisibility(View.GONE);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        loading_data_layout_admin.setVisibility(View.GONE);
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    loading_data_layout_admin.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    loading_data_layout_admin.setVisibility(View.GONE);
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                loading_data_layout_admin.setVisibility(View.GONE);
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


    private void getTodayAttendanceDivisionWise() {
        loading_data_layout_admin.setVisibility(View.VISIBLE);
        attendanceReportList.clear();
        SharedPreferencesHandler sc = new SharedPreferencesHandler(AdminReportScreen.this);
        Call<ResponseBody> calls = RetrofitClient.getInstance().getApiInterface()
                .get_today_attendance_of_division_wise("Bearer " + sc.DirectReadPreference(Constdata.token, AdminReportScreen.this),
                        sc.DirectReadPreference(Constdata.department, AdminReportScreen.this),
                        sc.DirectReadPreference(Constdata.division, AdminReportScreen.this));
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");

                        loading_data_layout_admin.setVisibility(View.GONE);
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Data fetch successfully...!")) {
                                JSONArray result_data = jsonObject.getJSONArray("responseData");
                                int present_counter = 0;
                                int absent_counter = 0;
                                int late_counter = 0;
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
                                        if (jsonObject1.getString("a_status").equals("A")) {
                                            absent_counter++;
                                        } else if (jsonObject1.getString("a_status").equals("LATE")) {
                                            late_counter++;
                                        } else if (jsonObject1.getString("a_status").equals("P")) {
                                            present_counter++;
                                        }
                                    }
                                    admin_report_attendance_recyclerview.setVisibility(View.VISIBLE);
                                    adminAttendanceReportAdapter.notifyDataSetChanged();
                                    update_user_today_dashboard(present_counter, absent_counter, late_counter);

                                }


                            } else {
                                loading_data_layout_admin.setVisibility(View.GONE);
                                showMessage(api_message);
                            }

                        } else {
                            showMessage(api_message);
                            loading_data_layout_admin.setVisibility(View.GONE);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        loading_data_layout_admin.setVisibility(View.GONE);
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    loading_data_layout_admin.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    loading_data_layout_admin.setVisibility(View.GONE);
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                loading_data_layout_admin.setVisibility(View.GONE);
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

    private void update_user_today_dashboard(int present, int absent, int late) {
        present_team_ticker.setText(String.valueOf(present));
        absent_team_ticker.setText(String.valueOf(absent));
        late_team_ticker.setText(String.valueOf(late));

    }

}
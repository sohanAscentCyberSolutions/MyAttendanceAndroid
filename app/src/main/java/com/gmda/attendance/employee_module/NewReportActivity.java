package com.gmda.attendance.employee_module;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmda.attendance.Adapter.newAdapter.AttendanceReportAdapter;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.newmodels.AttendanceReport;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView all_attendance_details, this_month_attendance_details, today_attendance_details;
    Spinner any_month_spinner_attendance_details;
    RecyclerView attendance_details_recycler_view;
    String[] month_name = {"Any Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] month_code = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private TickerView punch_in_time, punch_out_time, working_hours, today_attendance_status_tc;
    private SharedPreferencesHandler sc;
    private AttendanceReportAdapter attendanceReportAdapter;
    private ArrayList<AttendanceReport> attendanceReportArrayList;
    private LinearLayout loading_data_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        initViews();
    }

    private void initViews() {
        sc = new SharedPreferencesHandler(NewReportActivity.this);
        loading_data_layout = findViewById(R.id.loading_data_layout);
        punch_in_time = findViewById(R.id.punch_in_time);
        punch_out_time = findViewById(R.id.punch_out_time);
        working_hours = findViewById(R.id.working_hours);
        all_attendance_details = findViewById(R.id.all_attendance_details);
        this_month_attendance_details = findViewById(R.id.this_month_attendance_details);
        today_attendance_details = findViewById(R.id.today_attendance_details);
        any_month_spinner_attendance_details = findViewById(R.id.any_month_spinner_attendance_details);
        attendance_details_recycler_view = findViewById(R.id.attendance_details_recycler_view);
        today_attendance_status_tc = findViewById(R.id.today_attendance_status_tc);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewReportActivity.this);
        attendance_details_recycler_view.setLayoutManager(mLayoutManager);
        attendance_details_recycler_view.setItemAnimator(new DefaultItemAnimator());
        attendanceReportArrayList = new ArrayList<>();

        attendanceReportAdapter = new AttendanceReportAdapter(NewReportActivity.this, attendanceReportArrayList);
        attendance_details_recycler_view.setAdapter(attendanceReportAdapter);

        ArrayAdapter aa = new ArrayAdapter(NewReportActivity.this, R.layout.spinner_text_view, month_name);
        aa.setDropDownViewResource(R.layout.spinner_text_view);
        any_month_spinner_attendance_details.setAdapter(aa);

        any_month_spinner_attendance_details.setOnItemSelectedListener(this);

        setUpFunction();
        get_all_attendance_of_user();


        all_attendance_details.setOnClickListener(v -> {
            all_attendance_details.setTextColor(getResources().getColor(R.color.white));
            all_attendance_details.setBackgroundResource(R.drawable.btn_background);
            this_month_attendance_details.setTextColor(getResources().getColor(R.color.purple_700));
            this_month_attendance_details.setBackgroundResource(R.drawable.btn_light_background);
            today_attendance_details.setBackgroundResource(R.drawable.btn_light_background);
            today_attendance_details.setTextColor(getResources().getColor(R.color.purple_700));
            get_all_attendance_of_user();

        });

        this_month_attendance_details.setOnClickListener(v -> {
            this_month_attendance_details.setTextColor(getResources().getColor(R.color.white));
            this_month_attendance_details.setBackgroundResource(R.drawable.btn_background);
            all_attendance_details.setTextColor(getResources().getColor(R.color.purple_700));
            all_attendance_details.setBackgroundResource(R.drawable.btn_light_background);
            today_attendance_details.setBackgroundResource(R.drawable.btn_light_background);
            today_attendance_details.setTextColor(getResources().getColor(R.color.purple_700));
            get_user_this_month_attendance();

        });


        today_attendance_details.setOnClickListener(v -> {
            today_attendance_details.setTextColor(getResources().getColor(R.color.white));
            today_attendance_details.setBackgroundResource(R.drawable.btn_background);
            this_month_attendance_details.setTextColor(getResources().getColor(R.color.purple_700));
            this_month_attendance_details.setBackgroundResource(R.drawable.btn_light_background);
            all_attendance_details.setBackgroundResource(R.drawable.btn_light_background);
            all_attendance_details.setTextColor(getResources().getColor(R.color.purple_700));
            get_user_today_attendance();

        });


    }

    private void get_user_today_attendance() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        String currentDate = formatter.format(date);
        loading_data_layout.setVisibility(View.VISIBLE);
        attendance_details_recycler_view.setVisibility(View.GONE);
        attendanceReportArrayList.clear();
        ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> calls = apiInterFace
                .read_employee_attendance_by_date("Bearer " + sc.DirectReadPreference(Constdata.token, NewReportActivity.this),
                        currentDate,
                        sc.DirectReadPreference(Constdata.emp_id, NewReportActivity.this)
                );
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        Log.d("TAG", "onResponse: " + jsonObject);
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Attendance details")) {
                                JSONArray result_data = jsonObject.getJSONArray("responseData");
                                loading_data_layout.setVisibility(View.GONE);
                                if (result_data.length() > 0) {
                                    for (int i = 0; i < result_data.length(); i++) {
                                        JSONObject jsonObject1 = result_data.getJSONObject(i);
                                        attendanceReportArrayList.add(new AttendanceReport(
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
                                    attendance_details_recycler_view.setVisibility(View.VISIBLE);
                                    attendanceReportAdapter.notifyDataSetChanged();

                                } else {
                                    showMessage("No Data is found");
                                    loading_data_layout.setVisibility(View.GONE);
                                }


                            } else {
                                loading_data_layout.setVisibility(View.GONE);
                                showMessage(api_message);
                            }

                        } else {
                            loading_data_layout.setVisibility(View.GONE);
                            showMessage(api_message);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        loading_data_layout.setVisibility(View.GONE);
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    loading_data_layout.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    loading_data_layout.setVisibility(View.GONE);
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                loading_data_layout.setVisibility(View.GONE);
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

    private void get_user_this_month_attendance() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        loading_data_layout.setVisibility(View.VISIBLE);
        attendance_details_recycler_view.setVisibility(View.GONE);
        attendanceReportArrayList.clear();

        ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> calls = apiInterFace
                .get_user_monthly_details_by_api("Bearer " + sc.DirectReadPreference(Constdata.token, NewReportActivity.this),
                        String.valueOf(month + 1),
                        String.valueOf(year),
                        sc.DirectReadPreference(Constdata.emp_id, NewReportActivity.this));
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
                                loading_data_layout.setVisibility(View.GONE);

                                if (result_data.length() > 0) {
                                    for (int i = 0; i < result_data.length(); i++) {
                                        JSONObject jsonObject1 = result_data.getJSONObject(i);
                                        attendanceReportArrayList.add(new AttendanceReport(
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
                                    attendance_details_recycler_view.setVisibility(View.VISIBLE);
                                    attendanceReportAdapter.notifyDataSetChanged();
                                } else {
                                    showMessage("No Data is found");
                                    loading_data_layout.setVisibility(View.GONE);
                                }
                            } else {
                                loading_data_layout.setVisibility(View.GONE);
                                showMessage(api_message);
                            }

                        } else {
                            loading_data_layout.setVisibility(View.GONE);
                            showMessage(api_message);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        loading_data_layout.setVisibility(View.GONE);
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    loading_data_layout.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    loading_data_layout.setVisibility(View.GONE);
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                loading_data_layout.setVisibility(View.GONE);
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

    private void get_all_attendance_of_user() {
        attendanceReportArrayList.clear();
        loading_data_layout.setVisibility(View.VISIBLE);
        ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);

        Call<ResponseBody> calls = apiInterFace
                .fetch_employee_all_attendance("Bearer " + sc.DirectReadPreference(Constdata.token, NewReportActivity.this),
                        sc.DirectReadPreference(Constdata.emp_id, NewReportActivity.this));
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        Log.d("TAG", "onResponse: " + jsonObject);
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Attendance list")) {
                                JSONArray result_data = jsonObject.getJSONArray("responseData");
                                loading_data_layout.setVisibility(View.GONE);
                                if (result_data.length() > 0) {
                                    for (int i = 0; i < result_data.length(); i++) {
                                        JSONObject jsonObject1 = result_data.getJSONObject(i);
                                        attendanceReportArrayList.add(new AttendanceReport(
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
                                    attendance_details_recycler_view.setVisibility(View.VISIBLE);
                                    attendanceReportAdapter.notifyDataSetChanged();

                                } else {
                                    loading_data_layout.setVisibility(View.GONE);
                                    showMessage("No Data is found");
                                }


                            } else {
                                loading_data_layout.setVisibility(View.GONE);
                                showMessage(api_message);
                            }

                        } else {
                            loading_data_layout.setVisibility(View.GONE);
                            showMessage(api_message);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        loading_data_layout.setVisibility(View.GONE);
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    loading_data_layout.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    loading_data_layout.setVisibility(View.GONE);
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                loading_data_layout.setVisibility(View.GONE);
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

    private void setUpFunction() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String currentDate = formatter.format(date);

        Log.d("Date", currentDate);
        ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterFace.read_employee_attendance_by_date(
                "Bearer " + sc.DirectReadPreference(Constdata.token, NewReportActivity.this),
                currentDate, sc.DirectReadPreference(Constdata.emp_id, NewReportActivity.this));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        Log.d("TAG", "onResponse: " + jsonObjectMessage);
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Attendance details")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("responseData");
                                JSONObject jObject = jsonArray.getJSONObject(0);

                                set_ticker_view(jObject.getString("punch_in"), jObject.getString("punch_out"),
                                        jObject.getString("working_hr")
                                        , jObject.getString("a_status")
                                );


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

    private void set_ticker_view(String punch_in, String punch_out, String working_hr, String status) {
        String s1;
        String s2;
        String status_showing = "--";
        if (Objects.equals(punch_in, "null")) {
            s1 = "---";
        } else {
            s1 = punch_in.substring(punch_in.indexOf("T") + 1, punch_in.indexOf("."));
        }

        if (Objects.equals(punch_out, "null")) {
            s2 = "---";
        } else {
            s2 = punch_out.substring(punch_out.indexOf("T") + 1, punch_out.indexOf("."));
        }


        if (status.equals("A")) {
            status_showing = "Absent";

        } else if (status.equals("P")) {
            status_showing = "Present";
        } else {
            status_showing = "Late";
        }


        punch_in_time.setText(s1);
        punch_out_time.setText(s2);
        working_hours.setText(working_hr);
        today_attendance_status_tc.setText(status_showing);


    }

    private void onFailed(String s, String s1) {
        Toast.makeText(this, "" + s1, Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String api_message) {
        Toast.makeText(this, "" + api_message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            //   Toast.makeText(getContext(), "Select Month Please", Toast.LENGTH_SHORT).show();
        } else {
            String selected_month_code = month_code[i];
            load_any_month_attendance(selected_month_code);
        }
    }

    private void load_any_month_attendance(String selected_month_code) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        loading_data_layout.setVisibility(View.VISIBLE);
        attendance_details_recycler_view.setVisibility(View.GONE);
        attendanceReportArrayList.clear();

        ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> calls = apiInterFace
                .get_user_monthly_details_by_api("Bearer " + sc.DirectReadPreference(Constdata.token, NewReportActivity.this),
                        String.valueOf(selected_month_code),
                        String.valueOf(year),
                        sc.DirectReadPreference(Constdata.emp_id, NewReportActivity.this));
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");
                        Log.d("TAG", "onResponse: " + jsonObject);
                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");

                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Data fetch successfully...!")) {
                                JSONArray result_data = jsonObject.getJSONArray("responseData");
                                loading_data_layout.setVisibility(View.GONE);

                                if (result_data.length() > 0) {
                                    for (int i = 0; i < result_data.length(); i++) {
                                        JSONObject jsonObject1 = result_data.getJSONObject(i);
                                        attendanceReportArrayList.add(new AttendanceReport(
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
                                    attendance_details_recycler_view.setVisibility(View.VISIBLE);
                                    attendanceReportAdapter.notifyDataSetChanged();

                                } else {
                                    showMessage("No Data is found");
                                    loading_data_layout.setVisibility(View.GONE);
                                }


                            } else {
                                loading_data_layout.setVisibility(View.GONE);
                                showMessage(api_message);
                            }

                        } else {
                            loading_data_layout.setVisibility(View.GONE);
                            showMessage(api_message);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        loading_data_layout.setVisibility(View.GONE);
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    loading_data_layout.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    loading_data_layout.setVisibility(View.GONE);
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                loading_data_layout.setVisibility(View.GONE);
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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
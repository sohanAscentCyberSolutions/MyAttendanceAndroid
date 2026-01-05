package com.gmda.attendance.common_module;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gmda.attendance.Activities.admin.AdminDashboardScreen;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.CommonClass.TypeWriter;
import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.employee_module.MainActivity;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";

    int version;
    SharedPreferencesHandler s;
    TypeWriter textview;
    private Dialog loading_dialog;
    private LocationManager locationManager;
    private boolean GpsStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textview = findViewById(R.id.powered_by_gmda);

        loading_dialog = new Dialog(SplashScreenActivity.this);
        loading_dialog.setContentView(R.layout.loading_dailog);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        textview.setCharacterDelay(100);
        textview.animateText("Powered by GMDA");
        TextView textView = loading_dialog.findViewById(R.id.loading_text);
        textView.setText(R.string.checking_you);
        s = new SharedPreferencesHandler(SplashScreenActivity.this);

    }

    public void checkGpsStatus() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            if (Utils.is_network_connected(SplashScreenActivity.this)) {
                check_app_version();
            } else {
                Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SplashScreenActivity.this, "Please on the gps", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }


    private void check_app_version() {
        loading_dialog.show();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;

            System.out.println("VersionCode : " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ApiInterFace apiInterFace = RetrofitClient.getOneMapDepartmentApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterFace.check_app_version("BEARER VERSION.GMDA@2022", "gmda_attendance_app");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_response_msg = jsonObject.getString("responseType");
                        loading_dialog.dismiss();
                        JSONObject json_object_inner = jsonObject.getJSONObject("responsemsg");
                        String data_msg = json_object_inner.getString("Msg");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("responseData");
                        if (api_response_msg.equals("Success")) {
                            if (data_msg.equals("Data Fetched")) {
                                String app_host_url = jsonObject1.getString("app_host_url");
                                String under_maintenance = jsonObject1.getString("under_maintainence");
                                String under_maintenance_text = jsonObject1.getString("under_maintainence_text");
                                String version_description = jsonObject1.getString("version_description");
                                s.DirectWritePreference("image_quality", "10", SplashScreenActivity.this);
                                String s = jsonObject1.getString("priority_min_version").toString();
                                int priority_min_version = Double.valueOf(s).intValue();
                                String s1 = jsonObject1.getString("current_min_appversion").toString();
                                int current_min_appversion = Double.valueOf(s1).intValue();
                                match_version_and_show_dialog(under_maintenance, under_maintenance_text, version_description,
                                        app_host_url,
                                        priority_min_version, current_min_appversion);
                            } else {
                                Toast.makeText(SplashScreenActivity.this, data_msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SplashScreenActivity.this, data_msg, Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        loading_dialog.dismiss();
                        e.printStackTrace();
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    loading_dialog.dismiss();

                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    loading_dialog.dismiss();
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading_dialog.dismiss();
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
        loading_dialog.dismiss();
        Toast.makeText(SplashScreenActivity.this, s1, Toast.LENGTH_SHORT).show();
    }

    private void match_version_and_show_dialog(String under_maintenance, String under_maintenance_text, String version_description,
                                               String app_host_url, int priority_min_version, int current_min_appversion) {
        if (under_maintenance.equals("YES")) {
            OpenNewVersion("maintenance",
                    app_host_url,
                    version_description,
                    under_maintenance_text, priority_min_version, current_min_appversion);
        } else {
            if (version >= priority_min_version) {
                if (version < current_min_appversion) {
                    OpenNewVersion("optional_update",
                            app_host_url,
                            version_description,
                            under_maintenance_text, priority_min_version, current_min_appversion);
                } else {
                    next_intent();
                }
            } else {
                OpenNewVersion("priority_update",
                        app_host_url,
                        version_description,
                        under_maintenance_text, priority_min_version, current_min_appversion);
            }

        }

    }

    private void next_intent() {
        switch (s.DirectReadPreference(Constdata.emp_role, SplashScreenActivity.this)) {
            case "EMPLOYEE":
            case "Staff":
            case "Employee":
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                break;
            case "ADMIN":
                startActivity(new Intent(SplashScreenActivity.this, AdminDashboardScreen.class));
                break;
            default:
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                break;
        }
        finish();
    }


    private void OpenNewVersion(String action, String app_host_url,
                                String version_description, String under_maintainence_text,
                                int priority_min_version, int current_min_appversion) {


        Dialog dialog_update = new Dialog(SplashScreenActivity.this);
        dialog_update.setContentView(R.layout.dialog_update);
        dialog_update.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        GifImageView gif_maintainence = dialog_update.findViewById(R.id.gif_maintainence);
        LinearLayout ll_update = dialog_update.findViewById(R.id.ll_update);
        TextView tv_dismiss = dialog_update.findViewById(R.id.tv_dismiss);
        TextView tv_title = dialog_update.findViewById(R.id.tv_title);
        TextView tv_desc = dialog_update.findViewById(R.id.tv_desc);
        LinearLayout ll_Doupdate = dialog_update.findViewById(R.id.ll_Doupdate);

        switch (action) {
            case "maintenance":
                gif_maintainence.setImageResource(R.drawable.error);
                tv_dismiss.setVisibility(View.GONE);
                ll_update.setVisibility(View.GONE);
                tv_title.setText("System Under Maintenance");
                tv_desc.setText(under_maintainence_text);
                dialog_update.setCancelable(false);
                break;
            case "optional_update":
                gif_maintainence.setImageResource(R.drawable.hello);
                ll_update.setVisibility(View.VISIBLE);
                tv_dismiss.setVisibility(View.VISIBLE);
                tv_title.setText("New App Update Available" + "\n (Version- " + current_min_appversion + ")");
                tv_desc.setText(version_description);
                dialog_update.setCancelable(true);
                break;
            case "priority_update":
                gif_maintainence.setImageResource(R.drawable.hello);
                ll_update.setVisibility(View.VISIBLE);
                tv_dismiss.setVisibility(View.GONE);
                tv_title.setText("App Update Required!" + "\n (Version- " + current_min_appversion + ")");
                tv_desc.setText(version_description);
                dialog_update.setCancelable(false);
                break;
        }

        tv_dismiss.setOnClickListener(v -> {
            dialog_update.dismiss();
            dialog_update.hide();
            next_intent();
        });

        dialog_update.setOnCancelListener(dialog -> {
            dialog_update.dismiss();
            dialog_update.hide();
            if (action.equals("optional_update")) {
                next_intent();
            } else {
                finishAffinity();
            }
        });

        ll_Doupdate.setOnClickListener(v -> {

            if (app_host_url.length() > 3) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(app_host_url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                        .FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                dialog_update.dismiss();
                finish();
            } else {
                Toast.makeText(SplashScreenActivity.this, "Invalid Url", Toast.LENGTH_SHORT).show();
            }
        });
        dialog_update.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.is_network_connected(SplashScreenActivity.this)) {
            Handler handler = new Handler();
            handler.postDelayed(this::checkGpsStatus, 1300);
        } else {
            Toast.makeText(this, "Check Your Internet Connection First", Toast.LENGTH_SHORT).show();
        }
    }


}








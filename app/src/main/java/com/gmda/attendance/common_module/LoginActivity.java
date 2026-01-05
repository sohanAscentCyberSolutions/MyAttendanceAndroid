package com.gmda.attendance.common_module;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gmda.attendance.employee_module.MainActivity;
import com.gmda.attendance.Activities.admin.AdminDashboardScreen;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity  {
    public static final String MyPREFERENCES = "MyPrefs";
    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    SharedPreferences sharedpreferences;
    private EditText login_user_id, login_user_password;
    private TextView tvLocation, forget_password_tap_here_tv, login_btn_for_register;
    private ProgressDialog progressDialog;
    private Button login_btn_for_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        phoneReadMethod();
        tvLocation.setText(getString(R.string.empty));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage("Logging You Please Wait....");
        progressDialog.setCancelable(false);

        forget_password_tap_here_tv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, user_forget_password_screen.class)));

        login_btn_for_password.setOnClickListener(v -> {
            if (login_user_id.getText().toString().trim().isEmpty()) {
                showMessage("Enter User ID ");
            } else if (login_user_password.getText().toString().trim().isEmpty()) {
                showMessage("Enter User Password");
            } else {
                hit_api_for_login(login_user_id.getText().toString().trim(), login_user_password.getText().toString().trim());
            }

        });

        login_btn_for_register.setOnClickListener(v -> {
            Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(in);
        });
    }


    private void hit_api_for_login(String userId, String password) {
        progressDialog.show();
        ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterface.login_user_api_new(userId, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                    //    Log.d("TAG", "onResponse: "+jsonObject);
                        String api_result = jsonObject.getString("responseType");
                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        String api_message = jsonObjectMessage.getString("msg");
                        progressDialog.dismiss();
                        if (api_result.equals("success")) {
                            JSONObject result_data = jsonObject.getJSONObject("responseData");
                            SharedPreferencesHandler s = new SharedPreferencesHandler(LoginActivity.this);
                            s.DirectWritePreference(Constdata.token, jsonObject.optString("token"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.emp_id, result_data.optString("emp_id"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.department, result_data.optString("department"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.division, result_data.optString("division"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.designation, result_data.optString("designation"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.emp_role, result_data.optString("emp_role"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.emp_name, result_data.getString("emp_name"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.work_location, result_data.getString("work_location"), LoginActivity.this);
                            s.DirectWritePreference(Constdata.image, result_data.getString("image"), LoginActivity.this);
                            launch_screen(result_data.optString("emp_role"));
                            Log.d("TAG", "onResponse:Token "+jsonObject.optString("token"));


                        } else {

                            progressDialog.dismiss();
                            showMessage(api_message);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed(
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed(
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    progressDialog.dismiss();
                    onFailed(
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                progressDialog.dismiss();
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed(
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed(
                            "Check Your Network Settings & try again.");


                } else {
                    onFailed(
                            "Error Failure: " + t.getMessage());
                }
            }
        });
    }

    private void launch_screen(String emp_role) {
        if (emp_role.equalsIgnoreCase("ADMIN")) {
            startActivity(new Intent(LoginActivity.this, AdminDashboardScreen.class));
            finish();
        } else if (emp_role.equalsIgnoreCase("SuperAdmin")) {
            startActivity(new Intent(LoginActivity.this, AdminDashboardScreen.class));
            finish();
        }else{
            Intent intent =new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("emp_role",emp_role);
            startActivity(intent);
            finish();
        }

    }

    private void onFailed(String s1) {
        Toast.makeText(this, "" + s1, Toast.LENGTH_SHORT).show();
    }


    private void showMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();


    }

    private void initViews() {
        tvLocation = findViewById(R.id.tvLocation);
        login_user_id = findViewById(R.id.login_user_id);
        login_user_password = findViewById(R.id.login_user_password);
        forget_password_tap_here_tv = findViewById(R.id.forget_password_tap_here_tv);
        login_btn_for_password = findViewById(R.id.login_btn_for_password);
        login_btn_for_register= findViewById(R.id.login_btn_for_register);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            login_user_id.setText(credentials.getId().substring(3)); //get the selected phone number
//Do what ever you want to do with your selected phone number here

        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            // *** No phone numbers available ***
            Toast.makeText(LoginActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }
    }


    private void phoneReadMethod() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(LoginActivity.this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

}





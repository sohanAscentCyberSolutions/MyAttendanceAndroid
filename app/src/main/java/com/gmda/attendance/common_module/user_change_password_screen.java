package com.gmda.attendance.common_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gmda.attendance.CommonClass.OtpEditText;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.Retrofit.RetrofitClientOtp;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_change_password_screen extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    OtpEditText et_otp;
    Button btn_validate, btn_sentOtp;
    EditText et_mobile;
    CardView layoutSendOtp, layout_otp_verify;
    SharedPreferences sharedpreferences;
    String mobile_number;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password_screen);

        btn_sentOtp = findViewById(R.id.btn_sendOtp);
        et_otp = findViewById(R.id.et_otp);


        et_mobile = findViewById(R.id.phone_number_box);
        btn_validate = findViewById(R.id.validate);
        layoutSendOtp = findViewById(R.id.layoutSendOtp);
        layout_otp_verify = findViewById(R.id.layout_otp_verify);

        progressDialog = new ProgressDialog(user_change_password_screen.this);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage("Authenticating. Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        btn_sentOtp.setOnClickListener(view -> {
            if (TextUtils.isEmpty(et_mobile.getText().toString())) {
                et_mobile.setError("Enter mobile number");
                et_mobile.requestFocus();
            } else if (et_mobile.getText().toString().length() < 10) {
                Toast.makeText(user_change_password_screen.this, "Enter Correct No", Toast.LENGTH_SHORT).show();
            } else {
                mobile_number = et_mobile.getText().toString().trim();
                check_user_exist_or_not();
            }
        });

        btn_validate.setOnClickListener(v -> {
            if (et_otp.getText().toString().isEmpty()) {
                Toast.makeText(user_change_password_screen.this, "Enter Otp ", Toast.LENGTH_SHORT).show();
            } else if (et_otp.getText().length() < 5) {
                Toast.makeText(user_change_password_screen.this, "Enter Valid Otp", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();
                ApiInterFace apiInterFace = RetrofitClient.getOneMapDepartmentApiClient().create(ApiInterFace.class);
                Call<ResponseBody> call = apiInterFace.VerifyOtpNew(mobile_number,et_otp.getText().toString());

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                JSONObject jsonObject = new JSONObject(result);
                                String api_result = jsonObject.getString("responseType");

                                JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                                progressDialog.dismiss();
                                String api_message = jsonObjectMessage.getString("msg");
                                if (api_result.equals("success")) {
                                    Toast.makeText(user_change_password_screen.this, "Otp Verification Complete ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(user_change_password_screen.this, user_change_password_fill_screen.class));
                                    finish();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(user_change_password_screen.this, "" + api_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Log.i("Resp Exc: ", e.getMessage() + "");
                                onFailed("An unexpected error has occured.",
                                        "Error: " + e.getMessage() + "\n" +
                                                "Please Try Again later ");
                            }


                        } else if (response.code() == 404) {
                            progressDialog.dismiss();
                            Log.i("Resp Exc: ", "" + response.code());
                            onFailed("An unexpected error has occured.",
                                    "Error Code: " + response.code() + "\n" +
                                            "Please Try Again later ");


                        } else {
                            Log.i("Resp Exc: ", "" + response.code());
                            progressDialog.dismiss();
                            onFailed("An unexpected error has occured.",
                                    "Error Code: " + response.code() + "\n" +
                                            "Please Try Again later ");

                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("Resp onFailure: ", "" + t.getMessage());
                        progressDialog.dismiss();
                        if (t.getMessage().startsWith("Unable to resolve host")) {
                            onFailed("Slow or No Connection!",
                                    "Check Your Network Settings & try again.");


                        } else if (t.getMessage().startsWith("timeout")) {
                            onFailed("Slow or No Connection!",
                                    "Check Your Network Settings & Try Again.");


                        } else {
                            onFailed("An unexpected error has occured.",
                                    "Error Failure: " + t.getMessage());


                        }
                    }
                });
            }
        });


    }

    private void check_user_exist_or_not() {
        progressDialog.show();
        ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterface.checkUserExist(et_mobile.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        progressDialog.dismiss();
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equalsIgnoreCase("success")) {
                            send_otp_to_user();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(user_change_password_screen.this, "" + api_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    progressDialog.dismiss();
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                progressDialog.dismiss();
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & Try Again.");


                } else {
                    onFailed("An unexpected error has occured.",
                            "Error Failure: " + t.getMessage());


                }
            }
        });
    }

    private void send_otp_to_user() {
        progressDialog.show();
        ApiInterFace apiInterface = RetrofitClient.getOneMapDepartmentApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call =apiInterface.sendOtpToUserNew(et_mobile.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");

                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        progressDialog.dismiss();
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equalsIgnoreCase("success")) {
                            layoutSendOtp.setVisibility(View.GONE);
                            layout_otp_verify.setVisibility(View.VISIBLE);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(user_change_password_screen.this, "" + api_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    progressDialog.dismiss();
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                progressDialog.dismiss();
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & Try Again.");


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

}
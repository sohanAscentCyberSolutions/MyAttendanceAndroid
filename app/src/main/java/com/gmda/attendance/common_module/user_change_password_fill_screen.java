package com.gmda.attendance.common_module;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_change_password_fill_screen extends AppCompatActivity {


    private static final String TAG = "TAG";
    EditText et_old_pass, et_new_pass, emp_et;
    Button btn_submit;
    LinearLayout layout_Emp, forget_Layout;
    private ProgressDialog progressDialog;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        emp_et = findViewById(R.id.emp_et);
        et_old_pass = findViewById(R.id.et_oldPassword);
        et_new_pass = findViewById(R.id.et_emp_new_password);
        btn_submit = findViewById(R.id.btn_submit);
        layout_Emp = findViewById(R.id.layout_Emp);
        forget_Layout = findViewById(R.id.forget_Layout);

        progressDialog = new ProgressDialog(user_change_password_fill_screen.this);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage("Changing Password. Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changPassword();
            }
        });

    }

    private void changPassword() {
        if (TextUtils.isEmpty(et_new_pass.getText().toString())) {
            et_new_pass.setError("Enter new password");
            et_new_pass.requestFocus();
            return;
        }else if (et_new_pass.getText().toString().length() < 6) {
            Toast.makeText(this, "Minimum 6 Digits Password Required", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            SharedPreferencesHandler s7 = new SharedPreferencesHandler(user_change_password_fill_screen.this);
            Log.d(TAG, "changePassword: " + (s7.DirectReadPreference(Constdata.token, user_change_password_fill_screen.this)));
            Log.d(TAG, "changePassword: " + (s7.DirectReadPreference(Constdata.emp_id, user_change_password_fill_screen.this)));

            ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);
            Call<ResponseBody> call = apiInterFace.ChangeUserPasswordNew(s7.DirectReadPreference(Constdata.emp_id, user_change_password_fill_screen.this),
                    et_new_pass.getText().toString());

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
                                Toast.makeText(user_change_password_fill_screen.this, "" + api_message, Toast.LENGTH_SHORT).show();
                                SharedPreferencesHandler s1 = new SharedPreferencesHandler(user_change_password_fill_screen.this);
                                s1.DirectClearPreferences(user_change_password_fill_screen.this);
                                startActivity(new Intent(user_change_password_fill_screen.this, SplashScreenActivity.class));
                                finish();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(user_change_password_fill_screen.this, "" + api_message, Toast.LENGTH_SHORT).show();
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
    }

    private void onFailed(String s, String s1) {
        Toast.makeText(this, "" + s1, Toast.LENGTH_SHORT).show();
    }
}

package com.gmda.attendance.ViewModel;


import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.Models.CreateUser;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CreateUserVM extends ViewModel {

    private MutableLiveData<CreateUser> MutableLiveData;


    public LiveData<CreateUser> LiveData(final Activity activity, String token, String emp_name, String dob, String mob_no, String division,
                                         String department, String designation, String emp_level, String work_location, String emp_role, String emp_pwd, String emp_img) {
        MutableLiveData = new MutableLiveData<>();
        if (Utils.isOnline(activity)) {

            ApiInterFace apiInterFace = RetrofitClient.getInstance().getApiInterface();
            apiInterFace.createUser("Bearer " + token, emp_name, dob, mob_no, division,
                    department, designation, emp_level, work_location, emp_role, emp_pwd, emp_img).enqueue(new Callback<CreateUser>() {
                @Override
                public void onResponse(Call<CreateUser> call, Response<CreateUser> response) {
                    Utils.dismissProgress();
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getResponseMsg());
                        MutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<CreateUser> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    Utils.dismissProgress();
                    Utils.dismissProgress();
                    if (t.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT at")) {
                        Utils.OpenDialog(activity, activity);
                    } else {

                    }
                }
            });

        } else {

        }
        return MutableLiveData;
    }
}
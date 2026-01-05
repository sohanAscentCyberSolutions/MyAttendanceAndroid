package com.gmda.attendance.ViewModel;


import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.Models.ReadAttendanceDate;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadAttendanceDateVM extends ViewModel {

 private MutableLiveData<ReadAttendanceDate> userMutableLiveData;

    public LiveData<ReadAttendanceDate> liveData(final Activity activity, String token, String a_date) {
        userMutableLiveData = new MutableLiveData<>();
        if (Utils.isOnline(activity)) {
           Utils.showProgress(activity);

            ApiInterFace apiInterFace = RetrofitClient.getInstance().getApiInterface();
            apiInterFace.getAttendanceByDate("Bearer "+token,a_date).enqueue(new Callback<ReadAttendanceDate>() {
                @Override
                public void onResponse(Call<ReadAttendanceDate> call, Response<ReadAttendanceDate> response) {
                       Utils.dismissProgress();
                    if (response.body() != null) {

//                        Log.d("responce",response.body()+""+response.message());
                        userMutableLiveData.setValue(response.body());
                    }
                }
                @Override
                public void onFailure(Call<ReadAttendanceDate> call, Throwable t) {
                   Utils.dismissProgress();
                    Utils.dismissProgress();
                    if (t.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT at")) {
                        Utils.OpenDialog(activity,activity);
                    } else {
                        Toast.makeText(activity, "Enter Login credential correctly", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Utils.dismissProgress();
            Utils.nointernet(activity,activity);
        }
        return userMutableLiveData;
    }
}
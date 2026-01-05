package com.gmda.attendance.ViewModel;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.Models.ReadAttendance;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadAttemdenceVM  extends ViewModel {

    private MutableLiveData<ReadAttendance> userMutableLiveData;

    public LiveData<ReadAttendance> liveData(final Activity activity, String token) {
        userMutableLiveData = new MutableLiveData<>();
        if (Utils.isOnline(activity)) {
            Utils.showProgress(activity);

            ApiInterFace apiInterFace = RetrofitClient.getInstance().getApiInterface();
            apiInterFace.getAttendance("Bearer "+token
            ).enqueue(new Callback<ReadAttendance>() {
                @Override
                public void onResponse(Call<ReadAttendance> call, Response<ReadAttendance> response) {
                    Utils.dismissProgress();
                    if (response.body() != null) {
                        userMutableLiveData.setValue(response.body());
                    }
                }
                @Override
                public void onFailure(Call<ReadAttendance> call, Throwable t) {
                    Utils.dismissProgress();
                    Utils.dismissProgress();
                    if (t.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT at")) {
                        Utils.OpenDialog(activity,activity);
                    } else {
                        Utils.nointernet(activity, activity);
                    }
                }
            });

        } else {
            Utils.nointernet(activity,activity);
        }
        return userMutableLiveData;
    }
}

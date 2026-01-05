package com.gmda.attendance.ViewModel;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.Models.PunchOut;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;

public class PunchOutVM extends ViewModel {
  private MutableLiveData<PunchOut> userMutableLiveData;

    public LiveData<PunchOut> liveData(final Activity activity, String token,String emp_id ,String punchout_lat,String punchout_long) {
        userMutableLiveData = new MutableLiveData<>();
        if (Utils.isOnline(activity)) {


            ApiInterFace apiInterFace = RetrofitClient.getInstance().getApiInterface();
         /*   apiInterFace.getPunchOut( "Bearer "+token,emp_id,punchout_lat, punchout_long).enqueue(new Callback<PunchOut>() {
                @Override
                public void onResponse(Call<PunchOut> call, Response<PunchOut> response) {
                    if (response.body() != null) {
                        Log.d("TAG", "onResponse: "+response.message());
                        userMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<PunchOut> call, Throwable t) {

                    if (t.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT at")) {
                        Utils.OpenDialog(activity,activity);

                    } else {

                    }

                }
            });*/
                }
        else {
            Utils.nointernet(activity, activity);
        }
        return userMutableLiveData;

        }
}
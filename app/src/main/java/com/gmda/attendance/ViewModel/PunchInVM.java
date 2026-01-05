package com.gmda.attendance.ViewModel;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.Models.PunchIn;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PunchInVM extends ViewModel {
  private MutableLiveData<PunchIn> userMutableLiveData;

    public LiveData<PunchIn> liveData(final Activity activity, String token,String emp_id ,String punchin_lat,String punchin_long) {
        userMutableLiveData = new MutableLiveData<>();
        if (Utils.isOnline(activity)) {

            ApiInterFace apiInterFace = RetrofitClient.getInstance().getApiInterface();
            apiInterFace.getPunchIn("Bearer "+token,emp_id, punchin_lat, punchin_long).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    Log.d("TAG1", "onResponse: "+ response.body().getResponseMsg());
//                 //   Log.d("TAG1", "onResponse: "+ Objects.requireNonNull(response.body().getResponseType()));
//                    response.body().getResponseMsg();


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("TAG11", "onFailure: "+t.getMessage());

                    t.getMessage();
                }
            });
            /*apiInterFace.getPunchIn("Bearer "+token,emp_id, punchin_lat, punchin_long).enqueue(new Callback<PunchIn>() {
                @Override
                public void onResponse(Call<PunchIn> call, Response<PunchIn> response) {

                    Log.d("TAG", "test23: "+response);
                    if (response.body() != null) {

                        userMutableLiveData.setValue(response.body());


                    }
                }
                @Override
                public void onFailure(Call<PunchIn> call, Throwable t) {
                    Log.d("TAG", "testfailure23 : "+t.getMessage());
                    if (t.getMessage().contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT at")) {
                        Utils.OpenDialog(activity,activity);


                    }
                }
            });
                }*/
        }
        else {
            Utils.nointernet(activity, activity);
        }
        return userMutableLiveData;

        }
}
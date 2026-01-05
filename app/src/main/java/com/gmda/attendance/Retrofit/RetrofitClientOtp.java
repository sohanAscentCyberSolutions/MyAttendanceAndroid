package com.gmda.attendance.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClientOtp {
    private static String BASE_URL = "https://onemapdepts.gmda.gov.in/ATTENDANCE_API_V1.0/api/";
    //    private static String BASE_URL = "https://onemapdepts.gmda.gov.in/smsapi/query/";
    private static RetrofitClientOtp retrofitClient;
    private static Retrofit retrofit;


    //  this is a type of logger to see the response in log cat
    private OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    public RetrofitClientOtp() {

        OkHttpClient okHttpClient = null;
        try {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    // SingleTone class is created for checking retrofit client instance/object has created or not
    public static synchronized RetrofitClientOtp getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClientOtp();
        }
        return retrofitClient;
    }

    //create method for getting the api
    public ApiInterFace getApiInterface() {
        return retrofit.create(ApiInterFace.class);
    }
}


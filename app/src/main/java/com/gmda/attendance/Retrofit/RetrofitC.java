package com.gmda.attendance.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitC {
    private static String BASE_URL_Version="https://onemapggm.gmda.gov.in/";
    // private static String BASE_URL="http://172.16.2.5:8082/";

    private static Retrofit retrofitGmda;
    public static Retrofit getApiClientGmda() {

        if (retrofitGmda == null) {

            OkHttpClient okHttpClient = null;
            try {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofitGmda = new Retrofit.Builder()
                    .baseUrl(BASE_URL_Version)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofitGmda;
    }
}

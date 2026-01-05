package com.gmda.attendance.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

//    private static String BASE_URL="https://onemapdepts.gmda.gov.in/ATTENDANCE_API_V1.0_ST/api/v2/";
//    private static String BASE_URL = "https://onemapdepts.gmda.gov.in/ATTENDANCE_API_V1.0/api/v2/";
//   public static String BASE_URL="https://onemapdepts.gmda.gov.in/ATTENDANCE_API_V1.0/api/";

    private static final String BASE_URL = "https://onemapdepts.gmda.gov.in/attendance_API_V2.0/";
    private static final String BASE_URL_STAGING = "https://onemapcitizens.gmda.gov.in/attendance_API_V2.0/";


    private static RetrofitClient retrofitClient;
    private static Retrofit retrofit;
    private static Retrofit retrofitNew;
    //  this is a type of logger to see the response in log cat
    private OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();


    public RetrofitClient() {
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
                .baseUrl(BASE_URL_STAGING)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    // SingleTone class is created for checking retrofit client instance/object has created or not
    public static synchronized RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public static Retrofit getApiClient() {
        if (retrofitNew == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).
                    readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).
                    build();

            retrofitNew = new Retrofit.Builder()
                    .baseUrl(BASE_URL_STAGING)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitNew;
    }

    public static Retrofit getOneMapDepartmentApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Retrofit retrofit = null;
        if (client != null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://onemapdepts.gmda.gov.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://onemapdepts.gmda.gov.in/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //create method for getting the api
    public ApiInterFace getApiInterface() {
        return retrofit.create(ApiInterFace.class);
    }

}

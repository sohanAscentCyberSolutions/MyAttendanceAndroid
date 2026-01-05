package com.gmda.attendance.CommonClass;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private  static SharedPreferencesHandler sharedPreferencesHandler;
    private  static   Context context;


    @Override
    public void onCreate() {
        context=this;
        super.onCreate();
        sharedPreferencesHandler=new SharedPreferencesHandler(this);
    }


    public static Context getContext(){
        return context;
    }

    public static SharedPreferencesHandler getpreff(){
    return sharedPreferencesHandler;
    }
}

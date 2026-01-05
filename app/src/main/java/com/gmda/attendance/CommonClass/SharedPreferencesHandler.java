package com.gmda.attendance.CommonClass;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.gmda.attendance.common_module.SplashScreenActivity;

/* Created By Rahul Bhardwaj on 08-10-2020

 */

public class SharedPreferencesHandler {
    private Activity activity;
    private Context context;
    SharedPreferences sharedpref;



    public SharedPreferencesHandler(Context context) {
        this.context = context;
        sharedpref =context.getSharedPreferences("ASM", Context.MODE_PRIVATE);
    }


    public void DirectWritePreference(String key ,String value,Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.commit();
        editor.apply();

    }
    public String DirectReadPreference(String key ,Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");

    }
    public void DirectClearPreferences(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent refresh = new Intent(context, SplashScreenActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(refresh);
        Toast.makeText(context, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
    }



}
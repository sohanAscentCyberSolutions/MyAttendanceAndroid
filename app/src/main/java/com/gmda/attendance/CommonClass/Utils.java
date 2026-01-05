package com.gmda.attendance.CommonClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.gmda.attendance.R;

import java.util.Locale;

/**
 * Created by Rahul Bhardwaj.
 */

public class Utils {
  /*  public static String site_url = "my_url";
    public static String base_url = site_url + "";*/

    private static ProgressDialog progressDialog,progressDialogn;
    public static String credentials = "username:password";
    // create Base64 encodet string
    public static String authorization ="name";
    public static String authorizationd =
            "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

    public static final boolean isOnline(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    public static void showProgressnew(Activity activity) {
        if (progressDialogn == null) {
            progressDialogn = new ProgressDialog(activity);
            progressDialogn.setTitle(activity.getString(R.string.app_name));
            progressDialogn.setMessage("Please wait.");
            progressDialogn.setCanceledOnTouchOutside(true);
            progressDialogn.setCancelable(true);
        }
        if (!progressDialogn.isShowing()) {
            try {
                progressDialogn.show();
            } catch (Exception e) {
                try {
                    progressDialogn.show();
                } catch (Exception e1) {

                }
            }
        }
    }

    public static void dismissProgressn() {
        if (progressDialogn != null) {
            if (progressDialogn.isShowing()) {
                progressDialogn.dismiss();
                progressDialogn = null;
            }
        }
    }
  /*  public static void showProgress(Activity activity) {
        if (progressDialog == null) {
            progressDialog = new Dialog(activity);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setContentView(R.layout.progress_loader);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window dialogWindow = progressDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
        }
        if (!progressDialog.isShowing()) {
            try {
                progressDialog.show();
            } catch (Exception e) {
                try {
                    progressDialog.show();
                } catch (Exception e1) {

                }
            }
        }
    }
    */
    public static void showProgress(Activity activity) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle(activity.getString(R.string.app_name));
            progressDialog.setMessage("Please wait.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing()) {
            try {
                progressDialog.show();
            } catch (Exception e) {
                try {
                    progressDialog.show();
                } catch (Exception e1) {

                }
            }
        }
    }

    public static void dismissProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    public static void OpenDialog(final Context c, final Activity activity, final String Subject, final String message, final int task) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(c).inflate(R.layout.no_data_found_popup, viewGroup, false);
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        TextView textData = dialogView.findViewById(R.id.textData);
        TextView titleMsg = dialogView.findViewById(R.id.titleMsg);
        titleMsg.setText(Subject);
        textData.setText(message);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                switch (task) {
                    case 0:
                        break;
                    case 1:
                        activity.onBackPressed();
                        break;

                }
            }
        });
        alertDialog.show();
    }

    public static void OpenDialog(final Context c, final Activity activity) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(c).inflate(R.layout.no_data_found_popup, viewGroup, false);
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        TextView textData = dialogView.findViewById(R.id.textData);
        TextView titleMsg = dialogView.findViewById(R.id.titleMsg);
        titleMsg.setText("Session Expired");
        textData.setText("You need to Login again.");
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* alertDialog.dismiss();
                App.getpreff().ClearPreferences();
                activity.finishAffinity();
                activity.startActivity(new Intent(activity, LoginActivity.class));
*/
            }
        });
        alertDialog.show();
    }

 /*   public static void showSnackbarAlert(View view, String message) {
        TSnackbar snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.RED);
        TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }*/

  /*  public static void showSnackbar(View view, String message) {
        TSnackbar snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.GREEN);
        TextView textView = snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }*/
    public static final void nointernet(Context context, final Activity activity) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Info");
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and reopen the Application");
            alertDialog.setIcon(R.drawable.ic_warning);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    activity.finish();
                    activity.startActivity(activity.getIntent());
                }
            });

            alertDialog.show();
        } catch (Exception e) {
            Log.d("no Internet", "Show Dialog: " + e.getMessage());
        }
    }
  /*  public static void setLocale(Activity activity, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, activity.getBaseContext().getResources().getDisplayMetrics());
        App.getpreff().WritePreference(Constdata.language_selected, lang);
    }*/


    public static boolean is_network_connected(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }



}

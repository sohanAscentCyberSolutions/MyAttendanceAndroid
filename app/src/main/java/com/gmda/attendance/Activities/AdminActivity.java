package com.gmda.attendance.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.R;

import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    //LogOutVM logOutVM;
    androidx.appcompat.app.AlertDialog dialog1;
    Toolbar toolbar;
    public LinearLayout user_detail_layout;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    CardView createUser,attendanceReport;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //logOutVM = ViewModelProviders.of(this).get(LogOutVM.class);
        /*navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);*/


//        drawer = findViewById(R.id.drawer_layout);
        createUser = findViewById(R.id.createUser);
        attendanceReport = findViewById(R.id.attendance_Report);



       // logout=findViewById(R.id.logout);

     /*   toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);*/

       /* View headerview = navigationView.getHeaderView(0);
//        user_detail_layout = headerview.findViewById(R.id.user_detail_layout);

        headerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, User_Detail_Activity.class));
            }
        });
*/


       SharedPreferencesHandler s9 =new SharedPreferencesHandler(AdminActivity.this);

       if (s9.DirectReadPreference(Constdata.emp_role,AdminActivity.this).equalsIgnoreCase("Superadmin")){
          /* createUser.setVisibility(View.GONE);
           attendanceReport.setVisibility(View.GONE);*/
           startActivity(new Intent(AdminActivity.this,Attendance_Report_Activity.class));
           finish();

       }
        attendanceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AdminActivity.this,Attendance_Report_Activity.class));
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,CreateUserActivity.class));
            }
        });
        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutUser();
            }
        });*/
    }


/*    private void logOutUser() {
        final SharedPreferencesHandler s = new SharedPreferencesHandler(AdminActivity.this);

        Call<LogOut> call = RetrofitClient.getInstance().getApiInterface()
                .userLogout("Bearer " + s.DirectReadPreference(Constdata.token, AdminActivity.this));

        call.enqueue(new Callback<LogOut>() {
            @Override
            public void onResponse(Call<LogOut> call, Response<LogOut> response) {

                if (response.body()!=null) {

                    SharedPreferencesHandler s1 = new SharedPreferencesHandler(AdminActivity.this);
                    s1.DirectClearPreferences(AdminActivity.this);
                    startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                    finish();
                    Toast.makeText(AdminActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                }
                else if (response.body()==null){

                    Toast.makeText(AdminActivity.this, "Token is expired....Please login again...!! ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                    finish();

                }



                //    userLogout(response.body().getResponseData());
                //response.body().
            }

            @Override
            public void onFailure(Call<LogOut> call, Throwable t) {

                t.getMessage();
            }
        });
    }*/
}

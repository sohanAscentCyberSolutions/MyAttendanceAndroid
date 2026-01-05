package com.gmda.attendance.employee_module;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.gmda.attendance.common_module.user_change_password_screen;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.Models.LogOut;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.common_module.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
User_Detail_Activity extends AppCompatActivity {


    TextView tv_emp_id, tv_emp_name, tv_role, tv_division, tv_designation, tv_department, tv_work_location;
    CardView change_password_card, logout_card;
    SharedPreferencesHandler s1;
    private Dialog dialog_loading;
    CircleImageView profile_image;
    SharedPreferences sp;
    String user_image;
    private static final int PICK_GALLERY_REQUEST = 101 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__detail_);


        change_password_card = findViewById(R.id.change_password_card);
        tv_emp_id = findViewById(R.id.tv_emp_id);
        tv_emp_name = findViewById(R.id.tv_emp_name);
        tv_role = findViewById(R.id.tv_role);
        tv_division = findViewById(R.id.tv_division);
        tv_designation = findViewById(R.id.tv_designation);
        tv_department = findViewById(R.id.tv_department);
        tv_work_location = findViewById(R.id.tv_work_location);
        logout_card = findViewById(R.id.logout_card);

        profile_image = findViewById(R.id.profile_image);
        dialog_loading = new Dialog(User_Detail_Activity.this);
        dialog_loading.setContentView(R.layout.loading_dailog);
        dialog_loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        s1 = new SharedPreferencesHandler(User_Detail_Activity.this);
        tv_emp_id.setText(s1.DirectReadPreference(Constdata.emp_id, User_Detail_Activity.this));
        tv_emp_name.setText(s1.DirectReadPreference(Constdata.emp_name, User_Detail_Activity.this));
        tv_department.setText(new StringBuilder().append("🏢 Department : ").append(s1.DirectReadPreference(Constdata.department, User_Detail_Activity.this)).toString());
        tv_designation.setText(new StringBuilder().append("Designation : ").append(s1.DirectReadPreference(Constdata.designation, User_Detail_Activity.this)).toString());
        tv_work_location.setText(new StringBuilder().append("📍 Loc.").append(s1.DirectReadPreference(Constdata.work_location, User_Detail_Activity.this)).toString());
        tv_division.setText(new StringBuilder().append("Division : ").append(s1.DirectReadPreference(Constdata.division, User_Detail_Activity.this)).toString());
        tv_role.setText(new StringBuilder().append("💼 Role : ").append(s1.DirectReadPreference(Constdata.emp_role, User_Detail_Activity.this)).toString());




        user_image = s1.DirectReadPreference(Constdata.image,User_Detail_Activity.this).toString();


        if(!Objects.equals("null",user_image)){
            byte[] decodedString = Base64.decode(user_image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profile_image.setImageBitmap(decodedByte);
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, PICK_GALLERY_REQUEST);
            }
        });

        change_password_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Detail_Activity.this, user_change_password_screen.class));

            }
        });
        logout_card.setOnClickListener(view -> {
            s1.DirectClearPreferences(User_Detail_Activity.this);
        });


    }



    private void onFailed(String s, String s1) {
        Toast.makeText(this, "" + s1, Toast.LENGTH_SHORT).show();
    }


    public void showProgressbar() {
        dialog_loading.show();
        dialog_loading.setCancelable(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == PICK_GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = imageReturnedIntent.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    profile_image.setImageBitmap(selectedImage);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    sp.edit().putString("dp", encodedImage).commit();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void hideProgressbar() {
        dialog_loading.dismiss();
    }
}

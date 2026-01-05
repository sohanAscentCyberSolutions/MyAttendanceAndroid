package com.gmda.attendance.Activities;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gmda.attendance.BuildConfig;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.CorrectImageRotation;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.CommonClass.Utils;
import com.gmda.attendance.Models.AddImages;
import com.gmda.attendance.Models.CreateUser;
import com.gmda.attendance.Models.DepartmentModel;
import com.gmda.attendance.Models.DivisionModel;
import com.gmda.attendance.Models.WorkLocation;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.ViewModel.CreateUserVM;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText et_employeeName, et_dob, et_mobile, et_designation, et_emp_level, et_Role, et_password;
    Button btn_submit;
    ImageView take_image;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private byte[] tree_image_byte;
    AlertDialog dialog1;
    String currentPhotoPath;
    private String treeimage = "";
    Calendar myCalendar;
    private String mImageUrl = "";
    DivisionModel divisionModel;
    DepartmentModel departmentModel;
    WorkLocation workLocation;
    ArrayList<String> division_name;
    ArrayList<String> department_name;
    ArrayList<String> workLocation_name;
    String div,depart,work;
    CreateUserVM createUserVM;
    Bitmap bitmap;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Spinner sp_work_location, sp_division, sp_department,sp_emp_level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        et_password = findViewById(R.id.et_password);
        et_employeeName = findViewById(R.id.et_employee_name);
        et_dob = findViewById(R.id.et_date_of_birth);
        et_mobile = findViewById(R.id.et_mobile_number);
        sp_division = findViewById(R.id.sp_Division);
        sp_department = findViewById(R.id.sp_Department);
        et_designation = findViewById(R.id.et_designation);
        et_Role = findViewById(R.id.et_role);
        take_image = findViewById(R.id.take_image);
        btn_submit = findViewById(R.id.btn_submit);
        sp_work_location = findViewById(R.id.sp_work_location);


        sp_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depart = sp_department.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        sp_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                div =  sp_division.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_work_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                work=  sp_work_location.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Call<DepartmentModel> calls=RetrofitClient.getInstance().getApiInterface().getDepartment();
        calls.enqueue(new Callback<DepartmentModel>() {
            @Override
            public void onResponse(Call<DepartmentModel> call, Response<DepartmentModel> response) {

                if (response.body()!=null){

                    if (response.body().getResponseType().equals("success")) {
                        departmentModel = response.body();
                        department_name = new ArrayList<>();
                        department_name.add(getString(R.string.Select_department_name));
                        for (int i = 0; i < response.body().getResponseData().size(); i++) {
                            department_name.add(response.body().getResponseData().get(i).getDeptName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateUserActivity.this, R.layout.support_simple_spinner_dropdown_item, department_name);
                        sp_department.setAdapter(adapter);
                    }
                    /* dataDivision(response.body().getResponseData());*/
                }

            }

            @Override
            public void onFailure(Call<DepartmentModel> call, Throwable t) {

            }
        });



        SharedPreferencesHandler sf=new SharedPreferencesHandler(CreateUserActivity.this);
        Call<DivisionModel> callf=RetrofitClient.getInstance().getApiInterface()
                .getDivision();
        callf.enqueue(new Callback<DivisionModel>() {
            @Override
            public void onResponse(Call<DivisionModel> call, Response<DivisionModel> response) {
                if (response.body()!=null){
                    if (response.body().getResponseType().equals("success")) {
                        divisionModel = response.body();
                        division_name = new ArrayList<>();
                        division_name.add(getString(R.string.Select_division_name));
                        for (int i = 0; i < response.body().getResponseData().size(); i++) {
                            division_name.add(response.body().getResponseData().get(i).getDivName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateUserActivity.this, R.layout.support_simple_spinner_dropdown_item, division_name);
                        sp_division.setAdapter(adapter);
                    }
                }}
            @Override
            public void onFailure(Call<DivisionModel> call, Throwable t) {
            }
        });


        Call<WorkLocation> callD=RetrofitClient.getInstance().getApiInterface().getWorkLocation();
        callD.enqueue(new Callback<WorkLocation>() {
            @Override
            public void onResponse(Call<WorkLocation> call, Response<WorkLocation> response) {
                if (response.body()!=null){
                    if (response.body().getResponseType().equals("success")) {
                        workLocation = response.body();
                        workLocation_name = new ArrayList<>();
                        workLocation_name.add(getString(R.string.Select_workLocation));
                        for (int i = 0; i < response.body().getResponseData().size(); i++) {
                            workLocation_name.add(response.body().getResponseData().get(i).getLocName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateUserActivity.this, R.layout.support_simple_spinner_dropdown_item, workLocation_name);
                        sp_work_location.setAdapter(adapter);
                    }
                    /* dataDivision(response.body().getResponseData());*/
                }
            }

            @Override
            public void onFailure(Call<WorkLocation> call, Throwable t) {

            }
        });

        createUserVM = ViewModelProviders.of(this).get(CreateUserVM.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String token= sharedpreferences.getString("token","");
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateUserActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        take_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cameraIntent();
            }
        });

        // Need Parameter of image in string
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();

            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_dob.setText(sdf.format(myCalendar.getTime()));
    }


    public void validation() {

        if (TextUtils.isEmpty(et_employeeName.getText().toString())) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_phone));
            et_employeeName.setError(getString(R.string.err_msg_phone));
            et_employeeName.requestFocus();
            return;

            // It check a password is empty or not .
        } else if (TextUtils.isEmpty(et_dob.getText().toString())) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_password));
            et_dob.setError(getString(R.string.err_msg_phone));
            //requestFocus(password);
            et_dob.requestFocus();
            return;
        } else if (TextUtils.isEmpty(et_mobile.getText().toString())) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_password));
            et_mobile.setError(getString(R.string.err_msg_phone));
            //requestFocus(password);
            et_mobile.requestFocus();
            return;
        } else if (sp_division.getSelectedItemPosition() == 0) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_password));

            //requestFocus(password);
            sp_division.requestFocus();
            return;
        } else if (sp_department.getSelectedItemPosition() == 0) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_password));

            //requestFocus(password);
            sp_department.requestFocus();
            return;
        } else if (TextUtils.isEmpty(et_designation.getText().toString())) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_password));
            et_designation.setError(getString(R.string.err_msg_phone));
            //requestFocus(password);
            et_designation.requestFocus();
            return;
        } else if (sp_work_location.getSelectedItemPosition() == 0) {
            sp_work_location.requestFocus();
            return;
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_password));
            et_password.setError(getString(R.string.err_msg_phone));
            //requestFocus(password);
            et_password.requestFocus();
            return;
        } else if (TextUtils.isEmpty(et_Role.getText().toString())) {
            //Utils.showSnackbarAlert(v, getString(R.string.err_msg_password));
            et_Role.setError(getString(R.string.err_msg_phone));
            //requestFocus(password);
            et_Role.requestFocus();
            return;
        }
        upload_Image(treeimage, tree_image_byte);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void upload_Image(String image, byte[] myimage11) {
        File file_photo = new File(String.valueOf(image));
        RequestBody photoFile = RequestBody.create(MediaType.parse("multipart/form-data"), myimage11);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("attachment", file_photo.getName(), photoFile);
        Call<AddImages> call = RetrofitClient.getInstance().getApiInterface()
        .uploadImage(photo);
        //App.getpreff().ReadPreferences(Constdata.token)

        call.enqueue(new Callback<AddImages>() {
            @Override
            public void onResponse(Call<AddImages> call, Response<AddImages> response) {
                if (response.isSuccessful()) {
                    Log.d("image", response.body().getResponseData());
                     createUser(response.body().getResponseData());
                }
                else {
                    Log.d(TAG, "onResponse: "+response.message( ));
                }
            }

            @Override
            public void onFailure(Call<AddImages> call, Throwable t) {
                Utils.dismissProgress();
//                    Toast.makeText(getApplicationContext(), "Image upload Failed", Toast.LENGTH_LONG).show();
                Log.i("ResponseBody", t.getMessage() + "");
            }
        });

    }




    private void createUser(String image) {
        SharedPreferencesHandler s=new SharedPreferencesHandler(CreateUserActivity.this);
        Log.d("TokeninRead",""+ s.DirectReadPreference(Constdata.token,CreateUserActivity.this));
        createUserVM.LiveData(CreateUserActivity.this,
                s.DirectReadPreference(Constdata.token,CreateUserActivity.this),
                et_employeeName.getText().toString() + "",
                et_dob.getText().toString() + "",
                et_mobile.getText().toString() + "",
                div + "",
                depart+ "",
                et_designation.getText().toString() + "",
                sp_emp_level.getSelectedItem().toString() + "",
                work + "",
                et_Role.getText().toString() + "",
                et_password.getText().toString() + "",
               image + ""
                )
                .observe(CreateUserActivity.this, new Observer<CreateUser>() {
                    @Override
                    public void onChanged(CreateUser createUser) {
                        if (createUser.getResponseType().equals("success")) {
                            Log.d(TAG, "onChanged: "+createUser.getResponseMsg().getMsg());
                            AlertDialog.Builder builder = new AlertDialog.Builder(CreateUserActivity.this);
                            builder.setMessage(R.string.Data_successfully_submitted);
                            builder.setCancelable(false);
                            // add the buttons
                            builder.setPositiveButton(R.string.Okay,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog1.dismiss();
                                            startActivity(new Intent(CreateUserActivity.this, AdminActivity.class));
                                            finish();
                                        }
                                    });
                            dialog1 = builder.create();
                            dialog1.show();
                        }
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                Uri imageUri = Uri.fromFile(new File(currentPhotoPath));

                Bitmap bmp = CorrectImageRotation.handleSamplingAndRotationBitmap(getApplicationContext(), imageUri);
                take_image.setImageBitmap(bmp);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (bmp.getWidth() > 720 || bmp.getHeight() > 720) {
                    bmp = getResizedBitmap(bmp, 720);
                }
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                try {

                    treeimage = currentPhotoPath;
                    tree_image_byte = stream.toByteArray();
                    Log.d(TAG, "onActivityResult: "+treeimage);
                    Log.d(TAG, "onActivityResult: "+tree_image_byte);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}

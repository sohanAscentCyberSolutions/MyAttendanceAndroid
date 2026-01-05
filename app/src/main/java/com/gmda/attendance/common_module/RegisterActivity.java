package com.gmda.attendance.common_module;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final int GALLERY_REQUEST_CODE = 105;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final String[] imageSelectionArray = {
            "From Gallery"
    };
    EditText et_employeeName, et_dob, et_mobile, et_employee_id, et_password, et_email_id;
    Button btn_submit;
    Calendar myCalendar;

    ArrayList<String> department_name;
    ArrayList<String> department_code;

    ArrayList<String> division_name;
    ArrayList<String> division_code;
    ArrayList<String> division_id;
    ArrayList<String> division_status;
    ArrayList<String> location_Name;


    ArrayList<String> designation_code;
    ArrayList<String> designation_name;


    ProgressBar department_progress_bar;
    LinearLayout division_layout;
    LinearLayout designation_layout;

    TextView sp_work_location, sp_division, sp_department, sp_designation;
    ProgressDialog progressDialog;
    ImageView take_image;
    CheckBox no_employee_id_check_box;
    String selectedDivisionId = "";
    String selectDesignationId = "";
    String selectLocationId = "";
    String selectedDepartmentId = "";
    Bitmap convertedImage;
    String Base64ImageUpload = "";
    private String treeimage = "";
    private String mImageUrl = "";
    private ArrayList<String> department_name_list;
    private ArrayList<String> department_code_list;
    private ArrayList<String> division_name_list;
    private ArrayList<String> division_code_list;
    private ArrayList<String> location_name_list;
    private ArrayList<String> location_code_list;
    private Uri imageUri;
    private String img_path_1 = "";
    private File imageFile;
    private byte[] imageInByte;
    private int rq_code_camera = 1;

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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

    public static String getDriveFile(Context context, Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        department_code_list = new ArrayList<>();
        department_name_list = new ArrayList<>();
        division_name_list = new ArrayList<>();
        division_code_list = new ArrayList<>();
        location_name_list = new ArrayList<>();
        location_code_list = new ArrayList<>();
        designation_code = new ArrayList<>();
        designation_name = new ArrayList<>();

        et_email_id = findViewById(R.id.et_email_id);
        department_progress_bar = findViewById(R.id.department_progress_bar);
        division_layout = findViewById(R.id.division_layout);
        designation_layout = findViewById(R.id.designation_layout);

        et_password = findViewById(R.id.et_password);
        et_employeeName = findViewById(R.id.et_employee_name);
        et_dob = findViewById(R.id.et_date_of_birth);
        et_mobile = findViewById(R.id.et_mobile_number);
        sp_division = findViewById(R.id.sp_division);
        sp_department = findViewById(R.id.sp_Department);
        sp_designation = findViewById(R.id.sp_designation);
        take_image = findViewById(R.id.take_image);
        btn_submit = findViewById(R.id.btn_submit);
        sp_work_location = findViewById(R.id.sp_work_location);
        no_employee_id_check_box = findViewById(R.id.no_employee_id_check_box);
        et_employee_id = findViewById(R.id.et_employee_id);
        department_code = new ArrayList<>();
        department_name = new ArrayList<>();
        division_name = new ArrayList<>();
        division_code = new ArrayList<>();
        division_status = new ArrayList<>();
        division_id = new ArrayList<>();
        location_Name = new ArrayList<>();


        progressDialog = new ProgressDialog(RegisterActivity.this);

        progressDialog.setMessage("Loading");
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);


        sp_department.setOnClickListener(view1 -> {
            SpinnerDialog spinnerDialog4 = new SpinnerDialog(RegisterActivity.this, department_name_list, "Please Select Department Name");
            spinnerDialog4.bindOnSpinerListener((s, i1) -> {
                sp_department.setText(s);
                selectedDivisionId = "";
                sp_division.setText("Select Division Name");
                selectedDepartmentId = department_code_list.get(i1);
                get_all_division(selectedDepartmentId);
            });
            spinnerDialog4.showSpinerDialog();
        });

        sp_division.setOnClickListener(view1 -> {
            SpinnerDialog spinnerDialog4 = new SpinnerDialog(RegisterActivity.this, division_name_list, "Please Select Division Name");
            spinnerDialog4.bindOnSpinerListener((s, i1) -> {
                sp_division.setText(s);
                selectedDivisionId = division_code_list.get(i1);
                selectDesignationId = "";
                sp_designation.setText("Select Designation Name");
                get_all_designation(selectedDepartmentId, selectedDivisionId);
            });
            spinnerDialog4.showSpinerDialog();
        });

        sp_designation.setOnClickListener(view1 -> {
            SpinnerDialog spinnerDialog4 = new SpinnerDialog(RegisterActivity.this, designation_name, "Please Select Designation Name");
            spinnerDialog4.bindOnSpinerListener((s, i1) -> {
                sp_designation.setText(s);
                selectDesignationId = designation_code.get(i1);
            });
            spinnerDialog4.showSpinerDialog();
        });

        sp_work_location.setOnClickListener(view1 -> {
            SpinnerDialog spinnerDialog4 = new SpinnerDialog(RegisterActivity.this, location_name_list, "Please Select Location Name");
            spinnerDialog4.bindOnSpinerListener((s, i1) -> {
                sp_work_location.setText(s);
                selectLocationId = location_code_list.get(i1);
            });
            spinnerDialog4.showSpinerDialog();
        });

        get_all_department();
        get_all_location();

        no_employee_id_check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onCheckedChanged: " + b);
                if (b) {
                    if (et_mobile.getText().toString().isEmpty()) {
                        Toast.makeText(RegisterActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    } else if (et_mobile.getText().toString().length() < 10) {
                        Toast.makeText(RegisterActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    } else {
                        et_employee_id.setText(et_mobile.getText().toString());
                    }
                } else {
                    et_employee_id.setText("");

                }
            }
        });


        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        et_dob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            // Handle the selected date
                            String selectedDate = (selectedMonth + 1) + "-" + selectedDay +"-"+ selectedYear ;
                            et_dob.setText(selectedDate);
                        }
                    }, year, month, day);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        take_image.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Select Profile Image");
            builder.setItems(imageSelectionArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick:which " + which);
                    if (which == 0) {
                        int REQUEST_PERMISSION = 20;
                        String readImagePermission2;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            readImagePermission2 = Manifest.permission.READ_MEDIA_IMAGES;
                        } else {
                            readImagePermission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
                        }
                        int hasREAD_IMAGE_STORAGEPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), readImagePermission2);
                        List<String> permissions = new ArrayList<String>();
                        if (hasREAD_IMAGE_STORAGEPermission != PackageManager.PERMISSION_GRANTED) {
                            permissions.add(readImagePermission2);
                        }
                        if (!permissions.isEmpty()) {
                            ActivityCompat.requestPermissions(RegisterActivity.this, permissions.toArray(new String[permissions.size()]), 101);
                        } else {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_PERMISSION);
                        }
                    }

                }
            });
            builder.show();
        });
        btn_submit.setOnClickListener(v -> validation());
    }

    private void get_all_designation(String selectedDepartmentId, String selectedDivisionId) {
        progressDialog.show();
        designation_name.clear();
        designation_code.clear();
        ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterface.getDesignationName(selectedDepartmentId, selectedDivisionId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String responseType = jsonObject.getString("responseType");
                        JSONObject responseMsg = jsonObject.optJSONObject("responseMsg");
                        String msg = responseMsg.optString("msg");

                        progressDialog.dismiss();
                        if (responseType.equalsIgnoreCase("success")) {
                            if (msg.equalsIgnoreCase("Designation list")) {
                                JSONArray result_array = jsonObject.getJSONArray("responseData");
                                for (int i = 0; i < result_array.length(); i++) {
                                    JSONObject object = result_array.getJSONObject(i);
                                    designation_name.add(object.optString("desgn_name"));
                                    designation_code.add(object.optString("desgn_id"));

                                }
                                designation_layout.setVisibility(View.VISIBLE);

                            } else {

                                Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed2("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }
                } else if (response.code() == 404) {
                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");
                } else {
                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("Resp onFailure: ", "" + t.getMessage());
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else {
                    onFailed2("An unexpected error has occured.",
                            "Error Failure: " + t.getMessage());


                }
            }
        });


    }

    private void AllowPermissions() {
        Log.d(TAG, "AllowPermissions: ");
        String readImagePermission2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            readImagePermission2 = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            readImagePermission2 = Manifest.permission.READ_EXTERNAL_STORAGE;

        }
        int hasCAMERAPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int hasREAD_IMAGE_STORAGEPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), readImagePermission2);


        List<String> permissions = new ArrayList<String>();
        if (hasCAMERAPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (hasREAD_IMAGE_STORAGEPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(readImagePermission2);
        }
        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 101);
        } else {
            Log.d(TAG, "AllowPermissions:rq_code_camera " + rq_code_camera);
            camera_intent(rq_code_camera);
        }

    }

    private void registerUserToDB() {
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterface.registerUser(
                et_employee_id.getText().toString(),
                et_employeeName.getText().toString(),
                et_mobile.getText().toString(),
                selectLocationId,
                selectedDepartmentId,
                selectedDivisionId,
                selectDesignationId,
                et_password.getText().toString(),
                et_dob.getText().toString(),
                Base64ImageUpload,
                et_email_id.getText().toString()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String responseType = jsonObject.getString("responseType");
                        JSONObject responseMsg = jsonObject.optJSONObject("responseMsg");
                        String message = responseMsg.optString("msg");

                        progressDialog.dismiss();
                        if (message.equalsIgnoreCase("Employee registered successfully")) {
                            if (responseType.equalsIgnoreCase("success")) {
                                Toast.makeText(RegisterActivity.this, "Your Account In Review", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, SplashScreenActivity.class));
                                finish();

                            } else {
                                Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {

                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {

                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {

                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("Resp onFailure: ", "" + t.getMessage());
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else {
                    onFailed("An unexpected error has occured.",
                            "Error Failure: " + t.getMessage());


                }
            }
        });

    }

    private void onFailed(String s, String s1) {
        Toast.makeText(this, "" + s1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 101) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions", "Permission Granted: " + permissions[i]);
                    camera_intent(rq_code_camera);
                    return;
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.d("Permissions", "Permission Denied: " + permissions[i]);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private String getRealPathFromURI(Uri photoUri) {
        String result;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(photoUri, projection, null, null, null);
        if (cursor == null) {
            result = photoUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(projection[0]);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                try {
                    Uri photoUri = imageUri;
                    if (photoUri != null) {
                        try {
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        photoUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();

                            convertedImage = getResizedBitmap(bmp, 512);
                            convertedImage.compress(Bitmap.CompressFormat.JPEG, 20, stream);

                            take_image.setImageBitmap(convertedImage);

                            imageFile = new File(getRealPathFromURI(photoUri));
                            img_path_1 = getRealPathFromURI(photoUri);

                            Log.i("imageFile ", "" + ": " + imageFile);

                            try {
                                imageInByte = stream.toByteArray();
                                Base64ImageUpload = getEncoded64ImageStringFromBitmap(convertedImage);
                                Log.i("img_byt_1 ", "" + ": " + imageInByte);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("img_stts e", "" + "Image Stts" + e.getMessage());
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error reading image file", Toast.LENGTH_LONG).show();
                        Log.i("img_stts error", "" + "Error reading image file");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                    Log.i("img_stts e", "" + "Image Stts" + e.getMessage());
                }
            } else if (requestCode == 20) {
                if (data.getData() != null) {
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        try {
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(
                                        photoUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();

                            convertedImage = getResizedBitmap(bmp, 512);
                            convertedImage.compress(Bitmap.CompressFormat.JPEG, 20, stream);

                            take_image.setImageBitmap(convertedImage);
                            imageFile = new File(getDriveFile(RegisterActivity.this, photoUri));
                            img_path_1 = getDriveFile(RegisterActivity.this, photoUri);

                            Log.i("imageFile ", "" + ": " + imageFile);
                            Log.d("TAG", "onActivityResult: " + img_path_1);
                            try {
                                imageInByte = stream.toByteArray();
                                Base64ImageUpload = getEncoded64ImageStringFromBitmap(convertedImage);

                                Log.i("img_byt_1 ", "" + ": " + imageInByte);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("img_stts e", "" + "Image Stts" + e.getMessage());
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error reading image file", Toast.LENGTH_LONG).show();
                        Log.i("img_stts error", "" + "Error reading image file");
                    }
                }
            }


        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Request Cancelled.", Toast.LENGTH_SHORT).show();
            Log.i("img_stts Cancelled", "" + "Request Cancelled");
        } else {
            getContentResolver().delete(imageUri, null, null);
            Toast.makeText(getApplicationContext(), "Data not getting.", Toast.LENGTH_SHORT).show();
            Log.i("img_stts DataNtGetting", "" + "Data not getting" + imageUri);
        }
    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


    private void camera_intent(int rq_code_camera) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, rq_code_camera);

    }


    public void validation() {
        if (TextUtils.isEmpty(et_employeeName.getText().toString())) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_dob.getText().toString())) {
            Toast.makeText(this, "Enter Date Of Birth", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_mobile.getText().toString())) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
        }else if(et_mobile.getText().length()<10){
            Toast.makeText(this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_email_id.getText().toString())) {
            Toast.makeText(this, "Enter Email Id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_employee_id.getText().toString())) {
            Toast.makeText(this, "Enter Employee Id", Toast.LENGTH_SHORT).show();
        } else if (selectDesignationId.isEmpty()) {
            Toast.makeText(this, "Select Designation", Toast.LENGTH_SHORT).show();
        } else if (selectedDepartmentId.isEmpty()) {
            Toast.makeText(this, "Select Department Name", Toast.LENGTH_SHORT).show();
        } else if (selectedDivisionId.isEmpty()) {
            Toast.makeText(this, "Select Division Name", Toast.LENGTH_SHORT).show();
        } else if (selectLocationId.isEmpty()) {
            Toast.makeText(this, "Select Location  Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else if (et_password.getText().toString().length() < 6) {
            Toast.makeText(this, "Minimum 6 Digits Password Required", Toast.LENGTH_SHORT).show();
        } else if (img_path_1.isEmpty()) {
            Toast.makeText(this, "Image Required", Toast.LENGTH_SHORT).show();
        } else {
            registerUserToDB();
        }

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_dob.setText(sdf.format(myCalendar.getTime()));
    }

    private void get_all_department() {
        department_name_list.clear();
        department_code_list.clear();
        department_progress_bar.setVisibility(View.VISIBLE);

        ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterface.get_all_department();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String message = jsonObject.getString("responseType");
                        JSONObject responseMsg = jsonObject.optJSONObject("responseMsg");
                        String msg = responseMsg.optString("msg");

                        department_progress_bar.setVisibility(View.GONE);
                        if (message.equalsIgnoreCase("success")) {
                            if (msg.equalsIgnoreCase("Department list")) {
                                JSONArray result_array = jsonObject.getJSONArray("responseData");
                                for (int i = 0; i < result_array.length(); i++) {
                                    JSONObject object = result_array.getJSONObject(i);
                                    department_name_list.add(object.optString("dept_name"));
                                    department_code_list.add(object.optString("dept_id"));
                                }
                            } else {
                                department_progress_bar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            department_progress_bar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        department_progress_bar.setVisibility(View.GONE);
                        e.printStackTrace();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed2("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }
                } else if (response.code() == 404) {
                    department_progress_bar.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");
                } else {
                    department_progress_bar.setVisibility(View.GONE);
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                department_progress_bar.setVisibility(View.GONE);
                Log.i("Resp onFailure: ", "" + t.getMessage());
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else {
                    onFailed2("An unexpected error has occured.",
                            "Error Failure: " + t.getMessage());


                }
            }
        });

    }

    private void get_all_division(String departmentID) {
        progressDialog.show();
        division_name_list.clear();
        division_code_list.clear();
        ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterface.get_all_division(departmentID);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String responseType = jsonObject.getString("responseType");
                        JSONObject responseMsg = jsonObject.optJSONObject("responseMsg");
                        String msg = responseMsg.optString("msg");

                        progressDialog.dismiss();
                        if (responseType.equalsIgnoreCase("success")) {
                            if (msg.equalsIgnoreCase("Division details")) {
                                JSONArray result_array = jsonObject.getJSONArray("responseData");
                                for (int i = 0; i < result_array.length(); i++) {
                                    JSONObject object = result_array.getJSONObject(i);
                                    division_name_list.add(object.optString("div_name"));
                                    division_code_list.add(object.optString("div_id"));

                                }
                                division_layout.setVisibility(View.VISIBLE);

                            } else {

                                Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed2("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }
                } else if (response.code() == 404) {
                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");
                } else {
                    progressDialog.dismiss();
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("Resp onFailure: ", "" + t.getMessage());
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else {
                    onFailed2("An unexpected error has occured.",
                            "Error Failure: " + t.getMessage());


                }
            }
        });

    }


    private void onFailed2(String s, String s1) {
        Toast.makeText(this, "" + s1, Toast.LENGTH_SHORT).show();
    }


    private void get_all_location() {
        location_code_list.clear();
        location_name_list.clear();

        ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterface.all_working_location();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String responseType = jsonObject.getString("responseType");
                        JSONObject responseMsg = jsonObject.optJSONObject("responseMsg");
                        String msg = responseMsg.optString("msg");


                        if (responseType.equalsIgnoreCase("success")) {
                            if (msg.equalsIgnoreCase("Working locations")) {
                                JSONArray result_array = jsonObject.getJSONArray("responseData");
                                for (int i = 0; i < result_array.length(); i++) {
                                    JSONObject object = result_array.getJSONObject(i);
                                    location_name_list.add(object.optString("location_name"));
                                    location_code_list.add(object.optString("location_id"));
                                }

                            } else {

                                Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed2("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }
                } else if (response.code() == 404) {
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");
                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed2("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Resp onFailure: ", "" + t.getMessage());
                if (t.getMessage().startsWith("Unable to resolve host")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else if (t.getMessage().startsWith("timeout")) {
                    onFailed2("Slow or No Connection!",
                            "Check Your Network Settings & try again.");


                } else {
                    onFailed2("An unexpected error has occured.",
                            "Error Failure: " + t.getMessage());


                }
            }
        });

    }

}
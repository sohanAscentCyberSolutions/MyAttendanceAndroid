package com.gmda.attendance.employee_module;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;

import com.gmda.attendance.Activities.ApplyLeaveActivity;
import com.gmda.attendance.AddTeacherAttendance.Activity.StudentAttendanceActivity;
import com.gmda.attendance.AddTeacherAttendance.Activity.StudentAttendanceHistoryActivity;
import com.gmda.attendance.CommonClass.Constdata;
import com.gmda.attendance.CommonClass.LocationAssistant;
import com.gmda.attendance.CommonClass.SharedPreferencesHandler;
import com.gmda.attendance.CommonClass.TypeWriter;
import com.gmda.attendance.R;
import com.gmda.attendance.Retrofit.ApiInterFace;
import com.gmda.attendance.Retrofit.RetrofitClient;
import com.gmda.attendance.common_module.LoginActivity;
import com.gmda.attendance.services.LocationService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.common.util.concurrent.ListenableFuture;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationAssistant.Listener, NavigationView.OnNavigationItemSelectedListener {

    private static final int REQ_CODE = 1001;
    public static byte[] attendance_image_byte_array;
    private final Executor executor = Executors.newSingleThreadExecutor();
    public double latitudeUser = 0.0;
    public String status;
    protected LocationManager locationManager;
    // image capture dialog
    protected Context context;
    String Base64ImageUpload = "";
    ImageView logout_image_view;
    //static image path
    Dialog dialogSelfie;
    PreviewView mPreviewView;
    Button captureImage;
    EditText attendance_remarks_edit_text;
    ImageCapture imageCapture;
    double longitudeUser = 0.0;
    String locationAddress;
    CardView report_card_view_click, mark_student_attendance_card_view;
    TextView tv_userDate;
    TextView this_month_tv;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    CircleImageView imageView;
    Toolbar toolbar;
    CardView card_two;
    TextView tvLatitude;
    CircleImageView image_view_user, profile_image_user_header;
    TextView tvEmployeeName, tvDesignation, tvLocations, tvUser, profile_two_tv;
    String currentDate;
    TickerView tv_user_total_late, presentTicker, absentTicker, tv_user_holiday;
    String deviceID;
    SharedPreferencesHandler sc;
    TextView you_day_marked_successfully_tv;
    SharedPreferencesHandler s1;
    Animation shake;
    TypeWriter your_day_in_card_tv;
    private Geocoder geocoder;
    private TextView tvLocation;
    private LocationAssistant assistant;
    private ProgressDialog progressDialogs;
    private Button attendance_punch_in_day_btn, attendance_punch_out_day_btn;
    private CircleImageView check_in_circle, check_out_circle;
    private TextView punch_in_text_view, punch_out_text_view;
    private TextView punch_in_text_view_time, working_hour_tv, punch_out_text_view_time;
    private View view_view;
    private Dialog dialog_loading;
    private String classSelected;
    private ArrayList<String> classList = new ArrayList<>();
    private TextView versionName;
    private LocationRequest locationRequest;

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

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 1000, false);
        assistant.setVerbose(true);
        setContentView(R.layout.activity_main);
        init_location();
        initViews();
        navigationView = findViewById(R.id.nav_view);
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        sc = new SharedPreferencesHandler(MainActivity.this);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvLocation.setText(getString(R.string.empty));
        card_two = findViewById(R.id.card_two);
        this_month_tv = findViewById(R.id.this_month_tv);
        logout_image_view = findViewById(R.id.logout_image_view);
        tv_userDate = findViewById(R.id.tv_userDate);


        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);

        imageView = findViewById(R.id.imageView);
        presentTicker = findViewById(R.id.presentTicker);
        absentTicker = findViewById(R.id.absentTicker);
        tv_user_holiday = findViewById(R.id.tv_user_holiday);

        tv_user_total_late = findViewById(R.id.tv_user_total_lateTicker);


        s1 = new SharedPreferencesHandler(MainActivity.this);
        image_view_user = findViewById(R.id.image_view_user);
        tvEmployeeName = findViewById(R.id.profile_name);
        tvDesignation = findViewById(R.id.profile_designation);
        tvLocations = findViewById(R.id.profile_location);

        profile_image_user_header = findViewById(R.id.profile_image_user_header);
        //tvLongitude = findViewById(R.id.longitude);
        tvLatitude = findViewById(R.id.latitude);


        // get device id and device name
        //  deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //   Log.d(TAG, "onCreate:Device Id " + deviceID);
        //   String str = Build.MODEL;

        //    Log.d(TAG, "onCreate: " + Environment.DIRECTORY_PICTURES);

        //    Log.d(TAG, "onCreate: " + Build.HARDWARE);

        //     Toast.makeText(MainActivity.this, "Device Name" + str, Toast.LENGTH_SHORT).show();

        classList.add("7th A");
        classList.add("7th B");
        classList.add("8th A");
        classList.add("8th B");

        progressDialogs = new ProgressDialog(MainActivity.this);
        progressDialogs.setTitle(getString(R.string.app_name));
        progressDialogs.setMessage("Loading. Please wait.");
        progressDialogs.setCancelable(false);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.getNavigationIcon();
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);

        tvUser = headerview.findViewById(R.id.tvUser);
        profile_two_tv = headerview.findViewById(R.id.profile_two_tv);

        tvEmployeeName.setText(s1.DirectReadPreference(Constdata.emp_name, MainActivity.this));
        tvDesignation.setText(s1.DirectReadPreference(Constdata.designation, MainActivity.this));
        tvLocations.setText(s1.DirectReadPreference(Constdata.department, MainActivity.this));
        tvUser.setText(s1.DirectReadPreference(Constdata.emp_name, MainActivity.this));


        String user_image = s1.DirectReadPreference(Constdata.image, MainActivity.this);
        if (!Objects.equals("null", user_image)) {
            byte[] decodedString = Base64.decode(user_image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image_view_user.setImageBitmap(decodedByte);
        } else {
            image_view_user.setBackgroundDrawable(getDrawable(R.drawable.profiletwo));
        }


        profile_two_tv.setText(s1.DirectReadPreference(Constdata.designation, MainActivity.this));
        AllowPermissions();
        updateUserData();
        buttonStatus();


        attendance_punch_in_day_btn.setOnClickListener(view -> {
            if (latitudeUser == 0.0 && longitudeUser == 0.0) {
                Toast.makeText(MainActivity.this, "Wait While Accessing Your Location", Toast.LENGTH_SHORT).show();
            } else {
                showSelfieDialog("punchIn");
            }

        });

        attendance_punch_out_day_btn.setOnClickListener(view -> {
            if (latitudeUser == 0.0 && longitudeUser == 0.0) {
                Toast.makeText(MainActivity.this, "Wait While Accessing Your Location", Toast.LENGTH_SHORT).show();
            } else {
                showSelfieDialog("punchOut");
            }
            //  stopLocationService();
        });


        report_card_view_click.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, NewReportActivity.class));
        });

        mark_student_attendance_card_view.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, StudentAttendanceActivity.class));
        });


//        String emp_role= getIntent().getStringExtra("emp_role");
//        if(emp_role.equalsIgnoreCase("TEACHER")){
//            report_card_view_click.setVisibility(View.GONE);
//        }else{
//            mark_student_attendance_card_view.setVisibility(View.GONE);
//        }

        logout_image_view.setOnClickListener(view -> {
            sc.DirectClearPreferences(MainActivity.this);
        });


    }

    private void showSelfieDialog(String mode) {

        dialogSelfie = new Dialog(MainActivity.this);
        dialogSelfie.setContentView(R.layout.activity_custom_dialog);
        dialogSelfie.setCancelable(true);
        dialogSelfie.show();

        mPreviewView = dialogSelfie.findViewById(R.id.previewView);
        captureImage = dialogSelfie.findViewById(R.id.captureImg);
        attendance_remarks_edit_text = dialogSelfie.findViewById(R.id.attendance_remarks_edit_text);

        if (mode.equalsIgnoreCase("punchIn")) {
            captureImage.setText("Capture & PunchIn");
            attendance_remarks_edit_text.setVisibility(View.VISIBLE);
        } else {
            captureImage.setText("Capture & Punch Out");
            attendance_remarks_edit_text.setVisibility(View.GONE);
        }
        captureImage.setOnClickListener(v -> {
            captureImageFunction(mode);
        });
        startCamera();
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
        ImageCapture.Builder builder = new ImageCapture.Builder();
        imageCapture = builder.setTargetRotation(MainActivity.this.getWindowManager().getDefaultDisplay().getRotation()).build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle((LifecycleOwner) MainActivity.this, cameraSelector, preview, imageAnalysis, imageCapture);


    }

    private void captureImageFunction(String mode) {
        imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(ImageProxy imageProxy) {

                try {
                    InputStream imageStream = null;
                    try {
                        imageStream = MainActivity.this.getContentResolver().openInputStream(getImageUri(MainActivity.this, convertImageProxyToBitmap(imageProxy)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap convertedImage = getResizedBitmap(bmp, 256);
                    convertedImage.compress(Bitmap.CompressFormat.JPEG, Integer.parseInt(s1.DirectReadPreference("image_quality", MainActivity.this)), stream);

                    Base64ImageUpload = getEncoded64ImageStringFromBitmap(convertedImage);
                    imageProxy.close();
                    dialogSelfie.dismiss();
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          if (mode.equalsIgnoreCase("punchIn")) {
                                              punchIN();
                                          } else if (mode.equalsIgnoreCase("punchOut")) {
                                              punchOut();
                                          }
                                      }
                                  }
                    );

                    try {
                        attendance_image_byte_array = stream.toByteArray();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("img_stts e", "" + "Image Stts" + e.getMessage());
                }
            }

            @Override
            public void onError(ImageCaptureException exception) {

            }
        });
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


    private Bitmap convertImageProxyToBitmap(ImageProxy image) {
        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byteBuffer.rewind();
        byte[] bytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bytes);
        byte[] clonedBytes = bytes.clone();
        return BitmapFactory.decodeByteArray(clonedBytes, 0, clonedBytes.length);
    }

    private String getRealPathFromURI(Uri photoUri) {
        String result;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = MainActivity.this.getContentResolver().query(photoUri, projection, null, null, null);
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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }


    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(MainActivity.this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(MainActivity.this));
    }


    private void AllowPermissions() {
        String readImagePermission2;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            readImagePermission2 = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            readImagePermission2 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }

        int hasCAMERAPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int hasREAD_IMAGE_STORAGEPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), readImagePermission2);
        int hasACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int hasACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> permissions = new ArrayList<String>();
        if (hasCAMERAPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (hasREAD_IMAGE_STORAGEPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(readImagePermission2);
        }
        if (hasACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (hasACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 101);
        }

    }


    private void initViews() {
        view_view = findViewById(R.id.view_view);
        your_day_in_card_tv = findViewById(R.id.your_day_in_card_tv);
        report_card_view_click = findViewById(R.id.report_card_view_click);
        mark_student_attendance_card_view = findViewById(R.id.mark_student_attendance_card_view);
        attendance_punch_in_day_btn = findViewById(R.id.attendance_punch_in_day_btn);
        attendance_punch_out_day_btn = findViewById(R.id.attendance_punch_out_day_btn);
        you_day_marked_successfully_tv = findViewById(R.id.you_day_marked_successfully_tv);
        check_in_circle = findViewById(R.id.img_selectfrom);
        check_out_circle = findViewById(R.id.check_out_circle);
        punch_in_text_view = findViewById(R.id.punch_in_text_view);
        punch_out_text_view = findViewById(R.id.punch_out_text_view);
        punch_in_text_view_time = findViewById(R.id.punch_in_time_tv);
        working_hour_tv = findViewById(R.id.working_hours_tv);
        punch_out_text_view_time = findViewById(R.id.punch_out_time_tv);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        your_day_in_card_tv.setCharacterDelay(100);
        your_day_in_card_tv.animateText("Welcome Mark You Day");

        dialog_loading = new Dialog(MainActivity.this);
        dialog_loading.setContentView(R.layout.loading_dailog);
        dialog_loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void buttonStatus() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDate = formatter.format(date);
        Log.d(TAG, "buttonStatus: " + currentDate);
        ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Call<ResponseBody> call = apiInterFace.read_employee_attendance_by_date(
                "Bearer " + sc.DirectReadPreference(Constdata.token, MainActivity.this),
                currentDate, sc.DirectReadPreference(Constdata.emp_id, MainActivity.this));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");
                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");
                        Log.d(TAG, "onResponse Manish: " + jsonObject);
                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("responseData");
                            JSONObject jObject = jsonArray.getJSONObject(0);
                            manage_button_state(jObject.optString("punch_in"), jObject.optString("punch_out"), jObject.optString("working_hr"),
                                    jObject.optString("punchin_image"), jObject.optString("punchout_image")
                            );

                        } else {
                            manage_button_state("null", "null", "null", "null", "null");

                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {

                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());

                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    private void manage_button_state(String punch_in, String punch_out, String working_hour, String punch_in_image, String punchout_image) {
        Log.d(TAG, "manage_button_state: " + punch_in + punch_out);
        working_hour_tv.setText("Working Hours: " + working_hour);
        if (Objects.equals("null", punch_in)) {
            punch_in_text_view_time.setText("-----");
            punch_out_text_view_time.setText("------");
            attendance_punch_in_day_btn.startAnimation(shake);
            you_day_marked_successfully_tv.setVisibility(View.GONE);
            attendance_punch_in_day_btn.setVisibility(View.VISIBLE);
            attendance_punch_out_day_btn.setVisibility(View.GONE);
        } else {
            String sp = punch_in.substring(punch_in.indexOf("T") + 1, punch_in.indexOf("."));
            Log.d(TAG, "manage_button_state: " + punch_in + punch_out);
            if (Objects.equals("null", punch_out)) {

                if (!Objects.equals("null", punch_in_image)) {
                    byte[] decodedString = Base64.decode(punch_in_image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    check_in_circle.setImageBitmap(decodedByte);
                }
                check_out_circle.startAnimation(shake);
                view_view.setBackgroundColor(getResources().getColor(R.color.bgColor));
                punch_in_text_view_time.setText(sp);
                punch_out_text_view_time.setText("------");
                attendance_punch_out_day_btn.startAnimation(shake);
                you_day_marked_successfully_tv.setVisibility(View.GONE);
                attendance_punch_in_day_btn.setVisibility(View.GONE);
                attendance_punch_out_day_btn.setVisibility(View.VISIBLE);
                //    startLocationService();
            } else {

                if (!Objects.equals("null", punch_in_image)) {
                    byte[] decodedString = Base64.decode(punch_in_image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    check_in_circle.setImageBitmap(decodedByte);
                }

                if (!Objects.equals("null", punchout_image)) {
                    byte[] decodedString = Base64.decode(punchout_image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    check_out_circle.setImageBitmap(decodedByte);
                }


                view_view.setBackgroundColor(getResources().getColor(R.color.bgColor));
                String s2 = punch_out.substring(punch_out.indexOf("T") + 1, punch_out.indexOf("."));
                punch_in_text_view_time.setText(sp);
                punch_out_text_view_time.setText(s2);
                you_day_marked_successfully_tv.startAnimation(shake);
                you_day_marked_successfully_tv.setVisibility(View.VISIBLE);
                attendance_punch_in_day_btn.setVisibility(View.GONE);
                attendance_punch_out_day_btn.setVisibility(View.GONE);
                //   stopLocationService();
            }
        }

    }

    private void showMessage(String api_message) {
        Toast.makeText(MainActivity.this, "" + api_message, Toast.LENGTH_SHORT).show();
    }

    private void onFailed(String s, String s1) {
        Toast.makeText(MainActivity.this, "" + s1, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUserData() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        SharedPreferencesHandler sc = new SharedPreferencesHandler(MainActivity.this);
        ApiInterFace apiInterFace = RetrofitClient.getApiClient().create(ApiInterFace.class);
        Log.d(TAG, "updateUserData:Token " + sc.DirectReadPreference(Constdata.token, MainActivity.this));
        Call<ResponseBody> calls = apiInterFace.
                get_user_monthly_details_by_api("Bearer " + sc.DirectReadPreference(Constdata.token, MainActivity.this),
                        String.valueOf(month + 1),
                        String.valueOf(year),
                        sc.DirectReadPreference(Constdata.emp_id, MainActivity.this));


        Log.d(TAG, "updateUserData: " + month + 1);
        Log.d(TAG, "updateUserData: " + year);
        Log.d(TAG, "updateUserData: " + sc.DirectReadPreference(Constdata.emp_id, MainActivity.this));
        Log.d(TAG, "updateUserData: " + sc.DirectReadPreference(Constdata.token, MainActivity.this));
        calls.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String api_result = jsonObject.getString("responseType");
                        Log.d(TAG, "onResponse:Monthly Data " + jsonObject);
                        JSONObject jsonObjectMessage = jsonObject.getJSONObject("responseMsg");

                        String api_message = jsonObjectMessage.getString("msg");
                        if (api_result.equals("success")) {
                            if (api_message.equals("Data fetch successfully...!")) {

                                JSONArray result_data = jsonObject.getJSONArray("responseData");
                                int present_counter = 0;
                                int absent_counter = 0;
                                int late_counter = 0;
                                if (result_data.length() > 0) {
                                    for (int i = 0; i < result_data.length(); i++) {
                                        Log.d(TAG, "onResponse: for loop");
                                        JSONObject jsonObject1 = result_data.getJSONObject(i);
                                        if (jsonObject1.getString("a_status").equals("A")) {
                                            absent_counter++;
                                        } else if (jsonObject1.getString("a_status").equals("LATE")) {
                                            late_counter++;
                                        } else if (jsonObject1.getString("a_status").equals("P")) {
                                            present_counter++;
                                        }
                                    }
                                }
                                update_user_monthly_dashboard(present_counter, absent_counter, late_counter);
                            } else {

                                showMessage(api_message);
                            }

                        } else {
                            showMessage(api_message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Resp Exc: ", e.getMessage() + "");
                        onFailed("An unexpected error has occured.",
                                "Error: " + e.getMessage() + "\n" +
                                        "Please Try Again later ");
                    }


                } else if (response.code() == 404) {
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");


                } else {
                    Log.i("Resp Exc: ", "" + response.code());
                    onFailed("An unexpected error has occured.",
                            "Error Code: " + response.code() + "\n" +
                                    "Please Try Again later ");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

    private void update_user_monthly_dashboard(int present, int absent, int late) {
        presentTicker.setText(String.valueOf(present));
        absentTicker.setText(String.valueOf(absent));
        tv_user_total_late.setText(String.valueOf(late));
    }


    private void punchIN() {
        if (latitudeUser != 0.0 && longitudeUser != 0.0) {
            showProgressbar();
            ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);
            String remarks = attendance_remarks_edit_text.getText().toString().isEmpty() ? "Regular Work" : attendance_remarks_edit_text.getText().toString();
            Call<ResponseBody> call = apiInterface.punchInAttendanceWithImage(
                    "Bearer " + sc.DirectReadPreference(Constdata.token, MainActivity.this),
                    String.valueOf(latitudeUser),
                    String.valueOf(longitudeUser),
                    sc.DirectReadPreference("emp_id", MainActivity.this),
                    locationAddress,
                    Base64ImageUpload, remarks);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String result = response.body().string();
                            JSONObject jsonObject = new JSONObject(result);
                            hideProgressbar();
                            JSONObject responseMsg = jsonObject.optJSONObject("responseMsg");
                            String msgToShow = responseMsg.optString("msg");
                            String message = jsonObject.optString("responseType");
                            showMessage(msgToShow);
                            if (message.equalsIgnoreCase("success")) {
                                updateUserData();
                                //  startLocationService();
                                buttonStatus();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == 404) {
                        hideProgressbar();
                        Log.i("Resp Exc: ", "" + response.code());
                        onFailed("An unexpected error has occured.",
                                "Error Code: " + response.code() + "\n" +
                                        "Please Try Again later ");

                    } else {
                        Log.i("Resp Exc: ", "" + response.code());
                        hideProgressbar();
                        onFailed("An unexpected error has occured.",
                                "Error Code: " + response.code() + "\n" +
                                        "Please Try Again later ");

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressbar();
                    Log.d(TAG, "onFailure: " + t.getMessage());
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

        } else {
            Toast.makeText(MainActivity.this, "Check Your Gps And Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void punchOut() {
        if (latitudeUser != 0.0 && longitudeUser != 0.0) {
            showProgressbar();
            ApiInterFace apiInterface = RetrofitClient.getApiClient().create(ApiInterFace.class);

            Call<ResponseBody> call = apiInterface.punchOutAttendanceWithImage(
                    "Bearer " + sc.DirectReadPreference(Constdata.token, MainActivity.this),
                    String.valueOf(latitudeUser),
                    String.valueOf(longitudeUser),
                    sc.DirectReadPreference("emp_id", MainActivity.this),
                    locationAddress,
                    Base64ImageUpload
            );
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            hideProgressbar();
                            String result = response.body().string();
                            JSONObject jsonObject = new JSONObject(result);
                            Log.d(TAG, "onResponse: " + jsonObject);
                            JSONObject responseMsg = jsonObject.optJSONObject("responseMsg");
                            String msgToShow = responseMsg.optString("msg");
                            String message = jsonObject.optString("responseType");
                            showMessage(msgToShow);
                            if (message.equalsIgnoreCase("success")) {
                                updateUserData();
                                buttonStatus();
                                //   stopLocationService();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == 404) {
                        hideProgressbar();
                        Log.i("Resp Exc: ", "" + response.code());
                        onFailed("An unexpected error has occured.",
                                "Error Code: " + response.code() + "\n" +
                                        "Please Try Again later ");
                    } else {
                        Log.i("Resp Exc: ", "" + response.code());
                        hideProgressbar();
                        onFailed("An unexpected error has occured.",
                                "Error Code: " + response.code() + "\n" +
                                        "Please Try Again later ");

                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressbar();
                    Log.d(TAG, "onFailure: " + t.getMessage());
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

        } else {
            Toast.makeText(MainActivity.this, "Check Your Gps And Try Again", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation();
        assistant.start();
    }

    @Override
    protected void onPause() {
        assistant.stop();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (assistant.onPermissionsUpdated(requestCode, grantResults))
            tvLocation.setOnClickListener(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assistant.onActivityResult(requestCode, resultCode);
    }

    @Override
    public void onNeedLocationPermission() {
        Log.d(TAG, "onNeedLocationPermission: ");
        tvLocation.setText("Need\nPermission");
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assistant.requestLocationPermission();
            }
        });
        assistant.requestAndPossiblyExplainLocationPermission();
    }

    @Override
    public void onExplainLocationPermission() {
        Log.d(TAG, "onExplainLocationPermission: ");
        new AlertDialog.Builder(this)
                .setMessage(R.string.permissionExplanation)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        assistant.requestLocationPermission();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        tvLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                assistant.requestLocationPermission();
                            }
                        });
                    }
                })
                .show();
    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView,
                                                        DialogInterface.OnClickListener fromDialog) {
        Log.d(TAG, "onLocationPermissionPermanentlyDeclined: ");
        new AlertDialog.Builder(this)
                .setMessage(R.string.permissionPermanentlyDeclined)
                .setPositiveButton(R.string.ok, fromDialog)
                .show();
    }

    @Override
    public void onNeedLocationSettingsChange() {
        Log.d(TAG, "onNeedLocationSettingsChange: ");
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.fake_gps_layout);
        dialog.setCancelable(false);
        LinearLayout linearLayout = dialog.findViewById(R.id.ll_exit);
        linearLayout.setOnClickListener(view -> {
            dialog.dismiss();
            System.exit(1);
        });
        dialog.show();
    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener
                                                   fromView, DialogInterface.OnClickListener fromDialog) {
        Log.d(TAG, "onFallBackToSystemSettings: ");
        new AlertDialog.Builder(this)
                .setMessage(R.string.switchOnLocationLong)
                .setPositiveButton(R.string.ok, fromDialog)
                .show();
    }

    @Override
    public void onNewLocationAvailable(Location location) {
        Log.d(TAG, "onNewLocationAvailable: ");
        if (location == null) return;
        tvLocation.setOnClickListener(null);

        latitudeUser = location.getLatitude();
        longitudeUser = location.getLongitude();
        try {
            List<Address> addressList = geocoder.getFromLocation(latitudeUser, longitudeUser, 1);
            if (addressList.isEmpty()) {
                locationAddress = "Location Not GeoCode";
            } else {
                locationAddress = (addressList.get(0).getAddressLine(0)) + (addressList.get(0).getLocality());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        tvLatitude.setText(location.getLongitude() + "\n" + location.getLatitude());
        tvLocation.setAlpha(1.0f);
        tvLocation.animate().alpha(0.5f).setDuration(400);
    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener
                                                fromView, DialogInterface.OnClickListener fromDialog) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.fake_gps_layout);

        dialog.setCancelable(false);
        LinearLayout linearLayout = dialog.findViewById(R.id.ll_exit);
        linearLayout.setOnClickListener(view -> {
            dialog.dismiss();
            System.exit(1);
        });
        dialog.show();
    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {
        Log.d(TAG, "onError: ");
        tvLocation.setText(getString(R.string.error));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_menu:
                startActivity(new Intent(MainActivity.this, User_Detail_Activity.class));
                break;
            case R.id.dashboard_menu:
                startActivity(new Intent(MainActivity.this, NewReportActivity.class));
                break;
            case R.id.class_report:
                classReportFunction();
                break;
            case R.id.logout_menu:
                sc.DirectClearPreferences(MainActivity.this);
                break;
            case R.id.apply_leave:
                startActivity(new Intent(MainActivity.this, ApplyLeaveActivity.class));
                break;
            case R.id.about_us_menu:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://onemapggm.gmda.gov.in/"));
                startActivity(browserIntent);
                break;
        }
        return false;
    }

    private void classReportFunction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayList<Integer> itemsSelected = new ArrayList();
        builder.setTitle("Select class from the list below:-")
                .setCancelable(false)
                .setSingleChoiceItems(classList.toArray(new String[0]), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemsSelected.add(which);
                    }
                })
                .setPositiveButton("Done!", (dialog, id) -> {

                    if (itemsSelected.isEmpty()) {
                        Toast.makeText(context, "Select class", Toast.LENGTH_SHORT).show();

                    } else {
                        for (int i : itemsSelected) {
                            classSelected = String.valueOf(classList.get(i));
                        }

                        Log.d(TAG, "onClick string: " + classSelected);
                        Intent dataTransfer = new Intent(this, StudentAttendanceHistoryActivity.class);
                        dataTransfer.putExtra("classSelected", classSelected);
                        startActivity(dataTransfer);

                    }

                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        builder.create().show();

    }


    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equalsIgnoreCase(serviceInfo.service.getClassName())) {
                    if (serviceInfo.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }


    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(MainActivity.this, LocationService.class);
            intent.setAction("location_start");
            startService(intent);

        }
    }


    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction("location_stop");
            startService(intent);

        }
    }


    public void showProgressbar() {
        dialog_loading.show();
        dialog_loading.setCancelable(false);
    }


    public void hideProgressbar() {
        dialog_loading.dismiss();
    }


    private void init_location() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2500);
        locationRequest.setFastestInterval(2000);
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isEnabled;

    }


    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
            } else {
                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        latitudeUser = locationResult.getLocations().get(index).getLatitude();
                                        longitudeUser = locationResult.getLocations().get(index).getLongitude();
                                        try {
                                            List<Address> addressList = geocoder.getFromLocation(latitudeUser, longitudeUser, 1);
                                            if (addressList.isEmpty()) {
                                                locationAddress = "Location Not GeoCode";
                                            } else {
                                                locationAddress = (addressList.get(0).getAddressLine(0)) + (addressList.get(0).getLocality());
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        Toast.makeText(MainActivity.this, "Location seems null", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    Toast.makeText(MainActivity.this, "Location seems disabled, Enable GPS.", Toast.LENGTH_SHORT).show();
                    turnOnGPS();
                }

            }
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    public static class ForgetPasswordActivity extends AppCompatActivity {
        EditText enternumber, newpassword, confirmnewpassword;
        Button sendotp, verifyotp, submit;
        LinearLayout numberlayout, otplayout, newpasswordlayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forget_password);

            numberlayout = findViewById(R.id.numberLayout);
            otplayout = findViewById(R.id.otpLayout);
            newpasswordlayout = findViewById(R.id.newPasswordLayout);


            enternumber = findViewById(R.id.enterNumbertext);
            newpassword = findViewById(R.id.newPasswordtext);
            confirmnewpassword = findViewById(R.id.confirmNewPasswordtext);

            sendotp = findViewById(R.id.sendotpbtn);
            verifyotp = findViewById(R.id.verifyotpbtn);
            submit = findViewById(R.id.submitPasswordbtn);

            sendotp.setOnClickListener(view -> {
                numberlayout.setVisibility(View.GONE);
                otplayout.setVisibility(View.VISIBLE);
            });
            verifyotp.setOnClickListener(view -> {
                numberlayout.setVisibility(View.GONE);
                otplayout.setVisibility(View.GONE);
                newpasswordlayout.setVisibility(View.VISIBLE);
            });
            submit.setOnClickListener(view -> {

                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                Toast.makeText(this, "Password is updated successfully!!!!", Toast.LENGTH_SHORT).show();

            });

        }
    }


}

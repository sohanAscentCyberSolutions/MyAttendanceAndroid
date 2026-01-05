package com.gmda.attendance.Retrofit;


import com.gmda.attendance.Models.AddImages;
import com.gmda.attendance.Models.ChangePassword;
import com.gmda.attendance.Models.CheckVersion;
import com.gmda.attendance.Models.CreateUser;
import com.gmda.attendance.Models.DateCalnderDate;
import com.gmda.attendance.Models.DepartmentModel;
import com.gmda.attendance.Models.DivTodayAttendence;
import com.gmda.attendance.Models.DivisionModel;
import com.gmda.attendance.Models.DurationRecord;
import com.gmda.attendance.Models.LastMonthAttendanceId;
import com.gmda.attendance.Models.LogOut;
import com.gmda.attendance.Models.ReadAttendance;
import com.gmda.attendance.Models.ReadAttendanceById;
import com.gmda.attendance.Models.ReadAttendanceDate;
import com.gmda.attendance.Models.ReadTotalData;
import com.gmda.attendance.Models.TodayAttendanceById;
import com.gmda.attendance.Models.TotalAttendanceToday;
import com.gmda.attendance.Models.UserDateWiseData;
import com.gmda.attendance.Models.UserModelAttendance;
import com.gmda.attendance.Models.UserMonthModel;
import com.gmda.attendance.Models.WorkLocation;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterFace {

    @FormUrlEncoded
    @POST("loginUser")
    Call<ResponseBody> login_user_api(
            @Field("emp_id") String emp_id,
            @Field("emp_pwd") String emp_pwd
    );


    @GET("department/list")
    Call<ResponseBody> get_departments(
            @Header("Authorization") String authorization
    );


    @GET("department/divisions/{id}")
    public Call<ResponseBody> get_division(@Path("id") int id,
                                           @Header("Authorization") String authorization
    );


    @GET("working-location")
    Call<ResponseBody> get_workinglocation(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("v2/createUser")
    Call<CreateUser> createUser(
            @Header("Authorization") String authorization,
            @Field("emp_name") String emp_name,
            @Field("dob") String dob,
            @Field("mob_no") String mob_no,
            @Field("division") String division,
            @Field("department") String department,
            @Field("designation") String designation,
            @Field("emp_level") String emp_level,
            @Field("work_location") String work_location,
            @Field("emp_role") String emp_role,
            @Field("emp_pwd") String emp_pwd,
            @Field("emp_img") String emp_image

    );

    @GET("v2/todayAttendenceRecord")
    Call<TotalAttendanceToday> getTotalAttendanceToday(
            @Header("Authorization") String authorization
    );

    @Multipart
    @POST("v2/add_images")
    Call<AddImages> uploadImage(
            @Part MultipartBody.Part attachment
    );


    @FormUrlEncoded
    @POST("v2/punchInAttendence")
    Call<ResponseBody> getPunchIn(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id,
            @Field("punchin_lat") String punchin_lat,
            @Field("punchin_long") String punchin_long
    );


    @FormUrlEncoded
    @PUT("v2/punchOutAttendence")
    Call<ResponseBody> getPunchOut(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id,
            @Field("punchout_lat") String punchout_lat,
            @Field("punchout_long") String punchout_long
    );

    @GET("v2/readAttendance")
    Call<ReadAttendance> getAttendance(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("v2/readAttendencebyId")
    Call<ReadAttendanceById> getAttendanceById(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id);


    @FormUrlEncoded
    @POST("v2/readAttendenceByDate")
    Call<ReadAttendanceDate> getAttendanceByDate(
            @Header("Authorization") String authorization,
            @Field("a_date") String a_date
    );


    @FormUrlEncoded
    @POST("v2/lastMonthAttendenceById")
    Call<LastMonthAttendanceId> getLastMonthAttendanceId(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id

    );


    @FormUrlEncoded
    @PUT("v2/changePassword")
    Call<ChangePassword> getChangePassword(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id,
            @Field("emp_pwd") String emp_pwd,
            @Field("new_password") String new_password
    );

//    @FormUrlEncoded
//    @PUT("updatePassword")
//    Call<UpdatePassword> getUpdatePassword(
//            @Field("emp_id") String emp_id,
//            @Field("new_password") String new_password
//    );

    @GET("v2/logoutUser")
    Call<LogOut> userLogout(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("v2/divtodayAttendenceRecord")
    Call<DivTodayAttendence> getTodayAttendanceRecord(
            @Header("Authorization") String authorization,
            @Field("department") String department,
            @Field("division") String division
    );

    @FormUrlEncoded
    @POST("v2/readAttendenceByDuration")
    Call<DurationRecord> getDurationAttendanceRecord(
            @Header("Authorization") String authorization,
            @Field("sdate") String sdate,
            @Field("edate") String edate,
            @Field("department") String department,
            @Field("division") String division
    );

    @FormUrlEncoded
    @POST("v2/readAttendenceByDuration")
    Call<DurationRecord> getDateWiseData(
            @Header("Authorization") String authorization,
            @Field("sdate") String sdate,
            @Field("mode") String mode,
            @Field("department") String department,
            @Field("division") String division
    );


    @FormUrlEncoded
    @POST("v2/divDateAttendenceRecord")
    Call<DateCalnderDate> getDateWiseAttendance(
            @Header("Authorization") String authorization,
            @Field("department") String department,
            @Field("division") String division,
            @Field("a_date") String a_date
    );

    @FormUrlEncoded
    @POST("oneauth_v8/query/app_versioning/read")
    Call<CheckVersion> checkVersion(
            @Field("app_name") String app_name,
            @Field("version") int version
    );

    @GET("v2/getDivisionName")
    Call<DivisionModel> getDivision(
    );

    @GET("v2/getDepartmentName")
    Call<DepartmentModel> getDepartment(
    );

    @GET("v2/getWorkingLocation")
    Call<WorkLocation> getWorkLocation(

    );

    @FormUrlEncoded
    @POST("v2/readTodayAttendenceById")
    Call<TodayAttendanceById> getTodayUserIdAtt(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id
    );


    @FormUrlEncoded
    @POST("v2/getEmployeeAttendanceList")
    Call<UserDateWiseData> getUserAttendanceDuration(
            @Header("Authorization") String authorization,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date
    );

    @FormUrlEncoded
    @POST("v2/getEmployeeAttendanceList")
    Call<UserDateWiseData> getUserAttendanceSingleDate(
            @Header("Authorization") String authorization,
            @Field("from_date") String from_date

    );


    @FormUrlEncoded
    @POST("v2/getEmployeeAttendanceList")
    Call<ResponseBody> get_user_attendance_by_date_api(
            @Header("Authorization") String authorization,
            @Field("from_date") String from_date
    );

    @FormUrlEncoded
    @POST("v2/readAlluser")
    Call<ReadTotalData> getTotalEmp(
            @Header("Authorization") String authorization,
            @Field("department") String department,
            @Field("division") String division
    );


    @FormUrlEncoded
    @POST("v2/getMonthlyAttendanceReport")
    Call<UserModelAttendance> getAttendanceMonthAdmin(
            @Header("Authorization") String authorization,
            @Field("month") String month,
            @Field("year") String year
    );

    @FormUrlEncoded
    @POST("v2/getMonthlyAttendanceDetail")
    Call<UserMonthModel> getAttendanceMonthUser(
            @Header("Authorization") String authorization,
            @Field("month") String month,
            @Field("year") String year,
            @Field("emp_id") String emp_id

    );


    @FormUrlEncoded
    @POST("v2/send_otp_nonuser")
    Call<ResponseBody> sentOtp(
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("v2/verify_otp_nonuser")
    Call<ResponseBody> verifyOtp(
            @Field("mobile") String mobile,
            @Field("otp") String otp

    );

    @FormUrlEncoded
    @POST("v2/readAttendencebyId")
    Call<ResponseBody> fetch_monthly_attendance_by_id(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id);


    @FormUrlEncoded
    @POST("v2/readAttendencebyId")
    Call<ResponseBody> get_all_attendance_by_id(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id);


    @FormUrlEncoded
    @POST("v2/divtodayAttendenceRecord")
    Call<ResponseBody> get_today_attendance_of_division_wise(
            @Header("Authorization") String authorization,
            @Field("department") String department,
            @Field("division") String division
    );


    @FormUrlEncoded
    @POST("v2/readAlluser")
    Call<ResponseBody> fetch_all_user_by_api(
            @Header("Authorization") String authorization,
            @Field("department") String department,
            @Field("division") String division
    );


    @FormUrlEncoded
    @POST("developer_services/data/ReadAppInfoData")
    Call<ResponseBody> check_app_version(
            @Header("Authorization") String token,
            @Field("app_name") String app_name);


    @FormUrlEncoded
    @POST("v2/update_password")
    Call<ResponseBody> update_password(
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("v2/checkUserExist")
    Call<ResponseBody> check_user_exist(
            @Field("mobile") String mobile);


    //New Interface for User Register Module

    @GET("getDepartmentName")
    Call<ResponseBody> get_all_department(
    );

    @GET("getWorkingLocation")
    Call<ResponseBody> all_working_location(
    );

    @FormUrlEncoded
    @POST("getDivisionName")
    Call<ResponseBody> get_all_division(
            @Field("dept_id") String dept_id
    );


    @Multipart
    @POST("registerUser")
    Call<ResponseBody> registerUserMultiPart(
            @Part("emp_id") RequestBody emp_id,
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile,
            @Part("work_location_code") RequestBody work_location_code,
            @Part("dept_id") RequestBody dept_id,
            @Part("div_id") RequestBody div_id,
            @Part("designation") RequestBody designation,
            @Part("password") RequestBody password,
            @Part("dob") RequestBody dob,
            @Part MultipartBody.Part image_normal_1
    );

    @FormUrlEncoded
    @POST("registerUser")
    Call<ResponseBody> registerUser(
            @Field("emp_id") String emp_id,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("work_location_code") String work_location_code,
            @Field("dept_id") String dept_id,
            @Field("div_id") String div_id,
            @Field("designation") String designation,
            @Field("password") String password,
            @Field("dob") String dob,
            @Field("image") String image,
            @Field("email_id") String email

    );

    @FormUrlEncoded
    @POST("loginUser")
    Call<ResponseBody> login_user_api_new(
            @Field("emp_id") String emp_id,
            @Field("emp_pwd") String emp_pwd
    );

    @Multipart
    @POST("punchInAttendence")
    Call<ResponseBody> punchInAttendanceWithImage_Multipart(
            @Header("Authorization") String authorization,
            @Part("punchin_latitude") RequestBody punchin_latitude,
            @Part("punchin_longitude") RequestBody punchin_longitude,
            @Part("emp_id") RequestBody emp_id,
            @Part("punchin_address") RequestBody address,
            @Part MultipartBody.Part image_normal_1
    );

    @FormUrlEncoded
    @POST("punchInAttendence")
    Call<ResponseBody> punchInAttendanceWithImage(
            @Header("Authorization") String authorization,
            @Field("punchin_latitude") String punchin_latitude,
            @Field("punchin_longitude") String punchin_longitude,
            @Field("emp_id") String emp_id,
            @Field("punchin_address") String address,
            @Field("punchin_image") String image,
            @Field("remarks") String remarks

    );

    @Multipart
    @PUT("punchOutAttendence")
    Call<ResponseBody> punchOutAttendanceWithImageMultipart(
            @Header("Authorization") String authorization,
            @Part("punchout_latitude") RequestBody punchin_latitude,
            @Part("punchout_longitude") RequestBody punchin_longitude,
            @Part("emp_id") RequestBody emp_id,
            @Part("punchout_address") RequestBody address,
            @Part MultipartBody.Part image_normal_1
    );


    @FormUrlEncoded
    @PUT("punchOutAttendence")
    Call<ResponseBody> punchOutAttendanceWithImage(
            @Header("Authorization") String authorization,
            @Field("punchout_latitude") String punchin_latitude,
            @Field("punchout_longitude") String punchin_longitude,
            @Field("emp_id") String emp_id,
            @Field("punchout_address") String address,
            @Field("punchout_image") String image
    );


    @FormUrlEncoded
    @POST("employeeAttendenceByDate")
    Call<ResponseBody> read_employee_attendance_by_date(
            @Header("Authorization") String authorization,
            @Field("attendance_date") String attendance_date,
            @Field("emp_id") String emp_id
    );

    @FormUrlEncoded
    @POST("getMonthlyAttendanceDetail")
    Call<ResponseBody> get_user_monthly_details_by_api(
            @Header("Authorization") String authorization,
            @Field("month") String month,
            @Field("year") String year,
            @Field("emp_id") String emp_id
    );


    @FormUrlEncoded
    @POST("employeeAllAttendance")
    Call<ResponseBody> fetch_employee_all_attendance(
            @Header("Authorization") String authorization,
            @Field("emp_id") String emp_id);


    @FormUrlEncoded
    @POST("checkUserExist")
    Call<ResponseBody> checkUserExist(
            @Field("mobile") String mobile);

    //depts
    @FormUrlEncoded
    @POST("smsapi/query/send_otp_nonuser")
    Call<ResponseBody> sendOtpToUserNew(
            @Field("mobile") String mobile_number
    );


    @FormUrlEncoded
    @POST("smsapi/query/verify_otp_nonuser")
    Call<ResponseBody> VerifyOtpNew(
            @Field("mobile") String mobile_number,
            @Field("otp") String otp
    );


    @FormUrlEncoded
    @PUT("changePassword")
    Call<ResponseBody> ChangeUserPasswordNew(
            @Field("emp_id") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("getDesignationName")
    Call<ResponseBody> getDesignationName(
            @Field("dept_id") String dept_id,
            @Field("div_id") String div_id
    );
}
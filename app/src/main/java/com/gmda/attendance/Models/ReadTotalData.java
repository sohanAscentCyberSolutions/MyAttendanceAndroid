package com.gmda.attendance.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ReadTotalData{

	@SerializedName("responseType")
	private String responseType;

	@SerializedName("responseData")
	private List<ResponseDataItem> responseData;

	@SerializedName("responseMsg")
	private ResponseMsg responseMsg;

	public String getResponseType(){
		return responseType;
	}

	public List<ResponseDataItem> getResponseData(){
		return responseData;
	}

	public ResponseMsg getResponseMsg(){
		return responseMsg;
	}
	public class ResponseMsg{

		@SerializedName("msg")
		private String msg;

		public String getMsg(){
			return msg;
		}
	}


	public class ResponseDataItem{

		@SerializedName("emp_role")
		private String empRole;

		@SerializedName("emp_name")
		private String empName;

		@SerializedName("mob_no")
		private String mobNo;

		@SerializedName("emp_pwd")
		private String empPwd;

		@SerializedName("otp")
		private String otp;

		@SerializedName("modified_date")
		private String modifiedDate;

		@SerializedName("created_by")
		private String createdBy;

		@SerializedName("work_location")
		private String workLocation;

		@SerializedName("token")
		private String token;

		@SerializedName("division")
		private String division;

		@SerializedName("login_status")
		private String loginStatus;

		@SerializedName("emp_image")
		private String empImage;

		@SerializedName("dob")
		private String dob;

		@SerializedName("imei_no")
		private String imeiNo;

		@SerializedName("modified_by")
		private String modifiedBy;

		@SerializedName("emp_level")
		private String empLevel;

		@SerializedName("designation")
		private String designation;

		@SerializedName("created_date")
		private String createdDate;

		@SerializedName("department")
		private String department;

		@SerializedName("objectid")
		private int objectid;

		@SerializedName("emp_id")
		private String empId;

		@SerializedName("status")
		private String status;

		public String getEmpRole(){
			return empRole;
		}

		public String getEmpName(){
			return empName;
		}

		public String getMobNo(){
			return mobNo;
		}

		public String getEmpPwd(){
			return empPwd;
		}

		public String getOtp(){
			return otp;
		}

		public String getModifiedDate(){
			return modifiedDate;
		}

		public String getCreatedBy(){
			return createdBy;
		}

		public String getWorkLocation(){
			return workLocation;
		}

		public String getToken(){
			return token;
		}

		public String getDivision(){
			return division;
		}

		public String getLoginStatus(){
			return loginStatus;
		}

		public String getEmpImage(){
			return empImage;
		}

		public String getDob(){
			return dob;
		}

		public String getImeiNo(){
			return imeiNo;
		}

		public String getModifiedBy(){
			return modifiedBy;
		}

		public String getEmpLevel(){
			return empLevel;
		}

		public String getDesignation(){
			return designation;
		}

		public String getCreatedDate(){
			return createdDate;
		}

		public String getDepartment(){
			return department;
		}

		public int getObjectid(){
			return objectid;
		}

		public String getEmpId(){
			return empId;
		}

		public String getStatus(){
			return status;
		}
	}
}
package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

public class ChangePassword{

	@SerializedName("responseType")
	private String responseType;

/*	@SerializedName("responseData")
	private ResponseData responseData*/;

	@SerializedName("responseMsg")
	private ResponseMsg responseMsg;

	public String getResponseType(){
		return responseType;
	}

	/*public ResponseData getResponseData(){
		return responseData;
	}*/

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



/*
	public class ResponseData{

		@SerializedName("emp_role")
		private String empRole;

		@SerializedName("emp_name")
		private String empName;

		@SerializedName("mob_no")
		private Object mobNo;

		@SerializedName("emp_pwd")
		private String empPwd;

		@SerializedName("otp")
		private Object otp;

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
		private Object empImage;

		@SerializedName("dob")
		private Object dob;

		@SerializedName("imei_no")
		private String imeiNo;

		@SerializedName("modified_by")
		private Object modifiedBy;

		@SerializedName("emp_level")
		private int empLevel;

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
		private Object status;

		public String getEmpRole(){
			return empRole;
		}

		public String getEmpName(){
			return empName;
		}

		public Object getMobNo(){
			return mobNo;
		}

		public String getEmpPwd(){
			return empPwd;
		}

		public Object getOtp(){
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

		public Object getEmpImage(){
			return empImage;
		}

		public Object getDob(){
			return dob;
		}

		public String getImeiNo(){
			return imeiNo;
		}

		public Object getModifiedBy(){
			return modifiedBy;
		}

		public int getEmpLevel(){
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

		public Object getStatus(){
			return status;
		}
	}
*/
}
package com.gmda.attendance.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserModelAttendance{

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

		@SerializedName("division")
		private String division;

		@SerializedName("emp_name")
		private String empName;

		@SerializedName("totalAbsent")
		private String totalAbsent;

		@SerializedName("totalLate")
		private String totalLate;

		@SerializedName("totalWorkingHr")
		private int totalWorkingHr;

		@SerializedName("designation")
		private String designation;

		@SerializedName("totalPresent")
		private String totalPresent;

		@SerializedName("department")
		private String department;

		@SerializedName("totalLateAbsent")
		private int totalLateAbsent;

		@SerializedName("emp_id")
		private String empId;

		public String getDivision(){
			return division;
		}

		public String getEmpName(){
			return empName;
		}

		public String getTotalAbsent(){
			return totalAbsent;
		}

		public String getTotalLate(){
			return totalLate;
		}

		public int getTotalWorkingHr(){
			return totalWorkingHr;
		}

		public String getDesignation(){
			return designation;
		}

		public String getTotalPresent(){
			return totalPresent;
		}

		public String getDepartment(){
			return department;
		}

		public int getTotalLateAbsent(){
			return totalLateAbsent;
		}

		public String getEmpId(){
			return empId;
		}
	}
}
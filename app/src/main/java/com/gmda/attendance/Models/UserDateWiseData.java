package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDateWiseData{
	private String responseType;
	private List<ResponseDataItem> responseData;
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
		private String msg;

		public String getMsg(){
			return msg;
		}
	}


	public class ResponseDataItem{

		@SerializedName("punch_out")
		private String punchOut;

		@SerializedName("punch_in")
		private String punchIn;

		@SerializedName("working_hr")
		private String workingHr;

		@SerializedName("punchin_long")
		private String punchinLong;

		@SerializedName("punchout_lat")
		private String punchoutLat;


		@SerializedName("attendance_date")
		private String attendanceDate;

		@SerializedName("punchout_long")
		private String punchoutLong;

		@SerializedName("division")
		private String division;

		@SerializedName("punchin_lat")
		private String punchinLat;
		@SerializedName("remarks")
		private Object remarks;

		@SerializedName("emp_id")
		private String empId;

		@SerializedName("a_status")
		private String aStatus;


		@SerializedName("created_by")
		private String created_by;

		@SerializedName("created_date")
		private String createdDate;

		@SerializedName("remarks")





		public String getPunchOut(){
			return punchOut;
		}

		public String getPunchIn(){
			return punchIn;
		}

		public String getWorkingHr(){
			return workingHr;
		}

		public String getPunchinLong(){
			return punchinLong;
		}

		public String getPunchoutLat(){
			return punchoutLat;
		}

		public String getAttendanceDate(){
			return attendanceDate;
		}

		public String getPunchoutLong(){
			return punchoutLong;
		}

		public String getCreatedBy(){
			return created_by;
		}

		public String getPunchinLat(){
			return punchinLat;
		}

		public String getCreatedDate(){
			return createdDate;
		}




		public String getEmpId(){
			return empId;
		}

		public String getAStatus(){
			return aStatus;
		}
	}

}
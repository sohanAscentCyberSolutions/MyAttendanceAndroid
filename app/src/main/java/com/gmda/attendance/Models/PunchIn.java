package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PunchIn{

		private String responseType;

		private ResponseMsg responseMsg;

		private List<Object> responseData =null ;

		public String getResponseType() {
			return responseType;
		}

		public void setResponseType(String responseType) {
			this.responseType = responseType;
		}

		public ResponseMsg getResponseMsg() {
			return responseMsg;
		}

		public void setResponseMsg(ResponseMsg responseMsg) {
			this.responseMsg = responseMsg;
		}

		public List<Object> getResponseData() {
			return responseData;
		}

		public void setResponseData(List<Object> responseData) {
			this.responseData = responseData;
		}


	public class ResponseMsg {


		private String msg;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}
	}


	/*@SerializedName("responseType")
	private String responseType;

	@SerializedName("responseData")
	private ResponseData responseData;

	@SerializedName("responseMsg")
	private ResponseMsg responseMsg;

	public String getResponseType(){
		return responseType;
	}

	public ResponseData getResponseData(){
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
	public class ResponseData {

		@SerializedName("punch_out")
		private Object punchOut;

		@SerializedName("punch_in")
		private String punchIn;

		@SerializedName("working_hr")
		private Object workingHr;

		@SerializedName("punchin_long")
		private String punchinLong;

		@SerializedName("punchout_lat")
		private Object punchoutLat;

		@SerializedName("attendance_date")
		private String attendanceDate;

		@SerializedName("punchout_long")
		private Object punchoutLong;

		@SerializedName("created_by")
		private String createdBy;

		@SerializedName("punchin_lat")
		private String punchinLat;

		@SerializedName("created_date")
		private String createdDate;

		@SerializedName("objectid")
		private int objectid;

		@SerializedName("remarks")
		private Object remarks;

		@SerializedName("emp_id")
		private String empId;

		@SerializedName("a_status")
		private String aStatus;

		public Object getPunchOut() {
			return punchOut;
		}

		public String getPunchIn() {
			return punchIn;
		}

		public Object getWorkingHr() {
			return workingHr;
		}

		public String getPunchinLong() {
			return punchinLong;
		}

		public Object getPunchoutLat() {
			return punchoutLat;
		}

		public String getAttendanceDate() {
			return attendanceDate;
		}

		public Object getPunchoutLong() {
			return punchoutLong;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public String getPunchinLat() {
			return punchinLat;
		}

		public String getCreatedDate() {
			return createdDate;
		}

		public int getObjectid() {
			return objectid;
		}

		public Object getRemarks() {
			return remarks;
		}

		public String getEmpId() {
			return empId;
		}

		public String getAStatus() {
			return aStatus;
		}
	}*/

package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkLocation{

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

	public class ResponseDataItem {

		@SerializedName("lat_max")
		private String latMax;

		@SerializedName("lat_min")
		private String latMin;

		@SerializedName("created_dt")
		private String createdDt;

		@SerializedName("lon_max")
		private String lonMax;

		@SerializedName("loc_code")
		private String locCode;

		@SerializedName("dept_name")
		private String deptName;

		@SerializedName("loc_name")
		private String locName;

		@SerializedName("lon_min")
		private String lonMin;

		@SerializedName("created_by")
		private String createdBy;

		@SerializedName("objectid")
		private int objectid;

		public String getLatMax() {
			return latMax;
		}

		public String getLatMin() {
			return latMin;
		}

		public String getCreatedDt() {
			return createdDt;
		}

		public String getLonMax() {
			return lonMax;
		}

		public String getLocCode() {
			return locCode;
		}

		public String getDeptName() {
			return deptName;
		}

		public String getLocName() {
			return locName;
		}

		public String getLonMin() {
			return lonMin;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public int getObjectid() {
			return objectid;
		}
	}
	}
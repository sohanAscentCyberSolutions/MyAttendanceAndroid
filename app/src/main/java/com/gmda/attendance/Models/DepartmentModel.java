package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepartmentModel{

	@SerializedName("responseType")
	private String responseType;

	@SerializedName("responseData")
	private List<ResponseDataItem> responseData=null;

	@SerializedName("responseMsg")
	private ResponseMsg responseMsg=null;

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

		@SerializedName("dept_name")
		private String deptName;

		public String getDeptName(){
			return deptName;
		}
	}
}
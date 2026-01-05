package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DivisionModel{

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

		@SerializedName("div_code")
		private String divCode;

		@SerializedName("div_name")
		private String divName;

		public String getDivCode(){
			return divCode;
		}

		public String getDivName(){
			return divName;
		}
	}
}
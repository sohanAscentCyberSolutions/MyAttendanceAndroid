package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

public class AddImages{

	@SerializedName("responseType")
	private String responseType;

	@SerializedName("responseData")
	private String responseData;

	@SerializedName("responseMsg")
	private ResponseMsg responseMsg;

	public String getResponseType(){
		return responseType;
	}

	public String getResponseData(){
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
}
package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

public class LogOut{

	@SerializedName("responseType")
	private String responseType;

	@SerializedName("responseMsg")
	private ResponseMsg responseMsg=null;

	public String getResponseType(){
		return responseType;
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
package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

public class ResponseMsg{

	@SerializedName("msg")
	private String msg;

	public String getMsg(){
		return msg;
	}
}
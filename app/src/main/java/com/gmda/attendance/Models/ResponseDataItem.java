package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

public class ResponseDataItem{

	@SerializedName("punch_out")
	private Object punchOut;

	@SerializedName("punch_in")
	private String punchIn;

	@SerializedName("working_hr")
	private String workingHr;

	@SerializedName("punchin_long")
	private String punchinLong;

	@SerializedName("punchout_lat")
	private Object punchoutLat;

	@SerializedName("emp_name")
	private String empName;

	@SerializedName("attendance_date")
	private String attendanceDate;

	@SerializedName("punchout_long")
	private Object punchoutLong;

	@SerializedName("division")
	private String division;

	@SerializedName("punchin_lat")
	private String punchinLat;

	@SerializedName("designation")
	private String designation;

	@SerializedName("department")
	private String department;

	@SerializedName("remarks")
	private String remarks;

	@SerializedName("emp_id")
	private String empId;

	@SerializedName("a_status")
	private String aStatus;

	public Object getPunchOut(){
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

	public Object getPunchoutLat(){
		return punchoutLat;
	}

	public String getEmpName(){
		return empName;
	}

	public String getAttendanceDate(){
		return attendanceDate;
	}

	public Object getPunchoutLong(){
		return punchoutLong;
	}

	public String getDivision(){
		return division;
	}

	public String getPunchinLat(){
		return punchinLat;
	}

	public String getDesignation(){
		return designation;
	}

	public String getDepartment(){
		return department;
	}

	public String getRemarks(){
		return remarks;
	}

	public String getEmpId(){
		return empId;
	}

	public String getAStatus(){
		return aStatus;
	}
}
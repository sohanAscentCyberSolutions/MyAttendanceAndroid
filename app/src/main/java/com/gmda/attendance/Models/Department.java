package com.gmda.attendance.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Department {

    @SerializedName("status")
    private int Status;


    @SerializedName("data")
    private List<Department.ResponseDataItem> responseData=null;


    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setResponseData(List<ResponseDataItem> responseData) {
        this.responseData = responseData;
    }

    public List<Department.ResponseDataItem> getResponseData(){
        return responseData;
    }



    public class ResponseDataItem{

        @SerializedName("dept_name")
        private String deptName;

        public String getDeptName(){
            return deptName;
        }
    }

}

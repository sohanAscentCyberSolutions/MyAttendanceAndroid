package com.gmda.attendance.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckVersion {

    @SerializedName("responseType")
    @Expose
    private String responseType;

    @SerializedName("responseMsg")
    @Expose
    private ResponseMsg responseMsg = null;

    @SerializedName("responseData")
    @Expose
    private List<ResponseData> responseData = null;

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

    public List<ResponseData> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseData> responseData) {
        this.responseData = responseData;
    }

    public static class ResponseMsg {
        @SerializedName("msg")
        @Expose
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class ResponseData {

        @SerializedName("objectid")
        @Expose
        private String objectid;

        @SerializedName("app_name")
        @Expose
        private String app_name;

        @SerializedName("version_to_update")
        @Expose
        private String version_to_update;

        @SerializedName("update_status")
        @Expose
        private String update_status;

        @SerializedName("google_play_url")
        @Expose
        private String google_play_url;

        @SerializedName("apk_url")
        @Expose
        private String apk_url;

        @SerializedName("maintenance_status")
        @Expose
        private String maintenance_status;

        @SerializedName("maintenance_msg")
        @Expose
        private String maintenance_msg;

        @SerializedName("new_version")
        @Expose
        private String new_version;

        public String getObjectid() {
            return objectid;
        }

        public void setObjectid(String objectid) {
            this.objectid = objectid;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getVersion_to_update() {
            return version_to_update;
        }

        public void setVersion_to_update(String version_to_update) {
            this.version_to_update = version_to_update;
        }

        public String getUpdate_status() {
            return update_status;
        }

        public void setUpdate_status(String update_status) {
            this.update_status = update_status;
        }

        public String getGoogle_play_url() {
            return google_play_url;
        }

        public void setGoogle_play_url(String google_play_url) {
            this.google_play_url = google_play_url;
        }

        public String getApk_url() {
            return apk_url;
        }

        public void setApk_url(String apk_url) {
            this.apk_url = apk_url;
        }

        public String getMaintenance_status() {
            return maintenance_status;
        }

        public void setMaintenance_status(String maintenance_status) {
            this.maintenance_status = maintenance_status;
        }

        public String getMaintenance_msg() {
            return maintenance_msg;
        }

        public void setMaintenance_msg(String maintenance_msg) {
            this.maintenance_msg = maintenance_msg;
        }

        public String getNew_version() {
            return new_version;
        }

        public void setNew_version(String new_version) {
            this.new_version = new_version;
        }
    }

}
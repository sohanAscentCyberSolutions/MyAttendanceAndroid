package com.gmda.attendance.newmodels;

public class AttendanceReport {
    private String emp_name, emp_id, department, division, designation, working_hr, a_status, remarks;
    private String attendance_date, punch_in, punchin_lat, punchin_long, punch_out, punchout_lat, punchout_long;

    private String punch_in_address,punch_out_address;
    private String punchin_image,punchout_image;


    public String getPunchin_image() {
        return punchin_image;
    }

    public void setPunchin_image(String punchin_image) {
        this.punchin_image = punchin_image;
    }

    public String getPunchout_image() {
        return punchout_image;
    }

    public void setPunchout_image(String punchout_image) {
        this.punchout_image = punchout_image;
    }

    public AttendanceReport(String emp_name, String emp_id, String department, String division, String
            designation, String attendance_date,
                            String punch_in, String punchin_lat, String punchin_long, String punch_out,
                            String punchout_lat, String punchout_long, String working_hr, String a_status, String remarks,
                            String punch_in_address,
                            String punch_out_address,
                            String punchin_image,
                            String punchout_image

                            ) {
        this.emp_name = emp_name;
        this.emp_id = emp_id;
        this.department = department;
        this.division = division;
        this.designation = designation;
        this.working_hr = working_hr;
        this.a_status = a_status;
        this.remarks = remarks;
        this.attendance_date = attendance_date;
        this.punch_in = punch_in;
        this.punchin_lat = punchin_lat;
        this.punchin_long = punchin_long;
        this.punch_out = punch_out;
        this.punchout_lat = punchout_lat;
        this.punchout_long = punchout_long;
        this.punch_in_address = punch_in_address;
        this.punch_out_address = punch_out_address;
        this.punchin_image = punchin_image;
        this.punchout_image = punchout_image;
    }

    public String getPunch_in_address() {
        return punch_in_address;
    }

    public void setPunch_in_address(String punch_in_address) {
        this.punch_in_address = punch_in_address;
    }

    public String getPunch_out_address() {
        return punch_out_address;
    }

    public void setPunch_out_address(String punch_out_address) {
        this.punch_out_address = punch_out_address;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorking_hr() {
        return working_hr;
    }

    public void setWorking_hr(String working_hr) {
        this.working_hr = working_hr;
    }

    public String getA_status() {
        return a_status;
    }

    public void setA_status(String a_status) {
        this.a_status = a_status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAttendance_date() {
        return attendance_date;
    }

    public void setAttendance_date(String attendance_date) {
        this.attendance_date = attendance_date;
    }

    public String getPunch_in() {
        return punch_in;
    }

    public void setPunch_in(String punch_in) {
        this.punch_in = punch_in;
    }

    public String getPunchin_lat() {
        return punchin_lat;
    }

    public void setPunchin_lat(String punchin_lat) {
        this.punchin_lat = punchin_lat;
    }

    public String getPunchin_long() {
        return punchin_long;
    }

    public void setPunchin_long(String punchin_long) {
        this.punchin_long = punchin_long;
    }

    public String getPunch_out() {
        return punch_out;
    }

    public void setPunch_out(String punch_out) {
        this.punch_out = punch_out;
    }

    public String getPunchout_lat() {
        return punchout_lat;
    }

    public void setPunchout_lat(String punchout_lat) {
        this.punchout_lat = punchout_lat;
    }

    public String getPunchout_long() {
        return punchout_long;
    }

    public void setPunchout_long(String punchout_long) {
        this.punchout_long = punchout_long;
    }
}

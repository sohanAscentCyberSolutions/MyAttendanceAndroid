package com.gmda.attendance.AddTeacherAttendance.Model;
public class StudentAttendanceModel {

    public StudentAttendanceModel(int student_roll_no, String student_name) {
        this.student_roll_no = student_roll_no;
        this.student_name = student_name;
    }

    public int getStudent_roll_no() {
        return student_roll_no;
    }

    public String getStudent_name() {
        return student_name;
    }

    int student_roll_no;
    String student_name;
}

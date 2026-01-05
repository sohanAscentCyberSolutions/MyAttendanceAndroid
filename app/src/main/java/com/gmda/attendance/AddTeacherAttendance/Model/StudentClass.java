package com.gmda.attendance.AddTeacherAttendance.Model;
public class StudentClass {

    public String getStudent_class() {
        return student_class;
    }

    public String getSection() {
        return section;
    }


    public StudentClass(String student_class, String section) {
        this.student_class = student_class;
        this.section = section;
    }


    String student_class;
    String section;
}

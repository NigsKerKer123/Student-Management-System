package com.example.sms.course;

public class Course {
    private String courseId;
    private String courseName;

    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public String getCollegeId() {
        return courseId;
    }

    public String getCollegeName() {
        return courseName;
    }

    public void setCollegeName(String collegeName) {
        this.courseName = collegeName;
    }
}

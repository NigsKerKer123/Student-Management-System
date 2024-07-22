package com.example.sms.course;

public class ItemCourse {
    String courseName;
    String number;
    String courseId;

    public ItemCourse(String number, String courseName, String courseId) {
        this.courseName = courseName;
        this.number = number;
        this.courseId = courseId;
    }
    public String getCourseName() {
        return courseName;
    }
    public String getNumber() {
        return number;
    }
    public String getCourseId() {
        return courseId;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}

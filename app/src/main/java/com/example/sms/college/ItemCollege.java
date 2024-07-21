package com.example.sms.college;

public class ItemCollege {
    String collegeName;
    String number;
    String collegeId;

    public ItemCollege(String collegeName, String number, String collegeId) {
        this.collegeName = collegeName;
        this.number = number;
        this.collegeId = collegeId;
    }
    public String getCollegeName() {
        return collegeName;
    }
    public String getNumber() {
        return number;
    }
    public String getCollegeId() {
        return collegeId;
    }
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}

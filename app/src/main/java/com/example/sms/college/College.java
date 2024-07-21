package com.example.sms.college;

public class College {
    private String collegeName;
    private String collegeId;

    public College(String collegeId, String collegeName) {
        this.collegeName = collegeName;
        this.collegeId = collegeId;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
}

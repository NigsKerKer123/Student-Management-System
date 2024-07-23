package com.example.sms.student;

public class ItemStudent {
    String number;
    String studentName;
    String studentId;

    public ItemStudent(String number, String studentName, String studentId) {
        this.studentName = studentName;
        this.number = number;
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getNumber() {
        return number;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
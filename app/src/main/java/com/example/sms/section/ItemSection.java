package com.example.sms.section;

public class ItemSection {
    String number;
    String sectionName;
    String sectionId;

    public ItemSection(String number, String sectionName, String sectionId) {
        this.sectionName = sectionName;
        this.number = number;
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }
    public String getNumber() {
        return number;
    }
    public String getSectionId() {
        return sectionId;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
    public void setNumber(String number) {
        this.number = number;
    }
}

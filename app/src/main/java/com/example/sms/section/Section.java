package com.example.sms.section;

public class Section {
    private String sectionId;
    private String sectionName;

    public Section(){
    }

    public Section(String sectionId, String sectionName) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }
    public String getSectionId() {
        return sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String collegeName) {
        this.sectionName = sectionName;
    }
}

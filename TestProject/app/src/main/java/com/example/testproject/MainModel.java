package com.example.testproject;

import java.io.Serializable;

public class MainModel {

    private String name;
    private String course;
    private String email;
    private String turl;
    private String description;
    private boolean isFul;

    public MainModel() {}

    public MainModel(String name, String course, String email, String turl, String description, boolean isFul) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.turl = turl;
        this.description = description;
        this.isFul = isFul;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }

    public boolean isFul() {
        return isFul;
    }

    public void setFul(boolean ful) {
        isFul = ful;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

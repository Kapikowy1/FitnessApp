package com.example.testproject;

import java.io.Serializable;

public class MainModel implements Serializable {
    String name,course,email,turl,description;


    MainModel(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MainModel(String name, String course, String email, String turl, String description) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.turl = turl;
        this.description = description;
    }
    // kontruktor uzywany jest aby stworzyc instancje klasy Mainmodel z wartosciami name,course
    // email oraz turl ,które nie mają żadnych określonych z góry wartości, wartosci sa ustawianie pozniej

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

    // metody geter oraz seter pozwalaja na dostep i modyfikacje zmiennych odpowiednio
    // do ustawiania ich wartosci seter i geter do pobierania wartosci
}

package com.example.testproject;

import java.io.Serializable;

public class MainModel {

    private String name;
    private String recipeType;
    private String turl;
    private String description;


    public MainModel() {}

    public MainModel(String name, String recipeType, String turl, String description) {
        this.name = name;
        this.recipeType = recipeType;
        this.turl = turl;
        this.description = description;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }

    public String getDescription() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = description;
    }

}

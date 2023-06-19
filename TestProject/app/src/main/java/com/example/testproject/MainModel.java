package com.example.testproject;


public class MainModel {
    private String name;
    private String recipeType;
    private String turl;
    private String description;

    private String reps,sets;

    public MainModel() {}

    public MainModel(String name, String recipeType, String turl, String description, String reps, String sets) {
        this.name = name;
        this.recipeType = recipeType;
        this.turl = turl;
        this.description = description;
        this.reps=reps;
        this.sets=sets;

    }
    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
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

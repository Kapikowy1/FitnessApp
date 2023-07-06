package com.example.testproject.Dieta.DietPlan;

public class BMRUserData {
    private final double weightValue;
    private final double heightValue;
    private final double ageValue;
    private final String genderValue;
    private final double activityMultiplier;

    public BMRUserData(double weightValue, double heightValue, double ageValue, String genderValue, double activityMultiplier) {
        this.weightValue = weightValue;
        this.heightValue = heightValue;
        this.ageValue = ageValue;
        this.genderValue = genderValue;
        this.activityMultiplier = activityMultiplier;
    }
    public double getWeightValue() {
        return weightValue;
    }

    public double getHeightValue() {
        return heightValue;
    }

    public double getAgeValue() {
        return ageValue;
    }

    public String getGenderValue() {
        return genderValue;
    }



    public double getActivityMultiplier() {
        return activityMultiplier;
    }

}

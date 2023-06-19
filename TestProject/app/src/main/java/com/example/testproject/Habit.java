package com.example.testproject;

public class Habit {
    private String name;
    private String hour;
    private String place;

    private String date;
    private boolean onAlarm;
    private boolean habitDone;

    private long countdownTime;

    public Habit(String name, String hour, String place,String date, boolean onAlarm, boolean habitDone,long countdownTime) {
        this.name = name;
        this.hour = hour;
        this.place = place;
        this.date = date;
        this.onAlarm = onAlarm;
        this.habitDone = habitDone;
        this.countdownTime = countdownTime;
    }

    public void setHabitDone(boolean habitDone) {
        this.habitDone = habitDone;
    }

    public Habit() {

    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getHour() {
        return hour;
    }

    public String getPlace() {
        return place;
    }

    public boolean isOnAlarm() {
        return onAlarm;
    }

    public boolean isHabitDone() {
        return habitDone;
    }

    public long getCountdownTime() {
        return countdownTime;
    }

    public void setCountdownTime(long countdownTime) {
        this.countdownTime = countdownTime;
    }

    public void toggleHabitDone() {
        habitDone = !habitDone;
    }
}
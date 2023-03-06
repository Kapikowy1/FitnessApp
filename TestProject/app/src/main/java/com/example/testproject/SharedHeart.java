package com.example.testproject;

import android.app.Application;
import android.content.SharedPreferences;

public class SharedHeart extends Application {

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }

    public SharedPreferences getMyPrefs() {
        return sharedPreferences;
    }
}
package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class HabitMainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_main);

        // Uruchomienie usługi AlarmService
       /* Intent serviceIntent = new Intent(this, AlarmService.class);
        startService(serviceIntent);*/
        AlarmScheduler alarmScheduler = new AlarmScheduler(this);

        alarmScheduler.startBackgroundCheck(); // Wywołanie metody startBackgroundCheck po utworzeniu instancji AlarmScheduler


        // Tworzenie instancji MyAlarmsFragment
        MyAlarmsFragment myAlarmsFragment = new MyAlarmsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container_habit, myAlarmsFragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }
    @Override
    public void onBackPressed() {
        Intent intentMain = new Intent(this, ActivityHome.class);
        startActivity(intentMain);
        finish(); // Opcjonalnie możesz zakończyć bieżącą aktywność, aby po powrocie do MainActivity nie było możliwości ponownego powrotu do aktualnej aktywności poprzez przycisk wstecz
    }
}
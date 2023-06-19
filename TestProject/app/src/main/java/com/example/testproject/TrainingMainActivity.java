package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TrainingMainActivity extends AppCompatActivity {


    TextView readyPlans,myplans;
    Exercises_Fragment exercises_fragment=new Exercises_Fragment();

    My_training_plans_fragment my_training_plans_fragment=new My_training_plans_fragment();
    TrainingPlanFragment trainingPlanFragment= new TrainingPlanFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_main);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container_training, exercises_fragment);

        transaction.addToBackStack(null);
        transaction.commit();



        readyPlans=findViewById(R.id.readyPlansTV);
        myplans=findViewById(R.id.myPlansTV);



        myplans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.fragment_container_training, my_training_plans_fragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        readyPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.fragment_container_training, trainingPlanFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    @Override

    public void onBackPressed() {
        Intent intentMain = new Intent(this, ActivityHome.class);
        startActivity(intentMain);
        finish(); // Opcjonalnie możesz zakończyć bieżącą aktywność, aby po powrocie do MainActivity nie było możliwości ponownego powrotu do aktualnej aktywności poprzez przycisk wstecz
    }

}
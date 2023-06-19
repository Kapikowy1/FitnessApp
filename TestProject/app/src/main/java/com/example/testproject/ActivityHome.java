package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityHome extends AppCompatActivity {
    private Button trainingBtn,dietBtn,habitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        trainingBtn = findViewById(R.id.training_Btn);
        dietBtn = findViewById(R.id.diet_Btn);
        habitBtn = findViewById(R.id.habit_Btn);

        trainingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainingIntent=new Intent(ActivityHome.this,TrainingMainActivity.class);
                ActivityHome.this.startActivity(trainingIntent);
                //przejdź do TrainingMainActivity
            }
        });
        dietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dietiIntent=new Intent(ActivityHome.this,MainActivity.class);
                ActivityHome.this.startActivity(dietiIntent);
                //przejdź do MainActivity
            }
        });
        habitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent habitIntent=new Intent(ActivityHome.this,HabitMainActivity.class);
                ActivityHome.this.startActivity(habitIntent);
                //przejdź do HabitMainActivity
            }
        });
    }
}
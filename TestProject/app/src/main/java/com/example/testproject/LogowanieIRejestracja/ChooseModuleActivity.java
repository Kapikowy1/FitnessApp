package com.example.testproject.LogowanieIRejestracja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testproject.Dieta.RecipeSearch.MainActivity;

import com.example.testproject.R;
import com.example.testproject.Trening.TrainingMainActivity;

public class ChooseModuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button trainingBtn = findViewById(R.id.training_Btn);
        Button dietBtn = findViewById(R.id.diet_Btn);


        trainingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainingIntent=new Intent(ChooseModuleActivity.this, TrainingMainActivity.class);
                ChooseModuleActivity.this.startActivity(trainingIntent);
                //przejdź do TrainingMainActivity
            }
        });
        dietBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dietiIntent=new Intent(ChooseModuleActivity.this, MainActivity.class);
                ChooseModuleActivity.this.startActivity(dietiIntent);
                //przejdź do MainActivity
            }
        });

    }
}
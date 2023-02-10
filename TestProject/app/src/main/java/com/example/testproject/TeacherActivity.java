package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TeacherActivity extends AppCompatActivity {
    ImageView teacherImage;
    TextView teacherName,teacherDesc;
    Button btnRecepieSave,btnRecepieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String teacherDescript =getIntent().getStringExtra("desc");

        btnRecepieSave=findViewById(R.id.saveRecipe);
        btnRecepieStore=findViewById(R.id.myRecipes);

        teacherDesc = findViewById(R.id.TeacherDescription);
        teacherName = findViewById(R.id.TeacherName);
        teacherImage=(ImageView) findViewById(R.id.TeacherImage);

        Glide.with(this)
                .load(imageUrl)
                .into(teacherImage);
        teacherDesc.setText(teacherDescript);
        teacherName.setText(name);


        btnRecepieStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherActivity.this,RecipeStorageActivity.class);
                startActivity(intent);
            }
        });
        btnRecepieSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
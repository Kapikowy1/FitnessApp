package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TeacherActivity extends AppCompatActivity {
    ImageView teacherImage;
    TextView teacherName,teacherDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String teacherDescript =getIntent().getStringExtra("desc");

        teacherDesc = findViewById(R.id.TeacherDescription);
        teacherName = findViewById(R.id.TeacherName);
        teacherImage=(ImageView) findViewById(R.id.TeacherImage);

        Glide.with(this)
                .load(imageUrl)
                .into(teacherImage);
        teacherDesc.setText(teacherDescript);
        teacherName.setText(name);


        /*MainModel model = (MainModel) getIntent().getSerializableExtra("Model");

        TextView name= findViewById(R.id.TeacherName);
        TextView desc=findViewById(R.id.TeacherDescription);
        ImageView teacherPic=findViewById(R.id.TeacherImage);

        name.setText(model.getName());*/





    }
}
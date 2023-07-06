package com.example.testproject.Trening;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testproject.R;

public class ExerciseDescriptionActivity extends AppCompatActivity {
    private ImageView exerciseImage;
    private TextView exerciseName,exerciseDesc,exerciseSets,exerciseReps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_description);

        getDataFromIntent();

        exerciseDesc = findViewById(R.id.exercise_details_description);
        exerciseName = findViewById(R.id.exercise_details_name);
        exerciseImage=(ImageView) findViewById(R.id.exercise_details_image);
        exerciseReps=findViewById(R.id.exercise_description_reps_TV);
        exerciseSets=findViewById(R.id.exercise_description_sets_TV);

    }
    private void getDataFromIntent(){
        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String recipeDescript =getIntent().getStringExtra("description");
        String sets = getIntent().getStringExtra("sets");
        String reps = getIntent().getStringExtra("reps");
        setTextOnItems(name,sets,reps,recipeDescript,imageUrl);
    }

    private void setTextOnItems(String name, String sets, String reps, String recipeDescript, String imageUrl) {
        exerciseDesc.setText(Html.fromHtml(recipeDescript, Html.FROM_HTML_MODE_LEGACY));
        exerciseName.setText(name);
        exerciseSets.setText("Sets: "+sets);
        exerciseReps.setText("Reps: "+reps);
        setImage(imageUrl);
    }

    private void setImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(exerciseImage);
    }


}
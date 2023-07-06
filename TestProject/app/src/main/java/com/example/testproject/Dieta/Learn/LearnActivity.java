package com.example.testproject.Dieta.Learn;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testproject.Dieta.Quiz.QuizActivity;

import com.example.testproject.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LearnActivity extends AppCompatActivity {
    private int textindex= 0, textcount = 0;
    private ShapeableImageView Img;

    private TextView learningtext;
    private Button SubmitButton;
    private ProgressBar progressBarLearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        SubmitButton = findViewById(R.id.submitButtonLearn);
        Img = findViewById(R.id.learningImage);
        learningtext = findViewById(R.id.learningText);
        progressBarLearn = findViewById(R.id.progressBarLearn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference learningref = database.getReference("learningtext");
        // Pobieranie referencji do danych z Firebase

        learningref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchLearningData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("1", "loadLearning:onCancelled", databaseError.toException());
            }
        });

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textcount++;
                textindex++;
                if(textindex==6){
                    startQuizIntent();
                }else {
                    learningref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            fetchLearningData(dataSnapshot);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w("1", "loadLearning:onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        });
    }
    private void fetchLearningData(DataSnapshot dataSnapshot) {
        List<String> learningList = new ArrayList<>();
        List<String> learningImgList = new ArrayList<>();

        for (DataSnapshot learningSnapshot : dataSnapshot.getChildren()) {
            String learningString = learningSnapshot.child("tresc").getValue(String.class);
            String learnImgUrl = learningSnapshot.child("lurl").getValue(String.class);

            learningList.add(learningString);
            learningImgList.add(learnImgUrl);
        }
        if (learningList.size() > 0 ) {
            displayLearningContent(learningList, learningImgList, textindex);
        }else {
            Toast.makeText(LearnActivity.this, "Nie ma rekordów w bazie danych", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayLearningContent(List<String> learningList, List<String> learningImgList, int textindex) {

            String learningString = learningList.get(textindex);
            String learnImgUrl = learningImgList.get(textindex);
            learningtext.setText(learningString);
            Glide.with(LearnActivity.this).load(learnImgUrl).into(Img);

            progressBarLearn.setProgress(textcount);
            progressBarLearn.setMax(learningList.size());
    }
    private void startQuizIntent(){
        Intent quizIntent=new Intent(LearnActivity.this, QuizActivity.class);
        startActivity(quizIntent);
    }
}
package com.example.testproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LearnFragment extends Fragment {

    private int learntextindex= 0, learntextcount = 0;
    ShapeableImageView learnImg;
    private Fragment fragment = this;
    TextView learnTV;
    Button learnSubmitButton;
    private ProgressBar progressBarLearn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        learnSubmitButton = view.findViewById(R.id.submitButtonLearn);
        learnImg = view.findViewById(R.id.learningImage);
        learnTV = view.findViewById(R.id.learningText);
        progressBarLearn = view.findViewById(R.id.progressBarLearn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference learningref = database.getReference("learningtext");
        // Pobieranie referencji do danych z Firebase

        learningref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> learningList = new ArrayList<>();
                List<String> learningImgList = new ArrayList<>();

                for (DataSnapshot learingsnapshot : dataSnapshot.getChildren()) {
                    String learningString = learingsnapshot.child("tresc").getValue(String.class);
                    String learnImgUrl = learingsnapshot.child("lurl").getValue(String.class);

                    learningList.add(learningString);
                    learningImgList.add(learnImgUrl);
                }
                // Pobieranie treści do nauki i URL obrazków z bazy danych

                if (learningList.size() > 0) {
                    String learningString = learningList.get(learntextindex);
                    String learnImgUrl = learningImgList.get(learntextindex);

                    learnTV.setText(learningString);
                    Glide.with(fragment).load(learnImgUrl).into(learnImg);
                    // Wyświetlanie treści do nauki i obrazka na podstawie indeksu
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("1", "loadLearning:onCancelled", databaseError.toException());
            }
        });

        learnSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                learntextcount++;
                learntextindex++;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference learningref = database.getReference("learningtext");
                learningref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> learningList = new ArrayList<>();
                        List<String> learningImgList = new ArrayList<>();

                        for (DataSnapshot learingsnapshot : dataSnapshot.getChildren()) {
                            String learningString = learingsnapshot.child("tresc").getValue(String.class);
                            String learnImgUrl = learingsnapshot.child("lurl").getValue(String.class);
                            learningList.add(learningString);
                            learningImgList.add(learnImgUrl);
                        }
                        if (learningList.size() > learntextindex) {
                            String learningString = learningList.get(learntextindex);
                            String learnImgUrl = learningImgList.get(learntextindex);

                            learnTV.setText(learningString);
                            Glide.with(fragment).load(learnImgUrl).into(learnImg);
                            // Wyświetlanie kolejnej treści do nauki i obrazka na podstawie indeksu
                        } else {
                            QuizFragment quizFragment = new QuizFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, quizFragment);
                            fragmentTransaction.commit();
                            // Jeśli skończono naukę, przechodzenie do fragmentu QuizFragment
                        }
                        progressBarLearn.setProgress(learntextcount);
                        progressBarLearn.setMax(learningList.size());
                        // Aktualizacja paska postępu nauki
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("1", "loadLearning:onCancelled", databaseError.toException());
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
package com.example.testproject.Trening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.testproject.Dieta.RecipeSearch.MainAdapter;
import com.example.testproject.Dieta.RecipeSearch.MainModel;
import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CurrentPlanActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    AdapterExerciseDetails adapterExerciseDetails;
    TextView myPlanName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan);
        String DietPlanName = getIntent().getStringExtra("DietName");
        String currentPlanName = getIntent().getStringExtra("planName");
        recyclerView = findViewById(R.id.currentPlan_rv);
        myPlanName= findViewById(R.id.my_training_plan_name);

        if(DietPlanName != null){
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("dietplans").child(currentUserId).child(DietPlanName), MainModel.class)
                    .build();
            mainAdapter = new MainAdapter(options);
            recyclerView.setAdapter(mainAdapter);
        }else{
            if (currentPlanName.equals("Begginer") || currentPlanName.equals("Gym") || currentPlanName.equals("Stretch")) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Ready Plans").child(currentPlanName), MainModel.class)
                        .build();
                adapterExerciseDetails = new AdapterExerciseDetails(options);
                recyclerView.setAdapter(adapterExerciseDetails);
            } else {
                myPlanName.setText(currentPlanName);

                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("My plans").child(currentUserId).child(currentPlanName), MainModel.class)
                        .build();
                adapterExerciseDetails = new AdapterExerciseDetails(options);
                recyclerView.setAdapter(adapterExerciseDetails);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
        if (adapterExerciseDetails != null) {
            adapterExerciseDetails.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
        if (adapterExerciseDetails != null) {
            adapterExerciseDetails.stopListening();
        }
    }
}

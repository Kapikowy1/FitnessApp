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
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private AdapterExerciseDetails adapterExerciseDetails;
    private TextView myPlanName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_plan);

        recyclerView = findViewById(R.id.currentPlan_rv);
        myPlanName= findViewById(R.id.my_training_plan_name);
        String DietPlanName = getIntent().getStringExtra("DietName");
        String currentPlanName = getIntent().getStringExtra("planName");
        if(DietPlanName != null){
            setDietPlansRV();
        }else{
            if (currentPlanName.equals("Begginer") || currentPlanName.equals("Gym") || currentPlanName.equals("Stretch")) {
                setReadyPlansRV();
            } else {
                setMyPlansRV();
            }
        }
    }
    private void setDietPlansRV(){
        String DietPlanName = getIntent().getStringExtra("DietName");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("dietplans").child(currentUserId).child(DietPlanName), MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }
    private void setReadyPlansRV(){
        String currentPlanName = getIntent().getStringExtra("planName");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Ready Plans").child(currentPlanName), MainModel.class)
                .build();
        adapterExerciseDetails = new AdapterExerciseDetails(options);
        recyclerView.setAdapter(adapterExerciseDetails);
    }
    private void setMyPlansRV(){
        String currentPlanName = getIntent().getStringExtra("planName");
        myPlanName.setText(currentPlanName);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("My plans").child(currentUserId).child(currentPlanName), MainModel.class)
                .build();
        adapterExerciseDetails = new AdapterExerciseDetails(options);
        recyclerView.setAdapter(adapterExerciseDetails);
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

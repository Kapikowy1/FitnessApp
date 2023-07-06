package com.example.testproject.Trening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ReadyPlansActivity extends AppCompatActivity {
    private Plan_Rv_adapter planAdapter;

    private RecyclerView planRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan);

        planRV = findViewById(R.id.plans_rv);

        setUpRV();


    }
    @Override
    public void onStart() {
        super.onStart();
        if (planAdapter != null) {
            planAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (planAdapter != null) {
            planAdapter.stopListening();
        }

    }
    private void setUpRV(){
        planRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Query query = FirebaseDatabase.getInstance().getReference().child("Ready Plans").orderByKey();
        FirebaseRecyclerOptions<String> options =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(query, snapshot -> snapshot.getKey())
                        .build();
        planAdapter = new Plan_Rv_adapter(options);
        planRV.setAdapter(planAdapter);
    }
}
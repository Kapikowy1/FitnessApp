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
    Plan_Rv_adapter planadapter;

    private RecyclerView planRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan);

        planRV = findViewById(R.id.plans_rv);
        planRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Query query = FirebaseDatabase.getInstance().getReference().child("Ready Plans").orderByKey();

        FirebaseRecyclerOptions<String> options =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(query, snapshot -> snapshot.getKey())
                        .build();

        planadapter = new Plan_Rv_adapter(options);
        planRV.setAdapter(planadapter);

    }
    @Override
    public void onStart() {
        super.onStart();
        if (planadapter != null) {
            planadapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (planadapter != null) {
            planadapter.stopListening();
        }

    }
}
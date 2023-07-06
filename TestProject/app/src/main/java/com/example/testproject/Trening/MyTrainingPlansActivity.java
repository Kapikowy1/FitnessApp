package com.example.testproject.Trening;

import android.content.Intent;
import android.os.Bundle;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyTrainingPlansActivity extends AppCompatActivity {



    private Plan_Rv_adapter planAdapter;
    private RecyclerView myPlansRecyclerView;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    private final String currentUserId = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_training_plans);

        ImageView addPlanImg = findViewById(R.id.add_new_plan);
        myPlansRecyclerView =findViewById(R.id.myplansrv);

        if (currentUser != null) {
            setUpRV();
        } else {
            Toast.makeText(getApplicationContext(), "Zaloguj się ponownie", Toast.LENGTH_SHORT).show();
        }
        addPlanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateTrainingPlanIntent();
            }
        });
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
        myPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Query query = FirebaseDatabase.getInstance().getReference().child("My plans").child(currentUserId);
        FirebaseRecyclerOptions<String> options =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(query, snapshot -> snapshot.getKey())
                        .build();
        planAdapter = new Plan_Rv_adapter(options);
        myPlansRecyclerView.setAdapter(planAdapter);
    }
    private void startCreateTrainingPlanIntent(){
        Intent createtrainingplan=new Intent(MyTrainingPlansActivity.this, CreateTrainingPlanActivity.class);
        startActivity(createtrainingplan);
    }
}
package com.example.testproject.Trening;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.databinding.ActivityMyTrainingPlansBinding;

import com.example.testproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyTrainingPlansActivity extends AppCompatActivity {



    Plan_Rv_adapter planadapter;
    ImageView addPlanImg;
    RecyclerView myplansRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_training_plans);

        addPlanImg=findViewById(R.id.add_new_plan);
        myplansRecyclerView=findViewById(R.id.myplansrv);



        myplansRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Tutaj możesz użyć currentUserId w swoim kodzie
            Query query = FirebaseDatabase.getInstance().getReference().child("My plans").child(currentUserId);
            FirebaseRecyclerOptions<String> options =
                    new FirebaseRecyclerOptions.Builder<String>()
                            .setQuery(query, snapshot -> snapshot.getKey())
                            .build();
            planadapter = new Plan_Rv_adapter(options);
            myplansRecyclerView.setAdapter(planadapter);
        } else {
            Toast.makeText(getApplicationContext(), "Zaloguj się ponownie", Toast.LENGTH_SHORT).show();
        }


        addPlanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to createplan
                Intent createtrainingplanintent=new Intent(MyTrainingPlansActivity.this, CreateTrainingPlanActivity.class);
                startActivity(createtrainingplanintent);
            }
        });
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
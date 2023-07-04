package com.example.testproject.Dieta.DietPlan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.testproject.LogowanieIRejestracja.ChooseModuleActivity;
import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyDietPlansActivity extends AppCompatActivity {
    private BottomNavigationView navDiet;

    private RecyclerView dietplanRV;
    AdapterDietPlans planadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diet_plans);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dietplanRV = findViewById(R.id.diet_plans_rv);
        dietplanRV.setLayoutManager(new LinearLayoutManager(MyDietPlansActivity.this));

        Query query = FirebaseDatabase.getInstance().getReference().child("dietplans").child(currentUserId).orderByKey();

        FirebaseRecyclerOptions<String> options =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(query, snapshot -> snapshot.getKey())
                        .build();

        planadapter = new AdapterDietPlans(options);
        dietplanRV.setAdapter(planadapter);

        navDiet=findViewById(R.id.bottom_navigation_diet);

        navDiet.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.plan_and_calc_dietmenu:
                        Intent dietcalcintent=new Intent(MyDietPlansActivity.this, Dietplan_activity.class);
                        MyDietPlansActivity.this.startActivity(dietcalcintent);
                        break;
                    case R.id.my_diets:
                        Intent dietplansintent=new Intent(MyDietPlansActivity.this,MyDietPlansActivity.class);
                        MyDietPlansActivity.this.startActivity(dietplansintent);
                        break;
                    case R.id.home:
                        Intent intentHome= new Intent(MyDietPlansActivity.this, ChooseModuleActivity.class);
                        MyDietPlansActivity.this.startActivity(intentHome);
                        break;
                }
                return false;
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
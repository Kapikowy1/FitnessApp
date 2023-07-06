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

import java.util.HashMap;

public class MyDietPlansActivity extends AppCompatActivity {

    private RecyclerView dietplanRV;
    private AdapterDietPlans planadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diet_plans);

        dietplanRV = findViewById(R.id.diet_plans_rv);
        BottomNavigationView navigationBar = findViewById(R.id.bottom_navigation_diet);

        setmRecycler();
        navigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigateTo(item.getItemId());
                return false;
            }
        });
    }
    private void setmRecycler(){
        dietplanRV.setLayoutManager(new LinearLayoutManager(MyDietPlansActivity.this));
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("dietplans").child(currentUserId).orderByKey();
        FirebaseRecyclerOptions<String> options =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(query, snapshot -> snapshot.getKey())
                        .build();
        planadapter = new AdapterDietPlans(options);
        dietplanRV.setAdapter(planadapter);
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
    private void navigateTo(int itemId) {
        HashMap<Integer,Class<?>> activitymap=new HashMap<>();
        activitymap.put(R.id.plan_and_calc_dietmenu, Dietplan_activity.class);
        activitymap.put(R.id.my_diets, MyDietPlansActivity.class);
        activitymap.put(R.id.home,ChooseModuleActivity.class);

        Class<?> destinationactivity= activitymap.get(itemId);
        if(destinationactivity!=null){
            Intent intent=new Intent(MyDietPlansActivity.this,destinationactivity);
            startActivity(intent);
        }
    }

}
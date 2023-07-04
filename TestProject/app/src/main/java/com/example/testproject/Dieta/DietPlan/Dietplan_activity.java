package com.example.testproject.Dieta.DietPlan;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.LogowanieIRejestracja.ChooseModuleActivity;
import com.example.testproject.Dieta.RecipeSearch.SpinnerAdapter;
import com.example.testproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Dietplan_activity extends AppCompatActivity {
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = currentUser.getUid();
    DatabaseReference weightRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    SpinnerAdapter mAdapter = new SpinnerAdapter(databaseReference);
    private RecyclerView mRecyclerView;
    ImageView addItemImg, deleteItemImg;
    private ProgressBar progressCalories;
    private Button sumCaloriesBtn,savePlanBtn;
    private TextView progressPercentTV;
    private double sumCalories;
    private BottomNavigationView navigationBar;
    private double activityMultiplier,bmrCalc,genderConst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietplan);

        progressCalories = findViewById(R.id.progressBar);
        progressPercentTV = findViewById(R.id.progress_TV);
        sumCaloriesBtn = findViewById(R.id.calcButton);
        savePlanBtn=findViewById(R.id.save_dietplan);
        addItemImg = findViewById(R.id.add_item_img);
        deleteItemImg = findViewById(R.id.delete_item_img);
        mRecyclerView = findViewById(R.id.recycler_view);
        navigationBar=findViewById(R.id.bottom_navigation_diet);

        setmRecyclerView();
        fetchUserData();

        sumCaloriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double sumCalories = calculateSumCalories();
                updateProgressViews(sumCalories);
                showSumCaloriesToast(sumCalories);
            }
        });
        navigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigateTo(item.getItemId());
                return false;
            }
        });
        addItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addItem();
            }
        });
        deleteItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.delItem();
            }
        });
        savePlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlan();
            }
        });

    }


    private void fetchUserData(){
        weightRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String weight = dataSnapshot.child("weight").getValue(String.class);
                String height = dataSnapshot.child("height").getValue(String.class);
                String age = dataSnapshot.child("age").getValue(String.class);
                String genderValue = dataSnapshot.child("gender").getValue(String.class);
                String activityTypeValue = dataSnapshot.child("activityType").getValue(String.class);

                typeConversionToDouble(weight, height, age, activityTypeValue, genderValue);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Obsłuż błąd pobierania danych
            }
        });
    }

    private void typeConversionToDouble(String weight, String height, String age, String activityTypeValue, String genderValue){
        double weightValue = Double.parseDouble(weight);
        double heightValue = Double.parseDouble(height);
        double ageValue = Double.parseDouble(age);
        double activityMultiplier = getActivityMultiplier(activityTypeValue);

        calculateBMR(weightValue, heightValue, ageValue, genderValue, activityMultiplier);
    }

    private double getActivityMultiplier(String activityTypeValue) {
        //kod służy do przypisania wartosci double do danego stringa i oddaniu wartosci double do zmiennej
        Map<String, Double> activityMultiplierMap = new HashMap<>();
        activityMultiplierMap.put("None", 1.2);
        activityMultiplierMap.put("Low", 1.375);
        activityMultiplierMap.put("Medium", 1.55);
        activityMultiplierMap.put("High", 1.725);
        activityMultiplierMap.put("Very high", 1.9);

        return activityMultiplierMap.getOrDefault(activityTypeValue, 1.0);
    }

    private void calculateBMR(double weightValue, double heightValue, double ageValue, String genderValue, double activityMultiplier){
        if (genderValue.equals("M")) {
            genderConst = 88.36;
            bmrCalc = ((genderConst + ((13.4 * weightValue) + (4.8 * heightValue)) - (5.7 * ageValue)) * activityMultiplier);
        } else if (genderValue.equals("K")) {
            genderConst = 447.6;
            bmrCalc = ((genderConst + ((9.2 * weightValue) + (3.1 * heightValue)) - (4.3 * ageValue)) * activityMultiplier);
        } else {
            bmrCalc = 0.0; // Domyślna wartość
        }
    }

    private void savePlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dietplan_activity.this);
        builder.setTitle("Dodaj nazwę planu");
        final EditText editText = new EditText(Dietplan_activity.this);
        builder.setView(editText);
        builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String planName = editText.getText().toString();
                checkIfPlanExists(planName);
            }
        });
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void checkIfPlanExists(final String planName) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("dietplans").child(currentUserId);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(planName)) {
                    Toast.makeText(Dietplan_activity.this, "Nazwa planu już istnieje", Toast.LENGTH_SHORT).show();
                } else {
                    addRecipesToPlan(planName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Obsługa błędu
            }
        });
    }

    private void addRecipesToPlan(final String planName) {
        for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
            View view = mRecyclerView.getChildAt(i);
            Spinner spinner = view.findViewById(R.id.spinner);
            String recipeName = spinner.getSelectedItem().toString();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("teachers");
            databaseReference.orderByChild("name").equalTo(recipeName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String turl = snapshot.child("turl").getValue(String.class);
                        String description = snapshot.child("description").getValue(String.class);
                        String recipeType = snapshot.child("recipeType").getValue(String.class);
                        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("dietplans").child(currentUserId).child(planName);
                        DatabaseReference newRecipeRef = currentUserRef.push();
                        newRecipeRef.child("name").setValue(name);
                        newRecipeRef.child("turl").setValue(turl);
                        newRecipeRef.child("description").setValue(description);
                        newRecipeRef.child("recipeType").setValue(recipeType);
                    }
                    Toast.makeText(Dietplan_activity.this, "Dodano przepis pomyślnie", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Obsługa błędu
                }
            });
        }
    }
    private void setmRecyclerView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }
    private double calculateSumCalories() {
        double sumCalories = 0;

        for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
            View view = mRecyclerView.getChildAt(i);
            TextView textView = view.findViewById(R.id.koncowe_kcal);
            String text = textView.getText().toString();
            double value = Double.parseDouble(text);
            sumCalories += value;
        }
        return sumCalories;
    }

    private void updateProgressViews(double sumCalories) {
        progressCalories.setMax((int) bmrCalc);
        progressCalories.setProgress((int) sumCalories);
        int progressPercent = (int) (sumCalories / bmrCalc * 100);
        progressPercentTV.setText(progressPercent + "%");
    }

    private void showSumCaloriesToast(double sumCalories) {
        Toast.makeText(Dietplan_activity.this, "Suma kalorii: " + sumCalories, Toast.LENGTH_SHORT).show();
    }

    private void navigateTo(int itemId) {
        Intent intent = null;
        switch (itemId) {
            case R.id.plan_and_calc_dietmenu:
                intent = new Intent(Dietplan_activity.this, Dietplan_activity.class);
                break;
            case R.id.my_diets:
                intent = new Intent(Dietplan_activity.this, MyDietPlansActivity.class);
                break;
            case R.id.home:
                intent = new Intent(Dietplan_activity.this, ChooseModuleActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
package com.example.testproject;


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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dietplan_activity extends AppCompatActivity {
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = currentUser.getUid();
    DatabaseReference weightRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
    private RecyclerView mRecyclerView;
    ImageView addItemImg, deleteItemImg;
    private ProgressBar progressCalories;
    private Button calcCalories,savePlan;
    private TextView progressPercentTV;
    private double sumCalories;
    private BottomNavigationView navDiet;
    private double activitymultiplier,BMRcalc,gendercalc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietplan);

        //ustawienie zmiennych do progressbara
        progressCalories = findViewById(R.id.progressBar);
        progressPercentTV = findViewById(R.id.progress_TV);
        calcCalories = findViewById(R.id.calcButton);
        // zmienna do zapisywania planu
        savePlan=findViewById(R.id.save_dietplan);
        //ustawienie zmiennych do dodawania i odejmowania przedmiotow w RV
        addItemImg = findViewById(R.id.add_item_img);
        deleteItemImg = findViewById(R.id.delete_item_img);
        //ustawienie zmiennej do rv
        mRecyclerView = findViewById(R.id.recycler_view);
        //ustawienie zmiennej do paska nawigacji
        navDiet=findViewById(R.id.bottom_navigation_diet);
        navDiet.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.plan_and_calc_dietmenu:
                        Intent dietcalcintent=new Intent(Dietplan_activity.this,Dietplan_activity.class);
                        Dietplan_activity.this.startActivity(dietcalcintent);
                        break;
                    case R.id.my_diets:
                        Intent dietplansintent=new Intent(Dietplan_activity.this,MyDietPlansActivity.class);
                        Dietplan_activity.this.startActivity(dietplansintent);
                        break;
                    case R.id.home:
                        Intent intentHome= new Intent(Dietplan_activity.this,ActivityHome.class);
                        Dietplan_activity.this.startActivity(intentHome);
                        break;
                }
                return false;
            }
        });



        //ustawienie układu rv
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //stworzenie odwołania do bazy danych do ustawiania spinnera
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //zainicjowanie adaptera z argumentem odwolania sie do bazy danych
        SpinnerAdapter mAdapter = new SpinnerAdapter(databaseReference);
        //ustawienie adaptera dla recyclerview
        mRecyclerView.setAdapter(mAdapter);

        weightRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Pobierz dane i przetwórz je
                String weightStr = dataSnapshot.child("weight").getValue(String.class); // pobranie informacji o użytkowniku
                double weightValue = Double.parseDouble(weightStr);

                String heightStr= dataSnapshot.child("height").getValue(String.class);
                double heightValue = Double.parseDouble(heightStr);

                String ageStr=dataSnapshot.child("age").getValue(String.class);
                double ageValue = Double.parseDouble(ageStr);

                String genderValue = dataSnapshot.child("gender").getValue(String.class);
                String ActivityTypeValue=dataSnapshot.child("activityType").getValue(String.class);

                if (ActivityTypeValue.equals("None")){
                    activitymultiplier=1.2;
                }
                else if (ActivityTypeValue.equals("Low")){
                    activitymultiplier=1.375;
                }
                else if (ActivityTypeValue.equals("Medium")){
                    activitymultiplier=1.55;
                }
                else if (ActivityTypeValue.equals("High")){
                    activitymultiplier=1.725;
                }
                else if (ActivityTypeValue.equals("Very high")){
                    activitymultiplier=1.9;
                }

                if (genderValue.equals("M")){
                    gendercalc=88.36;
                    BMRcalc=((gendercalc+((13.4*weightValue)+(4.8*heightValue))-(5.7*ageValue))*activitymultiplier);
                    Log.d("1", String.valueOf(BMRcalc));
                } else if (genderValue.equals("K")){
                    gendercalc=447.6;
                    BMRcalc=((gendercalc+((9.2*weightValue)+(3.1*heightValue))-(4.3*ageValue))*activitymultiplier);
                }
                //obliczam zapotrzebowanie kaloryczne uzytkownika
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Obsłuż błąd pobierania danych
            }
        });



        //przycisk odpowiedzialny za obliczanie wartości do progressbara i ustawianie progressbara
        calcCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumCalories = 0;
                // Pobranie danych z TextView dla każdego elementu RecyclerView
                for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
                    View view = mRecyclerView.getChildAt(i); // Pobierz widok dla danego elementu
                    TextView textView = view.findViewById(R.id.koncowe_kcal); // Pobierz TextView z widoku
                    String text = textView.getText().toString(); // Pobierz tekst z TextView

                    double value = Double.parseDouble(text); // Konwertuj tekst na liczbę
                    sumCalories += value; //sumuj wszystkie textview z kaloriami
                }
                progressCalories.setMax((int) BMRcalc);
                progressCalories.setProgress((int) sumCalories);
                int progressPercent = (int) (sumCalories / (int) BMRcalc * 100);
                progressPercentTV.setText(progressPercent + "%");
                //ustawienia do progressbara

                Toast.makeText(Dietplan_activity.this, "Suma kalorii: " + sumCalories, Toast.LENGTH_SHORT).show();
            }
        });

        //przyciski odpowiedzialne za dodawanie i odejmowanie elementow do rv na podstawie metod znajdujacych sie w adapterze
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

        savePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlan();
            }
        });

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
}
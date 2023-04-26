package com.example.testproject;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
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
    private Button calcButt;
    private TextView progressPercentTV;

    private double sumCalories;

    private double activitymultiplier,BMRcalc,gendercalc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietplan);

        //ustawienie zmiennych do progressbara
        progressCalories = findViewById(R.id.progressBar);
        progressPercentTV = findViewById(R.id.progress_TV);
        calcButt = findViewById(R.id.calcButton);

        //ustawienie zmiennych do dodawania i odejmowania przedmiotow w RV
        addItemImg = findViewById(R.id.add_item_img);
        deleteItemImg = findViewById(R.id.delete_item_img);

        //ustawienie zmiennej do rv
        mRecyclerView = findViewById(R.id.recycler_view);

        //ustawienie układu rv
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //stworzenie odwołania do bazy danych do ustawiania spinnera
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //zainicjowanie adaptera z argumentem odwolania sie do bazy danych
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(databaseReference);
        //ustawienie adaptera dla recyclerview
        mRecyclerView.setAdapter(mAdapter);

        weightRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Pobierz dane i przetwórz je


                String weightStr = dataSnapshot.child("weight").getValue(String.class); // pobranie wartości typu String
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
                }
                else if (genderValue.equals("K")){
                    gendercalc=447.6;
                    BMRcalc=((gendercalc+((9.2*weightValue)+(3.1*heightValue))-(4.3*ageValue))*activitymultiplier);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Obsłuż błąd pobierania danych
            }
        });



        //przycisk odpowiedzialny za obliczanie wartości do progressbara i ustawianie progressbara
        calcButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumCalories = 0;
                // Pobranie danych z TextView dla każdego elementu RecyclerView
                for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
                    View view = mRecyclerView.getChildAt(i); // Pobierz widok dla danego elementu
                    TextView textView = view.findViewById(R.id.koncowe_kcal); // Pobierz TextView z widoku
                    String text = textView.getText().toString(); // Pobierz tekst z TextView
                    // Wykonaj działania na pobranych danych z TextView
                    // np. zapisz wartości do listy, wyślij dane do serwera itp.
                    double value = Double.parseDouble(text); // Konwertuj tekst na liczbę
                    sumCalories += value;
                }


                progressCalories.setMax((int) BMRcalc);
                progressCalories.setProgress((int) sumCalories);
                int progressPercent = (int) (sumCalories / (int) BMRcalc * 100);
                progressPercentTV.setText(progressPercent + "%");
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
    }
}
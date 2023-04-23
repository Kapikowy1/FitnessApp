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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dietplan_activity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    ImageView addItemImg, deleteItemImg;
    private ProgressBar progressCalories;
    private Button calcButt;
    private TextView progressPercentTV;

    private double sumCalories;


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

                progressCalories.setMax(2500);
                progressCalories.setProgress((int) sumCalories);
                int progressPercent = (int) (sumCalories / 2500 * 100);
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
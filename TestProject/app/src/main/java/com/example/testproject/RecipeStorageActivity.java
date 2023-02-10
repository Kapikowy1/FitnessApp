package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;

public class RecipeStorageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_storage);

        recyclerView=(RecyclerView) findViewById(R.id.favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainModel> options=
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers"),MainModel.class)
                        .build();
        mainAdapter=new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);



    }
    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }


























    public void saveData(String data) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("teachers.txt", MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Dane zapisane pomyślnie", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Błąd podczas zapisywania danych", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
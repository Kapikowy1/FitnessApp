package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RecipeStorageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_storage);

        recyclerView=(RecyclerView) findViewById(R.id.favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<MainModel> options=
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ulubieni_nauczyciele").child(currentUserId),MainModel.class)
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
}
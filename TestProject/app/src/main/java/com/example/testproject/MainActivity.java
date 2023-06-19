package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    Spinner recipeType;


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        recipeType= findViewById(R.id.chooseRecipeSpinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recipeType, R.layout.my_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeType.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<MainModel> options=
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers"),MainModel.class)
                        .build();

        mainAdapter=new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        // Sprawdzenie dostępności połączenia internetowego
        if (isInternetConnected()) {
            // Wykonaj operacje, gdy jest dostępne połączenie internetowe
            // ...
        } else {
            // Obsłuż przypadek braku połączenia internetowego
            Toast.makeText(this, "Brak połączenia internetowego", Toast.LENGTH_SHORT).show();
        }
        recipeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedRecipe = parent.getItemAtPosition(position).toString();
                if (selectedRecipe.equals("Wybierz rodzaj przepisu")){
                    Query query = FirebaseDatabase.getInstance().getReference().child("teachers");

                    FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(query, MainModel.class)
                            .build();
                    mainAdapter = new MainAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                    //wyświetlam rv jezeli spinner ma tekst wybierz rodzaj przepisu
                }else{
                    Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("recipeType").startAt(selectedRecipe).endAt(selectedRecipe+"\uf8ff");
                    FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(query, MainModel.class)
                            .build();
                    mainAdapter = new MainAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                    //sortuje wyniki wzgledem rodzaju przepisu
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.plan_and_calc:
                        Intent intent = new Intent(MainActivity.this, Dietplan_activity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                    case R.id.go_to_Quiz:
                        Intent intentQuiz =new Intent(MainActivity.this,Quiz_activity_Diet.class);
                        MainActivity.this.startActivity(intentQuiz);
                        break;
                    case R.id.home:
                        Intent intentHome= new Intent(MainActivity.this,ActivityHome.class);
                        MainActivity.this.startActivity(intentHome);
                        break;
                }
                return false;
            }
        });





    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    private void  txtSearch(String str) {
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("name").startAt(str).endAt(str+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
        //metoda do wyszukiwania w menu
    }


    //SORTOWANIE

    @Override //notacja @Override jest używana po to aby oznaczyć metodę, która jest nadpisaniem klasy nadrzędnej
    // ma na celu kontynuacje implementacji z klasy nadrzędnej
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item =menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        //zaimplementowanie menu
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                txtSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //WYSZUKIWANIE WYNIKOW
}
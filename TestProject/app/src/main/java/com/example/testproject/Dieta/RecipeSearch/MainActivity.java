package com.example.testproject.Dieta.RecipeSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.testproject.Dieta.Learn.LearnActivity;
import com.example.testproject.LogowanieIRejestracja.ChooseModuleActivity;
import com.example.testproject.Dieta.DietPlan.Dietplan_activity;
import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private Spinner recipeType;

    private final SparseArray<Class<?>> menuIntentMap = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView) findViewById(R.id.rv);
        recipeType= findViewById(R.id.chooseRecipeSpinner);

        setSpinnerAdapter();
        setRecyclerView();
        setupBottomNavigationView();

        if (isInternetConnected()) {
            Log.d("Internet state","connected");
        } else {
            Toast.makeText(this, "Brak połączenia internetowego", Toast.LENGTH_SHORT).show();
        }
        recipeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedRecipe = parent.getItemAtPosition(position).toString();
                if (selectedRecipe.equals("Wybierz rodzaj przepisu")){
                    resetRecyclerView();
                }else{
                    sortRecyclerview(selectedRecipe);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item =menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
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
    private void setSpinnerAdapter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.recipeType, R.layout.my_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipeType.setAdapter(adapter);
    }
    private void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<MainModel> options=
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers"),MainModel.class)
                        .build();
        mainAdapter=new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }
    private void resetRecyclerView(){
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers");
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void sortRecyclerview(String selectedRecipe){
        Query query = FirebaseDatabase.getInstance().getReference().child("teachers").orderByChild("recipeType").startAt(selectedRecipe).endAt(selectedRecipe+"\uf8ff");
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        initializeMenuIntentMap();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Class<?> intentClass = menuIntentMap.get(itemId);
                if (intentClass != null) {
                    Intent intent = new Intent(MainActivity.this, intentClass);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
    private void initializeMenuIntentMap() {
        menuIntentMap.put(R.id.plan_and_calc, Dietplan_activity.class);
        menuIntentMap.put(R.id.go_to_Quiz, LearnActivity.class);
        menuIntentMap.put(R.id.home, ChooseModuleActivity.class);
    }



}
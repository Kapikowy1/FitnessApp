package com.example.testproject.Trening;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Dieta.RecipeSearch.MainAdapter;
import com.example.testproject.Dieta.RecipeSearch.MainModel;
import com.example.testproject.LogowanieIRejestracja.ChooseModuleActivity;
import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class TrainingMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private Spinner chooseTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_main);

        chooseTypeSpinner= (Spinner) findViewById(R.id.chooseType);
        BottomNavigationView bottomnavTraining = (BottomNavigationView) findViewById(R.id.bottom_navigation_train);
        recyclerView=(RecyclerView) findViewById(R.id.rv_beginer);


        setUpChooseTypeSpinner();
        setUpRV();

        bottomnavTraining.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            private final HashMap<Integer, Class<?>> activityMap = new HashMap<>();
            {
                activityMap.put(R.id.ready_plans, ReadyPlansActivity.class);
                activityMap.put(R.id.home, ChooseModuleActivity.class);
                activityMap.put(R.id.my_training_plans_menuitem, MyTrainingPlansActivity.class);
            }

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Class<?> activityClass = activityMap.get(itemId);
                if (activityClass != null) {
                    Intent intent = new Intent(TrainingMainActivity.this, activityClass);
                    startActivity(intent);
                }
                return false;
            }
        });

        chooseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                if (selectedValue.equals("Wybierz partię")){
                    resetRecyclerView();
                }else{
                    sortRV(selectedValue);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void resetRecyclerView(){
        Query query = FirebaseDatabase.getInstance().getReference().child("exercises");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void sortRV(String selectedValue){
        Query query = FirebaseDatabase.getInstance().getReference().child("exercises").orderByChild("recipeType").startAt(selectedValue).endAt(selectedValue+"\uf8ff");
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
    private void setUpRV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        FirebaseRecyclerOptions<MainModel> options=
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("exercises"),MainModel.class)
                        .build();
        mainAdapter=new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }

    private void setUpChooseTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.exerciseType, R.layout.my_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseTypeSpinner.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }

    // kod może służyc do implementacji wyszukiwania w górnym navigation barze
    /*private void  txtSearch(String str) {
        Query query = FirebaseDatabase.getInstance().getReference().child("exercises").orderByChild("name").startAt(str).endAt(str+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
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
        super.onCreateOptionsMenu(menu, inflater);
    }*/


}
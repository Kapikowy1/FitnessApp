package com.example.testproject.Trening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.testproject.Dieta.RecipeSearch.MainAdapter;
import com.example.testproject.Dieta.RecipeSearch.MainModel;
import com.example.testproject.LogowanieIRejestracja.ChooseModuleActivity;
import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TrainingMainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    Spinner chooseTypeSpinner;

    BottomNavigationView bottomnavTraining;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_main);

        //ustawiam widoczność przycisków planów na gone


        chooseTypeSpinner= (Spinner) findViewById(R.id.chooseType);

        bottomnavTraining= (BottomNavigationView) findViewById(R.id.bottom_navigation_train);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.exerciseType, R.layout.my_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseTypeSpinner.setAdapter(adapter); //ustawiam arrayadapter dla spinnera wybierz rodzaj cwiczenia

        recyclerView=(RecyclerView) findViewById(R.id.rv_beginer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        FirebaseRecyclerOptions<MainModel> options=
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("exercises"),MainModel.class)
                        .build();
        mainAdapter=new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        //ustawiam rv dla widoku cwiczen

        bottomnavTraining.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ready_plans:
                        Intent readyplans=new Intent(TrainingMainActivity.this,ReadyPlansActivity.class);
                        startActivity(readyplans);
                        break;
                    case R.id.home:
                        Intent home=new Intent(TrainingMainActivity.this, ChooseModuleActivity.class);
                        startActivity(home);
                        break;
                    case R.id.my_training_plans_menuitem:
                        Intent mytrainingplans=new Intent(TrainingMainActivity.this, MyTrainingPlansActivity.class);
                        startActivity(mytrainingplans);
                        break;
                }
                return false;
            }
        });

        chooseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedValue = parent.getItemAtPosition(position).toString();

                if (selectedValue.equals("Wybierz partię")){//ustawiam sortowanie na spinnerze względem wpisanego rodzaju cwiczenia
                    Query query = FirebaseDatabase.getInstance().getReference().child("exercises");

                    FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(query, MainModel.class)
                            .build();
                    mainAdapter = new MainAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                }else{
                    Query query = FirebaseDatabase.getInstance().getReference().child("exercises").orderByChild("recipeType").startAt(selectedValue).endAt(selectedValue+"\uf8ff");
                    FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                            .setQuery(query, MainModel.class)
                            .build();
                    mainAdapter = new MainAdapter(options);
                    mainAdapter.startListening();
                    recyclerView.setAdapter(mainAdapter);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

    private void  txtSearch(String str) {//ustalam sposob wyszukiwania
        Query query = FirebaseDatabase.getInstance().getReference().child("exercises").orderByChild("name").startAt(str).endAt(str+"\uf8ff");

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(query, MainModel.class)
                .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        //ustawiam menu


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//ustawiam wyszukiwanie w menu
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

    /*@Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }*/
}
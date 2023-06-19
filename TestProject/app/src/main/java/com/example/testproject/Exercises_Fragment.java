package com.example.testproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class Exercises_Fragment extends Fragment {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    Spinner chooseTypeSpinner;
    LinearLayout choseplanLay;
    BottomNavigationView bottomnavTraining;

    @Override
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
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.exercises_fragment, container, false);

        choseplanLay=getActivity().findViewById(R.id.choose_plans_layout);
        choseplanLay.setVisibility(view.GONE);
        //ustawiam widoczność przycisków planów na gone


        chooseTypeSpinner= (Spinner) view.findViewById(R.id.chooseType);

        bottomnavTraining= (BottomNavigationView) view.findViewById(R.id.bottom_navigation_train);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.exerciseType, R.layout.my_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseTypeSpinner.setAdapter(adapter); //ustawiam arrayadapter dla spinnera wybierz rodzaj cwiczenia

        recyclerView=(RecyclerView) view.findViewById(R.id.rv_beginer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

                    case R.id.training_plan:
                        goToTrainingPlanFragment();
                        break;
                    case R.id.home:

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

        return view;
    }

    private void goToTrainingPlanFragment(){
        TrainingPlanFragment planFragment = new TrainingPlanFragment(); // Tworzenie instancji PlanFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager(); // Pobieranie menedżera fragmentów
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Rozpoczęcie transakcji fragmentu
        fragmentTransaction.replace(R.id.fragment_container_training, planFragment); // Zastąpienie aktualnego fragmentu PlanFragment
        fragmentTransaction.addToBackStack(null); // Dodanie transakcji do back stack (opcjonalne)
        fragmentTransaction.commit();

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

}
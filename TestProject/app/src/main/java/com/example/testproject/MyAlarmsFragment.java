package com.example.testproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MyAlarmsFragment extends Fragment {

    private AdapterHabit adapterHabit;

    private DatabaseReference habitRef;
    public MyAlarmsFragment() {
        // Required empty public constructor
    }
    public static MyAlarmsFragment newInstance(String param1, String param2) {
        MyAlarmsFragment fragment = new MyAlarmsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        habitRef = FirebaseDatabase.getInstance().getReference().child("habits").child(currentUserId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_alarms, container, false);
        ImageView addHabit=view.findViewById(R.id.new_habit_btn);
        RecyclerView recyclerView = view.findViewById(R.id.my_alarms_rv);
        // Konfiguracja FirebaseRecyclerOptions
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Configure FirebaseRecyclerOptions
        FirebaseRecyclerOptions<Habit> options = new FirebaseRecyclerOptions.Builder<Habit>()
                .setQuery(habitRef, Habit.class)
                .build();
        adapterHabit = new AdapterHabit(getContext(), options);
        recyclerView.setAdapter(adapterHabit);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlarmFragment createAlarmFragment=new CreateAlarmFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // 2. Rozpocznij transakcję fragmentu
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // 3. Zastąp bieżący fragment nowym fragmentem
                transaction.replace(R.id.fragment_container_habit, createAlarmFragment);
                // 4. Dodaj transakcję do wstecznego stosu transakcji
                transaction.addToBackStack(null);
                // 5. Potwierdź transakcję
                transaction.commit();
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Rozpoczęcie obserwacji zmian danych
        adapterHabit.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Zakończenie obserwacji zmian danych
        adapterHabit.stopListening();
    }


}
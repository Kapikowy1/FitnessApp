package com.example.testproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class TrainingPlanFragment extends Fragment {

    Plan_Rv_adapter planadapter;

    private RecyclerView planRV;

    LinearLayout choseplanLay;

    TextView myplansTV,readyplansTV;

    public TrainingPlanFragment() {
        // Required empty public constructor
    }

    public static TrainingPlanFragment newInstance(String readyPlansString, String myPlansString) {
        TrainingPlanFragment fragment = new TrainingPlanFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_training_plan, container, false);
        // Inflate the layout for this fragment


        choseplanLay=getActivity().findViewById(R.id.choose_plans_layout);
        if (choseplanLay != null) {
            choseplanLay.setVisibility(View.VISIBLE);
        } else {
            // Jeśli choseplanLay jest równa null, wykonaj odpowiednie działania lub obsłuż tę sytuację.
        }

        myplansTV=getActivity().findViewById(R.id.myPlansTV);
        readyplansTV=getActivity().findViewById(R.id.readyPlansTV);
        //ustawiam widocznosc plan przyciskow na visible



        //pobieram string z textview aby wysłać go do Plan_Rv_adapter



        planRV = view.findViewById(R.id.plans_rv);
        planRV.setLayoutManager(new LinearLayoutManager(getContext()));

        Query query = FirebaseDatabase.getInstance().getReference().child("Ready Plans").orderByKey();

        FirebaseRecyclerOptions<String> options =
                new FirebaseRecyclerOptions.Builder<String>()
                        .setQuery(query, snapshot -> snapshot.getKey())
                        .build();

        planadapter = new Plan_Rv_adapter(options);
        planRV.setAdapter(planadapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (planadapter != null) {
            planadapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (planadapter != null) {
            planadapter.stopListening();
        }
    }
}
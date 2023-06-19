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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class My_training_plans_fragment extends Fragment {


    LinearLayout choseplanLay;
    Plan_Rv_adapter planadapter;
    ImageView addPlanImg;
    TextView myplansTV,readyplansTV;
    RecyclerView myplansRecyclerView;

    private CreateTrainingPlanFragment createTrainingPlanFragment = new CreateTrainingPlanFragment();
    public My_training_plans_fragment() {
        // Required empty public constructor
    }


    public static My_training_plans_fragment newInstance(String readyPlansString, String myPlansString) {
        My_training_plans_fragment fragment = new My_training_plans_fragment();

        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_training_plans_fragment, container, false);
        // Inflate the layout for this fragment



        choseplanLay=getActivity().findViewById(R.id.choose_plans_layout);
        addPlanImg=view.findViewById(R.id.add_new_plan);
        myplansRecyclerView=view.findViewById(R.id.myplansRV);

        choseplanLay.setVisibility(view.VISIBLE);


        myplansTV=getActivity().findViewById(R.id.myPlansTV);
        readyplansTV=getActivity().findViewById(R.id.readyPlansTV);

        //ustawiam widocznosc plan przyciskow na visible



        myplansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Tutaj możesz użyć currentUserId w swoim kodzie
            Query query = FirebaseDatabase.getInstance().getReference().child("My plans").child(currentUserId);
            FirebaseRecyclerOptions<String> options =
                    new FirebaseRecyclerOptions.Builder<String>()
                            .setQuery(query, snapshot -> snapshot.getKey())
                            .build();
            planadapter = new Plan_Rv_adapter(options);
            myplansRecyclerView.setAdapter(planadapter);
        } else {
            Toast.makeText(getContext(), "Zaloguj się ponownie", Toast.LENGTH_SHORT).show();
        }


        addPlanImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreatePlanFragment();
            }
        });



        return view;
    }

    private void goToCreatePlanFragment(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_training, createTrainingPlanFragment);
        fragmentTransaction.commit();
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
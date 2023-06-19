package com.example.testproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CreateTrainingPlanFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView addExercise,delExercise;
    private Button createPlanBtn;
    private EditText plansNameEditText;
    private List<String> selectedExerciseNames;
    private String planNamesText;
    public CreateTrainingPlanFragment() {
        // Required empty public constructor
    }

    public static CreateTrainingPlanFragment newInstance(String param1, String param2) {
        CreateTrainingPlanFragment fragment = new CreateTrainingPlanFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);
        selectedExerciseNames = new ArrayList<>();
        recyclerView=view.findViewById(R.id.rv_plan_create);
        addExercise=view.findViewById(R.id.add_exercise_img);
        delExercise=view.findViewById(R.id.delete_exercise_img);
        createPlanBtn=view.findViewById(R.id.save_plan_btn);
        plansNameEditText=view.findViewById(R.id.my_plan_name_ET);


        plansNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planNamesText= String.valueOf(plansNameEditText.getText());
            }

            @Override
            public void afterTextChanged(Editable s) {
                planNamesText= String.valueOf(plansNameEditText.getText());
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //stworzenie odwołania do bazy danych do ustawiania spinnera
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //zainicjowanie adaptera z argumentem odwolania sie do bazy danych
        ExercisesPlanAdapter eAdapter = new ExercisesPlanAdapter(databaseReference);
        //ustawienie adaptera dla recyclerview
        recyclerView.setAdapter(eAdapter);

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eAdapter.addItem();
            }
        });
        //dodaj item do planu

        delExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eAdapter.delItem();
            }
        });
        //usun item z planu

        createPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (planNamesText == null || planNamesText.isEmpty()) {
                    showToast("Proszę wypełnić nazwę planu");
                } else {
                    saveExercisesToDatabase();
                }
            }
        });
        return view;
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void saveExercisesToDatabase() {
        DatabaseReference plansRef = FirebaseDatabase.getInstance().getReference().child("My plans");
        String currentUserIdd = FirebaseAuth.getInstance().getCurrentUser().getUid();
        plansRef.child(currentUserIdd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(planNamesText)) {
                    showToast("Plan o tej nazwie już istnieje");
                } else {
                    for (int i = 0; i < recyclerView.getChildCount(); i++) {
                        View view = recyclerView.getChildAt(i);
                        TextView spinnerTV = view.findViewById(R.id.spinner_TV);
                        EditText seriesET = view.findViewById(R.id.serieET);
                        EditText repeatsET = view.findViewById(R.id.powtorzeniaET);
                        String spinnerText = spinnerTV.getText().toString();
                        String seriesText = seriesET.getText().toString();
                        String repeatsText = repeatsET.getText().toString();
                        if (spinnerText != null && !spinnerText.equals("Wybierz ćwiczenie")) {
                            DatabaseReference exercisesRef = FirebaseDatabase.getInstance().getReference().child("exercises");
                            exercisesRef.orderByChild("name").equalTo(spinnerText).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot exerciseSnapshot : snapshot.getChildren()) {
                                        saveExerciseToPlan(exerciseSnapshot, seriesText, repeatsText);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    showToast("Prosze wybrać ćwiczenie");
                                }
                            });
                        } else {
                            showToast("Prosze wybrać ćwiczenie");
                        }
                    }
                    showToast("Plan dodano pomyślnie");
                    goToMyTrainingPlansFragment();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Błąd odczytu z bazy danych");
            }
        });
    }


    private void saveExerciseToPlan(DataSnapshot exerciseSnapshot, String seriesText, String repeatsText) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String name = exerciseSnapshot.child("name").getValue(String.class);
        String turl = exerciseSnapshot.child("turl").getValue(String.class);
        String recipeType = exerciseSnapshot.child("recipeType").getValue(String.class);
        String description = exerciseSnapshot.child("description").getValue(String.class);

        String currentUserIdd = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String exerciseId = databaseReference.child("My plans").child(currentUserIdd).child(planNamesText).push().getKey();
        DatabaseReference exerciseRef = databaseReference.child("My plans").child(currentUserIdd).child(planNamesText).child(exerciseId);

        exerciseRef.child("reps").setValue(repeatsText);
        exerciseRef.child("sets").setValue(seriesText);
        exerciseRef.child("name").setValue(name);
        exerciseRef.child("turl").setValue(turl);
        exerciseRef.child("recipeType").setValue(recipeType);
        exerciseRef.child("description").setValue(description);
    }

    public void goToMyTrainingPlansFragment() {
        My_training_plans_fragment my_training_plans_fragment=new My_training_plans_fragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_training, my_training_plans_fragment);
        fragmentTransaction.commit();
    }




}
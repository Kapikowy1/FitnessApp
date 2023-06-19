package com.example.testproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAlarmFragment extends Fragment {


    private EditText editTextTime, editTextDate, editTextName, editTextPlace;
    private Spinner spinnerFrequency;
    private Button buttonAddAlarm;

    DatabaseReference habitsRef = FirebaseDatabase.getInstance().getReference().child("habits");
    public CreateAlarmFragment() {
        // Required empty public constructor
    }


    public static CreateAlarmFragment newInstance(String param1, String param2) {
        CreateAlarmFragment fragment = new CreateAlarmFragment();
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
        View view= inflater.inflate(R.layout.fragment_create_alarm, container, false);

        editTextTime = view.findViewById(R.id.editTextTime);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextName = view.findViewById(R.id.edit_text_name);
        editTextPlace = view.findViewById(R.id.edit_text_place);
        spinnerFrequency = view.findViewById(R.id.spinnerFrequency);
        buttonAddAlarm = view.findViewById(R.id.buttonAddAlarm);

        buttonAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = editTextTime.getText().toString();
                String date = editTextDate.getText().toString();
                String name = editTextName.getText().toString();
                String place = editTextPlace.getText().toString();
                String frequency = spinnerFrequency.getSelectedItem().toString();

                saveHabitToDatabase(time, date, name, place, frequency);
                replaceFragment();
                Toast.makeText(getContext(), "Dodano alarm", Toast.LENGTH_SHORT).show();
            }
        });

        // Usuwanie alarmu


        // Inflate the layout for this fragment
        return view;
    }
    private void replaceFragment(){
        MyAlarmsFragment myAlarmsFragment=new MyAlarmsFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // 2. Rozpocznij transakcję fragmentu
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 3. Zastąp bieżący fragment nowym fragmentem
        transaction.replace(R.id.fragment_container_habit, myAlarmsFragment);
        // 4. Dodaj transakcję do wstecznego stosu transakcji
        transaction.addToBackStack(null);
        // 5. Potwierdź transakcję
        transaction.commit();
    }
    private void saveHabitToDatabase(String time, String date, String name, String place, String frequency) {
        habitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUserIdd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference newHabitRef = databaseReference.child("habits").child(currentUserIdd).child(name);

                newHabitRef.child("habitDone").setValue(false);
                newHabitRef.child("onAlarm").setValue(true);
                newHabitRef.child("frequency").setValue(frequency);
                newHabitRef.child("name").setValue(name);
                newHabitRef.child("date").setValue(date);
                newHabitRef.child("hour").setValue(time);
                newHabitRef.child("place").setValue(place);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Błąd bazy danych", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

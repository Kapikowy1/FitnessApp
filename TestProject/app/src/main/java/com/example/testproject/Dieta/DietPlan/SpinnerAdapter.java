package com.example.testproject.Dieta.DietPlan;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.ViewHolder> {
    private DatabaseReference mDatabase;
    private final List<String> mSpinnerData;
    int rvElementCount =3;
    public SpinnerAdapter(DatabaseReference databaseReference) {
        mDatabase = databaseReference;
        mSpinnerData = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                fetchSpinnerData(dataSnapshot);
                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RecyclerViewAdapter", "Error while getting data from Firebase", databaseError.toException());
            }
        });
        //ustawiam listę names w spinnerze
    }
    private void fetchSpinnerData(DataSnapshot dataSnapshot){
        mSpinnerData.clear();
        mSpinnerData.add("Wybierz Potrawe");
        for (DataSnapshot recipeSnapshot : dataSnapshot.child("teachers").getChildren()) {
            String recipeName = recipeSnapshot.child("name").getValue(String.class);
            mSpinnerData.add(recipeName);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_spinner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setCalories();
        holder.bind(mSpinnerData);
    }

    @Override
    public int getItemCount() {
        return rvElementCount;
    } //ustawiam ilość spinnerów

    public void addItem() {
        rvElementCount++;
        notifyItemInserted(mSpinnerData.size());
    }


    public void delItem() {
        rvElementCount--;
        notifyItemRemoved(mSpinnerData.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {
        private final Spinner mSpinner;
        private final TextView textViewCalories;
        public EditText multiplierEditText;

        ViewHolder(View itemView) {
            super(itemView);
            mSpinner = itemView.findViewById(R.id.spinner);
            textViewCalories = itemView.findViewById(R.id.koncowe_kcal);
            multiplierEditText = itemView.findViewById(R.id.mnoznik_kcal);
            multiplierEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    setCalories();
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    setCalories();
                }
            });
            mSpinner.setOnItemSelectedListener(this);
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
            String selectedRecipeName = parent.getItemAtPosition(position).toString();
            mDatabase.child("teachers").orderByChild("name").equalTo(selectedRecipeName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(selectedRecipeName.equals("Wybierz Potrawe")){
                                textViewCalories.setText("1");
                            }else {
                                setCalories();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
        public void setCalories() {
            String nameRecipe = mSpinner.getSelectedItem() != null ? mSpinner.getSelectedItem().toString() : "";
            if (!nameRecipe.isEmpty()) {
                mDatabase.child("teachers").orderByChild("name").equalTo(nameRecipe)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                                    fetchCaloriesData(recipeSnapshot);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                }
        }

        private void fetchCaloriesData(DataSnapshot recipeSnapshot) {
            double calories = recipeSnapshot.child("calories").getValue(Integer.class);
            String multiplierString = multiplierEditText.getText().toString();
            double mnoznikkalorii;
            try {
                mnoznikkalorii = Double.parseDouble(multiplierString);
            } catch (NumberFormatException e) {
                mnoznikkalorii = 0;
            }
            calculateKoncoweKalorie(calories,mnoznikkalorii);
        }
        private void calculateKoncoweKalorie(double calories,double mnoznikkalorii){
            double koncowekalorie = calories * mnoznikkalorii;
            textViewCalories.setText(String.valueOf(koncowekalorie));
        }

        void bind(List<String> spinnerData) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, spinnerData);
            mSpinner.setAdapter(adapter);
        }
    }


}


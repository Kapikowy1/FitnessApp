package com.example.testproject.Trening;



import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

public class ExercisesPlanAdapter extends RecyclerView.Adapter<ExercisesPlanAdapter.ViewHolder> {
    private final DatabaseReference mDatabase;
    private final List<String> dataToDialog;


    public ExercisesPlanAdapter(DatabaseReference databaseReference) {
        mDatabase = databaseReference;
        dataToDialog = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataToDialog.clear();
                dataToDialog.add("Wybierz cwiczenie");
                for (DataSnapshot exerciseSnapshot : dataSnapshot.child("exercises").getChildren()) {
                    String exerciseName = exerciseSnapshot.child("name").getValue(String.class);
                    dataToDialog.add(exerciseName);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ExercisesPlanAdapter", "Error while getting data from Firebase", databaseError.toException());
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ViewHolder(view);
    }//ustawiam wyglad elementu

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(dataToDialog);

    }
    int liczbaelementowRV=1;
    @Override
    public int getItemCount() {
        return liczbaelementowRV;
    }

    public void addItem() {
        liczbaelementowRV++;
        notifyItemInserted(dataToDialog.size());
        Log.d("Czy się wywoluje?", String.valueOf(liczbaelementowRV));
    }
    public void delItem() {
        liczbaelementowRV--;
        notifyItemRemoved(dataToDialog.size());
        Log.d("Czy się wywoluje?", String.valueOf(liczbaelementowRV));
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewPartia,spinnerTV;
        private Dialog dialog;

        ViewHolder(View itemView) {
            super(itemView);

            spinnerTV = itemView.findViewById(R.id.spinner_TV);
            textViewPartia = itemView.findViewById(R.id.partiaTV);
            EditText powtorzeniaEditText = itemView.findViewById(R.id.powtorzeniaET);


            spinnerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog=new Dialog(itemView.getContext());
                    dialog.setContentView(R.layout.dialog_searchable_spinner);

                    dialog.getWindow().setLayout(650,800);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    EditText editText = dialog.findViewById(R.id.edit_text);
                    ListView listView = dialog.findViewById(R.id.list_view);

                    ArrayAdapter<String> adapter= new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_list_item_1,dataToDialog);

                    listView.setAdapter(adapter);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedExerciseName = adapter.getItem(position);
                            spinnerTV.setText(selectedExerciseName);
                            dialog.dismiss();


                            mDatabase.child("exercises").orderByChild("name").equalTo(selectedExerciseName)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (selectedExerciseName.equals("Wybierz cwiczenie")){
                                                textViewPartia.setText("Type");
                                            }else {
                                                for (DataSnapshot exerciseSnapshot : snapshot.getChildren()) {

                                                    String exerGroup = exerciseSnapshot.child("recipeType").getValue(String.class);
                                                    textViewPartia.setText(exerGroup);
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                            }
                    });
                }
            });

            powtorzeniaEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
        void bind(List<String> spinnerData) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, spinnerData);
        }
    }
}


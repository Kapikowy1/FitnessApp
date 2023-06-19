package com.example.testproject;



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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExercisesPlanAdapter extends RecyclerView.Adapter<ExercisesPlanAdapter.ViewHolder> {
    private DatabaseReference mDatabase;
    private List<String> dataToDialog;
    int liczbaelementowRV=1;




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
                //tutaj tworze dane do dialogu ktory wyskakuje po kliknieciu spinnerTV
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ExercisesPlanAdapter", "Error while getting data from Firebase", databaseError.toException());
            }
        });

        //ustawiam listę names w spinnerze
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
        // przesyłam listę names z bazy danych do spinnera
    }

    @Override
    public int getItemCount() {
        return liczbaelementowRV;
    } //ustawiam ilość spinnerów

    public void addItem() {
        liczbaelementowRV++;
        notifyItemInserted(dataToDialog.size());
    }
    //dodaję item i aktualizuje widok

    public void delItem() {
        liczbaelementowRV--;
        notifyItemRemoved(dataToDialog.size());
    }
    //usuwam item i aktualizuje widok
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewPartia,spinnerTV;
        public EditText powtorzeniaEditText,serieEditText;

        Dialog dialog;

        //inicjuje zmienne dla obiektów w pojedyńczym itemie
        ViewHolder(View itemView) {
            super(itemView);

            spinnerTV = itemView.findViewById(R.id.spinner_TV);
            textViewPartia = itemView.findViewById(R.id.partiaTV);
            powtorzeniaEditText = itemView.findViewById(R.id.powtorzeniaET);
            serieEditText= itemView.findViewById(R.id.serieET);


            // inicjuje obiekty dla pojedyńczego itemu


            spinnerTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog=new Dialog(itemView.getContext());
                    dialog.setContentView(R.layout.dialog_searchable_spinner);

                    dialog.getWindow().setLayout(650,800);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    //ustawiam wyglad dialogu
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
                            adapter.getFilter().filter(s); //sortowanie wynikow w dialogu
                        }
                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedExerciseName = adapter.getItem(position); // Użyj adaptera do pobrania wybranego elementu
                            spinnerTV.setText(selectedExerciseName); // Ustaw tekst na spinnerTV
                            dialog.dismiss();
                            //tworze string o wartosci wybranej pozycji dialogu

                            mDatabase.child("exercises").orderByChild("name").equalTo(selectedExerciseName)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (selectedExerciseName.equals("Wybierz cwiczenie")){
                                                textViewPartia.setText("Type");
                                                //ustawiam tv na type jezeli nie wybrano zadnego cwiczenia
                                            }else {
                                                for (DataSnapshot exerciseSnapshot : snapshot.getChildren()) {

                                                    String exerGroup = exerciseSnapshot.child("recipeType").getValue(String.class);
                                                    textViewPartia.setText(exerGroup);
                                                    //ustawiam textview na partie ktora jest przyporzadkowana do danego name
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
            //nieuzyty textwatcher na powtorzeniaet




        }





        void bind(List<String> spinnerData) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_dropdown_item, spinnerData);


        }
    }


}


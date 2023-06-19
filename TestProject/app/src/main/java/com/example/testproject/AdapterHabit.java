package com.example.testproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AdapterHabit extends FirebaseRecyclerAdapter<Habit, AdapterHabit.ViewHolder> {

    private Context context;
    private boolean isLongClickStarted;
    private Timer longClickTimer;
    private ArrayList<String> alarmgroup;
    public AdapterHabit(Context context, @NonNull FirebaseRecyclerOptions<Habit> options) {
        super(options);
        this.context = context;
        this.isLongClickStarted = false;
        this.longClickTimer = new Timer();
        this.alarmgroup=new ArrayList<>();

        // Pobranie danych z bazy Firebase
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference habitsRef = FirebaseDatabase.getInstance().getReference()
                .child("habits")
                .child(currentUserID);

        habitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                        Habit habit = habitSnapshot.getValue(Habit.class);
                        if (habit != null) {
                            //dodawanie godzin do listy
                            alarmgroup.add(habit.getHour());
                        }
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Obsługa błędu pobierania danych z bazy danych
            }
        });
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.habit_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Habit habit) {
        holder.habit = habit;
        holder.habitNameTextView.setText(habit.getName());
        holder.habitHourTextView.setText(habit.getHour());
        holder.habitPlaceTextView.setText(habit.getPlace());
        holder.switchAlarm.setChecked(habit.isOnAlarm());
        holder.switchAlarm.setText(habit.getDate());
        holder.switchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAlarmOn = holder.switchAlarm.isChecked();

                // Zaktualizuj wartość isOnAlarm w bazie danych
                DatabaseReference habitRef = getRef(holder.getAbsoluteAdapterPosition());
                habitRef.child("onAlarm").setValue(isAlarmOn);
            }
        });

        final boolean[] isChecked = {habit.isHabitDone()};
        if (isChecked[0]) {
            holder.doneHabitImageView.setImageResource(R.drawable.check_box_full);
        } else {
            holder.doneHabitImageView.setImageResource(R.drawable.check_box_empty);
        }
        holder.doneHabitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Odwróć wartość isChecked
                isChecked[0] = !isChecked[0];

                // Zaktualizuj habitDone w bazie danych
                DatabaseReference habitRef = getRef(holder.getAbsoluteAdapterPosition());
                habitRef.child("habitDone").setValue(isChecked[0]);

                // Zmień drawable na podstawie nowej wartości isChecked
                if (habit.isHabitDone()) {
                    holder.doneHabitImageView.setImageResource(R.drawable.check_box_full);
                } else {
                    holder.doneHabitImageView.setImageResource(R.drawable.check_box_empty);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startLongClickTimer(habit.getName(), habit.getHour(), habit.getPlace());
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLongClickStarted) {
                    cancelLongClickTimer();
                    isLongClickStarted = false;
                    // Wykonaj akcję po zakończeniu przytrzymania przez 5 sekund
                    // Tutaj możesz umieścić kod do wykonania odpowiedniej akcji
                } else {
                    // Wykonaj akcję po kliknięciu na element (bez przytrzymania)
                    // Tutaj możesz umieścić kod do wykonania odpowiedniej akcji
                }
            }
        });
    }
    private void startLongClickTimer(String name, String hour, String place) {
        isLongClickStarted = true;
        longClickTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isLongClickStarted = false;
                // Wywołanie metody na głównym wątku, ponieważ zmiany UI można wykonywać tylko z głównego wątku
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDeleteDialog(name, hour, place);
                    }
                });
            }
        }, 2000); // 2000 ms = 2 sekundy
    }
    private void showDeleteDialog(String name, String hour, String place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Usuń");
        builder.setMessage("Czy na pewno chcesz usunąć ten element?");
        builder.setIcon(R.drawable.trashcan);
        builder.setPositiveButton("Tak", (dialog, which) -> {
            // Tutaj możesz umieścić kod do obsługi usuwania elementu
            // Możesz skorzystać z wartości name, hour, place do identyfikacji elementu do usunięcia
            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference habitRef = FirebaseDatabase.getInstance().getReference()
                    .child("habits")
                    .child(currentUserID);
            Query deleteQuery = habitRef.orderByChild("name").equalTo(name);
            deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                        Habit habit = habitSnapshot.getValue(Habit.class);
                        if (habit != null && habit.getPlace().equals(place)) {
                            habitSnapshot.getRef().removeValue();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Obsługa błędu związana z usuwaniem
                    Toast.makeText(context, "Wystąpił błąd podczas usuwania elementu", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });
        builder.setNegativeButton("Nie", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void cancelLongClickTimer() {
        longClickTimer.cancel();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView habitNameTextView;
        TextView habitHourTextView;
        TextView habitPlaceTextView;
        Switch switchAlarm;
        ImageView doneHabitImageView;
        Habit habit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            habitNameTextView = itemView.findViewById(R.id.habit_name);
            habitHourTextView = itemView.findViewById(R.id.habit_hour);
            habitPlaceTextView = itemView.findViewById(R.id.habit_place);
            switchAlarm = itemView.findViewById(R.id.on_of_alarm);
            doneHabitImageView = itemView.findViewById(R.id.done_habit_box);
        }

    }


}

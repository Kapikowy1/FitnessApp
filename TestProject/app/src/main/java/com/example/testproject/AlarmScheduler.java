package com.example.testproject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

public class AlarmScheduler {

    private static final String WAKELOCK_TAG_PREFIX = "myapp";
    private static final String TAG = WAKELOCK_TAG_PREFIX + ":AlarmScheduler";
    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private ArrayList<String> alarmgroup,alarmNames;
    private ArrayList<Boolean> alarmStates;

    public AlarmScheduler(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.alarmIntent = createAlarmIntent();
        alarmgroup = new ArrayList<>();
        alarmNames=new ArrayList<>();
        alarmStates=new ArrayList<>();
        scheduleAlarmsFromFirebase();
    }

    public void scheduleAlarmsFromFirebase() {
        // Pobierz dane z bazy danych Firebase i zastosuj je do ustawiania alarmów
        // Przykładowo, pobierz listę godzin alarmów
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference habitsRef = FirebaseDatabase.getInstance().getReference()
                .child("habits")
                .child(currentUserID);
        habitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    alarmgroup.clear(); // Wyczyść listę przed dodaniem nowych elementów
                    alarmStates.clear();
                    for (Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator(); iterator.hasNext();) {
                        DataSnapshot habitSnapshot = iterator.next();
                        Habit habit = habitSnapshot.getValue(Habit.class);
                        if (habit != null) {
                            alarmgroup.add(habit.getHour());
                            alarmNames.add(habit.getName());
                            alarmStates.add(habit.isOnAlarm());
                        }
                    }
                    Log.d("Tag", "111111" + alarmgroup);
                    Log.d("Tag","22222222"+alarmStates);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Obsługa błędu pobierania danych z bazy danych
            }
        });
        // Iteruj przez listę godzin alarmów
        for (String alarmTime : alarmgroup) {
            // Ustaw alarmy na podstawie godzin z bazy danych
            scheduleAlarm(alarmTime);
        }

    }
    private PendingIntent createAlarmIntent() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void scheduleAlarm(String alarmTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, getHourFromTime(alarmTime));
        calendar.set(Calendar.MINUTE, getMinuteFromTime(alarmTime));
        calendar.set(Calendar.SECOND, 0);

        // Ustaw alarm na konkretną godzinę
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        Log.d(TAG, "Scheduled alarm for: " + alarmTime);
    }

    private int getHourFromTime(String time) {
        // Pobierz godzinę z ciągu czasu w formacie "HH:mm"
        String[] timeParts = time.split(":");
        return Integer.parseInt(timeParts[0]);
    }

    private int getMinuteFromTime(String time) {
        // Pobierz minutę z ciągu czasu w formacie "HH:mm"
        String[] timeParts = time.split(":");
        return Integer.parseInt(timeParts[1]);
    }

    public void startBackgroundCheck() {
        // Uruchom wątek sprawdzający co minutę, czy alarm powinien się włączyć
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // Sprawdź, czy czas alarmu osiągnięty
                    if (checkIfAlarmTime()) {
                        // Wywołaj metodę do obsługi alarmu, np. wywołaj wibrację
                        handleAlarm();
                    }

                    try {
                        // Oczekuj jedną minutę
                        Thread.sleep(60000);
                        Log.d("startbackgroundcheck", "wykonalo sie" + alarmgroup);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private boolean checkIfAlarmTime() {
        ArrayList<String> clonedAlarmGroup = new ArrayList<>(alarmgroup);
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        // Sprawdź, czy aktualna godzina i minuta pasują do godzin alarmów z listy sklonowanej
        for (String alarmTime : clonedAlarmGroup) {
            int alarmHour = getHourFromTime(alarmTime);
            int alarmMinute = getMinuteFromTime(alarmTime);
            if (currentHour == alarmHour && currentMinute == alarmMinute) {
                Log.d("TAG", "Czas alarmu osiągnięty");
                return true; // Czas alarmu osiągnięty
            }
        }
        Log.d("TAG", "Czas alarmu nieosiągnięty");
        return false; // Czas alarmu nieosiągnięty
    }



    private void handleAlarm() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG_PREFIX + ":mywakelocktag");
        wakeLock.acquire(10000);

        // Pobierz aktualną godzinę
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String currentTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        // Pobierz nazwę alarmu na podstawie indeksu z alarmNames
        int alarmIndex = getAlarmIndexForTime(currentTime);
        String alarmName = alarmIndex != -1 ? alarmNames.get(alarmIndex) : "Unknown Alarm";
        // Tworzenie powiadomienia systemowego
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentTitle("Alarm")
                .setContentText("Godzina alarmu: " + currentTime + ", Nazwa alarmu: " + alarmName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        // Sprawdzenie uprawnień do wyświetlania powiadomień (NOTIFICATION_POLICY)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null && notificationManager.isNotificationPolicyAccessGranted()) {
            // Jeśli uprawnienia są nadane, wyświetl powiadomienie
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManagerCompat.notify(0, builder.build());
        } else {
            // Jeśli uprawnienia nie są nadane, poproś użytkownika o uprawnienia
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            context.startActivity(intent);
            Log.e(TAG, "Brak uprawnień do wyświetlania powiadomień.");
        }

        // Obsłuż alarm, np. włącz wibrację
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Wibracja przez 2 sekundy
            long[] pattern = {0, 2000};
            vibrator.vibrate(pattern, -1);
        }
        wakeLock.release();
    }

    private int getAlarmIndexForTime(String time) {
        for (int i = 0; i < alarmgroup.size(); i++) {
            String alarmTime = alarmgroup.get(i);
            if (time.equals(alarmTime)) {
                return i;
            }
        }
        return -1;
    }



    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Przechwyć zdarzenie alarmu, jeśli konieczne
        }
    }
}



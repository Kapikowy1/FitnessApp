package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TeacherActivity extends AppCompatActivity {
    ImageView teacherImage,heartImg,ImgRecepieStore,ImgRecepiedelete;
    TextView teacherName,teacherDesc;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);


        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String teacherDescript =getIntent().getStringExtra("description");



        heartImg=findViewById(R.id.saveRecipe);
        ImgRecepieStore=findViewById(R.id.myRecipes);
        ImgRecepiedelete=findViewById(R.id.deleteRecipe);
        teacherDesc = findViewById(R.id.TeacherDescription);
        teacherName = findViewById(R.id.TeacherName);
        teacherImage=(ImageView) findViewById(R.id.TeacherImage);

        Glide.with(this)
                .load(imageUrl)
                .into(teacherImage);
        teacherDesc.setText(Html.fromHtml(teacherDescript, Html.FROM_HTML_MODE_LEGACY));

        teacherName.setText(name);






        ImgRecepieStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherActivity.this,RecipeStorageActivity.class);
                startActivity(intent);
            }
        });

        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference favoriteTeachersRef = databaseReference.child("ulubieni_nauczyciele").child(currentUserId);
        String teacherId = favoriteTeachersRef.push().getKey();




        favoriteTeachersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String favname = getIntent().getStringExtra("name");
                for (DataSnapshot teacherSnapshot : snapshot.getChildren()) {

                    //tworze string name o wartosci name z bazy danych
                    String name = teacherSnapshot.child("name").getValue(String.class);
                    //tworze boolean isFav o wartosci isfav z bazy danych
                    boolean isFav = Boolean.TRUE.equals(teacherSnapshot.child("isfav").getValue(Boolean.class));


                    if (name.equals(favname)) { //jezeli string name zadeklarowany w petli for jest rowny stringowi favname
                        if (isFav) { //jezeli isFav=true
                            heartImg.setImageResource(R.drawable.heartful);

                            // already a favorite
                            Toast.makeText(TeacherActivity.this, "Ten nauczyciel już jest w ulubionych.", Toast.LENGTH_SHORT).show();
                        } else { // w przeciwnym wypadku
                            // mark as favorite
                            heartImg.setImageResource(R.drawable.heartempty);

                            teacherSnapshot.child("isfav").getRef().setValue(true);
                            Toast.makeText(TeacherActivity.this, "Nauczyciel dodany do ulubionych.", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        heartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String favname = getIntent().getStringExtra("name");
                String favdesc = getIntent().getStringExtra("description");
                String favimageUrl = getIntent().getStringExtra("imageUrl");
                String favcourse = getIntent().getStringExtra("course");

// Pobieranie listy nauczycieli z gałęzi ulubionych nauczycieli

                favoriteTeachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //tworze pętle iterującą przez każdy element listy nauczycieli co ma na celu sprawdzenie czy kazdy z nich spelnia warunki pętli
                        for (DataSnapshot teacherSnapshot : snapshot.getChildren()) {

                            //tworze string name o wartosci name z bazy danych
                            String name = teacherSnapshot.child("name").getValue(String.class);
                            //tworze boolean isFav o wartosci isfav z bazy danych
                            boolean isFav = Boolean.TRUE.equals(teacherSnapshot.child("isfav").getValue(Boolean.class));


                            if (name.equals(favname)) { //jezeli string name zadeklarowany w petli for jest rowny stringowi favname
                                if (isFav) { //jezeli isFav=true
                                    heartImg.setImageResource(R.drawable.heartful);

                                    // already a favorite
                                    Toast.makeText(TeacherActivity.this, "Ten nauczyciel już jest w ulubionych.", Toast.LENGTH_SHORT).show();

                                } else { // w przeciwnym wypadku
                                    // mark as favorite
                                    heartImg.setImageResource(R.drawable.heartempty);

                                    teacherSnapshot.child("isfav").getRef().setValue(true);
                                    Toast.makeText(TeacherActivity.this, "Nauczyciel dodany do ulubionych.", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }


                        }
                        // not a favorite yet, add to favorites
                        favoriteTeachersRef.child(teacherId).child("name").setValue(favname);

                        favoriteTeachersRef.child(teacherId).child("description").setValue(favdesc);

                        favoriteTeachersRef.child(teacherId).child("turl").setValue(favimageUrl);
                        favoriteTeachersRef.child(teacherId).child("course").setValue(favcourse);
                        favoriteTeachersRef.child(teacherId).child("isfav").setValue(true);
                        Toast.makeText(TeacherActivity.this, "Nauczyciel dodany do ulubionych.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Błąd pobierania ulubionych nauczycieli: " + error.getMessage());
                    }
                });



            }
        });




        ImgRecepiedelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserId = mAuth.getCurrentUser().getUid();

                DatabaseReference favoriteTeachersRef = databaseReference.child("ulubieni_nauczyciele").child(currentUserId);
                favoriteTeachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    String favname = getIntent().getStringExtra("name");
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot teacherSnapshot : snapshot.getChildren()) {
                            String name = teacherSnapshot.child("name").getValue(String.class);
                            boolean isFav = Boolean.TRUE.equals(teacherSnapshot.child("isfav").getValue(Boolean.class));
                            if (name.equals(favname) && isFav) {
                                heartImg.setImageResource(R.drawable.heartempty);
                                // found favorite teacher, remove from favorites
                                teacherSnapshot.getRef().removeValue();
                                Toast.makeText(TeacherActivity.this, "Nauczyciel usunięty z ulubionych.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        // teacher not found or not a favorite
                        Toast.makeText(TeacherActivity.this, "Nie można usunąć nauczyciela z ulubionych.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Błąd pobierania ulubionych nauczycieli: " + error.getMessage());
                    }
                });




            }
        });
    }
}
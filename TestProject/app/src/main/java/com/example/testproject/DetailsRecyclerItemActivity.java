package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.Html;

import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DetailsRecyclerItemActivity extends AppCompatActivity {
    private ImageView recipeImage;
    private TextView recipeName,recipeDesc;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String recipeDescript =getIntent().getStringExtra("description");


        recipeDesc = findViewById(R.id.recipe_description);
        recipeName = findViewById(R.id.recipe_name);
        recipeImage=(ImageView) findViewById(R.id.recipe_image);

        Glide.with(this)
                .load(imageUrl)
                .into(recipeImage);
        recipeDesc.setText(Html.fromHtml(recipeDescript, Html.FROM_HTML_MODE_LEGACY));
        recipeName.setText(name);






        /*ImgRecepieStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecipeActivity.this,RecipeStorageActivity.class);
                startActivity(intent);
            }
        });*/

        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference favoriteReciperef = databaseReference.child("ulubione_przepisy").child(currentUserId);
        String teacherId = favoriteReciperef.push().getKey();




       /* favoriteReciperef.addValueEventListener(new ValueEventListener() {
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

                        } else { // w przeciwnym wypadku
                            // mark as favorite
                            heartImg.setImageResource(R.drawable.heartempty);
                            teacherSnapshot.child("isfav").getRef().setValue(true);
                        }
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
       /* heartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String favname = getIntent().getStringExtra("name");
                String favdesc = getIntent().getStringExtra("description");
                String favimageUrl = getIntent().getStringExtra("imageUrl");
                String favcourse = getIntent().getStringExtra("course");

// Pobieranie listy nauczycieli z gałęzi ulubionych nauczycieli

                favoriteReciperef.addListenerForSingleValueEvent(new ValueEventListener() {
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


                                } else { // w przeciwnym wypadku
                                    // mark as favorite
                                    heartImg.setImageResource(R.drawable.heartempty);

                                    teacherSnapshot.child("isfav").getRef().setValue(true);

                                }
                                return;
                            }


                        }
                        // not a favorite yet, add to favorites
                        favoriteReciperef.child(teacherId).child("name").setValue(favname);

                        favoriteReciperef.child(teacherId).child("description").setValue(favdesc);

                        favoriteReciperef.child(teacherId).child("turl").setValue(favimageUrl);
                        favoriteReciperef.child(teacherId).child("course").setValue(favcourse);
                        favoriteReciperef.child(teacherId).child("isfav").setValue(true);

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

                DatabaseReference favoriteTeachersRef = databaseReference.child("ulubione_przepisy").child(currentUserId);
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

                                return;
                            }
                        }
                        // teacher not found or not a favorite

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Błąd pobierania ulubionych nauczycieli: " + error.getMessage());
                    }
                });




            }
        });*/
    }
}
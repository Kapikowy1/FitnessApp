package com.example.testproject.Trening;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.Html;

import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.testproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DetailsRecyclerItemActivity extends AppCompatActivity {
    private ImageView recipeImage;
    private TextView recipeName,recipeDesc;

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



    }
}
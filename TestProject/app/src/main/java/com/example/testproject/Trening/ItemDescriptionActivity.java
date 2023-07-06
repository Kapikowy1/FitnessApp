package com.example.testproject.Trening;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.text.Html;

import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.testproject.R;


public class ItemDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String recipeDescript =getIntent().getStringExtra("description");

        TextView recipeDesc = findViewById(R.id.recipe_description);
        TextView recipeName = findViewById(R.id.recipe_name);
        ImageView recipeImage = (ImageView) findViewById(R.id.recipe_image);

        Glide.with(this)
                .load(imageUrl)
                .into(recipeImage);
        recipeDesc.setText(Html.fromHtml(recipeDescript, Html.FROM_HTML_MODE_LEGACY));
        recipeName.setText(name);

    }
}
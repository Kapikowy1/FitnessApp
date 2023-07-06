package com.example.testproject.Dieta.RecipeSearch;


import android.content.Intent;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testproject.R;
import com.example.testproject.Trening.ItemDescriptionActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;


public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        setRecyclerItemText(holder,model);
        setRecycerItemImage(holder,model);
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);

    }
    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ShapeableImageView img;
        private final TextView name;
        private final TextView recipetype;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.nametxt);
            recipetype = itemView.findViewById(R.id.recipetypetext);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
            MainModel model = getItem(position);

            String name = model.getName();
            String imageUrl = model.getTurl();
            String description = model.getDescription();
            String recipeType = model.getRecipeType();

            Intent intent = new Intent(itemView.getContext(), ItemDescriptionActivity.class);
            intent.putExtra("description",description);
            intent.putExtra("name", name);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("recipeType", recipeType);
            itemView.getContext().startActivity(intent);

        }
    }
    private void setRecycerItemImage(myViewHolder holder, MainModel model){
        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
    }
    private void setRecyclerItemText(myViewHolder holder, MainModel model) {
        holder.name.setText(model.getName());
        holder.recipetype.setText(model.getRecipeType());
    }

}















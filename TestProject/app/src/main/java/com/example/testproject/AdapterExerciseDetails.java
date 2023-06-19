package com.example.testproject;




import android.content.Intent;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class AdapterExerciseDetails extends FirebaseRecyclerAdapter<MainModel, AdapterExerciseDetails.myViewHolder> {
    public AdapterExerciseDetails(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.reps.setText(model.getReps());
        holder.sets.setText(model.getSets());
        holder.name.setText(model.getName());
        holder.exertype.setText(model.getRecipeType());
        //przypisuje dane z modelu do holdera
        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)

                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        //wyswietlam obrazki
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_detailed, parent, false);
        return new myViewHolder(view);
        //ustawiam wygląd widoku
    }
    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView name, exertype,sets,reps;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img2);
            name = itemView.findViewById(R.id.exercise_name);
            exertype = itemView.findViewById(R.id.exercise_partia);
            sets=itemView.findViewById(R.id.exercise_sets);
            reps=itemView.findViewById(R.id.exercise_reps);
            itemView.setOnClickListener(this);
            //znajduje elementy itemu ustawiam listener na elemencie
        }
        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
            MainModel model = getItem(position);
            //pobieram pozycje itemu
            String name = model.getName();
            String imageUrl = model.getTurl();
            String description = model.getDescription();
            String recipeType = model.getRecipeType();
            //przypisuje stringi do wartosci z kliknietego itemu
            Intent intent = new Intent(itemView.getContext(), DetailsRecyclerItemActivity.class);
            intent.putExtra("description",description);
            intent.putExtra("name", name);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("recipeType", recipeType);
            //wysylam dane do Recipe activity
            itemView.getContext().startActivity(intent);
            //uruchamiam RecipeActivity
        }
    }
}
















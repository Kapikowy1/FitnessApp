package com.example.testproject.Trening;




import android.content.Intent;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testproject.Dieta.RecipeSearch.MainModel;
import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class AdapterExerciseDetails extends FirebaseRecyclerAdapter<MainModel, AdapterExerciseDetails.myViewHolder> {
    public AdapterExerciseDetails(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        setTextOnItem(holder,model);
        setImgOnItem(holder,model);
    }

    private void setImgOnItem(myViewHolder holder, MainModel model) {
        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
    }
    private void setTextOnItem(myViewHolder holder, MainModel model) {
        holder.reps.setText(model.getReps());
        holder.sets.setText(model.getSets());
        holder.name.setText(model.getName());
        holder.exertype.setText(model.getRecipeType());
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_detailed, parent, false);
        return new myViewHolder(view);
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
        }
        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
            MainModel model = getItem(position);

            String name = model.getName();
            String imageUrl = model.getTurl();
            String description = model.getDescription();
            String recipeType = model.getRecipeType();
            String reps=model.getReps();
            String sets=model.getSets();

            Intent intent = new Intent(itemView.getContext(), ExerciseDescriptionActivity.class);
            intent.putExtra("description",description);
            intent.putExtra("name", name);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("reps",reps);
            intent.putExtra("sets",sets);
            intent.putExtra("recipeType", recipeType);

            itemView.getContext().startActivity(intent);

        }
    }
}
















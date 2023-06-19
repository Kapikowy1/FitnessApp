package com.example.testproject;



import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdapterDietPlans extends FirebaseRecyclerAdapter<String, AdapterDietPlans.ViewHolder> {



    public AdapterDietPlans(@NonNull FirebaseRecyclerOptions<String> options) {
        super(options);



    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull String planName) {
        holder.planName.setText(planName);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_diet, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView planName;
        CardView cardView;


        String choosePlansString;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            planName = itemView.findViewById(R.id.planNameTV);
            cardView = itemView.findViewById(R.id.planCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String planNameValueDiet = planName.getText().toString();
            Intent intent = new Intent(itemView.getContext(), CurrentPlanActivity.class);
            intent.putExtra("DietName", planNameValueDiet);
            itemView.getContext().startActivity(intent);

        }
    }
}

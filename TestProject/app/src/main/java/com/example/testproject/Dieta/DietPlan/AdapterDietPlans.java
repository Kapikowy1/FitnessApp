package com.example.testproject.Dieta.DietPlan;



import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Trening.CurrentPlanActivity;
import com.example.testproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            planName = itemView.findViewById(R.id.planNameTV);

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

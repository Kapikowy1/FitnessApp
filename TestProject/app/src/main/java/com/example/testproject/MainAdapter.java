package com.example.testproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {




    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options ) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());
        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)//
                .circleCrop()
                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)//
                .into(holder.img);


    }




    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);//
        return new myViewHolder(view);
    }



    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView img;
        TextView name, course, email;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametxt);
            course = (TextView) itemView.findViewById(R.id.coursetext);
            email = (TextView) itemView.findViewById(R.id.emailtext);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            MainModel model = getItem(position);
            String name = model.getName();
            String imageUrl = model.getTurl();
            String imageDesc=model.getDescription();
            Toast.makeText(itemView.getContext(), "position"+position, Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(itemView.getContext(),TeacherActivity.class);
            intent.putExtra("desc",imageDesc);
            intent.putExtra("name", name);
            intent.putExtra("imageUrl", imageUrl);

            itemView.getContext().startActivity(intent);
        }
    }


}















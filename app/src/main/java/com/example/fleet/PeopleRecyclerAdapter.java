package com.example.fleet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PeopleRecyclerAdapter extends RecyclerView.Adapter<PeopleRecyclerAdapter.ViewHolder> {
    private int[] images;
    private Context context;

    // data is passed into the constructor
    PeopleRecyclerAdapter(Context context, int[] images) {
        this.images = images;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_person, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.profilePic.setImageResource(images[position]);
        holder.status_there.setVisibility(View.GONE);
        holder.status_otw.setVisibility(View.GONE);
        holder.status_coming.setVisibility(View.GONE);
        holder.status_not.setVisibility(View.GONE);

        //TODO if statements maken om status te displayen met status list

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return images.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profilePic;
        ImageView status_there;
        ImageView status_otw;
        ImageView status_coming;
        ImageView status_not;

        ViewHolder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_pic);
            status_there = itemView.findViewById(R.id.iv_status_there);
            status_otw = itemView.findViewById(R.id.iv_status_otw);
            status_coming = itemView.findViewById(R.id.iv_status_coming);
            status_not = itemView.findViewById(R.id.iv_status_not);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

    // convenience method for getting data at click position
    int getItem(int id) {
        return images[id];
    }
}

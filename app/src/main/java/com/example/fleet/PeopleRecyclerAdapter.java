package com.example.fleet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PeopleRecyclerAdapter extends RecyclerView.Adapter<PeopleRecyclerAdapter.ViewHolder> {
    private int[] images;
    private Context context;
    private ArrayList<String> userIds, userStatuses;
    private User user;
    private String groupId;
    private int groupStatus;

    // data is passed into the constructor
    PeopleRecyclerAdapter(Context context, int[] images, ArrayList<String> userIds, ArrayList<String> userStatuses, User user, String groupId, int groupStatus) {
        this.images = images;
        this.context = context;
        this.userIds = userIds;
        this.userStatuses = userStatuses;
        this.user = user;
        this.groupId = groupId;
        this.groupStatus = groupStatus;
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

        switch(userStatuses.get(position)){
            case "present":
                holder.status_there.setVisibility(View.VISIBLE);
            case "omw":
                holder.status_otw.setVisibility(View.VISIBLE);
            case "coming":
                holder.status_coming.setVisibility(View.VISIBLE);
            case "not coming":
                holder.status_not.setVisibility(View.VISIBLE);
        }

        holder.recycler_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("groupId", groupId);
                intent.putExtra("groupStatus",groupStatus);
                intent.putExtra("ShowStatusSwitch",true);
                intent.putExtra("switchStatusForThisId", userIds.get(position));
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return userIds.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePic;
        ImageView status_there;
        ImageView status_otw;
        ImageView status_coming;
        ImageView status_not;
        ConstraintLayout recycler_person;

        ViewHolder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_pic);
            status_there = itemView.findViewById(R.id.iv_status_there);
            status_otw = itemView.findViewById(R.id.iv_status_otw);
            status_coming = itemView.findViewById(R.id.iv_status_coming);
            status_not = itemView.findViewById(R.id.iv_status_not);
            recycler_person = itemView.findViewById(R.id.recycler_person);
        }

    }

    // convenience method for getting data at click position
    int getItem(int id) {
        return images[id];
    }
}

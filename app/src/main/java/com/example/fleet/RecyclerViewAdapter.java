package com.example.fleet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    private Bundle groupBundle;

    private int[] images;
    Context context;
    Activity activity;
    User user;

    private final ArrayList<String> groupNameList = new ArrayList<>();
    private final ArrayList<String> groupDescriptionList = new ArrayList<>();
    private final ArrayList<String> groupIdList = new ArrayList<>();
    private final ArrayList<Integer> groupUserCountList = new ArrayList<>();
    private final ArrayList<Integer> groupStatusList = new ArrayList<>();

    private final ArrayList<Group> groupList;

    public RecyclerViewAdapter(Context context,ArrayList<Group> groupList, String search, User user){ //[[1,2,3],[5,1,8]]
        this.user = user;
        groupBundle = user.getGroupsBundle();
        this.context = context;
        this.groupList = groupList;

        for (String key : groupBundle.keySet()){
            Group group = groupBundle.getParcelable(key);

            if (group.getName().contains(search)) {
                this.groupNameList.add(group.getName());
                this.groupDescriptionList.add(group.getDescription());
                this.groupIdList.add(group.getId());
                this.groupUserCountList.add(group.getUserCount());
                this.groupStatusList.add(group.isActive());
            }
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.description.setText(groupDescriptionList.get(position));
        holder.groupName.setText(groupNameList.get(position));
        holder.count.setText(String.valueOf(groupUserCountList.get(position)));

        if (groupStatusList.get(position) == 1){
            holder.active.setVisibility(View.VISIBLE);
        }
        else{
            holder.active.setVisibility(View.GONE);
        }
//        holder.picture.setImageResource(images[position]);

        holder.recycler_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("groupId", groupIdList.get(position));
                intent.putExtra("groupStatus",groupStatusList.get(position));
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(groupNameList == null){return 0;}
        return groupNameList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView description;
        TextView groupName;
        TextView count;
        ImageView active;
        ImageView picture;
        ConstraintLayout recycler_row;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.group_description);
            groupName = itemView.findViewById(R.id.group_name);
            count = itemView.findViewById(R.id.people_count);
            active = itemView.findViewById(R.id.status);
//            picture = itemView.findViewById(R.id.profile_pic);
            recycler_row = itemView.findViewById(R.id.recycler_row);
        }
    }
}
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    String[] data1;
    String[] data2;
    private int[] images;
    Context context;
    Activity activity;
    User user;
    private RequestQueue requestQueue;
    private final ArrayList<String> groupNameList;
    private final ArrayList<String> groupDescriptionList;
    private final ArrayList<String> groupIdList;

    public RecyclerViewAdapter(Context context, ArrayList<String> groupNameList,ArrayList<String> groupDescriptionList,ArrayList<String> groupIdList, User user){ //[[1,2,3],[5,1,8]]
        this.context = context;
        this.groupNameList = groupNameList;
        this.groupDescriptionList = groupDescriptionList;
        this.groupIdList = groupIdList;
        this.user = user;
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
//        holder.picture.setImageResource(images[position]);

        holder.recycler_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("groupId", groupIdList.get(position));
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
        ImageView picture;
        ConstraintLayout recycler_row;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.group_description);
            groupName = itemView.findViewById(R.id.group_name);
//            picture = itemView.findViewById(R.id.profile_pic);
            recycler_row = itemView.findViewById(R.id.recycler_row);
        }
    }
}

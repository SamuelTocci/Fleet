package com.example.fleet;

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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    String[] data1;
    String[] data2;
    int[] images;
    Context context;

    public RecyclerViewAdapter(Context ct, String[] s1, String[] s2, int[] img){
        context = ct;
        data1 = s1;
        data2 = s2;
        images = img;
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
        holder.description.setText(data1[position]);
        holder.groupName.setText(data2[position]);
        holder.picture.setImageResource(images[position]);

        holder.recycler_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data1 == null){return 0;}
        return data1.length;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView description,groupName;
        ImageView picture;
        ConstraintLayout recycler_row;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.group_description);
            groupName = itemView.findViewById(R.id.group_name);
            picture = itemView.findViewById(R.id.person_1);
            recycler_row = itemView.findViewById(R.id.recycler_row);
        }
    }
}
